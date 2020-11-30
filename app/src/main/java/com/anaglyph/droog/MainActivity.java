package com.anaglyph.droog;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    /*
     *  Author: Harry Cramp
     *
     *  Date: November 2020
     */

    private static final String NEW_DECK_DIALOGUE_TAG = "NEW_DECK";
    private static final String DECK_SELECTOR_INDEX = "DECK_INDEX";

    public static final String FLASHCARD_EDIT_MODE_TAG = "EDIT_MODE";
    public static final String FLASHCARD_DECK_NAME = "DECK_NAME";

    private Spinner deckSelector;

    private WordPair buildWordPairFromJSON(String jsonString) {
        try {
            Log.v("BUILD FROM JSON", "WAP, BAP, BA DE BAP BAP!");
            JSONObject jsonObject = new JSONObject(jsonString);

            String firstWord = jsonObject.getString(NewFlashcard.JSON_FIELD_FIRST_WORD);
            String secondWord = jsonObject.getString(NewFlashcard.JSON_FIELD_SECOND_WORD);
            int pairRank = jsonObject.getInt(NewFlashcard.JSON_FIELD_PAIR_RANK);

            Log.v("BUILD FROM JSON", "FIRST WORD: \t" + firstWord);
            Log.v("BUILD FROM JSON", "SECOND WORD: \t" + secondWord);
            Log.v("BUILD FROM JSON", "PAIRRANK: \t" + pairRank);

            WordPair wordPair = new WordPair(firstWord, secondWord);
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

    private void loadDeckFiles(File file) {
        Log.v("FILE DIR", "LOADING: " + file.getAbsolutePath());
        for(String pairName : file.list())
            loadDataFromJSONFile(new File(file.getAbsolutePath(), pairName));
    }

    private void loadDeckData(File file) {
        for(String deckName : file.list()) {
            addNewDeck(deckName);
            FlashcardStore.selectDeck(deckName);
            Log.v("FILE DIR", "FOUND: " + deckName);
            loadDeckFiles(new File(file.getAbsolutePath() + File.separator + deckName));
        }
    }

    private void loadData() {
        File dataDir = getApplicationContext().getFilesDir();

        Log.v("LOAD DATA", "LOADING DATA...");

        loadDeckData(dataDir);
    }

    public void addNewDeck(String deckName) {
        // add deck name to spinner
        String[] existingDeckNames = FlashcardStore.getDeckNames();
        String[] extendedList = new String[existingDeckNames.length + 1];

        for(int i = 0; i < existingDeckNames.length; i++)
            extendedList[i] = existingDeckNames[i];
        extendedList[extendedList.length - 1] = deckName;

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, extendedList);
        deckSelector.setAdapter(adapter);

        // add deck to flashcards store
        FlashcardStore.initDeck(deckName);

        // create persistence directory
        File dir = new File(getFilesDir(), deckName);
        if(!dir.exists())
            dir.mkdir();
    }

    private void loadSpinnerData() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, FlashcardStore.getDeckNames());
        deckSelector.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();

        loadSpinnerData();

        deckSelector.setSelection(getIntent().getIntExtra(DECK_SELECTOR_INDEX, 0));
    }

    @Override
    protected void onPause() {
        super.onPause();

        getIntent().putExtra(DECK_SELECTOR_INDEX, deckSelector.getSelectedItemPosition());
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
                intent.putExtra(FLASHCARD_EDIT_MODE_TAG, false);
                intent.putExtra(FLASHCARD_DECK_NAME, (String)deckSelector.getSelectedItem());
                startActivity(intent);
            }
        });

        final Button quizButton = (Button)findViewById(R.id.menuQuizButton);
        quizButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, QuizMenu.class);
                intent.putExtra(FLASHCARD_DECK_NAME, (String)deckSelector.getSelectedItem());
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
                intent.putExtra(FLASHCARD_DECK_NAME, (String)deckSelector.getSelectedItem());
                startActivity(intent);
            }
        });

        final Button newDeckButton = findViewById(R.id.menuNewDeckButton);
        newDeckButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new NewDeckDialogue(MainActivity.this).show(getSupportFragmentManager(), NEW_DECK_DIALOGUE_TAG);
            }
        });

        deckSelector = findViewById(R.id.menuDeckSelector);

        if(FlashcardStore.isEmpty())
            loadData();
    }
}
