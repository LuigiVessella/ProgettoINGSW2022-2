package com.example.progettoingsw2022_2;

import static com.example.progettoingsw2022_2.Helper.AccountUtils.checkCredentials;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.Test;
import org.junit.jupiter.api.AfterEach;

import java.util.ArrayList;

public class CheckCredentialsTest {

    /*

   CLASSI DI EQUIVALENZA:

         PASSWORD: {VALIDA, VUOTA, TROPPO CORTA, NON RISPETTA LA REGEX}
           - VALIDA = Almeno 8 caratteri di cui 1 Maiuscola,
           1 Minuscola, 1 Carattere speciale, 1 numero

         EMAIL: {VALIDA, VUOTA, NON VALIDA}
           - VALIDA = Deve rispettare la sintassi
           di un email corretta

 ------------------------------------------------------------------------------------

   CODICI DI ERRORE:
      9 = PASSWORD VUOTA,
      10 = PASSWORD TROPPO CORTA,
      11 = PASSWORD NON VALIDA (REGEX)
      12 = EMAIL VUOTA,
      13 = EMAIL NON VALIDA

-------------------------------------------------------------------------------------

   STRATEGIE DI TESTING UTILIZZATE:
      BlackBox secondo il criterio SECT

     */


    public ArrayList<Integer> codici_errore = new ArrayList<Integer>();

    @AfterEach
    public void clearArrayList() {
        codici_errore.clear();
    }

    @Test
    public void testCheckCredentials(){
        assertEquals(codici_errore, checkCredentials("ser.dimartino@studenti.unina.it", "Password.123"));
    }

    @Test
    public void tesPasswordVuota(){
        codici_errore.add(9);
        assertAll(
                () -> assertEquals(codici_errore, checkCredentials("fra.cutugno@studenti.unina.it", null)),
                () -> assertEquals(codici_errore, checkCredentials("lu.starace@studenti.unina.it", ""))
        );
    }

    @Test
    public void testPasswordTroppoCorta(){
        codici_errore.add(10);
        assertEquals(codici_errore, checkCredentials("gio.cutolo@studenti.unina.it", "Aa_09."));
    }

    @Test
    public void testPasswordNonValida(){
        codici_errore.add(11);
        assertAll(
                () -> assertEquals(codici_errore, checkCredentials("alb.aloisio@studenti.unina.it", "password?123")),  // MANCA LA MAIUSCOLA
                () -> assertEquals(codici_errore, checkCredentials("an.corazza@studenti.unina.it", "PASSWORD#123")),  //MANCA LA MINUSCOLA
                () -> assertEquals(codici_errore, checkCredentials("alb.aloisio@studenti.unina.it", "Password123")),  // MANCA IL CARATTERE SPECIALE
                () -> assertEquals(codici_errore, checkCredentials("an.corazza@studenti.unina.it", "Password_unoduetre")) // MANCA IL NUMERO
        );

    }

    @Test
    public void testEmailVuota(){
        codici_errore.add(12);
        assertAll(
                () -> assertEquals(codici_errore, checkCredentials(null, "Password.123")),
                () -> assertEquals(codici_errore, checkCredentials("", "Password.123"))
        );
    }

    @Test
    public void testEmaildNonValida(){
        codici_errore.add(13);
        assertAll(
                () -> assertEquals(codici_errore, checkCredentials("emailcompletamentesbagliata", "Password.123")),  // EMAIL COMPLETAMENTE SBAGLIATA
                () -> assertEquals(codici_errore, checkCredentials("@gmail.com", "Password.123")),  // MANCA LO USERNAME
                () -> assertEquals(codici_errore, checkCredentials("biagio@.net", "Password.123")),  // MANCA IL SECOND LEVEL DOMAIN
                () -> assertEquals(codici_errore, checkCredentials("matteo@libero.", "Password.123")), // MANCA IL TOP LEVEL DOMAIN
                () -> assertEquals(codici_errore, checkCredentials("luigivirgilio.it", "Password.123")), //MANCA LA @
                () -> assertEquals(codici_errore, checkCredentials("MATTEO[BIAGIO]LUIGI_@libero.IT", "Password.123")) // LO USERNAME PRESENTA CARATTERI NON CORRETTI
        );
    }

    @Test
    public void testErroriMultipli_9_12(){
        codici_errore.add(9);
        codici_errore.add(12);

        assertEquals(codici_errore, checkCredentials("", ""));
    }

    @Test
    public void testErroriMultipli_9_13(){
        codici_errore.add(9);
        codici_errore.add(13);

        assertEquals(codici_errore, checkCredentials("@studenti.@libero@com", ""));
    }

    @Test
    public void testErroriMultipli_10_12(){
        codici_errore.add(10);
        codici_errore.add(12);

        assertEquals(codici_errore, checkCredentials("", "Ab.34"));
    }

    @Test
    public void testErroriMultipli_10_13(){
        codici_errore.add(10);
        codici_errore.add(13);

        assertEquals(codici_errore, checkCredentials("emailcompletamentesbagliata", "Ab.34"));
    }

    @Test
    public void testErroriMultipli_11_12(){
        codici_errore.add(11);
        codici_errore.add(12);

        assertEquals(codici_errore, checkCredentials("", "Password.Password"));
    }

    @Test
    public void testErroriMultipli_11_13(){
        codici_errore.add(11);
        codici_errore.add(13);

        assertEquals(codici_errore, checkCredentials("@studenti.@libero@com", "@studenti.it"));
    }
}



