package com.github.olegostanin.japanesewords.сontext;

import android.widget.Button;
import android.widget.TextView;
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
    TextView kana;
    TextView romaji;

    List<Button> buttons;
}
