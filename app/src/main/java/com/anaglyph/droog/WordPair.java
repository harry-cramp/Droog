package com.anaglyph.droog;

public class WordPair {

    int pairRank;

    String firstWord;
    String secondWord;
    String hint;

    public WordPair(String firstWord, String secondWord, String hint) {
        this.firstWord = firstWord;
        this.secondWord = secondWord;
        this.hint = hint;
    }

    public int getPairRank() {
        return pairRank;
    }

    public String getFirstWord() {
        return firstWord;
    }

    public String getSecondWord() {
        return secondWord;
    }

    public String getHint() {
        return hint;
    }

}
