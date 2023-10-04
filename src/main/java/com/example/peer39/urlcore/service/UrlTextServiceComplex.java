package com.example.peer39.urlcore.service;

import com.example.peer39.urlcore.dto.UrlTextResponse;
import com.example.peer39.urlcore.model.Category;
import reactor.core.publisher.Flux;

import java.util.List;

public interface UrlTextServiceComplex {
    Flux<UrlTextResponse> categorizeUrls(List<String> urls);
}
