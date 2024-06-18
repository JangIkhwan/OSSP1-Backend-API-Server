package com.example.cavityapiserver.dto;

import lombok.*;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DataDTO {
    List<Prediction> pred;
}
