package com.example.cavityapiserver.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class WebClientResponse {
//    private Long code;
//    private String message;
    private List<Prediction> pred;
}