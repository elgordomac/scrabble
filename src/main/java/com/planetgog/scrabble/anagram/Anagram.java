/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.planetgog.scrabble.anagram;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author gmacdon76
 */
public class Anagram {

    private List<String> words;

    public Anagram() {

        words = new ArrayList<String>();

        InputStream in = Anagram.class.getClassLoader().getResourceAsStream("dictionary.txt");
        if (in != null) {
            
            System.out.println("in: " + in);
            try (BufferedReader br = new BufferedReader(new InputStreamReader(in))) {
                String line = br.readLine();

                while (line != null) {
                    words.add(line.trim());
                    line = br.readLine();
                }
            } catch (Exception e) {
                System.out.println(e);
            }
            
            System.out.println("Dictionary has " + words.size() + " words");
        }
        else {
            System.out.println("Input stream was null, " + ClassLoader.getSystemClassLoader());
        }
    }

    public String solve(String anagram) {
        String longest = "";
        for(String word: words) {
            if(subset(anagram, word) && word.length() > longest.length() && !word.equals(anagram)) {
                longest = word;
            }
        }
        
        return longest;
    }

    public List<String> solve(String anagram, int count) {
        List<String> words = new ArrayList<String>();
        words.add("aword");
        words.add("another");
        return words;
    }

    public boolean subset(String word, String letters) {
        char[] word1 = word.toCharArray();
        char[] word2 = letters.toCharArray();

        Map<Character, Integer> lettersInWord1 = new HashMap<Character, Integer>();

        for (char c : word1) {
            int count = 1;
            if (lettersInWord1.containsKey(c)) {
                count = lettersInWord1.get(c) + 1;
            }
            lettersInWord1.put(c, count);
        }

        for (char c : word2) {
            int count = -1;
            if (lettersInWord1.containsKey(c)) {
                count = lettersInWord1.get(c) - 1;
            }
            lettersInWord1.put(c, count);
        }

        for (char c : lettersInWord1.keySet()) {
            if (lettersInWord1.get(c) < 0) {
                return false;
            }
        }

        return true;
    }
}
