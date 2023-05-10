package com.example.progettoingsw2022_2;

import static com.example.progettoingsw2022_2.Helper.AccountUtils.getRestaurantFieldsErrors;
import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.Test;
import org.junit.jupiter.api.AfterEach;


import java.util.ArrayList;

public class GetRestaurantFiedlsErrorsTest {

    public ArrayList<Integer> codici_errore = new ArrayList<Integer>();

/*
   CLASSI DI EQUIVALENZA:

      NOME: {VALIDO, VUOTO, TROPPO CORTO}

      COPERTI: {VALIDO, VUOTO, FUORI-RANGE, NON VALIDO}
        - FUORI RANGE: <5 && > 1000
        - NON VALIDO: Non composto da soli numeri

      INDIRIZZO: {VALIDO, VUOTO, TROPPO CORTO, NON VALIDO}
        - NON VALIDO: Contiene caratteri speciali

      TELEFONO: {VALIDO, VUOTO, NON VALIDO}

 --------------------------------------------------------------------------------

   CODICI DI ERRORE:
    1 = NOME TROPPO CORTO (1 CARATTERE MINIMO)
    2 = NOME MANCANTE
    3 = NUMERO DI COPERTI MANCANTE
    4 = NUMERO DI COPERTI FUORI RANGE
    5 = INDIRIZZO MANCANTE
    6 = INDIRIZZO TROPPO CORTA (MINIMO 5 CARATTERI)
    7 = NUMERO DI TELEFONO MANCANTE
    8 = NUMERO DI TELEFONO NON VALIDO (10 CIFRE NUMERICHE RICHIESTE)
    9 = NUMERO DI COPERTI ERRATO
    10 = INDIRIZZO NON VALIDO

-------------------------------------------------------------------------------------

   STRATEGIE DI TESTING UTILIZZATE:
      BlackBox secondo il criterio WECT

   CASI DI TESTING RITENUTI NECESSARI:
     {VALIDO, VALIDO, VALIDO, VALIDO} : 1 Caso
     {TROPPO CORTO, VALIDO, VALIDO, VALIDO} : 1 Caso
     {VALIDO, FUORI-RANGE, VALIDO, VALIDO} : 2 Casi
     {VALIDO, NON VALIDO, VALIDO, VALIDO} : 2 Casi
     {VALIDO, VALIDO, TROPPO CORTO, VALIDO} : 1 Caso
     {VALIDO, VALIDO, NON VALIDO, VALIDO} : 1 Caso
     {VALIDO, VALIDO, VALIDO, NON VALIDO} : 1 Caso
     {NULL, NULL, NULL, NULL} : 1 Caso

 ---------------------------------------------------------------------------- */

    // L'ARRAYLIST DEVE ESSERE PULITO OGNI VOLTA CHE VIENE CONCLUSO UN CASO DI TEST
    @AfterEach
    public void clearArrayList(){
        codici_errore.clear();
    }

    @Test
    public void testgetRestaurantFieldsError(){
        ArrayList<Integer> actualErrors = getRestaurantFieldsErrors("Ristorante Test", "10", "Via Roma 1", "0123456789");
        assertEquals(codici_errore, actualErrors); //Funziona poichè non ci sono codici di errore: l'ArrayList risulta vuoto
    }

    @Test
    public void testNomeCampoTroppoCorto() {
        codici_errore.add(1);
        ArrayList<Integer> actualErrors = getRestaurantFieldsErrors("a", "10", "Via Roma 1", "0123456789");
        assertEquals(codici_errore, actualErrors);
    }

    @Test
    public void testCampoCopertiFuoriRange(){
        codici_errore.add(4);
        assertAll(
                () -> assertEquals(codici_errore, getRestaurantFieldsErrors("Ristorante Test", "1", "Via Roma 1", "0123456789")),
                () -> assertEquals(codici_errore, getRestaurantFieldsErrors("Ristorante Test", "100000", "Via Roma 1", "0123456789"))
        );
    }

    @Test
    public void testCampoCopertiNonValido(){
        codici_errore.add(9);
        assertAll(
                () -> assertEquals(codici_errore, getRestaurantFieldsErrors("Ristorante Test", "dieci", "Via Roma 1", "0123456789")),
                () -> assertEquals(codici_errore, getRestaurantFieldsErrors("Ristorante Test", "10a", "Via Roma 1", "0123456789"))
        );
    }


    @Test
    public void testLocazioneCampoTroppoCorto() {
        codici_errore.add(6);
        ArrayList<Integer> actualErrors = getRestaurantFieldsErrors("Ristorante Test", "10", "Via", "0123456789");
        assertEquals(codici_errore, actualErrors);
    }

    @Test
    public void testLocazioneNonValido() {
        codici_errore.add(10);
        ArrayList<Integer> actualErrors = getRestaurantFieldsErrors("Ristorante Test", "10", "Via_Napoli?", "0123456789");
        assertEquals(codici_errore, actualErrors);
    }


    @Test
    public void testNumeroTelefonoNonValido() {
        codici_errore.add(8);
        ArrayList<Integer> actualErrors = getRestaurantFieldsErrors("Ristorante Test", "10", "Via Roma 1", "12345678");
        assertEquals(codici_errore, actualErrors);
    }

    @Test
    public void testTuttiICampiVuoti() {
        codici_errore.add(2);
        codici_errore.add(3);
        codici_errore.add(5);
        codici_errore.add(7);
        ArrayList<Integer> actualErrors = getRestaurantFieldsErrors("", "", "", "");
        assertEquals(codici_errore, actualErrors);
    }
}
