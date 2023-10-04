package com.example.peer39.urlcore.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

@Configuration
public class WebClientConfig {
    private static final Logger logger = LoggerFactory.getLogger(WebClientConfig.class);
    @Value("${webclient.max-buffer-size}")
    private int maxBufferSize;

    @Bean
    public WebClient.Builder webClientBuilder() {
        ExchangeStrategies exchangeStrategies = ExchangeStrategies.builder()
                .codecs(configurer -> configurer
                        .defaultCodecs()
                        .maxInMemorySize(maxBufferSize))
                .build();

        WebClient.Builder webClientBuilder = WebClient.builder().exchangeStrategies(exchangeStrategies);
        webClientBuilder.filter(logRequest());
        webClientBuilder.filter(logResponse());

        return webClientBuilder;
    }

    private ExchangeFilterFunction logRequest() {
        return ExchangeFilterFunction.ofRequestProcessor(clientRequest -> {
            if (logger.isDebugEnabled()) {
                HttpMethod method = clientRequest.method();
                String url = clientRequest.url().toString();
                HttpHeaders headers = clientRequest.headers();

                logger.debug("Sending request:");
                logger.debug("Method: {}", method);
                logger.debug("URL: {}", url);
                logger.debug("Headers: {}", headers);
            }
            return Mono.just(clientRequest);
        });
    }

    private ExchangeFilterFunction logResponse() {
        return ExchangeFilterFunction.ofResponseProcessor(clientResponse -> {
            if (logger.isDebugEnabled()) {
                HttpStatus statusCode = clientResponse.statusCode();
                HttpHeaders headers = convertHeaders(clientResponse.headers());
                logger.debug("Received response:");
                logger.debug("Status code: {}", statusCode);
                logger.debug("Headers: {}", headers);
            }
            return Mono.just(clientResponse);
        });
    }

    private HttpHeaders convertHeaders(ClientResponse.Headers headers) {
        HttpHeaders httpHeaders = new HttpHeaders();
        headers.asHttpHeaders().forEach(httpHeaders::addAll);
        return httpHeaders;
    }
}


