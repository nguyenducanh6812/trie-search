package com.example.peer39.urlcore.model;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.*;

@Data
@Component
public class CategoryTrie {
    private TrieNode root;

    public CategoryTrie() {
        root = new TrieNode();
    }

    public void insert(String[] keywords, Category category) {
        for (String keyword : keywords) {
            TrieNode currentNode = root;
            for (char c : keyword.toCharArray()) {
                if (!currentNode.getChildren().containsKey(c)) {
                    currentNode.getChildren().put(c, new TrieNode());
                }
                currentNode = currentNode.getChildren().get(c);
            }
            currentNode.setCategory(category);
            currentNode.setEndOfWord(true);
        }
    }
    public List<Category> searchFullText(String text) {
        List<String> keywords = splitTextIntoKeywords(text);
        TrieNode currentNode = root;
        List<Category> matchingCategories = new ArrayList<>();
        searchKeywords(keywords, currentNode, matchingCategories);
        return removeDuplicatesAndPreserveOrder(matchingCategories);
    }

    private List<Category> removeDuplicatesAndPreserveOrder(List<Category> categories) {
        Set<Category> categorySet = new LinkedHashSet<>(categories);
        return new ArrayList<>(categorySet);
    }

    private void searchKeywords(List<String> keywords, TrieNode trieNode, List<Category> matchingCategories) {
        for (String keyword : keywords) {
            TrieNode currentNode = trieNode;
            for (char c : keyword.toCharArray()) {
                if (!currentNode.hasChild(c)) {
                    break;
                }
                currentNode = currentNode.getChild(c);
            }
            if (currentNode.isEndOfWord()) {
                matchingCategories.addAll(traverse(currentNode));
            }
            if (currentNode.hasChild(' ')) {
                TrieNode currentSubNode = currentNode.getChild(' ');
                List<String> subKeywords = keywords.subList(keywords.indexOf(keyword) + 1, keywords.size());
                searchSubKeywordsInSpecificTrieNode(subKeywords, currentSubNode, matchingCategories);
            }
        }
    }

    private void searchSubKeywordsInSpecificTrieNode(List<String> subKeywords, TrieNode currentSubNode, List<Category> matchingCategories) {
        subKeywordsLoop:
        for (String keyword : subKeywords) {
            TrieNode currentNode = currentSubNode;
            for (char c : keyword.toCharArray()) {
                if (!currentNode.hasChild(c)) {
                    break subKeywordsLoop;
                }
                currentNode = currentNode.getChild(c);
                if (currentNode.isEndOfWord()) {
                    matchingCategories.addAll(traverse(currentNode));
                }
            }
        }
    }


    private List<Category> traverse(TrieNode node) {
        List<Category> categories = new ArrayList<>();

        if (node.getCategory() != null) {
            categories.add(node.getCategory());
        }
        return categories;
    }

    private List<String> splitTextIntoKeywords(String text) {
        List<String> keywords = new ArrayList<>();
        StringTokenizer tokenizer = new StringTokenizer(text);
        while (tokenizer.hasMoreTokens()) {
            keywords.add(tokenizer.nextToken().toLowerCase());
        }
        return keywords;
    }
    private List<String> splitTextIntoPhrases(String text) {
        List<String> phrases = new ArrayList<>();
        StringBuilder phraseBuilder = new StringBuilder();

        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);

            if (isPhraseSeparator(c)) {
                if (phraseBuilder.length() > 0) {
                    phrases.add(phraseBuilder.toString().trim());
                    phraseBuilder.setLength(0);
                }
            } else {
                phraseBuilder.append(c);
            }
        }

        if (phraseBuilder.length() > 0) {
            phrases.add(phraseBuilder.toString().trim());
        }

        return phrases;
    }

    private boolean isPhraseSeparator(char c) {
        return c == '.' || c == '!' || c == '?' || c == ';';
    }
}