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
public class Board {
    
    private Tile[][] board;
    
    public Board() {
        
        board = new Tile[15][15];
        
        // create the board structure...
        for (int row = 0; row < 15; row++) {
            board[row] = new Tile[15];
            for (int col = 0; col < 15; col++) {
                Tile tile = new Tile();
                tile.used = false;
                tile.yellow = false;
                tile.row = row;
                tile.col = col;
                board[row][col] = tile;
            }
        }
        
        // set up the relationships
        for (int row = 0; row < 15; row++) {
            for (int col = 0; col < 15; col++) {
                Tile tile = get_tile(row, col);
                if(row > 0) { tile.up = get_tile(row - 1, col); }
                if(row < 14) { tile.down = get_tile(row + 1, col); }
                if(col > 0) { tile.left = get_tile(row, col - 1); }
                if(col < 14) { tile.right = get_tile(row, col + 1); }
            }
        }
        
        // add the feature pieces
        get_tile(0, 3).feature = "TW";
        get_tile(0, 6).feature = "TL";
        get_tile(0, 8).feature = "TL";
        get_tile(0, 11).feature = "TW";
        get_tile(1, 2).feature = "DL";
        get_tile(1, 5).feature = "DW";
        get_tile(1, 9).feature = "DW";
        get_tile(1, 12).feature = "DL";
        get_tile(2, 1).feature = "DL";
        get_tile(2, 4).feature = "DL";
        get_tile(2, 10).feature = "DL";
        get_tile(2, 13).feature = "DL";
        get_tile(3, 0).feature = "TW";
        get_tile(3, 3).feature = "TL";
        get_tile(3, 7).feature = "DW";
        get_tile(3, 11).feature = "TL";
        get_tile(3, 14).feature = "TW";
        get_tile(4, 2).feature = "DL";
        get_tile(4, 6).feature = "DL";
        get_tile(4, 8).feature = "DL";
        get_tile(4, 12).feature = "DL";
        get_tile(5, 1).feature = "DW";
        get_tile(5, 5).feature = "DL";
        get_tile(5, 9).feature = "DL";
        get_tile(5, 13).feature = "DW";
        get_tile(6, 0).feature = "TL";
        get_tile(6, 4).feature = "DL";
        get_tile(6, 10).feature = "DL";
        get_tile(6, 14).feature = "TL";
        get_tile(7, 3).feature = "DW";
        get_tile(7, 7).feature = "CENTER";
        get_tile(7, 11).feature = "DW";
        get_tile(8, 0).feature = "TL";
        get_tile(8, 4).feature = "DL";
        get_tile(8, 10).feature = "DL";
        get_tile(8, 14).feature = "TL";
        get_tile(9, 1).feature = "DW";
        get_tile(9, 5).feature = "DL";
        get_tile(9, 9).feature = "DL";
        get_tile(9, 13).feature = "DW";
        get_tile(10, 2).feature = "DL";
        get_tile(10, 6).feature = "DL";
        get_tile(10, 8).feature = "DL";
        get_tile(10, 12).feature = "DL";
        get_tile(11, 0).feature = "TW";
        get_tile(11, 3).feature = "TL";
        get_tile(11, 7).feature = "DW";
        get_tile(11, 11).feature = "TL";
        get_tile(11, 14).feature = "TW";
        get_tile(12, 1).feature = "DL";
        get_tile(12, 4).feature = "DL";
        get_tile(12, 10).feature = "DL";
        get_tile(12, 13).feature = "DL";
        get_tile(13, 2).feature = "DL";
        get_tile(13, 5).feature = "DW";
        get_tile(13, 9).feature = "DW";
        get_tile(13, 12).feature = "DL";
        get_tile(14, 3).feature = "TW";
        get_tile(14, 6).feature = "TL";
        get_tile(14, 8).feature = "TL";
        get_tile(14, 11).feature = "TW";
    }
    
    public void set_row(int row, String letters) {
        
        int col = 0;
        for(char letter : letters.toCharArray()) {
            Tile tile = board[row][col];
            if (letter != ' ') {
                tile.letter = letter;
                tile.yellow = true;
            }
            else {
                tile.letter = null;
                tile.yellow = false;
            }
            
            col++;
        }
    }
    
    public void set(int row, int col, Character letter) {
        board[row][col].letter = letter;
    }
    
    public Character get(int row, int col) {
        return board[row][col].letter;
    }
    
    public void set_tile(int row, int col, Tile tile) {
        board[row][col] = tile;
    }
    
    public Tile get_tile(int row, int col) {
        return board[row][col];
    }
    
    public void reset_letters() {
       for (int row = 0; row < 15; row++) {
            for (int col = 0; col < 15; col++) {                
                board[row][col].used = false;
            }
        }
    }
    
    public void reset() {
        
        for (int row = 0; row < 15; row++) {
            for (int col = 0; col < 15; col++) {
                Tile tile = board[row][col];
                tile.letter = null;
                tile.yellow = false;               
                tile.used = false;
            }
        }
    }
   
    public Distribution get_distribution() {
        
        Distribution distribution = new Distribution();
        for (int row = 0; row < 15; row++) {
            for (int col = 0; col < 15; col++) {
                if(board[row][col].letter != null) {
                    distribution.add(board[row][col].letter);
                }
            }
        }
        
        return distribution;
    }
    
    @Override
    public String toString() {
        
        StringBuilder sb = new StringBuilder();
        sb.append("  012345678901234");
        for (int row = 0; row < 15; row++) {
            String b = new String();
            if(row < 10) {
                b = b + " ";
            }
            b += row;
            for (int col = 0; col < 15; col++) {
                if(board[row][col].letter == null) {
                    b = b + " ";
                }
                else {
                    b += get(row, col);
                }
            }
            sb.append(b);
        }
        return sb.toString();
    }
}
