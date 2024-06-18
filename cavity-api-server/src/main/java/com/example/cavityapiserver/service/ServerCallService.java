package com.example.cavityapiserver.service;

import com.example.cavityapiserver.common.exception.BadWebClientRequestException;
import com.example.cavityapiserver.dto.Prediction;
import com.example.cavityapiserver.dto.WebClientResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import static com.example.cavityapiserver.common.response.status.BaseExceptionResponseStatus.WEB_CLIENT_ERROR;

@Slf4j
@Service
@RequiredArgsConstructor
public class ServerCallService {
    private final WebClient webClient;

    @Value("${images.path}")
    private String imageDir;

    public Prediction[] postCall(String image_file_name) {
        log.info("ServerCallService::postCall()");
        String url = "/predict";

        MultipartBodyBuilder multipartBodyBuilder = new MultipartBodyBuilder();

        try {
            InputStreamResource inputStreamResource = new InputStreamResource(new FileInputStream(imageDir + image_file_name));
            multipartBodyBuilder.part("file", inputStreamResource).filename(image_file_name);
        } catch (FileNotFoundException e) {
            log.info("file not found!");
            throw new BadWebClientRequestException(WEB_CLIENT_ERROR);
        }

        Mono<Prediction[]> response = webClient
                .post()
                .uri(url)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(multipartBodyBuilder.build()))
                .retrieve()
                .onStatus(httpStatus -> httpStatus != HttpStatus.OK,
                        res -> {
                            throw new BadWebClientRequestException(WEB_CLIENT_ERROR);
                        }
                )
                .bodyToMono(Prediction[].class);

        return response.block();
    }
}
