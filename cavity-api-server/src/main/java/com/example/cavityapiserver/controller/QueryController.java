package com.example.cavityapiserver.controller;

import com.example.cavityapiserver.common.response.BaseResponse;
import com.example.cavityapiserver.dto.*;
import com.example.cavityapiserver.service.ImageService;
import com.example.cavityapiserver.service.PredicitonService;
import com.example.cavityapiserver.service.QueryService;
import com.example.cavityapiserver.service.ServerCallService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("request")
public class QueryController {
    private final ImageService imageService;

    private final QueryService queryService;

    private final ServerCallService serverCallService;

    private final PredicitonService predicitonService;
    /*
    앱으로부터 사진을 업로드 받는 메소드
     */
    @Transactional
    @PostMapping(consumes = { "multipart/form-data" })
    public BaseResponse<QueryPostResponse> query(@RequestParam MultipartFile image, @RequestParam  String device_token, @RequestParam  Long request_id){
        log.info("QueryController::query()");


        String image_file_name = imageService.uploadImage(image); // 이미지 업로드

        // 앱의 요청을 데이터베이스에 저장
        Long queryId = queryService.addQuery( QueryPostRequest.builder()
                .request_id(request_id)
                .device_token(device_token)
                .image_url(image_file_name)
                .build());

        // 모델 서버 호출
        Prediction[] webClientResponses = serverCallService.postCall(image_file_name);
        List<Prediction> list = Arrays.asList(webClientResponses);

        // 모델 서버의 inference 결과를 저장
        predicitonService.addResult(PredictionResponse.builder()
                .device_token(device_token)
                .request_id(request_id)
                .data(new DataDTO(list))
                .build());


        return new BaseResponse<>(new QueryPostResponse(queryId));
    }
}