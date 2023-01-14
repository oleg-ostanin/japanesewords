package com.github.olegostanin.japanesewords;

import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.olegostanin.japanesewords.model.WordContainer;

import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    private final ObjectMapper mapper = new ObjectMapper();
    private WordContainer wordContainer;

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
    }

    public void sendMessage(View view) {
        final TextView helloTextView = (TextView) findViewById(R.id.textView);
        helloTextView.setText("R.string.user_greeting");
    }
}