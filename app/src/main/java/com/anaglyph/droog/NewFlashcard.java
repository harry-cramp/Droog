package com.anaglyph.droog;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class NewFlashcard extends AppCompatActivity {

    public static final int NEW_FLASHCARD_MAX_LENGTH = 120;

    private boolean editMode;

    private String editFlashcardFirstWord;
    private String deckName;

    public static final String JSON_FIELD_FIRST_WORD = "Word 1";
    public static final String JSON_FIELD_SECOND_WORD = "Word 2";
    public static final String JSON_FIELD_PAIR_RANK = "PairRank";

    private static JSONObject buildJSONObject(WordPair wordPair) {
        try {
            JSONObject pairJSON = new JSONObject();
            pairJSON.put(JSON_FIELD_FIRST_WORD, wordPair.getFirstWord());
            pairJSON.put(JSON_FIELD_SECOND_WORD, wordPair.getSecondWord());
            pairJSON.put(JSON_FIELD_PAIR_RANK, wordPair.getPairRank());
            return pairJSON;
        }catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    private static String writeJSONObject(JSONObject jsonObject, File filesDir, String deckName) {
        String jsonString = jsonObject.toString();

        try {
            File file = new File(filesDir, deckName + File.separator + (String)jsonObject.get("Word 1"));
            Log.v("FILE DIR", file.getAbsolutePath());
            File saveDir = new File(filesDir, deckName);
            if(!saveDir.exists())
                saveDir.mkdir();
            FileWriter fileWriter = new FileWriter(file);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(jsonString);
            bufferedWriter.close();
            fileWriter.close();

            return jsonString;
        }catch(JSONException | IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void storeWordPairData(WordPair wordPair, File filesDir, String deckName) {
        writeJSONObject(buildJSONObject(wordPair), filesDir, deckName);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.new_flashcard);

        deckName = getIntent().getStringExtra(MainActivity.FLASHCARD_DECK_NAME);
        FlashcardStore.selectDeck(deckName);

        final Intent intent = getIntent();
        editMode = intent.getBooleanExtra(MainActivity.FLASHCARD_EDIT_MODE_TAG, false);

        // Handle interactions with page elements

        // warn user if button is pressed without filling in either text box
        final TextView firstWordBox = (TextView)findViewById(R.id.newFlashcardWord1);
        final TextView secondWordBox = (TextView)findViewById(R.id.newFlashcardWord2);
        final TextView maxLengthWarningBox = findViewById(R.id.charLimitWarningText);
        final Button newFlashcardButton = (Button)findViewById(R.id.newFlashcardButton);

        if(editMode) {
            newFlashcardButton.setText(R.string.new_flashcard_edit_button_text);

            firstWordBox.setText(intent.getStringExtra(Flashcards.FLASHCARD_FIRST_WORD_TAG));
            secondWordBox.setText(intent.getStringExtra(Flashcards.FLASHCARD_SECOND_WORD_TAG));

            editFlashcardFirstWord = intent.getStringExtra(Flashcards.FLASHCARD_FIRST_WORD_TAG);
        }

        final Context context = this.getApplicationContext();
        newFlashcardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("NEW FLASHCARD ON CLICK", "Button clicked");

                if(editMode) {
                    // delete edited word pair
                    Log.v("NEW FLASHCARD", "Selecting deck: " + deckName);
                    FlashcardStore.selectDeck(deckName);
                    WordPair pair = FlashcardStore.getPairFromWord(editFlashcardFirstWord);
                    FlashcardStore.deleteWordPair(getFilesDir(), pair);

                    // create new word pair
                    WordPair newPair = new WordPair(firstWordBox.getText().toString(), secondWordBox.getText().toString());
                    newPair.setPairRank(pair.getPairRank());
                    FlashcardStore.putWordPair(newPair);
                    storeWordPairData(newPair, getFilesDir(), intent.getStringExtra(MainActivity.FLASHCARD_DECK_NAME));

                    // exit activity and return to flashcard review section
                    finish();
                    return;
                }

                String firstWord = firstWordBox.getText().toString();
                if(firstWord.equals(R.string.empty_string)) {
                    firstWordBox.setHint(R.string.empty_text_field_warning);
                    firstWordBox.setHintTextColor(getResources().getColor(R.color.colorWarning, null));
                }else if(FlashcardStore.wordExists(firstWord)) {
                    Toast.makeText(context, R.string.new_flashcard_exists_toast, Toast.LENGTH_SHORT).show();
                    return;
                }else {
                    if(firstWord.length() > NEW_FLASHCARD_MAX_LENGTH) {
                        maxLengthWarningBox.setVisibility(View.VISIBLE);
                        return;
                    }
                    firstWordBox.setHint(R.string.new_flashcard_word_1);
                    firstWordBox.setHintTextColor(getResources().getColor(R.color.colorHint, null));
                }

                String secondWord = secondWordBox.getText().toString();
                if(secondWord.equals(R.string.empty_string)) {
                    secondWordBox.setHint(R.string.empty_text_field_warning);
                    secondWordBox.setHintTextColor(getResources().getColor(R.color.colorWarning, null));
                    return;
                }else if(FlashcardStore.wordExists(secondWord)) {
                    Toast.makeText(context, R.string.new_flashcard_exists_toast, Toast.LENGTH_SHORT).show();
                    return;
                }else {
                    if(secondWord.length() > NEW_FLASHCARD_MAX_LENGTH) {
                        maxLengthWarningBox.setVisibility(View.VISIBLE);
                        return;
                    }
                    secondWordBox.setHint(R.string.new_flashcard_word_2);
                    secondWordBox.setHintTextColor(getResources().getColor(R.color.colorHint, null));
                }

                maxLengthWarningBox.setVisibility(View.GONE);

                WordPair wordPair = new WordPair(firstWord, secondWord);

                // write the new word pair to file
                writeJSONObject(buildJSONObject(wordPair), getApplicationContext().getFilesDir(), intent.getStringExtra(MainActivity.FLASHCARD_DECK_NAME));

                // if neither box is empty, add strings to flashcard store and clear text
                FlashcardStore.putWordPair(wordPair);
                firstWordBox.setText(R.string.empty_string);
                secondWordBox.setText(R.string.empty_string);
                Toast.makeText(context, R.string.new_flashcard_toast, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.action_load_from_file:
                Intent intent = new Intent(this, LoadFlashcardsFromFile.class);
                intent.putExtra(MainActivity.FLASHCARD_DECK_NAME, deckName);
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.new_flashcard_menu, menu);
        return true;
    }

}
