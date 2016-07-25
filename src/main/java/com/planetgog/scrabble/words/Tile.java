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
public class Tile {
    
    int row = -1;
    int col = -1;
    boolean used = false;
    boolean yellow = false;
    String feature = null; // CENTER,DL,TL,DW,TW
    Character letter = null;
    Tile up = null;
    Tile down = null;
    Tile left = null;
    Tile right = null;
}
