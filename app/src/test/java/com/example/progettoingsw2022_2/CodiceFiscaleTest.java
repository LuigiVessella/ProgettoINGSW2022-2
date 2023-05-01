package com.example.progettoingsw2022_2;

import static com.example.progettoingsw2022_2.Helper.AccountUtils.isCodiceFiscaleValido;
import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.Test;


public class CodiceFiscaleTest {
    @Test
    public void testCodiceFiscaleValido() {
        String cf = "SPRBGI99P20F839N"; //BIAGIO SPERANZA 20/09/1999 - NAPOLI
        assertTrue(isCodiceFiscaleValido(cf));
    }

    @Test
    public void testCodiceFiscaleValidoConSpazio() {
        String cf = "SPRBGI9 9P20F8 39N"; //CODICE FISCALE CORRETTO MA CON AGGIUNTA DI SPAZI
        assertFalse(isCodiceFiscaleValido(cf));
    }

    @Test
    public void testCodiceFiscaleNonValido() {
        String cf = "BPRBGI99P20F839N"; //VARIAZIONE DI UN CARATTERE DEL CODICE FISCALE CORRETTO
        assertFalse(isCodiceFiscaleValido(cf));
    }

    @Test
    public void testCodiceFiscaleInventato() {
        String cf = "BBHFTI09V87H011N"; //CODICE FISCALE COMPLETAMENTE INVENTATO
        assertFalse(isCodiceFiscaleValido(cf));
    }

    @Test
    public void testCodiceFiscaleCaratteriNonValidi() {
        String cf = "$PRBGI.9P20F839%"; //CODICE FISCALE CON CARATTERI NON VALIDI
        assertFalse(isCodiceFiscaleValido(cf));
    }

    @Test
    public void testCodiceFiscaleConCaratteriEscape() {
        String cf = "SPR\u0009BGI99P20F839N"; //CODICE FISCALE CON UN CARATTERE DI ESCAPE
        assertFalse(isCodiceFiscaleValido(cf));
    }

    @Test
    public void testCodiceFiscaleLunghezzaNonCorretta() {
        assertAll(
                () -> assertFalse(isCodiceFiscaleValido("SPRBGI99P20F839N4VB")), // CODICE FISCALE TROPPO LUNGO
                () -> assertFalse(isCodiceFiscaleValido("SP")) // CODICE FISCALE TROPPO CORTO
        );
    }

    @Test
    public void testCodiceFiscaleNull() {
        String cf = null; //CODICE FISCALE NULL
        assertFalse(isCodiceFiscaleValido(cf));
    }
}