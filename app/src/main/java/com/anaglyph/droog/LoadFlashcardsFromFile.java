package com.anaglyph.droog;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class LoadFlashcardsFromFile extends AppCompatActivity {

    private final int LOAD_FILE_CODE = 1;

    private final String TEMP_FILE_DIR = File.separator + "temp.txt";

    private TextView exampleFileText;
    private String deckName;

    private void writeToOutputBox(String text) {
        exampleFileText.setText(exampleFileText.getText().toString() + text + "\n");
    }

    private void loadFlashcardsFromData(ArrayList<String> data) {
        exampleFileText.setText("");
        for(int i = 0; i < data.size(); i += 2) {
            String firstWord = data.get(i);
            String secondWord = data.get(i + 1);

            writeToOutputBox("LOADING " + firstWord + " -> " + secondWord);
            if(firstWord.length() > NewFlashcard.NEW_FLASHCARD_MAX_LENGTH || secondWord.length() > NewFlashcard.NEW_FLASHCARD_MAX_LENGTH) {
                writeToOutputBox("INPUT EXCEEDS 120 CHARACTERS, SKIPPING");
                continue;
            }
            WordPair newPair = new WordPair(firstWord, secondWord);
            NewFlashcard.storeWordPairData(newPair, getApplicationContext().getFilesDir(), deckName);
            FlashcardStore.putWordPair(newPair);
        }
        writeToOutputBox("DONE.");
    }

    private ArrayList<String> loadFileData(Uri uri) {
        ArrayList<String> data = new ArrayList<String>();
        try {
            String path = getFilesDir().getAbsolutePath() + TEMP_FILE_DIR;
            InputStream in = getContentResolver().openInputStream(uri);
            DataOutputStream out = new DataOutputStream(new FileOutputStream(path));

            int i;
            while ((i = in.read()) != -1)
                out.write(i);

            in.close();
            out.close();

            File tempFile = new File(path);
            FileReader fileReader = new FileReader(tempFile);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            String line;
            String secondLine = null;
            while((line = bufferedReader.readLine()) != null) {
                secondLine = bufferedReader.readLine();
                Log.v("READ FILE", "LOADING FLASHCARD " + line + " -> " + secondLine);
                data.add(line);
                data.add(secondLine);
            }

            bufferedReader.close();
            fileReader.close();
            tempFile.delete();
        }catch(IOException e) {
            e.printStackTrace();
        }
        return data;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.load_file_page);

        deckName = getIntent().getStringExtra(MainActivity.FLASHCARD_DECK_NAME);

        exampleFileText = findViewById(R.id.load_file_example_text);
        exampleFileText.setText(getResources().getString(R.string.from_file_eg_tree) + "\n"
                                + getResources().getString(R.string.from_file_eg_baum) + "\n"
                                + getResources().getString(R.string.from_file_eg_cat) + "\n"
                                + getResources().getString(R.string.from_file_eg_katze) + "\n"
                                + getResources().getString(R.string.from_file_eg_eye) + "\n"
                                + getResources().getString(R.string.from_file_eg_auge) + "\n"
                                + getResources().getString(R.string.ellipsis) + "\n");

        Button loadFileButton = findViewById(R.id.loadFileButton);
        loadFileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("text/plain");
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(intent, LOAD_FILE_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == LOAD_FILE_CODE && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            ArrayList<String> fileData = loadFileData(uri);
            loadFlashcardsFromData(fileData);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}
