package com.github.olegostanin.japanesewords.model;

import java.util.List;
import java.util.Objects;

public class WordModel {
    private Long id;
    private Long order;
    private String kana;
    private String kanji;
    private List<String> english;
    private String romaji;

    private Long correctAnswers = 0L;
    private Long correctAnswersInARow = 0L;
    private Long lastCorrectAnswerTs = 0L;

    private Boolean lastAnswerCorrect = true;
    private Boolean shouldBeInQueue = false;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrder() {
        return order;
    }

    public void setOrder(Long order) {
        this.order = order;
    }

    public String getKana() {
        return kana;
    }

    public void setKana(String kana) {
        this.kana = kana;
    }

    public String getKanji() {
        return kanji;
    }

    public void setKanji(String kanji) {
        this.kanji = kanji;
    }

    public List<String> getEnglish() {
        return english;
    }

    public void setEnglish(List<String> english) {
        this.english = english;
    }

    public String getRomaji() {
        return romaji;
    }

    public void setRomaji(String romaji) {
        this.romaji = romaji;
    }

    public Long getCorrectAnswers() {
        return correctAnswers;
    }

    public void setCorrectAnswers(Long correctAnswers) {
        this.correctAnswers = correctAnswers;
    }

    public Long getCorrectAnswersInARow() {
        return correctAnswersInARow;
    }

    public void setCorrectAnswersInARow(Long correctAnswersInARow) {
        this.correctAnswersInARow = correctAnswersInARow;
    }

    public Long getLastCorrectAnswerTs() {
        return lastCorrectAnswerTs;
    }

    public void setLastCorrectAnswerTs(Long lastCorrectAnswerTs) {
        this.lastCorrectAnswerTs = lastCorrectAnswerTs;
    }

    public Boolean getLastAnswerCorrect() {
        return lastAnswerCorrect;
    }

    public void setLastAnswerCorrect(Boolean lastAnswerCorrect) {
        this.lastAnswerCorrect = lastAnswerCorrect;
    }

    public Boolean getShouldBeInQueue() {
        return shouldBeInQueue;
    }

    public void setShouldBeInQueue(Boolean shouldBeInQueue) {
        this.shouldBeInQueue = shouldBeInQueue;
    }

    public void incrementCorrectAnswersInARow() {
        correctAnswersInARow++;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WordModel model = (WordModel) o;
        if (kanji != null && !kanji.isEmpty() && kanji.equals(model.kanji)) {
            return true;
        }
        return id.equals(model.id) || romaji.equals(model.romaji) || english.get(0).equals(model.english.get(0));
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, english.get(0), romaji, kanji);
    }
}
