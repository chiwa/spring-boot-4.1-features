package dev.zengcode.features.lazyjdbc;

import java.util.List;

public record DemoResponse(
        String scenario,
        String description,
        List<TimelineStep> timeline,
        String takeaway
) {
}
