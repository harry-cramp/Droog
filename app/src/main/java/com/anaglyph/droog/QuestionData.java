package com.anaglyph.droog;

public class QuestionData {

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

    public String getQuestion() {
        return question;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public String[] getAnswers() {
        return answers;
    }

}
