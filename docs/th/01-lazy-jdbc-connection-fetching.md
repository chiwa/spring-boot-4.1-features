# Spring Boot 4.1 เจาะลึกฟีเจอร์ Lazy JDBC Connection Fetching (พร้อมความลับของ JPA ที่หลายคนไม่เคยรู้!)

เคยเจอปัญหานี้ไหมครับ? ระบบคนเข้าเยอะๆ แล้วอยู่ดีๆ ก็พัง พร้อมกับ Error ใน Log สีแดงเถือก:
`HikariPool-1 - Connection is not available, request timed out after 30000ms.`

คุณอาจจะคิดว่า "สงสัย Database รับไม่ไหว" หรือ "สงสัย Query ช้า" แต่พอไปดูที่ Database กลับพบว่า CPU/Memory โล่งมาก... อ้าว แล้ว Connection มันหายไปไหนหมด?

หนึ่งในสาเหตุยอดฮิตที่ซ่อนตัวอยู่เงียบๆ คือ **Eager Connection Fetching** ครับ

---

## 🛑 ต้นตอของปัญหา: จองก่อน ไม่รอแล้วนะ

เวลาที่เราเขียน Spring Boot ปกติเรามักจะครอบการทำงานด้วย `@Transactional` เพื่อให้การทำงานเชื่อมต่อกันเป็น Transaction เดียวกันใช่ไหมครับ? 

```java
@Transactional
public void checkoutOrder(OrderRequest request) {
    // 1. เรียก External API เพื่อตัดบัตรเครดิต (สมมติว่าใช้เวลา 2 วินาที)
    paymentService.charge(request.getCard()); 
    
    // 2. บันทึกข้อมูลลง Database (ใช้เวลา 0.05 วินาที)
    orderRepository.save(new Order(request)); 
}
```

ดูเผินๆ เหมือนไม่มีอะไรผิดปกติ แต่ภายใต้ Framework (อย่างเช่น `DataSourceTransactionManager` ที่ใช้ใน Spring JDBC หรือ MyBatis) ทันทีที่โค้ดวิ่งเข้าสู่เมธอด `@Transactional` **ระบบจะวิ่งไปดึง Connection ออกมาจาก HikariCP Pool ทันที!** 

นั่นหมายความว่า ระหว่างที่ระบบกำลังรอ Payment API ตอบกลับมาเป็นเวลา 2 วินาที... Database Connection ถูกดึงออกมา "ถือค้างไว้เฉยๆ" โดยไม่ได้รัน SQL อะไรเลย! ถ้ามีคนกด Checkout พร้อมกัน 20 คน Connection Pool ก็จะเต็มทันที (Default ของ Hikari คือ 10) ระบบก็จะพังระเนระนาด

---

## 🚀 ทางออกที่คาดหวังใน Spring Boot 4.1: Lazy JDBC Connection Fetching

Spring Boot 4.1 คาดว่าจะมีแนวทางสนับสนุนเพื่อแก้ปัญหานี้ให้ง่ายขึ้น (เช่น การตั้งค่า `spring.datasource.connection-fetch: lazy`) 

> [!WARNING]
> **Important Note:**
> Repository นี้ใช้คลาส `LazyConnectionDataSourceProxy` เพื่อ **จำลอง** พฤติกรรม Lazy JDBC Connection Fetching ที่คาดหวัง การตั้งค่า Native ของ Spring Boot 4.1 ควรอ้างอิงและตรวจสอบจาก Official Documentation ของ Spring Boot อีกครั้งเมื่อถึงเวลาอัปเกรด

**มันทำงานยังไง?**
เมื่อเราเปิดโหมด `lazy` ระบบจะ **ไม่ดึง Connection ออกมาทันที** ที่เข้า `@Transactional` แต่มันจะใช้ Connection จำลอง (Proxy) รับหน้าไปก่อน 

และมันจะยอมดึง Physical Connection ออกมาจาก HikariCP ก็ต่อเมื่อ **มีการส่งคำสั่ง SQL ไปยัง Database จริงๆ เท่านั้น** (ในตัวอย่างข้างต้นคือบรรทัด `orderRepository.save()`) 

ผลลัพธ์คือ จากที่ต้องเสีย Connection ไปฟรีๆ 2 วินาที ตอนนี้ Connection จะถูกใช้งานแค่ 0.05 วินาทีตอนเซฟข้อมูลเท่านั้น! ประสิทธิภาพของ Connection Pool จะเพิ่มขึ้นอย่างมหาศาลโดยไม่ต้องแก้โค้ดแม้แต่บรรทัดเดียว

---

## 🤯 ความลับระดับสถาปัตยกรรม (Deep Architectural Insight)

อ่านมาถึงตรงนี้ หลายคนที่ใช้ **Spring Data JPA** (Hibernate) อาจจะรีบไปเติมคอนฟิกนี้ในโปรเจกต์ทันที... แต่เดี๋ยวก่อนครับ! นี่คือความลับระดับสถาปัตยกรรมที่ผมค้นพบตอนทำ Repository นี้:

> **ถ้าโปรเจกต์คุณเน้นใช้งาน Spring Data JPA กับ Hibernate เป็นหลัก คุณอาจจะไม่เห็นความแตกต่างที่ชัดเจนในหลายๆ Use Case ครับ!**

**ทำไมล่ะ?** 
เพราะว่า Hibernate สามารถประวิงเวลา (Delay) การดึง Physical Connection ออกมาในหลายๆ Flow การทำงานที่เป็นที่นิยมอยู่แล้วครับ 

ใน Use Case ทั่วไป ตัว Hibernate มักจะเตรียม Logical Connection ไว้รับหน้าก่อน และอาจจะไม่ดึง Connection จริงออกมาจนกว่าจะเริ่มมีคำสั่ง SQL ส่งไปถึง Database 

อย่างไรก็ตาม **พฤติกรรมที่แท้จริงจะขึ้นอยู่กับ Configuration** (เช่น Hibernate connection handling mode, transaction manager, provider configuration, และรูปแบบที่โค้ดเข้าถึง Database) 

**สรุปคือ:**
ฟีเจอร์ Lazy Fetching (อย่างเช่น `connection-fetch: lazy` ที่คาดหวังใน Spring Boot 4.1) อาจจะไม่ได้สร้างผลกระทบที่ชัดเจนนักสำหรับคนใช้ JPA แต่ว่ามันคือสิ่งที่จะมา "ปลดล็อค" ให้กับคนที่ใช้เฟรมเวิร์กอื่นๆ อย่างเช่น **Spring JDBC (JdbcTemplate), MyBatis, หรือ jOOQ** ต่างหาก!

มันจะเป็นการนำประโยชน์ที่ฝั่ง JPA มักจะได้รับอยู่แล้วใน Use Case ทั่วไป ลงมาให้เครื่องมือสาย SQL ปกติ (ที่อาศัย `DataSourceTransactionManager`) ได้ใช้งาน เพื่อช่วยลดปัญหา Connection Pool Exhaustion ครับ!

---

## 🎯 สรุป (Takeaways)

1. การทำ External API Call, อัปโหลดไฟล์, หรือคำนวณงานหนักๆ ภายในเมธอดที่มี `@Transactional` เสี่ยงต่อการทำให้ Connection Pool เต็มอย่างรวดเร็ว
2. ถ้าคุณใช้ **Spring Data JPA** คุณอาจจะได้รับการปกป้องจากปัญหานี้อยู่แล้วในหลายๆ สถานการณ์ด้วยกลไกภายในของ Hibernate
3. ถ้าคุณใช้ **Spring JDBC, MyBatis, หรือ jOOQ** แนวทาง Lazy Fetching (ไม่ว่าจะเป็น `LazyConnectionDataSourceProxy` หรือฟีเจอร์ใหม่ของ Spring Boot 4.1) คือสิ่งที่คุณควรนำมาใช้! มันคือกำไรแบบฟรีๆ สำหรับระบบของคุณ

สนใจดูโค้ดตัวอย่างแบบจับเวลาจริงและการพิสูจน์ Connection ใน Pool ทีละสเตป? สามารถเข้าไปรันโปรเจกต์ตัวอย่างได้ที่ GitHub Repository ของบทความชุดนี้ครับ!

👉 [GitHub Repository: Spring Boot 4.1 Features Deep Dive](https://github.com/chiwa/spring-boot-4.1-features)
