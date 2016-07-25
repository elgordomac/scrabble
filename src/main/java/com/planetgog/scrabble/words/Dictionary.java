/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.planetgog.scrabble.words;

import com.planetgog.scrabble.anagram.Anagram;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author gmacdon76
 */
public class Dictionary {
    
    private Set<String> words = new HashSet<>();
    
    public Dictionary() {
        
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
    
    public boolean isValid(String word) {
        return words.contains(word);
    }
    
    Set<String> getWords() {
        return words;
    }
    
    public void setWords(Set<String> words) {
        this.words = words;
    }
}
