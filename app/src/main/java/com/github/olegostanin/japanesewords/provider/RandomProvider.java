package com.github.olegostanin.japanesewords.provider;

import java.util.concurrent.ThreadLocalRandom;

public class RandomProvider {
    private static final int LIST_SIZE_MULTIPLIER = 4;
    private static final int MISTAKE_MAGIC = 3;

    public int getRandomIndex(final int questionListSize) {
        final long random = ThreadLocalRandom.current().nextLong();
        final String binary = Long.toBinaryString(random);
        double startPointer = 0;
        double endPointer = (questionListSize * LIST_SIZE_MULTIPLIER) - 1;
        int bitPosition = 0;

        while (startPointer < (endPointer - 1) && bitPosition < binary.length()) {
            final double diff = endPointer - startPointer;
            final double toSubtract = diff / (1 + ThreadLocalRandom.current().nextDouble());
            if ('0' == binary.charAt(bitPosition)) {
                final double toAdd = diff - toSubtract;
                startPointer += toAdd;
            } else {
                endPointer -= toSubtract;
            }
            bitPosition++;
        }

        int result = (int) Math.round(startPointer);
        while (result > questionListSize - 1) {
            // The possibility of getting here is quite low so recursive call is ok.
            result = getRandomIndex(questionListSize);
        }
        return result;
    }

    public boolean shouldLearnFromMistakes() {
        return ThreadLocalRandom.current().nextInt(MISTAKE_MAGIC) == 0;
    }
}
