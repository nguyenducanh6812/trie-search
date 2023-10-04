package com.example.peer39.urlcore.service;

import com.example.peer39.urlcore.dto.UrlTextResponse;
import reactor.core.publisher.Flux;

import java.util.List;

public interface UrlTextService {
    Flux<UrlTextResponse> fetchTextFromUrls(List<String> urls);

    Flux<UrlTextResponse> categorizeUrls(List<String> urls);
}
