package com.github.olegostanin.japanesewords.service;

import com.github.olegostanin.japanesewords.model.WordModel;
import com.github.olegostanin.japanesewords.provider.RandomProvider;
import com.github.olegostanin.japanesewords.сontext.WordContext;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ThreadLocalRandom;

import static com.github.olegostanin.japanesewords.service.LearningMode.NEW;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WordService {
    final WordContext wordContext;
    final RandomProvider randomProvider = new RandomProvider();

    final Queue<WordModel> mistakes = new LinkedList<>();

    LearningMode learningMode = NEW;

    public void initModels() {
        wordContext.initModels();
    }

    public int learnedWordsSize() {
        return wordContext.getLearnedWords().size();
    }

    public WordModel mistakeOrUnique(final List<Long> frameWords) {
        if (!randomProvider.shouldLearnFromMistakes()) {
            return uniqueModel(frameWords);
        }

        if (mistakes.isEmpty()) {
            return uniqueModel(frameWords);
        }

        final WordModel mistake = mistakes.poll();
        if (mistake != null && !frameWords.contains(mistake.getId())) {
            frameWords.add(mistake.getId());
            return mistake;
        } else {
            mistakes.add(mistake);
        }

        return uniqueModel(frameWords);
    }

    public WordModel uniqueModel(final List<Long> frameWords) {
        WordModel toReturn = model();
        while (frameWords.contains(toReturn.getId())) {
            toReturn = model();
        }
        frameWords.add(toReturn.getId());
        return toReturn;
    }

    private WordModel model() {
        final List<WordModel> questionList = learningMode == NEW
                ? wordContext.getNewWords()
                : wordContext.getLearnedWords();
        final int modelIndex = randomProvider.getRandomIndex(questionList.size());
        return questionList.get(modelIndex);
    }

    public LearningMode switchMode() {
        if (learningMode == LearningMode.NEW) {
            learningMode = LearningMode.REPEAT;
        } else {
            learningMode = LearningMode.NEW;
        }
        return learningMode;
    }

    public void handleCorrectAnswer(final WordModel model) {
        if (model.getLastAnswerCorrect()) {
            model.incrementCorrectAnswersInARow();
        }
        model.setLastAnswerCorrect(true);
        wordContext.putModelInWordStatMap(model);
    }

    public void handleIncorrectAnswer(final WordModel model) {
        model.setCorrectAnswersInARow(0L);
        model.setLastAnswerCorrect(false);
        mistakes.add(model);
        wordContext.putModelInWordStatMap(model);
    }
}
