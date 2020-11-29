package com.anaglyph.droog;

import android.util.Log;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;

public class FlashcardStore {

    private static int NEXT_PAIR = 0;

    private static String DECK_NAME;

    private static HashMap<String, LinkedList<WordPair>> decks = new HashMap<String, LinkedList<WordPair>>();

    public static void initDeck(String deckName) {
        decks.put(deckName, new LinkedList<WordPair>());
    }

    public static void selectDeck(String deckName) {
        DECK_NAME = deckName;
    }

    public static String[] getDeckNames() {
        String[] deckNames = new String[decks.size()];

        int index = 0;
        for(String name : decks.keySet())
            deckNames[index++] = name;

        return deckNames;
    }

    public static String getAnswer(String word) {
        for(WordPair pair : decks.get(DECK_NAME)) {
            if(pair.getFirstWord().equals(word))
                return pair.getSecondWord();
            else if(pair.getSecondWord().equals(word))
                return pair.getFirstWord();
        }

        return null;
    }

    public static void putWordPair(String firstWord, String secondWord, String hint) {
        putWordPair(new WordPair(firstWord, secondWord, hint));
    }

    public static void putWordPair(WordPair wordPair) {
        LinkedList<WordPair> wordPairs = decks.get(DECK_NAME);
        if(wordPairs == null)
            return;

        for(int i = 0; i < wordPairs.size(); i++) {
            WordPair comparePair = wordPairs.get(i);
            if(wordPair.getPairRank() < comparePair.getPairRank()) {
                wordPairs.add(i, wordPair);
                return;
            }
        }
        wordPairs.add(wordPair);
    }

    public static void rebuildList() {
        LinkedList<WordPair> wordPairs = decks.get(DECK_NAME);
        if(wordPairs == null)
            return;

        LinkedList<WordPair> newPairList = new LinkedList<WordPair>();
        for(WordPair wordPair : wordPairs)
            newPairList.add(wordPair);
        wordPairs.clear();
        for(WordPair wordPair : newPairList)
            putWordPair(wordPair);
    }

    public static String getHint(String word) {
        LinkedList<WordPair> wordPairs = decks.get(DECK_NAME);
        for(WordPair pair : wordPairs) {
            if(pair.getFirstWord().equals(word) || pair.getSecondWord().equals(word))
                return pair.getHint();
        }

        return null;
    }

    public static int getWordPairCount() {
        LinkedList<WordPair> wordPairs = decks.get(DECK_NAME);
        if(wordPairs == null)
            return 0;

        return wordPairs.size();
    }

    public static WordPair getNextPair() {
        LinkedList<WordPair> wordPairs = decks.get(DECK_NAME);
        if(wordPairs == null)
            return null;

        if(NEXT_PAIR < wordPairs.size())
            return wordPairs.get(NEXT_PAIR++);
        else {
            NEXT_PAIR = 0;
            rebuildList();
            return null;
        }
    }

    public static WordPair getPairFromWord(String word) {
        LinkedList<WordPair> wordPairs = decks.get(DECK_NAME);
        if(wordPairs == null)
            return null;

        for(WordPair pair : wordPairs) {
            if(pair.getFirstWord().equals(word) || pair.getSecondWord().equals(word))
                return pair;
        }
        return null;
    }

    public static void deleteWordPair(File filesDir, WordPair wordPair) {
        File file = new File(filesDir, DECK_NAME + File.separator + wordPair.getFirstWord());
        Log.v("DELETE WORD PAIR", "DELETING: " + file.getAbsolutePath());

        boolean deleted = file.delete();
        Log.v("DELETE WORD PAIR", "SUCCESS: " + deleted);
        if(deleted) {
            LinkedList<WordPair> wordPairs = decks.get(DECK_NAME);

            for(int i = 0; i < wordPairs.size(); i++) {
                if(wordPair.equals(wordPairs.get(i))) {
                    wordPairs.remove(wordPair);
                    Log.v("DELETE WORD PAIR", "WORD PAIR FULLY REMOVED");
                    return;
                }
            }
        }
        Log.v("DELETE WORD PAIR", "WORD PAIR NOT REMOVED FROM MEMORY");
    }

    public static boolean wordExists(String word) {
        LinkedList<WordPair> wordPairs = decks.get(DECK_NAME);
        if(wordPairs == null)
            return false;

        for(WordPair pair : wordPairs) {
            if(pair.getFirstWord().equals(word) || pair.getSecondWord().equals(word))
                return true;
        }
        return false;
    }

    public static boolean isEmpty() {
        LinkedList<WordPair> wordPairs = decks.get(DECK_NAME);
        if(wordPairs == null)
            return true;

        return wordPairs.size() == 0;
    }

    public static String getRandomAnswer(String answer, boolean reversed) {
        Random random = new Random();
        LinkedList<WordPair> wordPairs = decks.get(DECK_NAME);
        if(wordPairs == null)
            return null;

        String randomWord = answer;
        // random word should not be same as answer
        while(randomWord.equals(answer)) {
            WordPair wordPair = wordPairs.get(random.nextInt(wordPairs.size()));
            randomWord = (reversed) ? wordPair.getFirstWord() : wordPair.getSecondWord();
        }

        return randomWord;
    }

    public static QuestionData generateQuestionData(WordPair wordPair, boolean reversed) {
        String answer = (reversed) ? wordPair.getFirstWord() : wordPair.getSecondWord();
        String firstRandomAnswer = FlashcardStore.getRandomAnswer(answer, reversed);
        String secondRandomAnswer = FlashcardStore.getRandomAnswer(answer, reversed);
        while(firstRandomAnswer.equals(secondRandomAnswer))
            secondRandomAnswer = FlashcardStore.getRandomAnswer(answer, reversed);
        return new QuestionData((!reversed) ? wordPair.getFirstWord() : wordPair.getSecondWord(), (!reversed) ? wordPair.getSecondWord() : wordPair.getFirstWord(), firstRandomAnswer, secondRandomAnswer);
    }

    public static void saveFlashcardData(File filesDir) {
        LinkedList<WordPair> wordPairs = decks.get(DECK_NAME);

        for(WordPair wordPair : wordPairs)
            NewFlashcard.storeWordPairData(wordPair, filesDir, DECK_NAME);
    }

}
