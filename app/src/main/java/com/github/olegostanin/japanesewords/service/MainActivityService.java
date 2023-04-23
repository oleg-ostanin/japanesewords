package com.github.olegostanin.japanesewords.service;

import android.graphics.Color;
import android.widget.Button;
import com.github.olegostanin.japanesewords.model.WordModel;
import com.github.olegostanin.japanesewords.сontext.MainActivityContext;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MainActivityService {

    final MainActivityContext activityContext;
    final WordService wordService;

    final List<Long> frameWords = new ArrayList<>();

    WordModel currentWord;

    int correctButton;

    boolean atLeastOneMistake = false;
    long rightCount = 0;

    public void setQuestionContext() {
        frameWords.clear();
        correctButton = ThreadLocalRandom.current().nextInt(activityContext.getButtons().size());
        List<Button> buttons = activityContext.getButtons();
        for (int i = 0; i < buttons.size(); i++) {
            final Button button = buttons.get(i);
            button.setBackgroundColor(Color.GRAY);
            button.setTextSize(22F);

            final WordModel model;
            if (i == correctButton) {
                currentWord = setMainModel();
                model = currentWord;
            } else {
                model = wordService.uniqueModel(frameWords);
            }
            if (model.getEnglish().get(0).length() > 19) {
                button.setTextSize(16F);
            }
            button.setText(model.getEnglish().get(0));
        }

        atLeastOneMistake = false;
    }

    public void initModels() {
        wordService.initModels();

        activityContext.getLearnedWords().setText(String.valueOf(wordService.learnedWordsSize()));
    }

    public void cheat() {
        currentWord.setCorrectAnswersInARow(11L);
        wordService.handleCorrectAnswer(currentWord);
        wordService.initModels();
        setQuestionContext();
    }

    public void useLevel() {
        final long currentUseLevel = currentWord.getUseLevel();
        final long newUseLevel = currentUseLevel + 1;
        currentWord.setUseLevel(newUseLevel);
    }

    public void switchMode() {
        final LearningMode learningMode = wordService.switchMode();
        if (learningMode == LearningMode.REPEAT) {
            activityContext.getRightCount().setTextColor(Color.GRAY);
        } else {
            activityContext.getRightCount().setTextColor(0xFF00FF99);
        }
        setQuestionContext();
    }

    public void handleAnswer(int buttonNum) {
        if (buttonNum != correctButton) {
            handleIncorrectAnswer(buttonNum);
        } else {
            handleCorrectAnswer();
        }
    }

    public String meanings() {
        StringBuilder sb = new StringBuilder();

        for (String translation : currentWord.getEnglish()) {
            sb.append(translation);
            sb.append("\n");
        }

        return sb.toString();
    }

    private void handleCorrectAnswer() {
        wordService.handleCorrectAnswer(currentWord);
        rightCount++;
        activityContext.getRightCount().setText(String.valueOf(rightCount));
        setQuestionContext();
    }

    private void handleIncorrectAnswer(final int buttonNum) {
        activityContext.getButtons().get(buttonNum).setBackgroundColor(Color.RED);

        if (atLeastOneMistake) {
            return;
        }

        wordService.handleIncorrectAnswer(currentWord);
        atLeastOneMistake = true;
    }

    public WordModel setMainModel() {
        final WordModel mainModel = wordService.mistakeOrUnique(frameWords);

        final String kana = mainModel.getKana();
        final String kanji = mainModel.getKanji();
        final String kanaPlusKanji = kana + "    " + mainModel.getKanji();

        // If there is a doubt that kanaPlusKanji fits on the screen we use just kana.
        final String question = (kana.length() < 7 && !kanji.isEmpty()) ? kanaPlusKanji : kana;

        activityContext.getKana().setText(question);
        activityContext.getRomaji().setText(mainModel.getRomaji());
        activityContext.getRomaji().setTextColor(Color.WHITE);
        activityContext.getDebug().setTextColor(Color.WHITE);

        return mainModel;
    }
}
