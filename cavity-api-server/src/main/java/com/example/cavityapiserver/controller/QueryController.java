package com.example.cavityapiserver.controller;

import com.example.cavityapiserver.common.response.BaseResponse;
import com.example.cavityapiserver.dto.*;
import com.example.cavityapiserver.service.ImageService;
import com.example.cavityapiserver.service.PredicitonService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequiredArgsConstructor
public class QueryController {
    private final ImageService imageService;

    private final PredicitonService predicitonService;

    /*
    앱으로부터 사진을 업로드 받는 메소드
     */
    @PostMapping(value="request", consumes = { "multipart/form-data" })
    public BaseResponse<PredictionPostResponse> request(@RequestParam MultipartFile image, @RequestParam  String device_token, @RequestParam  Long request_id){
        log.info("PredicitonRequestController::request");
        String image_location = imageService.uploadImage(image);
        PredictionPostRequest request = PredictionPostRequest.builder()
                .request_id(request_id)
                .device_token(device_token)
                .image_url(image_location)
                .build();
        Long prediction_request_id = predicitonService.addRequest(request);
        return new BaseResponse(prediction_request_id);
    }

    /*
    모델 서버로부터 결과를 받는 메소드
     */
    @PatchMapping("request/{requestId}/result")
    public BaseResponse<String> pushResult(
            @PathVariable Long requestId,
            @RequestBody PredictionPatchRequest patchRequest
    ){
        predicitonService.addResult(patchRequest);
        return new BaseResponse<>("ok");
    }

    /*
    앱에게 결과를 전송하기 위한 메소드
     */
    @GetMapping("result")
    public BaseResponse<PredictionGetResponse> sendResult(HttpServletRequest request){
        PredictionGetRequest getRequest = PredictionGetRequest.builder()
                .device_token(request.getParameter("device_token"))
                .request_id(Long.parseLong(request.getParameter("request_id")))
                .build();
        PredictionGetResponse result = predicitonService.getResult(getRequest);
        return new BaseResponse<>(result);
    }
}
