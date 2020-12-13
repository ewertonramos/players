package org.ewerton;


import org.junit.Test;

import static org.junit.Assert.*;

public class ArgumentReaderTest {

    private final ArgumentReader reader = new ArgumentReader();

    @Test
    public void parseArguments() {
        //given
        String[] args = {"1,2,3", "true", "1,2", "3"};

        //when
        reader.processArgs(args);

        //then
        assertEquals("hello ", reader.getMessage());
        assertArrayEquals(new int[]{1,2,3}, reader.getTotalIds());
        assertArrayEquals(new int[]{1,2}, reader.getIds());
        assertTrue(reader.isInitiator());
        assertEquals(3, reader.getDelay());
    }

}