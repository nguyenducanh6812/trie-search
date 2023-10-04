package com.example.peer39.urlcore.service;

import com.example.peer39.urlcore.model.Category;

import java.util.List;

public interface CategoryService {
    List<Category> getAllCategories();
    Category getCategoryByName(String categoryName);
    void addCategory(Category category);
    void deleteCategory(String categoryName);
    public List<Category> findMatchingCategories(String text);
}

