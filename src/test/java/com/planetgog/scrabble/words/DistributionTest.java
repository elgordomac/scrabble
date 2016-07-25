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
public class DistributionTest {
    
    @Test
    public void testExample() {
        
        Distribution distribution = new Distribution();
        distribution.add('a');
        distribution.add('p');
        distribution.add('p');
        distribution.add('l');
        distribution.add('e');
        
        assertTrue(distribution.can_make("apple"));
        assertTrue(distribution.can_make("ape"));
        assertTrue(!distribution.can_make("apples"));
        assertTrue(!distribution.can_make("zoo"));
    }
}
