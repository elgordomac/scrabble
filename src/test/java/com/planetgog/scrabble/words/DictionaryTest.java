/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.planetgog.scrabble.words;

import static junit.framework.Assert.*;
import org.junit.Test;

/**
 *
 * @author gmacdon76
 */
public class DictionaryTest {

    private Dictionary dictionary = new Dictionary();
    
    public void setUp() {
        
    }
    
    @Test
    public void testValid() {
        assertTrue(dictionary.isValid("chicken"));
        assertTrue(!dictionary.isValid("choockin"));
    }
    
}