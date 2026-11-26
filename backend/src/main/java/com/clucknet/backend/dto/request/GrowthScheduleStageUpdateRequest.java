package com.clucknet.backend.dto.request;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GrowthScheduleStageUpdateRequest {
    @jakarta.validation.constraints.NotNull private Integer startDay;
    @jakarta.validation.constraints.NotNull private Integer endDay;
    @jakarta.validation.constraints.NotNull private Double minTemperature;
    @jakarta.validation.constraints.NotNull private Double maxTemperature;
    @jakarta.validation.constraints.NotNull private Double minHumidity;
    @jakarta.validation.constraints.NotNull private Double maxHumidity;
}
