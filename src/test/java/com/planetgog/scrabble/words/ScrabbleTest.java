/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.planetgog.scrabble.words;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.util.HashSet;
import java.util.Set;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;
import org.junit.Test;

/**
 *
 * @author gmacdon76
 */
public class ScrabbleTest {
 
    Dictionary dictionary = new Dictionary();
    Scrabble scrabble = new Scrabble();
    
    @Test
    public void testFindWords() {
        
        Board board = new Board();
        board.set_row(0,  "               ");
        board.set_row(1,  "               ");
        board.set_row(2,  "               ");
        board.set_row(3,  "               ");
        board.set_row(4,  "      biz      ");
        board.set_row(5,  "      o        ");
        board.set_row(6,  "      to       ");
        board.set_row(7,  "               ");
        board.set_row(8,  "               ");
        board.set_row(9,  "               ");
        board.set_row(10, "               ");
        board.set_row(11, "               ");
        board.set_row(12, "               ");
        board.set_row(13, "               ");
        board.set_row(14, "               ");
        
        
        Rack rack = new Rack();
        rack.set("oo");
        
        Word w = scrabble.place_word(board, 4, 8, "zoo", rack.get_distribution(), "down");
        if (w != null) {
            // score and check if its the highest
            w.score = scrabble.score(w);
            System.out.println("word is " + w);
            assertTrue(w.score == 15);
        }
        else {
            System.out.print("word was nil :(");
            fail();
        }
        
    }
    
    public void testFindWords2() {
        
        Board board = new Board();
        board.set_row(0,  "   p           ");
        board.set_row(1,  "dozens         ");
        board.set_row(2,  "i  a           ");
        board.set_row(3,  "v  c           ");
        board.set_row(4,  "a  e           ");
        board.set_row(5,  "s              ");
        board.set_row(6,  "               ");
        board.set_row(7,  "               ");
        board.set_row(8,  "               ");
        board.set_row(9,  "               ");
        board.set_row(10, "               ");
        board.set_row(11, "               ");
        board.set_row(12, "               ");
        board.set_row(13, "               ");
        board.set_row(14, "               ");
        
        
        Rack rack = new Rack();
        rack.set("le");
        
        Word w = scrabble.place_word(board, 4, 0, "ale", rack.get_distribution(), "right");
        if (w != null) {
            // score and check if its the highest
            w.score = scrabble.score(w);
            System.out.println("word is " + w);
            fail();
        }
        else {
            System.out.println("word was nil, which is good because alee is not a word");
        }
    }
    
    public void  testFindWords3() {
        
        Board board = new Board();
        board.set_row(0,  "   p           ");
        board.set_row(1,  "dozens         ");
        board.set_row(2,  "i  a t         ");
        board.set_row(3,  "v  c e         ");
        board.set_row(4,  "a  e a         ");
        board.set_row(5,  "s    m         ");
        board.set_row(6,  "     y         ");
        board.set_row(7,  "               ");
        board.set_row(8,  "               ");
        board.set_row(9,  "               ");
        board.set_row(10, "               ");
        board.set_row(11, "               ");
        board.set_row(12, "               ");
        board.set_row(13, "               ");
        board.set_row(14, "               ");
        
        
        Rack rack = new Rack();
        rack.set("d");
        
        Word w = scrabble.place_word(board, 2, 3, "ad", rack.get_distribution(), "right");
        if (w != null) {
            // score and check if its the highest
            w.score = scrabble.score(w);
            System.out.println("word is " + w);
            fail();
        }
        else {
            System.out.println("word was nil, which is good because alee is not a word");
        }
    }   
    
    @Test
    public void testParse() {
        JsonObject jsonObject = new JsonParser().parse("{\n" +
"                                \"board\": [\"               \",\n" +
"                                    \"               \",\n" +
"                                    \"               \",\n" +
"                                    \"               \",\n" +
"                                    \"      biz      \",\n" +
"                                    \"      o        \",\n" +
"                                    \"      to       \",\n" +
"                                    \"               \",\n" +
"                                    \"               \",\n" +
"                                    \"               \",\n" +
"                                    \"               \",\n" +
"                                    \"               \",\n" +
"                                    \"               \",\n" +
"                                    \"               \",\n" +
"                                    \"               \"],\n" +
"                                \"rack\": \"oo\"\n" +
"                            }").getAsJsonObject();
        JsonArray boardArray = jsonObject.get("board").getAsJsonArray();

        Board board = new Board();
        int i=0;
        for(JsonElement row : boardArray) {
            board.set_row(i++, row.getAsString());
        }

        String rackStr = jsonObject.get("rack").getAsString();
        Rack rack = new Rack();
        rack.set(rackStr);
        
        System.out.println("Ready to start working with board: " + board + ", rack: " + rack);
        
        Dictionary trimmed = new Dictionary();
        Set<String> words = new HashSet<String>();
        words.add("zoo");
        trimmed.setWords(words);
        Scrabble scrabble = new Scrabble();
        scrabble.dictionary = trimmed;
                    Word word = scrabble.run(board, rack);
                        System.out.println("{\"success\":\"found " + word + "\"}"); 
                        assertTrue(word.word.equals("zoo"));
                    
    }
}