package com.example.cavityapiserver.controller;

import com.example.cavityapiserver.common.response.BaseResponse;
import com.example.cavityapiserver.dto.RequestPostRequest;
import com.example.cavityapiserver.dto.RequestPostResponse;
import com.example.cavityapiserver.service.ImageService;
import com.example.cavityapiserver.service.PredicitonRequestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("request")
@RequiredArgsConstructor
public class PredicitonRequestController {
    private final ImageService imageService;

    private final PredicitonRequestService predicitonRequestService;

    @PostMapping(consumes = { "multipart/form-data" })
    public BaseResponse<RequestPostResponse> request(@RequestParam MultipartFile image, @RequestParam  String device_token, @RequestParam  Long request_id){
        log.info("PredicitonRequestController::request");
        String image_location = imageService.uploadImage(image);
        RequestPostRequest request = RequestPostRequest.builder()
                .request_id(request_id)
                .device_token(device_token)
                .image_url(image_location)
                .build();
        Long prediction_request_id = predicitonRequestService.addRequest(request);
        return new BaseResponse(prediction_request_id);
    }
}
