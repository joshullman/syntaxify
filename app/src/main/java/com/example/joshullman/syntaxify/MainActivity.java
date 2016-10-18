package com.example.joshullman.syntaxify;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private TextView resultTEXT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        resultTEXT = (TextView)findViewById(R.id.TVresult);
    }

    public void onButtonClick(View view) {
        if(view.getId() == R.id.imageButton) {
            promptSpeechInput();
        }
    }

    public void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Say something!");

        try {
            startActivityForResult(intent, 100);
        }
        catch(ActivityNotFoundException a) {
            Toast.makeText(MainActivity.this, "Sorry, your device doesn't support speech language!", Toast.LENGTH_LONG).show();
        }
    }

    public void onActivityResult(int request_code, int result_code, Intent intent) {
        super.onActivityResult(request_code, result_code, intent);

        switch(request_code) {
            case 100: if(result_code == RESULT_OK && intent != null) {
                ArrayList<String> result = intent.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                resultTEXT.setText(syntaxify(result.get(0)));
            }
                break;
        }
    }

    public HashMap createSpokenReplacements() {
        HashMap<String, String> spokenReplacements = new HashMap<>();

            spokenReplacements.put(" end quote", "\"");
            spokenReplacements.put("quote ", "\"");
            spokenReplacements.put("enter", "\n");
            spokenReplacements.put("tab", "\t");
            spokenReplacements.put("period ", ". ");
            spokenReplacements.put("comma ", ",");
            spokenReplacements.put("exclamation point ", "! ");
            spokenReplacements.put("question mark ", "? ");
            spokenReplacements.put("ampersand", "&");
            spokenReplacements.put("modulo", "%");
            spokenReplacements.put(" end parentheses", ")");
            spokenReplacements.put("parentheses ", "(");
            spokenReplacements.put("dash", "-");
            spokenReplacements.put(" semi colon", ";");
            spokenReplacements.put(" colon", ":");
            spokenReplacements.put(" end brackets", "]");
            spokenReplacements.put("brackets", "[");
            spokenReplacements.put(" slash ", "/");
            spokenReplacements.put(" asterisk", "*");
            spokenReplacements.put(" underscore ", "_");

        return spokenReplacements;
    }

    public HashMap createBackslashReplacements() {
        HashMap<String, String> backslashReplacements = new HashMap<>();

            backslashReplacements.put("backslash \"", "quote");
            backslashReplacements.put("backslash \n", "enter");
            backslashReplacements.put("backslash \t", "tab");
            backslashReplacements.put("backslash .", "period");
            backslashReplacements.put("backslash ,", "comma");
            backslashReplacements.put("backslash !", "exclamation point");
            backslashReplacements.put("backslash ?", "question mark");
            backslashReplacements.put("backslash &", "ampersand");
            backslashReplacements.put("backslash %", "modulo");
            backslashReplacements.put("backslash )", "end parentheses");
            backslashReplacements.put("backslash (", "parentheses");
            backslashReplacements.put("backslash -", "dash");
            backslashReplacements.put("backslash ;", "semi colon");
            backslashReplacements.put("backslash :", "colon");
            backslashReplacements.put("backslash ]", "end brackets");
            backslashReplacements.put("backslash [", "brackets");
            backslashReplacements.put("backslash /", "slash");
            backslashReplacements.put("backslash *", "asterisk");
            backslashReplacements.put("backslash _", "underscore");


        return backslashReplacements;

    }

    public String syntaxify(String formattedString) {
        HashMap spokenReplacements = createSpokenReplacements();
        HashMap backslashReplacements = createBackslashReplacements();

        Iterator spokenIt = spokenReplacements.entrySet().iterator();
        Iterator backslashIt = backslashReplacements.entrySet().iterator();

        while (spokenIt.hasNext()) {
            Map.Entry pair = (Map.Entry)spokenIt.next();
            formattedString = formattedString.replace(pair.getKey().toString(), pair.getValue().toString());
            spokenIt.remove();
        }

        while (backslashIt.hasNext()) {
            Map.Entry pair = (Map.Entry)backslashIt.next();
            formattedString = formattedString.replace(pair.getKey().toString(), pair.getValue().toString());
            backslashIt.remove();
        }

        return formattedString;
    }
}
