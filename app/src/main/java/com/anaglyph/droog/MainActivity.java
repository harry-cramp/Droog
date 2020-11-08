package com.anaglyph.droog;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    /*
     *  Author: Harry Cramp
     *
     *  Date: November 2020
     */

    private WordPair buildWordPairFromJSON(String jsonString) {
        try {
            JSONObject jsonObject = new JSONObject(jsonString);

            String firstWord = jsonObject.getString(getResources().getString(R.string.json_flashcard_first_word_tag));
            String secondWord = jsonObject.getString(getResources().getString(R.string.json_flashcard_second_word_tag));
            String hint = jsonObject.getString(getResources().getString(R.string.json_flashcard_hint_tag));
            int pairRank = jsonObject.getInt(getResources().getString(R.string.json_flashcard_pair_rank_tag));

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

        final Button flashcardsButton = (Button)findViewById(R.id.menuFlashcardsButton);
        flashcardsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Flashcards.class);
                startActivity(intent);
            }
        });

        loadData();
    }
}
