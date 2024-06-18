
package com.example.cavityapiserver.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PredictionGetRequest {
    private String device_token;
    private Long request_id;
    private DataDTO data;
}