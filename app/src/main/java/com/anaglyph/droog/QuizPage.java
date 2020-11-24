package com.anaglyph.droog;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class QuizPage extends AppCompatActivity {

    private QuizQuestionAdapter quizQuestionAdapter;

    private ArrayList<QuestionData> loadQuestionData(Intent intent) {
        ArrayList<QuestionData> questions = new ArrayList<QuestionData>();
        // run through word pair list and choose random possible answers
        WordPair wordPair = FlashcardStore.getNextPair();
        int quizLength = intent.getIntExtra(QuizMenu.QUIZ_LENGTH_KEY, QuizMenu.SHORT_DURATION);
        int index = 0;
        while(index++ < quizLength) {
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
        quizQuestionAdapter = new QuizQuestionAdapter(this, loadQuestionData(getIntent()));
        final ListView list = (ListView)findViewById(R.id.quizList);
        list.setAdapter(quizQuestionAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.action_finish_quiz:
                // show results screen
                Intent intent = new Intent(this, QuizResults.class);
                intent.putExtra(getResources().getString(R.string.quiz_results_intent_count), quizQuestionAdapter.getCount());
                for(int i = 0; i < quizQuestionAdapter.getCount(); i++) {
                    QuestionData questionData = (QuestionData)quizQuestionAdapter.getItem(i);
                    intent.putExtra("" + i, questionData.isCorrect());
                }
                startActivity(intent);
                finish();
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
