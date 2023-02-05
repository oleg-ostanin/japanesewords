package com.github.olegostanin.japanesewords.provider;

import org.junit.Test;


public class RandomProviderTest {

    @Test
    public void testGetRandomIndex() {
        final RandomProvider indexProvider = new RandomProvider();
        final int size = 50;
        final int attempts = 10000000;
        final int[] array = new int[size];

        for (int i = 0; i < attempts; i++) {
            int index = indexProvider.getRandomIndex(size);
            int fromArray = array[index];

            fromArray++;
            array[index] = fromArray;
        }

        double total = 0;

        for (int i = 0; i < array.length; i++) {
            final int hits = array[i];

            final double percentage = (double) hits / ((double) attempts / 100);
            total += percentage;

            System.out.println(i + " : " + hits + " : " + percentage);
        }

        System.out.println(total);
    }
}