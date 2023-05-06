package com.example.progettoingsw2022_2;

import com.example.progettoingsw2022_2.Driver.StatisticsActivityMock;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import org.junit.jupiter.api.BeforeAll;




public class mediaTest {

    StatisticsActivityMock mock;

    @Before
    public void setUp(){
        mock = new StatisticsActivityMock();
    }

    @Test
    public void testMedia(){
        float result = mock.media(17, 50f);
        assertEquals(2.94f, result, 0.001f);
    }
    @Test
    public void testGiorniNegativiIncassoPositivo(){
        assertThrows(IllegalArgumentException.class,
                () -> mock.media(-3, 50.06f)
        );
    }
    @Test
    public void testmìMedia1(){
        float result = mock.media(17, 50f);
        assertEquals(2.94f, result, 0.001f);
    }
    @Test
    public void testmìMedia11(){
        float result = mock.media(17, 50f);
        assertEquals(2.94f, result, 0.001f);
    }


}
