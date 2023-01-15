package com.github.olegostanin.japanesewords.model;

import java.util.Objects;

public class WordModel {
    private Long id;
    private String kana;
    private String kanji;
    private String english;
    private String romaji;

    private Long correctAnswers = 0L;
    private Long correctAnswersInARow = 0L;
    private Long lastCorrectAnswerTs = 0L;
    private Long incorrectAnswers = 0L;
    private Long incorrectAnswersInARow = 0L;
    private Long lastIncorrectAnswerTs = 0L;

    private Boolean lastAnswerCorrect = false;
    private Boolean shouldBeInQueue = false;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getEnglish() {
        return english;
    }

    public void setEnglish(String english) {
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

    public Long getIncorrectAnswers() {
        return incorrectAnswers;
    }

    public void setIncorrectAnswers(Long incorrectAnswers) {
        this.incorrectAnswers = incorrectAnswers;
    }

    public Long getIncorrectAnswersInARow() {
        return incorrectAnswersInARow;
    }

    public void setIncorrectAnswersInARow(Long incorrectAnswersInARow) {
        this.incorrectAnswersInARow = incorrectAnswersInARow;
    }

    public Long getLastIncorrectAnswerTs() {
        return lastIncorrectAnswerTs;
    }

    public void setLastIncorrectAnswerTs(Long lastIncorrectAnswerTs) {
        this.lastIncorrectAnswerTs = lastIncorrectAnswerTs;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WordModel model = (WordModel) o;
        return id.equals(model.id) || romaji.equals(model.romaji) || english.equals(model.english);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, english, romaji);
    }
}
