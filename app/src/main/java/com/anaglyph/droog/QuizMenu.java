package com.anaglyph.droog;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class QuizMenu extends AppCompatActivity {

    private final int OPTION_SHORT = 0;
    private final int OPTION_LONG = 1;

    public static final int SHORT_DURATION = 10;
    public static final int LONG_DURATION = 25;

    public static final String QUIZ_LENGTH_KEY = "QUIZ_LENGTH";

    private Button startQuizButton;
    private TextView errorPlaceholder;

    @Override
    protected void onResume() {
        super.onResume();

        startQuizButton.setEnabled(true);
        errorPlaceholder.setText(R.string.empty_string);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quiz_menu);

        ArrayList<String> durations = new ArrayList<String>();
        durations.add(getResources().getString(R.string.quiz_menu_short_duration) + " " + SHORT_DURATION + " " + getResources().getString(R.string.quiz_menu_duration_suffix));
        durations.add(getResources().getString(R.string.quiz_menu_long_duration) + " " + LONG_DURATION + " " + getResources().getString(R.string.quiz_menu_duration_suffix));
        final ArrayAdapter<String> durationAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, durations);

        final Spinner durationSpinner = (Spinner)findViewById(R.id.durationSpinner);
        durationSpinner.setAdapter(durationAdapter);

        startQuizButton = (Button)findViewById(R.id.startQuizButton);

        durationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                startQuizButton.setEnabled(true);
                errorPlaceholder.setText(R.string.empty_string);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        errorPlaceholder = findViewById(R.id.errorTextNotEnoughData);
        FlashcardStore.selectDeck(getIntent().getStringExtra(MainActivity.FLASHCARD_DECK_NAME));
        final Context context = this.getApplicationContext();
        startQuizButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(FlashcardStore.getWordPairCount() < SHORT_DURATION && durationSpinner.getSelectedItemPosition() == OPTION_SHORT) {
                    startQuizButton.setEnabled(false);
                    errorPlaceholder.setText(getResources().getString(R.string.quiz_menu_error_insufficient_data_short));
                    return;
                }else if(FlashcardStore.getWordPairCount() < LONG_DURATION && durationSpinner.getSelectedItemPosition() == OPTION_LONG) {
                    startQuizButton.setEnabled(false);
                    errorPlaceholder.setText(getResources().getString(R.string.quiz_menu_error_insufficient_data_long));
                    return;
                }
                Intent intent = new Intent(context, QuizPage.class);
                intent.putExtra(QUIZ_LENGTH_KEY, (durationSpinner.getSelectedItemPosition() == OPTION_SHORT) ? SHORT_DURATION : LONG_DURATION);
                intent.putExtra(MainActivity.FLASHCARD_DECK_NAME, getIntent().getStringExtra(MainActivity.FLASHCARD_DECK_NAME));
                startActivity(intent);
                finish();
            }
        });
    }

}
