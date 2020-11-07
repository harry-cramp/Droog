package com.anaglyph.droog;

import java.util.ArrayList;
import java.util.HashMap;

public class FlashcardStore {

    public static ArrayList<WordPair> wordPairs = new ArrayList<WordPair>();

    public static String getAnswer(String word) {
        for(WordPair pair : wordPairs) {
            if(pair.getFirstWord().equals(word))
                return pair.getSecondWord();
            else if(pair.getSecondWord().equals(word))
                return pair.getFirstWord();
        }

        return null;
    }

    public static void putWordPair(String firstWord, String secondWord, String hint) {
        wordPairs.add(new WordPair(firstWord, secondWord, hint));
    }

    public static String getHint(String word) {
        for(WordPair pair : wordPairs) {
            if(pair.getFirstWord().equals(word) || pair.getSecondWord().equals(word))
                return pair.getHint();
        }

        return null;
    }

}
