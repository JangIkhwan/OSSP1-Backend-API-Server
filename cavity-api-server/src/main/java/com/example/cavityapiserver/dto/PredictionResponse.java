
package com.example.cavityapiserver.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PredictionResponse {
    private String device_token;
    private Long request_id;
    private DataDTO data;
}