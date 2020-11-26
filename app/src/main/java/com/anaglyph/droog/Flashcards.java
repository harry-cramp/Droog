package com.anaglyph.droog;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class Flashcards extends AppCompatActivity {

    private boolean revealed;

    public static final String FLASHCARD_FIRST_WORD_TAG = "FIRST_WORD";
    public static final String FLASHCARD_SECOND_WORD_TAG = "SECOND_TAG";
    public static final String FLASHCARD_HINT_TAG = "HINT_TAG";

    private WordPair wordPair;
    private Random random;

    private void reset() {
        wordPair = FlashcardStore.getNextPair();
        if(wordPair == null) {
            finish();
            return;
        }

        revealed = false;

        final TextView firstWordBox = (TextView)findViewById(R.id.revealWord1Box);
        final TextView secondWordBox = (TextView)findViewById(R.id.revealWord2Box);
        final TextView hintBox = (TextView)findViewById(R.id.revealHintBox);
        final Button hintButton = (Button)findViewById(R.id.hintButton);
        final Button revealButton = (Button)findViewById(R.id.revealButton);

        boolean reversed = random.nextBoolean();
        firstWordBox.setText((!reversed) ? wordPair.getFirstWord() : wordPair.getSecondWord());
        wordPair.setReversed(reversed);

        secondWordBox.setText(R.string.empty_string);
        hintBox.setText(R.string.empty_string);
        hintButton.setText(R.string.flashcard_hint_button);
        revealButton.setText(R.string.flashcard_reveal_button);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flashcards);

        revealed = false;
        random = new Random();

        wordPair = FlashcardStore.getNextPair();
        if(wordPair == null)
            finish();

        final TextView firstWordBox = (TextView)findViewById(R.id.revealWord1Box);
        final TextView secondWordBox = (TextView)findViewById(R.id.revealWord2Box);
        final TextView hintBox = (TextView)findViewById(R.id.revealHintBox);
        final Button hintButton = (Button)findViewById(R.id.hintButton);
        final Button revealButton = (Button)findViewById(R.id.revealButton);

        firstWordBox.setText(wordPair.getFirstWord());

        final Context context = this.getApplicationContext();
        hintButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!revealed)
                    hintBox.setText(wordPair.getHint());
                else {
                    // bad button
                    wordPair.decreasePairRank();
                    NewFlashcard.storeWordPairData(wordPair, getFilesDir());
                    reset();
                }
            }
        });

        revealButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!revealed) {
                    secondWordBox.setText((!wordPair.isReversed()) ? wordPair.getSecondWord() : wordPair.getFirstWord());
                    hintButton.setText(R.string.flashcard_bad_button);
                    revealButton.setText(R.string.flashcard_good_button);
                    revealed = true;
                }else {
                    // good button
                    wordPair.increasePairRank();
                    NewFlashcard.storeWordPairData(wordPair, getFilesDir());
                    reset();
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.action_edit_flashcard:
                Intent intent = new Intent(this, NewFlashcard.class);
                intent.putExtra(FLASHCARD_FIRST_WORD_TAG, wordPair.getFirstWord());
                intent.putExtra(FLASHCARD_SECOND_WORD_TAG, wordPair.getSecondWord());
                intent.putExtra(FLASHCARD_HINT_TAG, wordPair.getHint());
                intent.putExtra(MainActivity.FLASHCARD_EDIT_MODE_TAG, true);
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.flashcards_menu, menu);
        return true;
    }

}
