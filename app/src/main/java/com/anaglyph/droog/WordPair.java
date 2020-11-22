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

    public void increasePairRank() {
        pairRank++;
    }

    public void decreasePairRank() {
        pairRank--;
        if(pairRank < 0)
            pairRank = 0;
    }

    public void setPairRank(int pairRank) {
        this.pairRank = pairRank;
    }

    public boolean isReversed() {
        return reversed;
    }

    public void setReversed(boolean reversed) {
        this.reversed = reversed;
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
