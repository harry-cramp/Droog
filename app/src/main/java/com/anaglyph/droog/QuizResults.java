package com.anaglyph.droog;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class QuizResults extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quiz_results);

        // load data passed from finished quiz
        Intent intent = getIntent();
        int questionCount = intent.getIntExtra(getResources().getString(R.string.quiz_results_intent_count), 0);

        // count successfully answered questions
        int successfullyAnswered = 0;
        for(int i = 0; i < questionCount; i++) {
            boolean answeredCorrectly = intent.getBooleanExtra("" + i, false);
            if(answeredCorrectly)
                successfullyAnswered++;
        }

        // display result
        TextView resultText = findViewById(R.id.quizResultsScore);
        resultText.setText(successfullyAnswered + " / " + questionCount);

        Button finishButton = findViewById(R.id.quizFinishButton);
        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}
