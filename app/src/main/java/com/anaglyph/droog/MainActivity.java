package com.anaglyph.droog;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    /*
     *  Author: Harry Cramp
     *
     *  Date: November 2020
     */

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
    }
}
