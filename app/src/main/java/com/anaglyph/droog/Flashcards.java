package com.anaglyph.droog;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class Flashcards extends AppCompatActivity {

    private boolean revealed;
    private boolean reversed;
    private boolean correctOption;

    public static final String FLASHCARD_FIRST_WORD_TAG = "FIRST_WORD";
    public static final String FLASHCARD_SECOND_WORD_TAG = "SECOND_TAG";

    private String deckName;
    private WordPair wordPair;
    private Random random;
    private Button hintButton;
    private Button revealButton;
    private RadioGroup flashcardOptions;

    private RadioButton flashcardFirstOption;
    private RadioButton flashcardSecondOption;
    private RadioButton flashcardThirdOption;
    private RadioButton flashcardCorrectOption;

    private void reset() {
        wordPair = FlashcardStore.getNextPair();
        if(wordPair == null) {
            finish();
            return;
        }

        revealed = false;

        final TextView firstWordBox = (TextView)findViewById(R.id.revealWord1Box);
        final TextView secondWordBox = (TextView)findViewById(R.id.revealWord2Box);
        //final TextView hintBox = (TextView)findViewById(R.id.revealHintBox);
        //final Button hintButton = (Button)findViewById(R.id.hintButton);
        //final Button revealButton = (Button)findViewById(R.id.revealButton);

        flashcardOptions = findViewById(R.id.flashcardOptions);
        flashcardFirstOption = findViewById(R.id.flashcardOption1);
        flashcardSecondOption = findViewById(R.id.flashcardOption2);
        flashcardThirdOption = findViewById(R.id.flashcardOption3);

        flashcardOptions.setVisibility(View.GONE);
        Log.v("TRIGGER", "I'M TRIGGGGEREDDDD");

        flashcardFirstOption.setChecked(false);
        flashcardSecondOption.setChecked(false);
        flashcardThirdOption.setChecked(false);

        flashcardFirstOption.setEnabled(true);
        flashcardSecondOption.setEnabled(true);
        flashcardThirdOption.setEnabled(true);

        reversed = random.nextBoolean();
        firstWordBox.setText((!reversed) ? wordPair.getFirstWord() : wordPair.getSecondWord());
        wordPair.setReversed(reversed);

        secondWordBox.setText(R.string.empty_string);
        //hintBox.setText(R.string.empty_string);
        hintButton.setVisibility(View.VISIBLE);
        hintButton.setText(R.string.flashcard_hint_button);
        revealButton.setText(R.string.flashcard_reveal_button);
    }

    private void nextPair(boolean good) {
        if(!good)
            wordPair.decreasePairRank();
        else
            wordPair.increasePairRank();
        NewFlashcard.storeWordPairData(wordPair, getFilesDir(), deckName);
        reset();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flashcards);

        revealed = false;
        random = new Random();

        Intent intent = getIntent();
        deckName = intent.getStringExtra(MainActivity.FLASHCARD_DECK_NAME);
        FlashcardStore.selectDeck(deckName);

        wordPair = FlashcardStore.getNextPair();
        if(wordPair == null)
            finish();

        final TextView firstWordBox = (TextView)findViewById(R.id.revealWord1Box);
        final TextView secondWordBox = (TextView)findViewById(R.id.revealWord2Box);
        //final TextView hintBox = (TextView)findViewById(R.id.revealHintBox);
        hintButton = (Button)findViewById(R.id.hintButton);
        revealButton = (Button)findViewById(R.id.revealButton);

        flashcardOptions = findViewById(R.id.flashcardOptions);
        flashcardFirstOption = findViewById(R.id.flashcardOption1);
        flashcardSecondOption = findViewById(R.id.flashcardOption2);
        flashcardThirdOption = findViewById(R.id.flashcardOption3);

        Log.v("FLASHCARDS DECK WORD PAIR", deckName);
        firstWordBox.setText(wordPair.getFirstWord());

        final Context context = this.getApplicationContext();
        hintButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(FlashcardStore.getWordPairCount() < 3) {
                    Toast.makeText(context, R.string.flashcard_hint_insufficient_cards_warning, Toast.LENGTH_LONG).show();
                    return;
                }

                if(!revealed) {
                    //hintBox.setText(wordPair.getHint());
                    QuestionData questionData = FlashcardStore.generateQuestionData(wordPair, reversed);

                    String[] answers = questionData.getRandomisedAnswers();

                    int answerIndex = Integer.parseInt(answers[answers.length - 1]);

                    flashcardFirstOption.setText(answers[0]);
                    flashcardSecondOption.setText(answers[1]);
                    flashcardThirdOption.setText(answers[2]);

                    Log.v("ANSWER INDEX", "" + answerIndex);

                    switch (answerIndex) {
                        case 0:
                            flashcardCorrectOption = flashcardFirstOption;
                            break;

                        case 1:
                            flashcardCorrectOption = flashcardSecondOption;
                            break;

                        default:
                            flashcardCorrectOption = flashcardThirdOption;
                            break;
                    }

                    flashcardOptions.setVisibility(View.VISIBLE);

                    hintButton.setVisibility(View.GONE);
                }else {
                    // bad button
                    nextPair(false);
                }
            }
        });

        revealButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flashcardOptions.getVisibility() == View.VISIBLE) {
                    if(!revealed) {
                        secondWordBox.setText((!wordPair.isReversed()) ? wordPair.getSecondWord() : wordPair.getFirstWord());
                        revealButton.setText(R.string.flashcard_options_continue_text);

                        correctOption = flashcardCorrectOption.isChecked();
                        Log.v("ANSWER", "" + correctOption);

                        flashcardFirstOption.setEnabled(false);
                        flashcardSecondOption.setEnabled(false);
                        flashcardThirdOption.setEnabled(false);

                        revealed = true;
                    }else {
                        flashcardOptions.clearCheck();
                        nextPair(correctOption);
                    }
                    return;
                }

                if(!revealed) {
                    secondWordBox.setText((!wordPair.isReversed()) ? wordPair.getSecondWord() : wordPair.getFirstWord());
                    hintButton.setText(R.string.flashcard_bad_button);
                    revealButton.setText(R.string.flashcard_good_button);
                    revealed = true;
                }else {
                    // good button
                    nextPair(true);
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
                intent.putExtra(MainActivity.FLASHCARD_EDIT_MODE_TAG, true);
                intent.putExtra(MainActivity.FLASHCARD_DECK_NAME, deckName);
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
