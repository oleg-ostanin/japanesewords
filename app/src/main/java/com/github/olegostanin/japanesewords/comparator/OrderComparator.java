package com.github.olegostanin.japanesewords.comparator;

import com.github.olegostanin.japanesewords.model.WordModel;

import java.util.Comparator;

public class OrderComparator implements Comparator<WordModel> {
    @Override
    public int compare(WordModel o1, WordModel o2) {
        if (o1.getOrder() == 0L && o2.getOrder() == 0L) {
            return 0;
        }

        if (o1.getOrder() == 0L) {
            return 1;
        }

        if (o2.getOrder() == 0L) {
            return -1;
        }
        return (int) (o1.getOrder() - o2.getOrder());
    }
}
