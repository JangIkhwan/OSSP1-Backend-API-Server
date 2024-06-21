package com.example.cavityapiserver.controller;

import com.example.cavityapiserver.common.response.BaseResponse;
import com.example.cavityapiserver.dto.*;
import com.example.cavityapiserver.service.PredicitonService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PredicitonController {

    private final PredicitonService predicitonService;

    /*
    앱에게 결과를 전송하기 위한 메소드
     */
    @GetMapping("result")
    public BaseResponse<PredictionGetResponse> sendResult(HttpServletRequest request){
        log.info("PredicitonController::sendResult()");

        // 앱에서 요청한 inferenece 결과를 데이터베이스에서 찾아서 반환
        PredictionGetRequest getRequest = PredictionGetRequest.builder()
                .device_token(request.getParameter("device_token"))
                .request_id(Long.parseLong(request.getParameter("request_id")))
                .build();
        PredictionGetResponse result = predicitonService.getResult(getRequest);

        return new BaseResponse<>(result);
    }
}