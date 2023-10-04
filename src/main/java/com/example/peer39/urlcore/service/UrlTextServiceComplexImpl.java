package com.example.peer39.urlcore.service;

import com.example.peer39.urlcore.client.UrlTextClient;
import com.example.peer39.urlcore.dto.UrlTextResponse;
import com.example.peer39.urlcore.model.Category;
import com.example.peer39.urlcore.model.CategoryTrie;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UrlTextServiceComplexImpl implements UrlTextServiceComplex {

    private final UrlTextClient urlTextClient;
    private final CategoryServiceComplex categoryService;
    public UrlTextServiceComplexImpl(UrlTextClient urlTextClient, CategoryServiceComplex categoryService) {
        this.urlTextClient = urlTextClient;
        this.categoryService = categoryService;
    }

    @Override
    public Flux<UrlTextResponse> categorizeUrls(List<String> urls) {
        List<Mono<UrlTextResponse>> urlResponses = urls.stream()
                .map(urlTextClient::fetchTextFromUrl)
                .collect(Collectors.toList());

        return checkUrlCategory(urlResponses);
    }

    public Flux<UrlTextResponse> checkUrlCategory(List<Mono<UrlTextResponse>> urlResponses) {
        return Flux.fromIterable(urlResponses)
                .flatMapSequential(responseMono ->
                        responseMono.flatMap(response -> {
                            if (response != null && response.getErrorMessage() == null) {
                                processUrlTextResponse(response);
                                return Mono.just(response);
                            }
                            return responseMono;
                        })
                )
                .subscribeOn(Schedulers.parallel());
    }

    private Mono<UrlTextResponse> processUrlTextResponse(UrlTextResponse response) {
        if (response == null || response.getText() == null) {
            return Mono.just(response);
        }

        List<Category> matchingCategories = categoryService.findMatchingCategories(response.getText());
        response.setCategories(matchingCategories);

        return Mono.just(response);
    }
}