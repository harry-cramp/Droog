package com.anaglyph.droog;

import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class NewFlashcard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.new_flashcard);

        // Handle interactions with page elements
        final CheckBox autoHintsBox = (CheckBox)findViewById(R.id.newFlashcardAutoHintsBox);
        final TextView customHintBox = (TextView)findViewById(R.id.newFlashcardCustomHint);

        autoHintsBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                customHintBox.setEnabled(!autoHintsBox.isChecked());
            }
        });
    }

}
