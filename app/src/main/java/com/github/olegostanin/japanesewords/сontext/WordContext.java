package com.github.olegostanin.japanesewords.сontext;

import com.github.olegostanin.japanesewords.comparator.CorrectAnswersComparator;
import com.github.olegostanin.japanesewords.comparator.OrderComparator;
import com.github.olegostanin.japanesewords.model.WordCategory;
import com.github.olegostanin.japanesewords.model.WordContainer;
import com.github.olegostanin.japanesewords.model.WordModel;
import com.github.olegostanin.japanesewords.model.WordStat;
import com.github.olegostanin.japanesewords.model.WordStatMapWrapper;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WordContext {
    /**
     * If the number of correct answers in a row is more than this value the word is considered as learned.
     */
    static final long LEARNED = 9L;

    /**
     * If use level of a model is more than defined the model will be added to the list
     */
    static final long USE_LEVEL = 0L;

    /**
     * Number of words in current question list.
     */
    static final int QUESTION_LIST_SIZE = 50;

    final WordContainer wordContainer;
    final WordStatMapWrapper wordStatMapWrapper;

    final Comparator<WordModel> correctAnswersComparator = new CorrectAnswersComparator();
    final Comparator<WordModel> orderComparator = new OrderComparator();


    final List<WordModel> allWords = new ArrayList<>(6000);
    final List<WordModel> newWords = new ArrayList<>();
    final List<WordModel> learnedWords = new ArrayList<>();

    public void initModels() {
        allWords.clear();
        learnedWords.clear();
        newWords.clear();

        initAll();

        for (int i = 0; i < allWords.size(); i++) {
            final WordModel model = allWords.get(i);
            if (model.getCorrectAnswersInARow() <= LEARNED && newWords.size() < QUESTION_LIST_SIZE) {
                newWords.add(model);
            } else if (model.getCorrectAnswersInARow() > LEARNED) {
                learnedWords.add(model);
            }
        }

        Collections.sort(newWords, correctAnswersComparator);
        Collections.sort(learnedWords, correctAnswersComparator);
    }

    private boolean shouldBeAdded(final WordModel model) {
        if (newWords.size() >= QUESTION_LIST_SIZE) {
            return false;
        }

        if (model.getCorrectAnswersInARow() > LEARNED) {
            return false;
        }

        if (model.getUseLevel() > USE_LEVEL) {
            return false;
        }

        return true;
    }

    public void putModelInWordStatMap(final WordModel model) {
        final WordStat wordStat = WordStat.of(
                model.getId(),
                model.getCorrectAnswersInARow(),
                model.getUseLevel(),
                System.currentTimeMillis());
        wordStatMapWrapper.getWordStatMap().put(model.getId(), wordStat);
    }

    private void initAll() {
        final Map<Long, WordStat> wordStatMap = wordStatMapWrapper.getWordStatMap();

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
                    model.setUseLevel(wordStat.getU());
                    model.setLastCorrectAnswerTs(wordStat.getTs());
                }
            }
        }

        Collections.sort(allWords, orderComparator);
    }
}
