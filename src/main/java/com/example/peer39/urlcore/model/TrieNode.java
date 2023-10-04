package com.example.peer39.urlcore.model;

import java.util.HashMap;
import java.util.Map;

public class TrieNode {
    private final Map<Character, TrieNode> children;
    private Category category;
    private boolean isEndOfWord;

    public TrieNode() {
        children = new HashMap<>();
        category = null;
        isEndOfWord = false;
    }

    public Map<Character, TrieNode> getChildren() {
        return children;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public boolean isEndOfWord() {
        return isEndOfWord;
    }

    public void setEndOfWord(boolean endOfWord) {
        isEndOfWord = endOfWord;
    }

    public boolean hasChild(char c) {
        return children.containsKey(c);
    }

    public TrieNode getChild(char c) {
        return children.get(c);
    }

    public void addChild(char c, TrieNode node) {
        children.put(c, node);
    }
}