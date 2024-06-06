package com.example.cavityapiserver.service;

import com.example.cavityapiserver.common.exception.BadWebClientRequestException;
import com.example.cavityapiserver.dto.CallPostRequest;
import com.example.cavityapiserver.dto.CallPostResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static com.example.cavityapiserver.common.response.status.BaseExceptionResponseStatus.WEB_CLIENT_ERROR;

@Slf4j
@Service
@RequiredArgsConstructor
public class ServerCallService {
    private final WebClient webClient;
    public void postCall(CallPostRequest callPostRequest) {
        log.info("postCall() :: ");
        String url = "/test";
        Mono<CallPostResponse> callPostResponseMono = webClient
                .post()
                .uri(url)
                .bodyValue(callPostRequest)
                .retrieve()
                .onStatus(httpStatus -> httpStatus != HttpStatus.OK,
                        response ->{
                            throw new BadWebClientRequestException(WEB_CLIENT_ERROR);
                        }
                )
                .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(),
                        clientResponse ->
                                clientResponse.bodyToMono(String.class).map(RuntimeException::new))
                .bodyToMono(CallPostResponse.class);

        callPostResponseMono.subscribe(
                callPostResponse -> {
                    log.info("response from server: " + callPostResponse);
                }
        );
    }
}
