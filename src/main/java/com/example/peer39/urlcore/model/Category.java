package com.example.peer39.urlcore.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class Category {
    private final String name;
    private final List<CategoryKeyword> keywords;

    public Category(String name) {
        this(name, null);
    }

    public static class Builder {
        private String name;
        private List<CategoryKeyword> keywords;

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder keyword(String keyword) {
            if (keywords == null) {
                keywords = new ArrayList<>();
            }
            keywords.add(new CategoryKeyword(keyword));
            return this;
        }

        public Category build() {
            if (name == null) {
                throw new IllegalStateException("Category name cannot be null");
            }
            if (keywords == null || keywords.isEmpty()) {
                throw new IllegalStateException("Category must have at least one keyword");
            }
            return new Category(name, keywords);
        }
    }
}