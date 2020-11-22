package com.anaglyph.droog;

public class WordPair {

    int pairRank;

    private boolean reversed;

    String firstWord;
    String secondWord;
    String hint;

    public WordPair(String firstWord, String secondWord, String hint) {
        this.firstWord = firstWord;
        this.secondWord = secondWord;
        this.hint = (hint == null || hint.isEmpty()) ? "empty" : hint;
    }

    public int getPairRank() {
        return pairRank;
    }

    public boolean isReversed() {
        return reversed;
    }

    public void setReversed(boolean reversed) {
        this.reversed = reversed;
    }

    public void setPairRank(int pairRank) {
        this.pairRank = pairRank;
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
