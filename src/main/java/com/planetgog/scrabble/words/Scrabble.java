/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.planetgog.scrabble.words;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author gmacdon76
 */
public class Scrabble {
      
    Dictionary dictionary = new Dictionary();
    Map<Character, Integer> letter_values;
    String log_level = "debug";
    
    public Scrabble() {

        // set up the letter values...
        letter_values = new HashMap<Character, Integer>();
        letter_values.put(new Character('a'), 1);
        letter_values.put(new Character('b'), 4);
        letter_values.put(new Character('c'), 4);
        letter_values.put(new Character('d'), 2);
        letter_values.put(new Character('e'), 1);
        letter_values.put(new Character('f'), 4);
        letter_values.put(new Character('g'), 3);
        letter_values.put(new Character('h'), 3);
        letter_values.put(new Character('i'), 1);
        letter_values.put(new Character('j'), 10);
        letter_values.put(new Character('k'), 5);
        letter_values.put(new Character('l'), 2);
        letter_values.put(new Character('m'), 4);
        letter_values.put(new Character('n'), 2);
        letter_values.put(new Character('o'), 1);
        letter_values.put(new Character('p'), 4);
        letter_values.put(new Character('q'), 10);
        letter_values.put(new Character('r'), 1);
        letter_values.put(new Character('s'), 1);
        letter_values.put(new Character('t'), 1);
        letter_values.put(new Character('u'), 2);
        letter_values.put(new Character('v'), 5);
        letter_values.put(new Character('w'), 4);
        letter_values.put(new Character('x'), 8);
        letter_values.put(new Character('y'), 3);
        letter_values.put(new Character('z'), 10);
       
    }
    
    public void log_console(String level, String message) {
        
        if (level.equals("info") || (level.equals("debug") && log_level.equals("debug"))) {
            System.out.println(level + " : " + message);            
        }
    }
    
    public Word run(Board board, Rack rack) {
        
        Tile[] letters = rack.get_letters();
                
        long startTime = System.currentTimeMillis();
        log_console("info", "solving with letters " + letters + " for board:");
        debug_board(board);
        
        // get the seeds...
        List<Tile> seeds = find_seeds(board);
        
        Word word = position_words(board, seeds, rack, dictionary);

        System.out.print("completed in " + (System.currentTimeMillis() - startTime));
        return word;
    }
    
    public List<Tile> find_seeds(Board board) {
        
        // get the seed tiles (the yellow ones on the board)...
        List<Tile> seeds = new ArrayList<Tile>();
        for (int i = 0; i < 15; i += 1) {
            for (int j = 0; j < 15; j += 1) {
                Tile tile = board.get_tile(i, j);
                if (tile.yellow) {
                    
                    if((tile.up != null && !tile.up.yellow) ||
                        (tile.down != null && !tile.down.yellow) ||
                        (tile.left != null && !tile.left.yellow) ||
                        (tile.right != null && !tile.right.yellow)) {
                            seeds.add(tile);
                    }
                    else {
                        System.out.println("tile " + i + "," + j + " is boxed in, skipping as a seed");
                    }
                }
            }
        }
        
        // if there are no seed tiles then the seed is the center
        if (seeds.size() == 0) {
            seeds.add(board.get_tile(7, 7));
        }
        
        System.out.print("found " + seeds.size() + " seeds");
        return seeds;
    }
    
    Set<Character> get_letters(Board board) {
        Set<Character> unique = new HashSet<Character>();
        
        for (int row = 0; row < 15; row += 1) {
            for (int col = 0; col < 15; col += 1) {
                Character a = board.get(row, col);
                if(a != null) {
                    unique.add(a);
                }
            }
        }
        
        System.out.print("extracted " + unique.size() + " unique letters from board");
        return unique;
    }
    
    public Distribution get_distribution(Board board, Rack rack) {
        Distribution distribution = new Distribution();
        
        distribution.add(board.get_distribution());
        distribution.add(rack.get_distribution());
        
        System.out.println("extracted distribution from board");
        return distribution;
    }
    
    public void debug_board(Board board) {
        
        System.out.print("  012345678901234");
        for (int row = 0; row < 15; row += 1) {
            String b = "";
            if(row < 10) {
                b = b + " ";
            }
            b = b + row;
            for (int col = 0; col < 15; col += 1) {
                if(board.get(row, col) == null) {
                    b = b + " ";
                }
                else {
                    b += board.get(row, col);
                }
            }
            System.out.print(b);
        }
    }
    
    Word position_words(Board board, List<Tile> seeds, Rack rack, Dictionary dictionary) {
        
        Word highest = null;
        int iterations = 0;
        long startTime = System.currentTimeMillis();
        
        Set<String> words = dictionary.getWords();
       // words = dictionary.getNSWord("ad"); //XXX
        Distribution board_distribution = board.get_distribution();
        Distribution rack_distribution = rack.get_distribution();
        
        log_console("info", "letters " + rack.get_letters());
        log_console("info", "seeds " + seeds.size());
        log_console("info", "words " + words.size());
        log_console("info", "starting board: ");
        int skip_seed = 0;
        debug_board(board);
        Tile[] rack_letters = rack.get_letters();
        for(String word : words) {
                        
            // does the word contain one of the rack tiles
            boolean found = false;
            for(Tile tile : rack_letters) {
                if(tile.letter != null && word.indexOf(tile.letter) > -1) {
                    found = true;
                    break;
                }
            }
            if(!found) {
                continue;
            }
            
            // can we make the word from all the letters
            if(!board_distribution.can_make(word)) {
                continue;
            }
            
            // does the word fit on the board
            if(word.length() > 15) {
                continue;
            }
            
            // for each seed position the tiles in each direction...
            for(Tile seed : seeds) {
                
                int row = seed.row;
                int col = seed.col;
                
                // going right...
                
                // we can rule out a seed boxed in left and right as it will be covered already...
                if(seed.left == null || !seed.left.yellow || seed.right == null || !seed.right.yellow) {
                    
                    int start_col = col - word.length() + 1;  // if row is 4 and word length is 3 chars then start_row should be 2
                    if(start_col < 0) { start_col = 0; }
                    
                    int end_col = col;  // if col is 12 and word length is 6 chars then end_col should be 9
                    if((col + word.length() - 1) > 14) { end_col = 14 - word.length() + 1; }
                    
                    for(int i=start_col; i<=col; i++) {
                        
                        Word w = place_word(board, row, i, word, rack_distribution, "right");
                        if (w != null) {
                            // score and check if its the highest
                            w.score = score(w);
                            if (w.score > 0 && (highest == null || w.score > highest.score)) {
                                highest = w;
                            }
                        }
                        // reset
                        board.reset_letters();
                        iterations += 1;
                    }
                }
                
                // we must now check all tiles above and below the seed tiles if they are blank
                if(seed.up != null && seed.up.letter == null) {
                    Word w = place_word(board, seed.up, word, rack_distribution, "right");
                    if (w != null) {
                        // score and check if its the highest
                        w.score = score(w);
                        if (w.score > 0 && (highest == null || w.score > highest.score)) {
                            highest = w;
                        }
                    }
                    // reset
                    board.reset_letters();
                    iterations += 1;
                }
                if(seed.down != null && seed.down.letter == null) {
                    Word w = place_word(board, seed.down, word, rack_distribution, "right");
                    if (w != null) {
                        // score and check if its the highest
                        w.score = score(w);
                        if (w.score > 0 && (highest == null || w.score > highest.score)) {
                            highest = w;
                        }
                    }
                    // reset
                    board.reset_letters();
                    iterations += 1;
                }
                
                
                // going down...
                
                // we can rule out a seed boxed in top and bottom as it will be covered already...
                if(seed.up == null || !seed.up.yellow || seed.down == null || !seed.down.yellow) {
                    
                    int start_row = row - word.length() + 1;  // if row is 4 and word length is 3 chars then start_row should be 2
                    if(start_row < 0) { start_row = 0; }
                    
                    int end_row = row;  // if row is 12 and word length is 6 chars then end_row should be 9
                    if((row + word.length() - 1) > 14) { end_row = 14 - word.length() + 1; }
                    
                    for(int i=start_row; i<=end_row; i++) {
                        
                        Word w = place_word(board, i, col, word, rack_distribution, "down");
                        if (w != null) {
                            // score and check if its the highest
                            w.score = score(w);
                            if (w.score > 0 && (highest == null || w.score > highest.score)) {
                                highest = w;
                            }
                        }
                        // reset
                        board.reset_letters();
                        iterations += 1;
                    }
                }
                
                // we must now check all tiles above and below the seed tiles if they are blank
                if(seed.left != null && seed.left.letter == null) {
                    Word w = place_word(board, seed.left, word, rack_distribution, "down");
                    if (w != null) {
                        // score and check if its the highest
                        w.score = score(w);
                        if (w.score > 0 && (highest == null || w.score > highest.score)) {
                            highest = w;
                        }
                    }
                    // reset
                    board.reset_letters();
                    iterations += 1;
                }
                if(seed.right != null && seed.right.letter == null) {
                    Word w = place_word(board, seed.right, word, rack_distribution, "down");
                    if (w != null) {
                        // score and check if its the highest
                        w.score = score(w);
                        if (w.score > 0 && (highest == null || w.score > highest.score)) {
                            highest = w;
                        }
                    }
                    // reset
                    board.reset_letters();
                    iterations += 1;
                }
                
                
//                // get indexes of seed in word, iterate through those
//                var indexes = get_indexes_of(word, letter: String(seed.letter!));
//                
//                
//                // looking for a word going right (can rule out a seed boxed in left and right)...
//                if(seed.left == null || !seed.left!.yellow || seed.right == null || !seed.right!.yellow) {
//                    for index in indexes {
//                        if(index > col) { continue; } // head of the word won't fit on board
//                        if(col + word.length - index > 14) { continue; } // tail of the word won't fit on the board
//                    
//                        // fits on the board so place it...
//                        let w = place_word(board, row: row, col: col - index, word: word as String, distribution: rack_distribution, direction: "right");
//                        if (w != nil) {
//                            // score and check if its the highest
//                            w?.score = score(w!);
//                            if (highest == null || w!.score > highest!.score) {
//                                highest = w!;
//                            }
//                        }
//                        // reset
//                        board.reset_letters();
//                        iterations += 1;
//                    
//                    }
//                    // now deal with the seed derivitives...
//                    // we've already tried all horitonzal possibilities but we can try the tiles left
//                    // and right if they are blank to add a word in the vertical direction
//                    //TODO
//                    
//                    
//                    //e.g. seed row 7, col 2
//                   // for(var i=0; i<
//                    
//                    
//                    
//                }
//                
//                
//                // looking for a word going down...
//                //TODO - can rule out a seed boxed in top and bottom!
//                if(seed.up == null || !seed.up!.yellow || seed.down == null || !seed.down!.yellow) {
//                    for index in indexes {
//                        if(index > row) { continue; } // head of the word won't fit on board
//                        if(row + word.length - index > 14) { continue; } // tail of the word won't fit on the board
//                    
//                        // fits on the board so place it...
//                        let w = place_word(board, row: row - index, col: col, word: word as String, distribution: rack_distribution, direction: "down");
//                        if (w != nil) {
//                            // score and check if its the highest
//                            w?.score = score(w!);
//                            if (highest == null || w!.score > highest!.score) {
//                                highest = w!;
//                            }
//                        }
//                        // reset
//                        board.reset_letters();
//                        iterations += 1;
//                    
//                    }
//                    // now deal with the seed derivitives...
//                    // we've already tried all vertical possibilities but we can try the tiles up
//                    // and down if they are blank to add a word in the horizontal direction
//                    //TODO
//                    
//                    
//                    
//                }
            }
        }
        
        if (highest != null) {
            log_console("info", "overall highest: " + highest + " (tried " + iterations + " iterations)");
            
            place_word(board, highest.row, highest.col, highest.word, rack_distribution, highest.direction);
        } else {
            log_console("info", "failed to find a way to position the letters (tried " + iterations + " iterations)");
        }
        
        return highest;
    }
    
    
    Word place_word(Board board, int row, int col, String word, Distribution distribution, String direction) {
        return place_word(board, board.get_tile(row, col), word, distribution, direction);
    }
    
    Word place_word(Board board, Tile seed, String word, Distribution distribution, String direction) {
   //     log_console("debug", message: "trying to place word " + word + " going " + direction + " from (" + String(row) + "," + String(col) + ")");
            
        Tile tile = seed;
        Word w = new Word(word, seed.row, seed.col, direction, new ArrayList<Tile>());
        distribution.reset();
        
        if (direction.equals("down")) {
            
            for(Character c : word.toCharArray()) {
                if(tile != null) {
                    tile.used = true;
                    w.tiles.add(tile);
                    
                    if (tile.yellow) {
                        // already a tile here, check its the letter we need
                        if (!(tile.letter == c)) {
                            return null;
                        }
                    } else {
                        
                        // check we have this letter available and mark it used
                        if (distribution.use(c)) {
                            tile.letter = c;
                        } else {
                            return null;
                        }
                    }
                }
                else {
                    // ran out of tiles, unexpected as the space is already calculated
                    return null;
                }
                
                // move to the next tile
                tile = tile.down;
            }
        } else {
            for(Character c : word.toCharArray()) {
                if(tile != null) {
                    tile.used = true;
                    w.tiles.add(tile);
                    
                    if (tile.yellow) {
                        // already a tile here, check its the letter we need
                        if (!(tile.letter == c)) {
                            return null;
                        }
                    } else {
                        
                        // check we have this letter available and mark it used
                        if (distribution.use(c)) {
                            tile.letter = c;
                        } else {
                            return null;
                        }
                    }
                }
                else {
                    // ran out of tiles, unexpected as the space is already calculated
                    return null;
                }
                
                // move to the next tile
                tile = tile.right;
            }
        }
        
        if (w.tiles.size() > 0 && distribution.used() > 0) {
            return w;
        } else {
            return null;
        }
    }
    
    // returns an array of the words made by the new word, or null if the
    // result is illegal
    List<Word> find_words(Word word) {
        
        // all words are right or down
        List<Word> words = new ArrayList<Word>();
        
        // add the newly created words perpendicular to the made word...
        for(Tile tile : word.tiles) {
            
            if(tile.used && !tile.yellow) {
                
                if(word.direction.equals("down")) {
                    
                    Word w = find_word(tile, "right");
                    if(w != null && w.word.length() > 1) {
                        if(dictionary.isValid(w.word)) { //XXX dont count single character words!
                            words.add(w);
                        }
                    }
                }
                else {
                    
                    Word w = find_word(tile, "down");
                    if(w != null && w.word.length() > 1) {
                        if(dictionary.isValid(w.word)) {
                            words.add(w);
                        }
                    }
                }
            }
        }
        
        // its only possible to make one word in the same direction as the passed word...
        
        Word w = find_word(word.tiles.get(0), word.direction);
        if(w != null && w.word.length() > 1) {
            if(dictionary.isValid(w.word)) {
                words.add(w);
            }
        }
        
        return words;
    }
    
    Word find_word(Tile tile, String direction) {
        
        if(direction.equals("down")) {
            
            // keep peeking up to find the start of the word
            Tile start_tile = tile;
            while(start_tile.up != null && (start_tile.up.used || start_tile.up.yellow)) {
                start_tile = start_tile.up;
            }
            
            // read the word by going down
            String word = "";
            List<Tile> tiles = new ArrayList<Tile>();
            Tile current_tile = start_tile;
            while(current_tile != null && (current_tile.used || current_tile.yellow)) {
                word += (current_tile.letter);
                tiles.add(current_tile);
                
                current_tile = current_tile.down;
            }
            
            if(tiles.size() > 0) {
                return new Word(word, start_tile.row, start_tile.col, "down", tiles);
            }
        }
        else {
            // keep peeking left to find the start of the word
            Tile start_tile = tile;
            while(start_tile.left != null && (start_tile.left.used || start_tile.left.yellow)) {
                start_tile = start_tile.left;
            }
            
            // read the word by going right
            String word = "";
            List<Tile> tiles = new ArrayList<Tile>();
            Tile current_tile = start_tile;
            while(current_tile != null && (current_tile.used || current_tile.yellow)) {
                word += (current_tile.letter);
                tiles.add(current_tile);
                
                current_tile = current_tile.right;
            }
            
            if(tiles.size() > 0) {
                return new Word(word, start_tile.row, start_tile.col, "right", tiles);
            }
        }
        
        // there was no word created in the direction given, just the tile itself
        return null;
    }
    
    int score(Word w) {
        
        int result = 0;
        List<Word> words = find_words(w);
//        log_console("debug", message: "found a total of " + String(words.count) + " words");

        if(words != null) {
            for(Word word : words) {
                int s = score_word(word);
                result += s;
            }
        }
        
        return result;
    }
    
    int score_word(Word word){
        
        int dw = 0;
        int tw = 0;
        int result = 0;
        for(Tile tile : word.tiles) {
            int letter_score = get_letter_score(tile.letter);
            if (tile.yellow) {
                result += letter_score;
            } else {
                if (tile.feature != null && tile.feature.equals("DL")) {
                    result += (letter_score * 2);
                } else if (tile.feature != null && tile.feature.equals("TL")) {
                    result += (letter_score * 3);
                } else {
                    result += letter_score;
                }
                
                if (tile.feature != null && tile.feature.equals("DW")) {
                    dw += 1;
                }
                if (tile.feature != null && tile.feature.equals("TW")) {
                    tw += 1;
                }
            }
        }
        
        if (dw > 0) {
            result = result * 2 * dw;
        }
        if (tw > 0) {
            result = result * 3 * tw;
        }
        
        return result;
    }
    
    public int get_letter_score(Character letter) {
        return letter_values.get(letter);
    }
    
    public List<Integer> get_indexes_of(String word, Character letter) {
    
        List<Integer> indexes = new ArrayList<Integer>();
        for (int i=0; i<word.length(); i++) {
            if(word.charAt(i) == letter) {
                indexes.add(i);
            }
        }
        
        return indexes;
    }
}
