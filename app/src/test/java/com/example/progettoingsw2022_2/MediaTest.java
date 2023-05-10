package com.example.progettoingsw2022_2;

import com.example.progettoingsw2022_2.Driver.StatisticsActivityMock;
import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.Before;
import org.junit.Test;

/*
   CLASSI DI EQUIVALENZA:

      INCASSO: {NULL, NEGATIVO, POSITIVO}i

      GIORNI: {NULL, NEGATIVO, ZERO, POSITIVO}

 --------------------------------------------------------------------------------

   STRATEGIE DI TESTING UTILIZZATE:
      BlackBox e WhiteBox secondo il criterio SECT


     ==== BLACKBOX ====

 ------------------------------------------------------------------------------ */

public class MediaTest {

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
    public void testGiornoNegativoIncassoPositivo(){
        assertThrows(IllegalArgumentException.class,
                () -> mock.media(-3, 50.06f)
        );
    }
    @Test
    public void testGiornoPositivoIncassoNegativo(){
        assertThrows(IllegalArgumentException.class,
                () -> mock.media(3, -50.06f)
        );
    }
    @Test
    public void testGiornoEIncassoNegativi(){
        assertThrows(IllegalArgumentException.class,
                () -> mock.media(-3, -50.06f)
        );
    }

    @Test
    public void testGiornoZero(){
        assertAll(
                () ->  assertThrows(ArithmeticException.class,
                        () -> mock.media(0, 50.06f)
                ),
                () ->  assertThrows(ArithmeticException.class,
                        () -> mock.media(0, -50.06f)
                )
        );
    }

/*     ==== BLACKBOX ====

------------------------------------------------------------------------------ */

    @Test
    public void testGiornoNull(){
        Integer giorno = null;
        assertAll(
                () ->   assertThrows(NullPointerException.class,
                        () -> mock.media(giorno, 0.30f)
                ),
                () ->   assertThrows(NullPointerException.class,
                        () -> mock.media(giorno, -0.30f)
                )
        );
    }
    @Test
    public void testIncassoNull(){
        Float incasso = null;
        assertAll(
                () ->   assertThrows(NullPointerException.class,
                        () -> mock.media(0, incasso)
                ),
                () ->   assertThrows(NullPointerException.class,
                        () -> mock.media(3, incasso)
                ),
                () ->   assertThrows(NullPointerException.class,
                        () -> mock.media(-3, incasso)
                )
        );
    }

    @Test
    public void testCampiNull(){
        Integer giorno = null;
        Float incasso = null;
        assertThrows(NullPointerException.class,
                () -> mock.media(giorno, incasso)
        );
    }


}
