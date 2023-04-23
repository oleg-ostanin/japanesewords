package com.github.olegostanin.japanesewords.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WordStat {
    private long id;

    /**
     * Number of correct answers in a row.
     */
    private long a;

    /**
     * Use level.
     */
    private long u;

    /**
     * Timestamp of the last correct answer.
     */
    private long ts;

    public static WordStat of(final long id, final long a, final long u, final long ts) {
        return new WordStat(id, a, u, ts);
    }

}
