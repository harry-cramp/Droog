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

    public static final String JSON_FIELD_FIRST_WORD = "Word 1";
    public static final String JSON_FIELD_SECOND_WORD = "Word 2";
    public static final String JSON_FIELD_HINT = "Hint";
    public static final String JSON_FIELD_PAIR_RANK = "PairRank";

    private static JSONObject buildJSONObject(WordPair wordPair) {
        try {
            JSONObject pairJSON = new JSONObject();
            pairJSON.put(JSON_FIELD_FIRST_WORD, wordPair.getFirstWord());
            pairJSON.put(JSON_FIELD_SECOND_WORD, wordPair.getSecondWord());
            pairJSON.put(JSON_FIELD_HINT, wordPair.getHint());
            pairJSON.put(JSON_FIELD_PAIR_RANK, wordPair.getPairRank());
            return pairJSON;
        }catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    private static String writeJSONObject(JSONObject jsonObject, File filesDir) {
        String jsonString = jsonObject.toString();

        try {
            File file = new File(filesDir, (String)jsonObject.get("Word 1"));
            Log.v("FILE DIR", file.getAbsolutePath());
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

    public static void storeWordPairData(WordPair wordPair, File filesDir) {
        writeJSONObject(buildJSONObject(wordPair), filesDir);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.new_flashcard);

        Intent intent = getIntent();
        editMode = intent.getBooleanExtra(MainActivity.FLASHCARD_EDIT_MODE_TAG, false);

        // Handle interactions with page elements
        final CheckBox autoHintsBox = (CheckBox)findViewById(R.id.newFlashcardAutoHintsBox);
        final TextView customHintBox = (TextView)findViewById(R.id.newFlashcardCustomHint);

        // custom hints text box is disable if automatic hints box has been checked
        autoHintsBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                customHintBox.setEnabled(!autoHintsBox.isChecked());
            }
        });

        // warn user if button is pressed without filling in either text box
        final TextView firstWordBox = (TextView)findViewById(R.id.newFlashcardWord1);
        final TextView secondWordBox = (TextView)findViewById(R.id.newFlashcardWord2);
        final TextView maxLengthWarningBox = findViewById(R.id.charLimitWarningText);
        final Button newFlashcardButton = (Button)findViewById(R.id.newFlashcardButton);

        if(editMode) {
            newFlashcardButton.setText(R.string.new_flashcard_edit_button_text);

            firstWordBox.setText(intent.getStringExtra(Flashcards.FLASHCARD_FIRST_WORD_TAG));
            secondWordBox.setText(intent.getStringExtra(Flashcards.FLASHCARD_SECOND_WORD_TAG));
            customHintBox.setText(intent.getStringExtra(Flashcards.FLASHCARD_HINT_TAG));

            editFlashcardFirstWord = intent.getStringExtra(Flashcards.FLASHCARD_FIRST_WORD_TAG);
        }

        final Context context = this.getApplicationContext();
        newFlashcardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("NEW FLASHCARD ON CLICK", "Button clicked");

                if(editMode) {
                    // delete edited word pair
                    WordPair pair = FlashcardStore.getPairFromWord(editFlashcardFirstWord);
                    FlashcardStore.deleteWordPair(getFilesDir(), pair);

                    // create new word pair
                    WordPair newPair = new WordPair(firstWordBox.getText().toString(), secondWordBox.getText().toString(), customHintBox.getText().toString());
                    newPair.setPairRank(pair.getPairRank());
                    FlashcardStore.putWordPair(newPair);
                    storeWordPairData(newPair, getFilesDir());

                    // exit activity and return to flashcard review section
                    finish();
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

                WordPair wordPair = new WordPair(firstWord, secondWord, customHintBox.getText().toString());

                // write the new word pair to file
                writeJSONObject(buildJSONObject(wordPair), getApplicationContext().getFilesDir());

                // if neither box is empty, add strings to flashcard store and clear text
                FlashcardStore.putWordPair(wordPair);
                firstWordBox.setText(R.string.empty_string);
                secondWordBox.setText(R.string.empty_string);
                customHintBox.setText(R.string.empty_string);
                Toast.makeText(context, R.string.new_flashcard_toast, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.action_load_from_file:
                Intent intent = new Intent(this, LoadFlashcardsFromFile.class);
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
