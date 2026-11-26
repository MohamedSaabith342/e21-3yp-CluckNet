package com.clucknet.backend.dto.response;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TelemetryDataResponse {
    private java.time.Instant timestamp;
    private Long zoneId;
    private String deviceId;
    private Double temperature;
    private Double humidity;
    private Double nh3;
    private Double lpg;
}
