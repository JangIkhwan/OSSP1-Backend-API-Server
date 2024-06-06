package com.example.cavityapiserver.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {
	private String BASE_URL = "http://localhost:8080";

    // webClient Bean 등록
	@Bean
	public WebClient webClient(WebClient.Builder builder) {
		return builder
			.baseUrl(BASE_URL) // 호출할 API 서비스 도메인 URL
			.defaultHeaders(httpHeaders -> {
				httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
			})
			.build();
	}

}