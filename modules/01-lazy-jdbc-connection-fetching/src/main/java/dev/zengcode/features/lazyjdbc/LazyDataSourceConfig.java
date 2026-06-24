package dev.zengcode.features.lazyjdbc;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

/**
 * Simulates the Spring Boot 4.1 'spring.datasource.connection-fetch: lazy' property
 * by wrapping any instantiated DataSource in a LazyConnectionDataSourceProxy.
 * In a native Spring Boot 4.1 app, this is done automatically by the framework.
 */
@Component
public class LazyDataSourceConfig implements BeanPostProcessor {

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof DataSource && !(bean instanceof LazyConnectionDataSourceProxy)) {
            // We wrap the real DataSource in a LazyConnectionDataSourceProxy
            return new LazyConnectionDataSourceProxy((DataSource) bean);
        }
        return bean;
    }
}
