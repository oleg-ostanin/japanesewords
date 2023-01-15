package com.github.olegostanin.japanesewords;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.olegostanin.japanesewords.model.WordContainer;
import com.github.olegostanin.japanesewords.service.MainActivityService;
import com.github.olegostanin.japanesewords.сontext.MainActivityContext;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final ObjectMapper mapper = new ObjectMapper();
    MainActivityService service;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        WordContainer wordContainer = null;
        try {
            final InputStream ins = getResources().openRawResource(R.raw.japanesewords);
            wordContainer = mapper.readValue(ins, WordContainer.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        final MainActivityContext context = activityContext();
        service = new MainActivityService(context, wordContainer);
        service.initModels();
        service.setQuestionContext();
    }

    public void button0(View view) {
        service.handleAnswer(0);
    }

    public void button1(View view) {
        service.handleAnswer(1);
    }

    public void button2(View view) {
        service.handleAnswer(2);
    }

    public void button3(View view) {
        service.handleAnswer(3);
    }

    public void button4(View view) {
        service.handleAnswer(4);
    }

    public void button5(View view) {
        service.handleAnswer(5);
    }

    private MainActivityContext activityContext() {
        final TextView kana = (TextView) findViewById(R.id.textView0);
        final TextView romaji = (TextView) findViewById(R.id.textView1);
        final TextView debug = (TextView) findViewById(R.id.textViewDebug);
        kana.setTextSize(32F);
        romaji.setTextSize(32F);
        debug.setTextSize(10F);

        return MainActivityContext.builder()
                .kana(kana)
                .romaji(romaji)
                .debug(debug)
                .buttons(buttons())
                .build();
    }

    private List<Button> buttons() {
        final List<Button> buttons = new ArrayList<>();
        
        buttons.add((Button) findViewById(R.id.button0));
        buttons.add((Button) findViewById(R.id.button1));
        buttons.add((Button) findViewById(R.id.button2));
        buttons.add((Button) findViewById(R.id.button3));
        buttons.add((Button) findViewById(R.id.button4));
        buttons.add((Button) findViewById(R.id.button5));
        
        return buttons;
    }
}