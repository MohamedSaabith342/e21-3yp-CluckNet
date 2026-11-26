package com.clucknet.backend.dto.response;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LiveTelemetryResponse {
    private java.time.Instant timestamp;
    private Double temperature;
    private Double humidity;
    private Double nh3;
    private Double lpg;
}
