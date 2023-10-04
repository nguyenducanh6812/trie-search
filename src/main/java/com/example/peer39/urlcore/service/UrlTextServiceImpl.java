package com.example.peer39.urlcore.service;

import com.example.peer39.urlcore.client.UrlTextClient;
import com.example.peer39.urlcore.dto.UrlTextResponse;
import com.example.peer39.urlcore.model.Category;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UrlTextServiceImpl implements UrlTextService {
    Logger logger = LoggerFactory.getLogger(UrlTextServiceImpl.class);
    private final UrlTextClient urlTextClient;
    private final CategoryService categoryService;

    public UrlTextServiceImpl(UrlTextClient urlTextClient, CategoryService categoryService) {
        this.urlTextClient = urlTextClient;
        this.categoryService = categoryService;
    }

    @Override
    public Flux<UrlTextResponse> fetchTextFromUrls(List<String> urls) {
        logger.info("List url: {}", urls);
        return Flux.fromIterable(urls)
                .parallel()
                .runOn(Schedulers.parallel())
                .flatMap(urlTextClient::fetchTextFromUrl)
                .ordered(Comparator.comparingInt(u -> urls.indexOf(u.getUrl())));
    }

    @Override
    public Flux<UrlTextResponse> categorizeUrls(List<String> urls) {
        List<Mono<UrlTextResponse>> urlResponses = urls.stream()
                .map(urlTextClient::fetchTextFromUrl)
                .collect(Collectors.toList());

        return checkUrlCategory(urlResponses);
    }

    public Flux<UrlTextResponse> checkUrlCategory(List<Mono<UrlTextResponse>> urlResponses) {
        Flux<UrlTextResponse> processedResponses = Flux.fromIterable(urlResponses)
                .flatMapSequential(responseMono ->
                        responseMono.flatMap(response -> {
                            if (response != null && response.getErrorMessage() == null) {
                                categorizeUrl(response);
                                return Mono.just(response);
                            }
                            return responseMono;
                        })
                )
                .subscribeOn(Schedulers.parallel());

        Flux<UrlTextResponse> originalResponses = Flux.fromIterable(urlResponses)
                .flatMap(Mono::flux)
                .filter(response -> response != null && response.getErrorMessage() != null);

        return Flux.concat(processedResponses, originalResponses).distinct();
    }

    private void categorizeUrl(UrlTextResponse response) {
        List<Category> matchingCategories = categoryService.findMatchingCategories(response.getText());
        response.setCategories(matchingCategories);
    }
}