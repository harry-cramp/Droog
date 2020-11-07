package com.anaglyph.droog;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Flashcards extends AppCompatActivity {

    private boolean revealed;

    private WordPair wordPair;

    private void reset() {
        wordPair = FlashcardStore.getNextPair();
        if(wordPair == null)
            finish();

        revealed = false;

        final TextView firstWordBox = (TextView)findViewById(R.id.revealWord1Box);
        final TextView secondWordBox = (TextView)findViewById(R.id.revealWord2Box);
        final TextView hintBox = (TextView)findViewById(R.id.revealHintBox);
        final Button hintButton = (Button)findViewById(R.id.hintButton);
        final Button revealButton = (Button)findViewById(R.id.revealButton);

        firstWordBox.setText(wordPair.getFirstWord());
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
                    reset();
                }
            }
        });

        revealButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!revealed) {
                    secondWordBox.setText(wordPair.getSecondWord());
                    hintButton.setText(R.string.flashcard_bad_button);
                    revealButton.setText(R.string.flashcard_good_button);
                    revealed = true;
                }else {
                    // good button
                    reset();
                }
            }
        });
    }

}
