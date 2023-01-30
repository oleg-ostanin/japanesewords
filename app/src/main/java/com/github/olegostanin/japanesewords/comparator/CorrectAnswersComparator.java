package com.github.olegostanin.japanesewords.comparator;

import com.github.olegostanin.japanesewords.model.WordModel;

import java.util.Comparator;

public class CorrectAnswersComparator implements Comparator<WordModel> {
    @Override
    public int compare(WordModel o1, WordModel o2) {
        if (o1.getCorrectAnswersInARow().equals(o2.getCorrectAnswersInARow())) {
            return (int) (o1.getLastCorrectAnswerTs() - o2.getLastCorrectAnswerTs());
        }
        return (int) (o1.getCorrectAnswersInARow() - o2.getCorrectAnswersInARow());
    }
}
