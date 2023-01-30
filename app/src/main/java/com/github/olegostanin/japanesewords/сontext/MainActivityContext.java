package com.github.olegostanin.japanesewords.сontext;

import android.widget.Button;
import android.widget.Button;
import com.github.olegostanin.japanesewords.model.WordStat;
import com.github.olegostanin.japanesewords.model.WordStatMapWrapper;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Builder
@AllArgsConstructor
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MainActivityContext {
    WordStatMapWrapper wordStatMapWrapper;

    Button rightCount;
    Button learnedWords;
    Button kana;
    Button romaji;

    Button debug;

    List<Button> buttons;
}
