package com.example.peer39.urlcore.client;

import com.example.peer39.urlcore.dto.UrlTextResponse;
import reactor.core.publisher.Mono;

public interface UrlTextClient {
    Mono<UrlTextResponse> fetchTextFromUrl(String url);
}
