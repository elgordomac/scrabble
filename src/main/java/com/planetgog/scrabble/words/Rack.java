/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.planetgog.scrabble.words;

/**
 *
 * @author gmacdon76
 */
public class Rack {
    
    Tile[] rack = new Tile[7];
    
    public Rack() {
        
        for(int i=0; i<rack.length; i++) {
            rack[i] = new Tile();
        }
    }
    
    public void set(String letters) {
                
        for(int i=0; i<rack.length; i++) {
            rack[i].letter = null;
            if(letters.length() > i) {
                rack[i].letter = letters.charAt(i);
            }
        }
    }
    
    public Tile get(int position) {
        return rack[position];
    }
    
    public Tile[] get_letters() {
        
        return rack;
    }
    
    public void reset() {
        
        for(Tile tile : rack) {
            
            tile.letter = null;
            tile.yellow = false;
            tile.used = false;         
        }
    }
    
    public Distribution get_distribution() {
        
        Distribution distribution = new Distribution();
        distribution.add(get_letters());

        return distribution;
    }
}
