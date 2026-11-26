package com.clucknet.backend.dto.request;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FcmTokenRequest {
    @jakarta.validation.constraints.NotBlank private String token;
}
