package com.anaglyph.droog;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    /*
     *  Author: Harry Cramp
     *
     *  Date: November 2020
     */

    private WordPair buildWordPairFromJSON(String jsonString) {
        try {
            Log.v("BUILD FROM JSON", "WAP, BAP, BA DE BAP BAP!");
            JSONObject jsonObject = new JSONObject(jsonString);

            String firstWord = jsonObject.getString(NewFlashcard.JSON_FIELD_FIRST_WORD);
            String secondWord = jsonObject.getString(NewFlashcard.JSON_FIELD_SECOND_WORD);
            String hint = jsonObject.getString(NewFlashcard.JSON_FIELD_HINT);
            int pairRank = jsonObject.getInt(NewFlashcard.JSON_FIELD_PAIR_RANK);

            Log.v("BUILD FROM JSON", "FIRST WORD: \t" + firstWord);
            Log.v("BUILD FROM JSON", "SECOND WORD: \t" + secondWord);
            Log.v("BUILD FROM JSON", "HINT: \t" + hint);
            Log.v("BUILD FROM JSON", "PAIRRANK: \t" + pairRank);

            WordPair wordPair = new WordPair(firstWord, secondWord, hint);
            wordPair.setPairRank(pairRank);

            return wordPair;
        }catch(JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    private void loadDataFromJSONFile(File file) {
        try {
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            StringBuilder stringBuilder = new StringBuilder();
            String line = bufferedReader.readLine();
            while(line != null) {
                stringBuilder.append(line).append("\n");
                line = bufferedReader.readLine();
            }
            bufferedReader.close();
            fileReader.close();

            String response = stringBuilder.toString();
            Log.v("LOAD FROM JSON", response);
            FlashcardStore.putWordPair(buildWordPairFromJSON(response));
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadData() {
        File dataDir = getApplicationContext().getFilesDir();

        Log.v("LOAD DATA", "LOADING DATA...");

        for(String pairFile : dataDir.list()) {
            Log.v("LOAD DATA", "READING " + pairFile);
            File pairData = new File(getApplicationContext().getFilesDir(), pairFile);
            loadDataFromJSONFile(pairData);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Context context = this.getApplicationContext();
        final Button newFlashcardButton = (Button)findViewById(R.id.menuNewFlashcardButton);
        newFlashcardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, NewFlashcard.class);
                startActivity(intent);
            }
        });

        final Button quizButton = (Button)findViewById(R.id.menuQuizButton);
        quizButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, QuizMenu.class);
                startActivity(intent);
            }
        });

        final Button flashcardsButton = (Button)findViewById(R.id.menuFlashcardsButton);
        flashcardsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(FlashcardStore.isEmpty()) {
                    Toast.makeText(context, R.string.main_menu_no_flashcards_warning, Toast.LENGTH_LONG).show();
                    return;
                }
                Intent intent = new Intent(context, Flashcards.class);
                startActivity(intent);
            }
        });

        if(FlashcardStore.isEmpty())
            loadData();
    }
}
