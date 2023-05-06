package com.example.progettoingsw2022_2;

import static com.example.progettoingsw2022_2.Helper.AccountUtils.getRestaurantFieldsErrors;
import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.Test;
import org.junit.jupiter.api.AfterEach;


import java.util.ArrayList;

public class getRestaurantFiedlsErrorsTest {

    public ArrayList<Integer> codici_errore = new ArrayList<Integer>();


    /*
     getRestaurantFieldsError è un metodo che prende 4 parametri di tipo Stringa: Nome, Coperti, Indirizzo, Numero di telefono: N, C, I, T.
     Il valore di ritorno del metodo sarà un ArrayList di interi dove ognuno conterrà un valore

     Per ogni campo possimo identificare delle classi di equivalenza: N = {VALIDO, TROPPO CORTO, NULL}
                                                                                - TROPPO CORTO: Nome compostoda un solo carattere
                                                                      C = {VALIDO, FUORI RANGE, NON VALIDO, NULL}
                                                                                - FUORI RANGE: C > 1000 || C < 5
                                                                                - NON VALIDO: C contiene o è interamente composto da caratteri non numerici
                                                                      I = {VALIDO, TROPPO CORTO, NON VALIDO NULL}
                                                                                - TROPPO CORTO: I < 5
                                                                                - NON VALIDO: I contiene o è composto da caratteri speciali
                                                                      T = {VALIDO, NON VALIDO, NULL}
                                                                                - NON VALIDO: T non rispetta la regex che controlla che sia effettivamente un numero di telefono valido

    Valutando l'implementazione del metodo verso cui stiamo eseguendo del testing con strategia Black-Box, riteniamo che il WEAK EQUIVALENCE CLASS TESTING sia il criterio di copertura più indicato, fornendo
    8 Test Cases che riteniamo siano sufficienti a ritenere la batteria di testing esaustiva

     */



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
    public void testNumeroTelefonoCampoTroppoCorto() {
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
