package com.example.progettoingsw2022_2;

import static com.example.progettoingsw2022_2.Helper.AccountUtils.getRestaurantFieldsErrors;
import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.Test;
import org.junit.jupiter.api.AfterEach;


import java.util.ArrayList;

public class getRestaurantFiedlsErrorsTest {

    public ArrayList<Integer> codici_errore = new ArrayList<Integer>();


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
    public void testNomeCampoNullo() {
        codici_errore.add(2);
        ArrayList<Integer> actualErrors = getRestaurantFieldsErrors("", "10", "Via Roma 1", "0123456789");
        assertEquals(codici_errore, actualErrors);
    }

    @Test
    public void testNumeroCopertiNullo() {
        codici_errore.add(3);
        ArrayList<Integer> actualErrors = getRestaurantFieldsErrors("Ristorante Test", "", "Via Roma 1", "0123456789");
        assertEquals(codici_errore, actualErrors);
    }

    @Test
    public void testNumeroCopertiTroppoAlto() {
        codici_errore.add(4);
        ArrayList<Integer> actualErrors = getRestaurantFieldsErrors("Ristorante Test", "5000", "Via Roma 1", "0123456789");
        assertEquals(codici_errore, actualErrors);
    }

    @Test
    public void testNumeroCopertiTroppoBasso() {
        codici_errore.add(4);
        ArrayList<Integer> actualErrors = getRestaurantFieldsErrors("Ristorante Test", "2", "Via Roma 1", "0123456789");
        assertEquals(codici_errore, actualErrors);
    }

    @Test
    public void testLocazioneCampoNullo() {
        codici_errore.add(5);
        ArrayList<Integer> actualErrors = getRestaurantFieldsErrors("Ristorante Test", "10", "", "0123456789");
        assertEquals(codici_errore, actualErrors);
    }

    @Test
    public void testLocazioneCampoTroppoCorto() {
        codici_errore.add(6);
        ArrayList<Integer> actualErrors = getRestaurantFieldsErrors("Ristorante Test", "10", "Via", "0123456789");
        assertEquals(codici_errore, actualErrors);
    }

    @Test
    public void testNumeroTelefonoCampoNullo() {
        codici_errore.add(7);
        ArrayList<Integer> actualErrors = getRestaurantFieldsErrors("Ristorante Test", "10", "Via Roma 1", "");
        assertEquals(codici_errore, actualErrors);
    }

    @Test
    public void testNumeroTelefonoCampoTroppoCorto() {
        codici_errore.add(8);
        ArrayList<Integer> actualErrors = getRestaurantFieldsErrors("Ristorante Test", "10", "Via Roma 1", "12345678");
        assertEquals(codici_errore, actualErrors);
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
    public void testTuttiICampiVuoti() {
        codici_errore.add(2);
        codici_errore.add(3);
        codici_errore.add(5);
        codici_errore.add(7);
        ArrayList<Integer> actualErrors = getRestaurantFieldsErrors("", "", "", "");
        assertEquals(codici_errore, actualErrors);
    }
}
