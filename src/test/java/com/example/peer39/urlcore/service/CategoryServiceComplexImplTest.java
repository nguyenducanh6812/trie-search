package com.example.peer39.urlcore.service;

import com.example.peer39.urlcore.model.Category;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class CategoryServiceComplexImplTest {
    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private CategoryServiceComplex categoryServiceComplex;

    @Test
    @DisplayName("Should return an empty list when the given text is empty")
    void findMatchingCategoriesWhenTextIsEmpty() {
        String text = "";
        List<Category> matchingCategories = categoryServiceComplex.findMatchingCategories(text);

        assertTrue(matchingCategories.isEmpty());
    }

    @Test
    @DisplayName("Should return an empty list when no categories match the given text")
    void findMatchingCategoriesWhenNoCategoriesMatch() {
        String text = "Lorem ipsum dolor sit amet";
        List<Category> expectedCategories = new ArrayList<>();

        List<Category> matchingCategories = categoryServiceComplex.findMatchingCategories(text);

        assertEquals(expectedCategories, matchingCategories);
    }

    @Test
    @DisplayName("Should return a list with one matching category when only one category matches the given text")
    void findMatchingCategoriesWhenOneCategoryMatches() {
        String text = "Star Wars is a great movie may the force be with you";
        List<Category> expectedCategories = new ArrayList<>();
        Category starWarsCategory = new Category.Builder()
                .name("Star Wars")
                .keyword("star war")
                .keyword("starwars")
                .keyword("starwar")
                .keyword("starwars")
                .keyword("r2d2")
                .keyword("may the force be with you")
                .build();
        expectedCategories.add(starWarsCategory);

        List<Category> matchingCategories = categoryServiceComplex.findMatchingCategories(text);

        assertEquals(expectedCategories.size(), matchingCategories.size());
        assertTrue(matchingCategories.containsAll(expectedCategories));
    }

    @Test
    @DisplayName("Should return a list with multiple matching categories when multiple categories match the given text")
    void findMatchingCategoriesWhenMultipleCategoriesMatch() {
        //String text = "Star warlord";
        String text = "Star Wars is a popular movie franchise and basketball is a popular sport.";

        List<Category> expectedCategories = new ArrayList<>();
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
        expectedCategories.add(category1);
        expectedCategories.add(category2);

        List<Category> matchingCategories = categoryServiceComplex.findMatchingCategories(text);
        logger.info("Test matchingCategories: {}", matchingCategories);
        assertEquals(expectedCategories, matchingCategories);
    }

}