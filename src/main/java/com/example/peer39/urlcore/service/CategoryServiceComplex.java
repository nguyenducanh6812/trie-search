package com.example.peer39.urlcore.service;

import com.example.peer39.urlcore.model.Category;

import java.util.List;

public interface CategoryServiceComplex {
    List<Category> findMatchingCategories(String text);
}
