package com.github.olegostanin.japanesewords.provider;

import java.util.concurrent.ThreadLocalRandom;

public class RandomProvider {
    private static final int LIST_SIZE_MULTIPLIER = 4;
    private static final int MISTAKE_MAGIC = 3;

    /**
     * Provides random index with higher probability to hit index in the beginning of the list.
     * For the list of size 50 and number of attempts 10kk the result hit distribution will look like this:
     *         Index:0; hits:1674195; percentage:16.74195
     *         Index:1; hits:781792; percentage:7.81792
     *         Index:2; hits:544402; percentage:5.44402
     *         Index:3; hits:438269; percentage:4.38269
     *         ...
     *         Index:46; hits:72148; percentage:0.72148
     *         Index:47; hits:69009; percentage:0.69009
     *         Index:48; hits:65023; percentage:0.65023
     *         Index:49; hits:62131; percentage:0.62131
     *
     * @param questionListSize List size.
     * @return Index.
     */
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

    /**
     * Returns true in 1/MISTAKE_MAGIC cases.
     *
     * @return boolean value with shifted probability.
     */
    public boolean shouldLearnFromMistakes() {
        return ThreadLocalRandom.current().nextInt(MISTAKE_MAGIC) == 0;
    }
}
