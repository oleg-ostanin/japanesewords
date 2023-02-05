package com.github.olegostanin.japanesewords;

import android.graphics.Color;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.olegostanin.japanesewords.model.WordContainer;
import com.github.olegostanin.japanesewords.model.WordStat;
import com.github.olegostanin.japanesewords.model.WordStatMapWrapper;
import com.github.olegostanin.japanesewords.service.MainActivityService;
import com.github.olegostanin.japanesewords.сontext.MainActivityContext;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final String WORD_STAT_DIR = "/JapaneseWordStat";
    private static final String WORD_STAT_FILE = "japaneseWordStat.json";
    private final ObjectMapper mapper = new ObjectMapper();
    MainActivityService service;

    private WordStatMapWrapper wordStatMapWrapper;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        WordContainer wordContainer = readWordContainer();
        readWordStatMap();

        final MainActivityContext context = activityContext();
        service = new MainActivityService(context, wordContainer);
        service.initModels();
        service.setQuestionContext();
    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//        readWordStatMap();
//        service.initModels();
//        service.setQuestionContext();
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        writeWordStatMap();
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        writeWordStatMap();
    }

    private WordContainer readWordContainer() {
        try {
            final InputStream ins = getResources().openRawResource(R.raw.japanesewords4999new);
            return mapper.readValue(ins, WordContainer.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new WordContainer();
    }

    private void readWordStatMap() {
        try {
            File sdCard = Environment.getExternalStorageDirectory();
            File dir = new File (sdCard.getAbsolutePath() + WORD_STAT_DIR);
            File file = new File(dir, WORD_STAT_FILE);

            if(!file.exists()) {
                wordStatMapWrapper = new WordStatMapWrapper(new HashMap<>());
                return;
            }

            FileInputStream f = new FileInputStream(file);

            wordStatMapWrapper = mapper.readValue(f, WordStatMapWrapper.class);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void writeWordStatMap() {
        try {
            File sdCard = Environment.getExternalStorageDirectory();
            File dir = new File (sdCard.getAbsolutePath() + WORD_STAT_DIR);
            dir.mkdirs();
            File file = new File(dir, WORD_STAT_FILE);

            FileOutputStream f = new FileOutputStream(file);

            mapper.writeValue(f, wordStatMapWrapper);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void cheat(View view) {
        service.cheat();
    }

    public void switchMode(View view) {
        service.switchMode();
    }

    public void romajiVisible(View view) {
        final Button romaji = (Button) findViewById(R.id.textView1);
        romaji.setTextColor(Color.BLACK);
    }

    public void debugVisible(View view) {
        final Button debug = (Button) findViewById(R.id.textViewDebug);
        debug.setTextColor(Color.BLACK);
    }

    public void meanings(View view) {
        final String meanings = service.meanings();
        Toast.makeText(this, meanings, Toast.LENGTH_LONG).show();
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
        final Button kana = (Button) findViewById(R.id.textView0);
        final Button romaji = (Button) findViewById(R.id.textView1);
        final Button rightCount = (Button) findViewById(R.id.textViewRightCount);
        final Button learnedWords = (Button) findViewById(R.id.textViewLearnedWords);
        final Button debug = (Button) findViewById(R.id.textViewDebug);
        rightCount.setBackgroundColor(Color.WHITE);
        rightCount.setTextSize(24F);
        rightCount.setGravity(5);
        rightCount.setTextColor(0xFF00FF99);

        learnedWords.setBackgroundColor(Color.WHITE);
        learnedWords.setTextSize(24F);
        learnedWords.setGravity(5);
        learnedWords.setTextColor(0xFF00FF99);

        kana.setTextSize(32F);
        kana.setTextColor(Color.BLACK);

        kana.setBackgroundColor(Color.WHITE);
        romaji.setTextSize(26F);
        romaji.setBackgroundColor(Color.WHITE);
        debug.setTextSize(10F);
        debug.setTextColor(Color.BLACK);
        debug.setBackgroundColor(Color.WHITE);

        return MainActivityContext.builder()
                .wordStatMapWrapper(wordStatMapWrapper)
                .rightCount(rightCount)
                .learnedWords(learnedWords)
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