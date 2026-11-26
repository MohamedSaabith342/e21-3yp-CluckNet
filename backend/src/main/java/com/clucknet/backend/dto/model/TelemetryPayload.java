package com.clucknet.backend.dto.model;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TelemetryPayload {
    private Long zoneId;
    private String deviceId;
    private Double temperature;
    private Double humidity;
    private Double nh3;
    private Double lpg;
}
