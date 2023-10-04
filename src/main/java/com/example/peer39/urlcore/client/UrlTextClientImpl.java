package com.example.peer39.urlcore.client;

import com.example.peer39.urlcore.dto.UrlTextResponse;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

@Component
public class UrlTextClientImpl implements UrlTextClient {
    private final WebClient webClient;

    public UrlTextClientImpl(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    @Override
    public Mono<UrlTextResponse> fetchTextFromUrl(String url) {
        return webClient.get()
                .uri(url)
                .exchangeToMono(clientResponse -> {
                    HttpStatus responseStatus = clientResponse.statusCode();
                    if (responseStatus.is3xxRedirection()) {
                        return Mono.error(new ResponseStatusException(HttpStatus.SEE_OTHER, "Redirect encountered"));
                    } else if (responseStatus.is2xxSuccessful()) {
                        return clientResponse.bodyToMono(String.class)
                                .map(html -> {
                                    Document doc = Jsoup.parse(html);
                                    doc.select("script").remove();
                                    String text = doc.body().text();
                                    return new UrlTextResponse(url, text);
                                });
                    } else {
                        return Mono.error(new ResponseStatusException(responseStatus, "Failed to retrieve URL"));
                    }
                })
                .onErrorResume(throwable -> Mono.just(new UrlTextResponse(url, null, throwable.getMessage())));
    }
}