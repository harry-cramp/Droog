package com.anaglyph.droog;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class QuizMenu extends AppCompatActivity {

    private final int SHORT_DURATION = 10;
    private final int LONG_DURATION = 25;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quiz_menu);

        ArrayList<String> durations = new ArrayList<String>();
        durations.add(getResources().getString(R.string.quiz_menu_short_duration) + " " + SHORT_DURATION + " " + getResources().getString(R.string.quiz_menu_duration_suffix));
        durations.add(getResources().getString(R.string.quiz_menu_long_duration) + " " + LONG_DURATION + " " + getResources().getString(R.string.quiz_menu_duration_suffix));
        ArrayAdapter<String> durationAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, durations);

        Spinner durationSpinner = (Spinner)findViewById(R.id.durationSpinner);
        durationSpinner.setAdapter(durationAdapter);

        Button startQuizButton = (Button)findViewById(R.id.startQuizButton);
        final Context context = this.getApplicationContext();
        startQuizButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, QuizPage.class);
                startActivity(intent);
            }
        });
    }

}