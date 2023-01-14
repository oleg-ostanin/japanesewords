package com.github.olegostanin.japanesewords;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.olegostanin.japanesewords.model.WordCategory;
import com.github.olegostanin.japanesewords.model.WordContainer;
import com.github.olegostanin.japanesewords.model.WordModel;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class MainActivity extends AppCompatActivity {

    private final ObjectMapper mapper = new ObjectMapper();
    private WordContainer wordContainer;

    private final List<WordModel> wordModels = new ArrayList<>();

    private List<Button> buttons = new ArrayList<>();

    private final int[] numbers = new int[] {0, 1, 2, 3};

    private int listSize = 0;
    private int correctButton = Integer.MAX_VALUE;

    private long counter = 0L;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            final InputStream ins = getResources().openRawResource(R.raw.japanesewords);
            wordContainer = mapper.readValue(ins, WordContainer.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        setButtons();
        setInitialContext();
    }

    public void button0(View view) {
        setContext(0);
    }

    public void button1(View view) {
        setContext(1);
    }

    public void button2(View view) {
        setContext(2);
    }

    public void button3(View view) {
        setContext(3);
    }

    private void setInitialContext() {
        for (WordCategory category : wordContainer.getCategories()) {
            wordModels.addAll(category.getWords());
        }
        listSize = wordModels.size();

        final TextView kana = (TextView) findViewById(R.id.textView0);
        final TextView romaji = (TextView) findViewById(R.id.textView1);

        kana.setTextSize(32F);
        romaji.setTextSize(32F);
        romaji.setVisibility(View.VISIBLE);

        //romaji.setTextColor(3);

        setContext(0);
    }

    private void setContext(int buttonNum) {
        if (counter > 0 && buttonNum != correctButton) {
            return;
        }

        final TextView kana = (TextView) findViewById(R.id.textView0);
        final TextView romaji = (TextView) findViewById(R.id.textView1);

        correctButton = ThreadLocalRandom.current().nextInt(buttons.size());

        for (int i = 0; i < buttons.size(); i++) {
            final WordModel model = randomModel();
            final Button button = buttons.get(i);
            button.setText(model.getEnglish());

            if (i == correctButton) {
                kana.setText(model.getKana());
                romaji.setText(model.getRomaji());
            }
        }

        counter++;
    }

    private WordModel randomModel() {
        final int modelIndex = ThreadLocalRandom.current().nextInt(wordModels.size());
        return wordModels.get(modelIndex);
    }

    private void setButtons() {
        buttons.add((Button) findViewById(R.id.button0));
        buttons.add((Button) findViewById(R.id.button1));
        buttons.add((Button) findViewById(R.id.button2));
        buttons.add((Button) findViewById(R.id.button3));
    }
}