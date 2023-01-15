package com.github.olegostanin.japanesewords.сontext;

import android.widget.Button;
import android.widget.Button;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Builder
@AllArgsConstructor
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MainActivityContext {
    Button rightCount;
    Button kana;
    Button romaji;

    Button debug;

    List<Button> buttons;
}
