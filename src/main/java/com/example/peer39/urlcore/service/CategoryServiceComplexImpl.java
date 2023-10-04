package com.example.peer39.urlcore.service;

import com.example.peer39.urlcore.model.Category;
import com.example.peer39.urlcore.model.CategoryKeyword;
import com.example.peer39.urlcore.model.CategoryTrie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CategoryServiceComplexImpl implements CategoryServiceComplex {
    Logger logger = LoggerFactory.getLogger(getClass());
    private final CategoryTrie categoryTrie;

    public CategoryServiceComplexImpl(CategoryTrie categoryTrie) {
        this.categoryTrie = categoryTrie;
        buildTrie();
    }

    private void buildTrie() {
        List<Category> categories = initializeCategories();

        for (Category category : categories) {
            List<String> keywords = getCategoryKeywords(category);
            String[] keywordArray = keywords.toArray(new String[0]);
            categoryTrie.insert(keywordArray, category);
        }
    }


    private List<Category> initializeCategories() {
        List<Category> categories = new ArrayList<>();
        Category category1 = new Category.Builder()
                .name("Star Wars")
                .keyword("star war")
                .keyword("starwars")
                .keyword("starwar")
                .keyword("starwars")
                .keyword("r2d2")
                .keyword("may the force be with you")
                .build();

        Category category2 = new Category.Builder()
                .name("Basketball")
                .keyword("basketball")
                .keyword("nba")
                .keyword("ncaa")
                .keyword("lebron james")
                .keyword("john stokton")
                .keyword("anthony davis")
                .build();

        categories.add(category1);
        categories.add(category2);

        return categories;
    }

    public List<Category> findMatchingCategories(String text) {
        return categoryTrie.searchFullText(text);
    }


    private List<String> getCategoryKeywords(Category category) {
        List<String> keywords = new ArrayList<>();
        for (CategoryKeyword categoryKeyword : category.getKeywords()) {
            keywords.add(categoryKeyword.getKeyword().toLowerCase());
        }
        return keywords;
    }

}