package com.example.cavityapiserver.config;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Configuration
public class WebClientConfig {
	@Value("${WEBCLIENT_BASE_URL}")
	private String BASE_URL;

    // webClient Bean 등록
	@Bean
	public WebClient webClient(WebClient.Builder builder) {

		HttpClient httpClient = HttpClient.create()
				.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
				.responseTimeout(Duration.ofMillis(5000))
				.doOnConnected(conn ->
						conn.addHandlerLast(new ReadTimeoutHandler(10000, TimeUnit.MILLISECONDS))
								.addHandlerLast(new WriteTimeoutHandler(10000, TimeUnit.MILLISECONDS)));

		return builder
			.baseUrl(BASE_URL) // 호출할 API 서비스 도메인 URL
			.clientConnector(new ReactorClientHttpConnector(httpClient)) // 타임 아웃 설정
			.defaultHeaders(httpHeaders -> {
				httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
			})
			.build();
	}

}