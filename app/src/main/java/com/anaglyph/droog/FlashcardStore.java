package com.anaglyph.droog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class FlashcardStore {

    private static int NEXT_PAIR = 0;

    private static ArrayList<WordPair> wordPairs = new ArrayList<WordPair>();

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

    public static void putWordPair(WordPair wordPair) {
        wordPairs.add(wordPair);
    }

    public static String getHint(String word) {
        for(WordPair pair : wordPairs) {
            if(pair.getFirstWord().equals(word) || pair.getSecondWord().equals(word))
                return pair.getHint();
        }

        return null;
    }

    public static int getWordPairCount() {
        return wordPairs.size();
    }

    public static WordPair getNextPair() {
        if(NEXT_PAIR < wordPairs.size())
            return wordPairs.get(NEXT_PAIR++);
        else {
            NEXT_PAIR = 0;
            return null;
        }
    }

    public static boolean wordExists(String word) {
        for(WordPair pair : wordPairs) {
            if(pair.getFirstWord().equals(word) || pair.getSecondWord().equals(word))
                return true;
        }
        return false;
    }

    public static boolean isEmpty() {
        return wordPairs.size() == 0;
    }

    public static String getRandomAnswer(String answer) {
        Random random = new Random();

        String randomWord = answer;
        // random word should not be same as answer
        while(randomWord.equals(answer)) {
            WordPair wordPair = wordPairs.get(random.nextInt(wordPairs.size()));
            randomWord = wordPair.getSecondWord();
        }

        return randomWord;
    }

}
