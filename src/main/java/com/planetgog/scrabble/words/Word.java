/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.planetgog.scrabble.words;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author gmacdon76
 */

public class Word {
    
    String word = null;
    int row = -1;
    int col = -1;
    String direction = "";
    List<Tile> tiles = new ArrayList<>();
    int score = -1;
    
    Word(String word, int row, int col, String direction, List<Tile> tiles) {
        this.word = word;
        this.row = row;
        this.col = col;
        this.direction = direction;
        this.tiles = tiles;
    }
    
    @Override
    public String toString() {
        return word + " starting at " + row + "," + col + " going " + direction  + " (" + score + " pts)";
    }
}