package com.example.peer39.urlcore.service;

import com.example.peer39.urlcore.model.Category;
import com.example.peer39.urlcore.model.CategoryKeyword;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);
    private final List<Category> categories;

    public CategoryServiceImpl() {
        this.categories = new ArrayList<>();
        initializeCategories();
    }

    @Override
    public List<Category> getAllCategories() {
        return categories;
    }

    @Override
    public Category getCategoryByName(String categoryName) {
        return categories.stream()
                .filter(category -> category.getName().equalsIgnoreCase(categoryName))
                .findFirst()
                .orElse(null);
    }

    @Override
    public void addCategory(Category category) {
        categories.add(category);
    }

    @Override
    public void deleteCategory(String categoryName) {
        categories.removeIf(category -> category.getName().equalsIgnoreCase(categoryName));
    }
    public List<Category> findMatchingCategories(String text) {
        List<Category> matchingCategories = new ArrayList<>();
        for (Category category : categories) {
            if (categoryContainsKeywords(category, text)) {
                matchingCategories.add(category);
            }
        }
        if (matchingCategories.isEmpty()){
            matchingCategories.add(new Category("Uncategorized"));
        }

        return matchingCategories;
    }
    private boolean categoryContainsKeywords(Category category, String text) {
        for (CategoryKeyword keyword : category.getKeywords()) {
            logger.info("Category: {}", category.getName());
            logger.info("Text: {}", text);
            if (textContainsKeyword(text, keyword.getKeyword())) {
                return true;
            }
        }
        return false;
    }

    private boolean textContainsKeyword(String text, String keyword) {
        String[] words = text.toLowerCase().split("\\s+");
        for (String word : words) {
            if (word.equals(keyword.toLowerCase())) {
                return true;
            }
        }
        return false;
    }
    private void initializeCategories() {
        Category starWarsCategory = new Category.Builder()
                .name("Star Wars")
                .keyword("Star Wars")
                .keyword("starwars")
                .keyword("star war")
                .keyword("starwars")
                .keyword("r2d2")
                .keyword("may the force be with you")
                .build();
        categories.add(starWarsCategory);

        Category basketballCategory = new Category.Builder()
                .name("Basketball")
                .keyword("basketball")
                .keyword("nba")
                .keyword("ncaa")
                .keyword("lebron james")
                .keyword("john stokton")
                .keyword("anthony davis")
                .build();
        categories.add(basketballCategory);
    }
}

