package com.anaglyph.droog;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

public class QuizQuestionAdapter implements ListAdapter {

    private Context context;

    private ArrayList<QuestionData> questionData;

    public QuizQuestionAdapter(Context context, ArrayList<QuestionData> questionData) {
        this.context = context;
        this.questionData = questionData;
    }

    // takes quiz answer options and puts them into a randomised order
    private String[] randomiseOptions(String[] options) {
        String[] randomisedOptions = new String[options.length];
        Random random = new Random();

        for(String option : options) {
            int randomIndex = random.nextInt(options.length);
            while(randomisedOptions[randomIndex] != null)
                randomIndex = random.nextInt(options.length);
            randomisedOptions[randomIndex] = option;
        }

        return randomisedOptions;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEnabled(int position) {
        return false;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {}

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {}

    @Override
    public int getCount() {
        return questionData.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        QuestionData question = questionData.get(position);
        if(convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(R.layout.question_fragment, null);
            TextView questionView = (TextView)convertView.findViewById(R.id.questionText);
            RadioButton firstAnswerButton = (RadioButton)convertView.findViewById(R.id.answer1);
            RadioButton secondAnswerButton = (RadioButton)convertView.findViewById(R.id.answer2);
            RadioButton thirdAnswerButton = (RadioButton)convertView.findViewById(R.id.answer3);
            questionView.setText(question.getQuestion());
            String[] answers = randomiseOptions(question.getAnswers());
            firstAnswerButton.setText(answers[0]);
            secondAnswerButton.setText(answers[1]);
            thirdAnswerButton.setText(answers[2]);
        }
        return convertView;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return questionData.size();
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

}
