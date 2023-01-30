package com.github.olegostanin.japanesewords.service;

import android.graphics.Color;
import android.util.Log;
import android.widget.Button;
import com.github.olegostanin.japanesewords.comparator.CorrectAnswersComparator;
import com.github.olegostanin.japanesewords.comparator.OrderComparator;
import com.github.olegostanin.japanesewords.model.WordCategory;
import com.github.olegostanin.japanesewords.model.WordContainer;
import com.github.olegostanin.japanesewords.model.WordModel;
import com.github.olegostanin.japanesewords.model.WordStat;
import com.github.olegostanin.japanesewords.provider.RandomIndexProvider;
import com.github.olegostanin.japanesewords.сontext.MainActivityContext;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ThreadLocalRandom;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MainActivityService {
    /**
     * If the number of correct answers in a row is more than this value the word is considered as learned.
     */
    static final long LEARNED = 20L;

    /**
     * Number of words in current question list.
     */
    static final int QUESTION_LIST_SIZE = 200;

    final RandomIndexProvider indexProvider = new RandomIndexProvider(QUESTION_LIST_SIZE);
    final MainActivityContext activityContext;

    final WordContainer wordContainer;

    Map<Long, WordStat> wordStatMap;

    final List<WordModel> frameWords = new ArrayList<>();
    final Queue<WordModel> mistakes = new LinkedList<>();

    final List<WordModel> allWords = new ArrayList<>(6000);
    final List<WordModel> wordsToLearn = new ArrayList<>();
    final List<WordModel> learnedWords = new ArrayList<>();
    final Comparator<WordModel> correctAnswersComparator = new CorrectAnswersComparator();
    WordModel currentWord;

    int correctButton = Integer.MAX_VALUE;

    long counter = 0L;

    long counterToPullNextMistake = -1L;
    boolean atLeastOneMistake = false;
    long rightCount = 0;

    public void setQuestionContext() {
        if (counter % 10 == 0) {
            Collections.sort(wordsToLearn, correctAnswersComparator);
        }
        frameWords.clear();
        correctButton = ThreadLocalRandom.current().nextInt(activityContext.getButtons().size());
        List<Button> buttons = activityContext.getButtons();
        for (int i = 0; i < buttons.size(); i++) {
            final Button button = buttons.get(i);
            button.setBackgroundColor(Color.GRAY);
            button.setTextSize(22F);

            final WordModel model;
            if (i == correctButton) {
                currentWord = mistakeOrUnique();
                model = currentWord;

                final String kana = currentWord.getKana();
                final String kanji = currentWord.getKanji();
                final String kanaPlusKanji = kana + "    " + currentWord.getKanji();
                final String question = (kana.length() < 7 && !kanji.isEmpty()) ? kanaPlusKanji : kana;

                activityContext.getKana().setText(question);
                activityContext.getRomaji().setText(currentWord.getRomaji());
                activityContext.getRomaji().setTextColor(Color.WHITE);
                activityContext.getDebug().setTextColor(Color.WHITE);
            } else {
                 model= uniqueModel();
            }
            if (model.getEnglish().get(0).length() > 19) {
                button.setTextSize(16F);
            }
            button.setText(model.getEnglish().get(0));
        }

        atLeastOneMistake = false;
        counter++;
    }

    public void initModels() {
        allWords.clear();
        learnedWords.clear();
        wordsToLearn.clear();

        wordStatMap = activityContext.getWordStatMapWrapper().getWordStatMap();

        for (WordCategory category : wordContainer.getCategories()) {
            for (WordModel model : category.getWords()) {
                allWords.add(model);
                final long id = model.getId();
                if (wordStatMap.containsKey(id)) {
                    final WordStat wordStat = wordStatMap.get(model.getId());
                    if (wordStat == null) {
                        continue;
                    }
                    model.setCorrectAnswersInARow(wordStat.getA());
                    model.setLastCorrectAnswerTs(wordStat.getTs());
                }
            }
        }

        Collections.sort(allWords, new OrderComparator());

        for (int i = 0; wordsToLearn.size() < QUESTION_LIST_SIZE; i++) {
            final WordModel iWord = allWords.get(i);
            if (iWord.getCorrectAnswersInARow() <= LEARNED) {
                wordsToLearn.add(allWords.get(i));
            }
            else {
                learnedWords.add(iWord);
            }
        }

        Collections.sort(wordsToLearn, correctAnswersComparator);

//        for (WordModel wordModel : wordsToLearn) {
//            Log.i("tag", wordModel.getEnglish().get(0));
//        }

        activityContext.getLearnedWords().setText(String.valueOf(learnedWords.size()));
    }

    public void handleAnswer(int buttonNum) {
        debug();
        if (buttonNum != correctButton) {
            handleIncorrectAnswer(buttonNum);
        }
        else {
            handleCorrectAnswer();
        }
    }

    private void handleCorrectAnswer() {
        if (currentWord.getLastAnswerCorrect()) {
            currentWord.incrementCorrectAnswersInARow();
            if (currentWord.getCorrectAnswersInARow() > LEARNED) {
                wordsToLearn.remove(currentWord);
                initModels();
                learnedWords.add(currentWord);
                activityContext.getLearnedWords().setText(String.valueOf(learnedWords.size()));
            }
        }
        if (currentWord.getCorrectAnswersInARow() > 5) {
            currentWord.setShouldBeInQueue(false);
        }
        if (currentWord.getShouldBeInQueue() && mistakes.size() <= 4 && !mistakes.contains(currentWord)) {
            mistakes.add(currentWord);
        }
        currentWord.setLastAnswerCorrect(true);
        currentWord.setCorrectAnswers(currentWord.getCorrectAnswers() + 1);
        rightCount++;
        activityContext.getRightCount().setText(String.valueOf(rightCount));
        putCurrentWordInWordStatMap();
        setQuestionContext();
    }

    private void handleIncorrectAnswer(final int buttonNum) {
        activityContext.getButtons().get(buttonNum).setBackgroundColor(Color.RED);

        if (atLeastOneMistake) {
            return;
        }

        currentWord.setCorrectAnswersInARow(0L);
        currentWord.setLastAnswerCorrect(false);
        currentWord.setShouldBeInQueue(true);
        if (mistakes.size() <= 5 && !mistakes.contains(currentWord)) {
            mistakes.add(currentWord);
            setCounterToPollNextMistake();
        }
        putCurrentWordInWordStatMap();
        atLeastOneMistake = true;
    }

    private void putCurrentWordInWordStatMap() {
        wordStatMap.put(currentWord.getId(), WordStat.of(currentWord.getId(), currentWord.getCorrectAnswersInARow(),
                System.currentTimeMillis()));
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
                setCounterToPollNextMistake();
                return toReturn;
            }
        }

        return uniqueModel();
    }

    private void setCounterToPollNextMistake() {
        final int toNextMistake = ThreadLocalRandom.current().nextInt(3);
        counterToPullNextMistake = counter + 2 + toNextMistake;
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
        final int modelIndex = indexProvider.getRandomIndex();
        return wordsToLearn.get(modelIndex);
    }

    private void debug() {
        StringBuilder sb = new StringBuilder();

        for (WordModel wordModel : mistakes) {
            sb.append(wordModel.getKana());
            sb.append("-");
            sb.append(wordModel.getCorrectAnswersInARow());
            sb.append(";");
        }

        String debug = sb.toString();
        activityContext.getDebug().setText(debug);
    }

    private void debugSort() {
        StringBuilder sb = new StringBuilder();

        for (WordModel wordModel : wordsToLearn) {
            String english = wordModel.getEnglish().get(0);

            if (english.length() > 7) {
                english = english.substring(0, 6);
            }

            sb.append(english);
            sb.append("-");
            sb.append(wordModel.getCorrectAnswersInARow());
            sb.append(";");
        }

        String debug = sb.toString();
        activityContext.getDebug().setText(debug);
    }

    public String meanings() {
        StringBuilder sb = new StringBuilder();

        for (String translation : currentWord.getEnglish()) {
            sb.append(translation);
            sb.append("\n");
        }

        return sb.toString();
    }

}
