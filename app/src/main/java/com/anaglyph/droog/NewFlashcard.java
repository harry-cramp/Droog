package com.anaglyph.droog;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class NewFlashcard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.new_flashcard);

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
        final Button newFlashcardButton = (Button)findViewById(R.id.newFlashcardButton);

        final Context context = this.getApplicationContext();
        newFlashcardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("NEW FLASHCARD ON CLICK", "Button clicked");

                String firstWord = firstWordBox.getText().toString();
                if(firstWord.equals(R.string.empty_string)) {
                    firstWordBox.setHint(R.string.empty_text_field_warning);
                    firstWordBox.setHintTextColor(getResources().getColor(R.color.colorWarning, null));
                }else {
                    firstWordBox.setHint(R.string.new_flashcard_word_1);
                    firstWordBox.setHintTextColor(getResources().getColor(R.color.colorHint, null));
                }

                String secondWord = secondWordBox.getText().toString();
                if(secondWord.equals(R.string.empty_string)) {
                    secondWordBox.setHint(R.string.empty_text_field_warning);
                    secondWordBox.setHintTextColor(getResources().getColor(R.color.colorWarning, null));
                    return;
                }else {
                    secondWordBox.setHint(R.string.new_flashcard_word_2);
                    secondWordBox.setHintTextColor(getResources().getColor(R.color.colorHint, null));
                }

                // if neither box is empty, add strings to flashcard store and clear text
                FlashcardStore.putWordPair(firstWord, secondWord, customHintBox.getText().toString());
                firstWordBox.setText(R.string.empty_string);
                secondWordBox.setText(R.string.empty_string);
                customHintBox.setText(R.string.empty_string);
                Toast.makeText(context, R.string.new_flashcard_toast, Toast.LENGTH_SHORT).show();
            }
        });
    }

}
