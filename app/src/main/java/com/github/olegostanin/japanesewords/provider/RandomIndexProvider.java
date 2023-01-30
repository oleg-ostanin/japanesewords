package com.github.olegostanin.japanesewords.provider;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.concurrent.ThreadLocalRandom;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RandomIndexProvider {
    final int questionListSize;

    public int getRandomIndex() {
        final long random = ThreadLocalRandom.current().nextLong();
        final String binary = Long.toBinaryString(random);
        double startPointer = 0;
        double endPointer = (questionListSize * 4) - 1;
        int bitPosition = 0;

        while (startPointer < endPointer && bitPosition < binary.length()) {
            final double diff = endPointer - startPointer;
            final double toSubtract = diff / (1 + ThreadLocalRandom.current().nextDouble());
            final double toAdd = diff - toSubtract;
            final String bit = String.valueOf(binary.charAt(bitPosition));
            if ("0".equals(bit)) {
                startPointer += toAdd;
            } else {
                endPointer -= toSubtract;
            }
            bitPosition++;
        }
        int result = (int) Math.round(startPointer);
        while (result > questionListSize - 1) {
            result = getRandomIndex();
        }
        return result;
    }

    public int getRandomIndexOld() {
        final long random = ThreadLocalRandom.current().nextLong();
        final String binary = Long.toBinaryString(random);
        int startPointer = 0;
        int endPointer = questionListSize - 1;
        int bitPosition = 0;

        while (startPointer < endPointer && bitPosition < binary.length()) {
            final int diff = endPointer - startPointer;
            final int toSubtract = Math.max(1, diff / 4);
            final int toAdd = Math.max(1, diff - toSubtract);
            if ("0".equals(String.valueOf(binary.charAt(bitPosition)))) {
                startPointer += toAdd;
            } else {
                endPointer -= toSubtract;
            }
            bitPosition++;
        }
        return startPointer;
    }
}
