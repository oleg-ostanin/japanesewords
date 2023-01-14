package com.github.olegostanin.japanesewords.model;

import java.util.List;

public class WordCategory {
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<WordModel> getWords() {
        return words;
    }

    public void setWords(List<WordModel> words) {
        this.words = words;
    }

    private String name;
    private List<WordModel> words;
}
