/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.planetgog.scrabble.anagram;

/**
 *
 * @author gmacdon76
 */
public class AnagramTest {
    
    public void testSolve() {
        
        Anagram solver = new Anagram();
        String result = solver.solve("banana");
        System.out.println(result);
        result = solver.solve("interstellar");
        System.out.println(result);
    }
}
