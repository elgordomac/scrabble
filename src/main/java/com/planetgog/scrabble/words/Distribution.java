/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.planetgog.scrabble.words;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author gmacdon76
 */
public class Distribution {
    
    private class Letter {
        int initial = 0;
        int available = 0;
        
        public String toString() {
            return "in: " + initial + ", av:" + available;
        }
    }
    
    private Map<Character, Letter> distributionLetters = new HashMap<Character, Letter>();
    int used_count = 0;
    
    public void zero() {
        
        for(Character letter : distributionLetters.keySet()) {
            distributionLetters.get(letter).initial = 0;
            distributionLetters.get(letter).available = 0;
        }
    }
    
    public void reset() {
        for(Character letter : distributionLetters.keySet()) {
            distributionLetters.get(letter).available = distributionLetters.get(letter).initial;
        }
        used_count = 0;
    }
    
    public void add(List<Character> letters) {
        for(Character letter : letters) {
            add(letter);
        }
    }
    
    public void add(Character character) {
        if(!distributionLetters.containsKey(character)) {
            distributionLetters.put(character, new Letter());
        }
        distributionLetters.get(character).initial = distributionLetters.get(character).initial + 1;
    }
    
    public void add(Distribution distribution) {
        for(Character letter : distribution.distributionLetters.keySet()) {
            add(letter);
        }
    }
    
    public void add(Tile[] tiles) {
        for(Tile tile : tiles) {
            if(tile.letter != null) {
                add(tile.letter);
            }
        }
    }
    
    public void setup(List<Character> letters) {
        zero();
        add(letters);
        reset();
    }
    
    public boolean use(Character c) {
        Letter letter = distributionLetters.get(c);
        if(letter != null && letter.available > 0) {
            letter.available = letter.available - 1;
            used_count += 1;
            return true;
        }
        return false;
    }
    
    public boolean can_make(String word) {
        reset();
        for(Character letter : word.toCharArray()) {
            if(!use(letter)) {
                return false;
            }
        }
        
        return true;
    }
    
    public int used() {
        return used_count;
    }
    
    public String toString() {
        
        StringBuilder sb = new StringBuilder();
        for(Character letter : distributionLetters.keySet()) {
            sb.append(letter + " (" + distributionLetters.get(letter).toString() + ")");
        }
        return sb.toString();
    }
}
