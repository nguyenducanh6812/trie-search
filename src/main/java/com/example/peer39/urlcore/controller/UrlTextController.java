package com.example.peer39.urlcore.controller;

import com.example.peer39.urlcore.dto.UrlTextResponse;
import com.example.peer39.urlcore.service.UrlTextService;
import com.example.peer39.urlcore.service.UrlTextServiceComplex;
import com.example.peer39.urlcore.validation.ValidUrlList;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.List;

@RestController
@RequestMapping("/api/v1/urls")
public class UrlTextController {

    private final UrlTextService urlTextService;
    private final UrlTextServiceComplex urlTextServiceComplex;

    public UrlTextController(UrlTextService urlTextService, UrlTextServiceComplex urlTextServiceComplex) {
        this.urlTextService = urlTextService;
        this.urlTextServiceComplex = urlTextServiceComplex;
    }

    @PostMapping("/fetch-text")
    public Flux<UrlTextResponse> fetchTextFromUrls(@ValidUrlList @RequestBody List<String> urls) {
        return urlTextService.fetchTextFromUrls(urls);
    }

    @PostMapping("/classify")
    public Flux<UrlTextResponse> classifyUrls(@ValidUrlList @RequestBody List<String> urls) {
        return urlTextService.categorizeUrls(urls);
    }

    @PostMapping("/classify-advance")
    public Flux<UrlTextResponse> classifyAdvanceUrls(@ValidUrlList @RequestBody List<String> urls) {
        return urlTextServiceComplex.categorizeUrls(urls);
    }
}