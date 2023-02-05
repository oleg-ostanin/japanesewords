package com.github.olegostanin.japanesewords.provider;

import org.junit.Test;


public class RandomIndexProviderTest {

    @Test
    public void testGetRandomIndex() {
        final RandomIndexProvider indexProvider = new RandomIndexProvider();
        final int[] array = new int[8];

        for (int i = 0; i < 10000000; i++) {
            int index = indexProvider.getRandomIndex(8);
            int fromArray = array[index];

            fromArray++;
            array[index] = fromArray;
        }

        for (int i = 0; i < array.length; i++) {
            System.out.println(i + " = " + array[i]);
        }
    }
}