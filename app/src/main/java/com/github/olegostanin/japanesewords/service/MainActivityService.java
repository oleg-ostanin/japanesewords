package com.github.olegostanin.japanesewords.service;

import android.graphics.Color;
import android.widget.Button;
import com.github.olegostanin.japanesewords.model.WordCategory;
import com.github.olegostanin.japanesewords.model.WordContainer;
import com.github.olegostanin.japanesewords.model.WordModel;
import com.github.olegostanin.japanesewords.сontext.MainActivityContext;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ThreadLocalRandom;

@Builder
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MainActivityService {
    final MainActivityContext activityContext;

    final WordContainer wordContainer;

    final List<WordModel> frameWords = new ArrayList<>();
    final Queue<WordModel> mistakes = new LinkedList<>();

    final List<WordModel> wordModels = new ArrayList<>();
    WordModel currentWord;

    int listSize = 0;
    int correctButton = Integer.MAX_VALUE;

    long counter = 0L;
    long counterToPullNextMistake = 0L;

    public void setQuestionContext() {
        frameWords.clear();
        correctButton = ThreadLocalRandom.current().nextInt(activityContext.getButtons().size());
        List<Button> buttons = activityContext.getButtons();
        for (int i = 0; i < buttons.size(); i++) {
            final Button button = buttons.get(i);
            button.setBackgroundColor(Color.GRAY);
            button.setTextSize(24F);

            final WordModel model;
            if (i == correctButton) {
                currentWord = mistakeOrUnique();
                model = currentWord;
                activityContext.getKana().setText(currentWord.getKana());
                activityContext.getRomaji().setText(currentWord.getRomaji());
            } else {
                 model= uniqueModel();
            }
            button.setText(model.getEnglish());
        }

        counter++;
    }

    public void initModels() {
        for (WordCategory category : wordContainer.getCategories()) {
            wordModels.addAll(category.getWords());
        }
        listSize = wordModels.size();
    }

    public void handleAnswer(int buttonNum) {
        if (buttonNum != correctButton) {
            handleIncorrectAnswer(buttonNum);
        }
        else {
            handleCorrectAnswer();
        }
    }

    private void handleCorrectAnswer() {
        if (currentWord.getLastAnswerCorrect()) {
            currentWord.setCorrectAnswersInARow(currentWord.getCorrectAnswersInARow() + 1);
        }
        if (currentWord.getCorrectAnswersInARow() > 7) {
            currentWord.setShouldBeInQueue(false);
        }
        if (currentWord.getShouldBeInQueue() && mistakes.size() <= 5) {
            mistakes.add(currentWord);
        }
        currentWord.setCorrectAnswers(currentWord.getCorrectAnswers() + 1);
        setQuestionContext();
    }

    private void handleIncorrectAnswer(final int buttonNum) {
        activityContext.getButtons().get(buttonNum).setBackgroundColor(Color.RED);
        if (!currentWord.getLastAnswerCorrect()) {
            currentWord.setIncorrectAnswersInARow(currentWord.getIncorrectAnswersInARow() + 1);
        }
        currentWord.setIncorrectAnswers(currentWord.getIncorrectAnswers() + 1);
        currentWord.setLastAnswerCorrect(false);
        currentWord.setShouldBeInQueue(true);
        if (mistakes.size() <= 5) {
            mistakes.add(currentWord);
        }
    }

    private WordModel mistakeOrUnique() {
        if (counter != counterToPullNextMistake) {
            return uniqueModel();
        }
        WordModel toReturn;
        if (!mistakes.isEmpty()) {
            toReturn = mistakes.poll();
            if (!frameWords.contains(toReturn)) {
                frameWords.add(toReturn);
                final int toNextMistake = ThreadLocalRandom.current().nextInt(3);
                counterToPullNextMistake = counterToPullNextMistake + 2 + toNextMistake;
                return toReturn;
            }
        }

        return uniqueModel();
    }

    private WordModel uniqueModel() {
        WordModel toReturn = randomModel();
        while (frameWords.contains(toReturn)) {
            toReturn = randomModel();
        }
        frameWords.add(toReturn);
        return toReturn;
    }

    private WordModel randomModel() {
        final int modelIndex = ThreadLocalRandom.current().nextInt(wordModels.size());
        return wordModels.get(modelIndex);
    }

}
