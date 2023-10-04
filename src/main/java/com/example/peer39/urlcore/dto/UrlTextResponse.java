package com.example.peer39.urlcore.dto;

import com.example.peer39.urlcore.model.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UrlTextResponse {
    private String url;
    private String text;
    private String errorMessage;
    private List<Category> categories;
    public UrlTextResponse(String url, List<Category> categories){
        this(url, null, null, categories);
    }
    public UrlTextResponse(String url, String text) {
        this(url, text, null, null);
    }
    public UrlTextResponse(String url, String text, String errorMessage){
        this(url, text, errorMessage, null);
    }
}

