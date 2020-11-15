package com.anaglyph.droog;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class QuizPage extends AppCompatActivity {

    private ArrayList<QuestionData> loadQuestionData() {
        ArrayList<QuestionData> questions = new ArrayList<QuestionData>();
        // run through word pair list and choose random possible answers
        WordPair wordPair = FlashcardStore.getNextPair();
        while(wordPair != null) {
            String firstRandomAnswer = FlashcardStore.getRandomAnswer(wordPair.getSecondWord());
            String secondRandomAnswer = FlashcardStore.getRandomAnswer(wordPair.getSecondWord());
            while(firstRandomAnswer.equals(secondRandomAnswer))
                secondRandomAnswer = FlashcardStore.getRandomAnswer(wordPair.getSecondWord());
            questions.add(new QuestionData(wordPair.getFirstWord(), wordPair.getSecondWord(), firstRandomAnswer, secondRandomAnswer));
            wordPair = FlashcardStore.getNextPair();
        }

        return questions;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quiz_page);

        // load question data into list
        QuizQuestionAdapter quizQuestionAdapter = new QuizQuestionAdapter(this, loadQuestionData());
        final ListView list = (ListView)findViewById(R.id.quizList);
        list.setAdapter(quizQuestionAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.action_finish_quiz:
                // show results screen
                return true;

            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.quiz_menu, menu);
        return true;
    }

}
