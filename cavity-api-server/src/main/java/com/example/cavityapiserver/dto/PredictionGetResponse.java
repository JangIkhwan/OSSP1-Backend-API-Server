
package com.example.cavityapiserver.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PredictionGetResponse {
    private List<Prediction> pred;
}