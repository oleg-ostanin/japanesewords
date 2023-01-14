package com.github.olegostanin.japanesewords.model;


import java.util.List;

public class WordContainer {
    public List<WordCategory> getCategories() {
        return categories;
    }

    public void setCategories(List<WordCategory> categories) {
        this.categories = categories;
    }

    private List<WordCategory> categories;
}
