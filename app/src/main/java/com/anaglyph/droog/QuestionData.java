package com.anaglyph.droog;

import android.util.Log;

import java.util.Random;

public class QuestionData {

    private boolean correct;

    private String question;
    private String correctAnswer;
    private String[] answers;

    public QuestionData(String question, String correctAnswer, String... answers) {
        this.question = question;
        this.correctAnswer = correctAnswer;
        this.answers = new String[3];
        this.answers[0] = answers[0];
        this.answers[1] = answers[1];
        this.answers[2] = correctAnswer;
    }

    public boolean isCorrect() {
        return correct;
    }

    public void setCorrect() {
        correct = true;
    }

    public String getQuestion() {
        return question;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public String[] getAnswers() {
        return answers;
    }

    public String[] getRandomisedAnswers() {
        Random random = new Random();
        String[] randomisedAnswers = new String[answers.length + 1];
        boolean[] answerTaken = new boolean[answers.length];

        int answerIndex = -1;
        for(int i = 0; i < randomisedAnswers.length - 1; i++) {
            int randIndex = random.nextInt(answers.length);
            while(answerTaken[randIndex])
                randIndex = random.nextInt(answers.length);

            if(randIndex == 2) {
                answerIndex = i;
                Log.v("ANSWER INDEX IN LOOP", "" + answerIndex);
            }

            randomisedAnswers[i] = answers[randIndex];
            answerTaken[randIndex] = true;
        }

        randomisedAnswers[randomisedAnswers.length - 1] = "" + answerIndex;

        return randomisedAnswers;
    }

}
