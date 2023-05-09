package com.example.progettoingsw2022_2;

import com.example.progettoingsw2022_2.Driver.OrdineMock;
import com.example.progettoingsw2022_2.Driver.StatisticsActivityMock;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

/*
 Metodologie di testing utilizzate: Black-Box e Weak equivalence class testing poichè: ci sono 4 tipi di input per il parametro Ordini:
           1) Validi
           2) null
           3) Lista Vuota
           4) con data Sbagliata in qualche modo

 Invece per quanto riguarda la data di inizio può essere:
           1) Valida, che a sua volta può essere
                1.1) Verosimile e precedente agli ordini
                1.2) Futura agli ordini
                1.3) Inverosimilmente precedente agli ordini
           2) Null


 Oltre al caso di testing in cui si valuta il corretto funzionamento del metodo testandolo in condizioni normali, abbiamo voluto testare il metodo fornendo tutti i tipi di ordini testandoli
 con una Data di Inizio Valida e Verosimile; e fornndo tutti i casi di Data Iniziale insieme ad un ordine corretto, arrivando a 7 casi di testing che riteniamo siano necessari:
            {1.1 , 1}
            {1.1 , 2}
            {1.1 , 3}
            {1.1 , 4}
            {1.2 , 1}
            {1.3 , 1}
            {2 , 1}

 */

public class GetIncassoRangeGiorniTest {

    StatisticsActivityMock statisticsActivityMock;
    ArrayList<OrdineMock> ordiniM;
    DateTimeFormatter formatter;

    @Before
    public void setUp(){
        statisticsActivityMock = new StatisticsActivityMock();
        ordiniM = new ArrayList<>();
        formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    }

    @AfterEach
    public void clearArrayList(){
        ordiniM.clear();
    }


    @Test
    public void testGetIncassoRangeGiorni() {
        ordiniM.add(new OrdineMock(3, "2023-05-04"));
        ordiniM.add(new OrdineMock(105, "2023-05-04"));
        ordiniM.add(new OrdineMock(72, "2023-02-04"));

        LocalDate dataInizio = LocalDate.parse("2023-02-05", formatter);

        int result = statisticsActivityMock.getIncassoRangeGiorni(dataInizio, ordiniM);
        assertEquals(108, result);
    }

    @Test
    public void testZeroOrdini()  {
        LocalDate dataInizio = LocalDate.parse("2023-02-01", formatter);

        int result = statisticsActivityMock.getIncassoRangeGiorni(dataInizio, ordiniM);
        assertEquals(0, result);

    }

    @Test
    public void testOrdiniNull()  {
        LocalDate dataInizio = LocalDate.parse("2023-02-01", formatter);

        int result = statisticsActivityMock.getIncassoRangeGiorni(dataInizio, null);
        assertEquals(0, result);

    }

    @Test
    public void testDataInizioNull()  {
        ordiniM.add(new OrdineMock(3, "2023-05-04"));
        ordiniM.add(new OrdineMock(105, "2023-05-04"));
        ordiniM.add(new OrdineMock(72, "2023-02-04"));

        assertThrows(NullPointerException.class,
                () -> statisticsActivityMock.getIncassoRangeGiorni(null, ordiniM));
    }

    @Test
    public void testDataInizioFutura() {
        ordiniM.add(new OrdineMock(3, "2023-05-04"));
        ordiniM.add(new OrdineMock(105, "2023-05-04"));
        ordiniM.add(new OrdineMock(72, "2023-02-04"));

        LocalDate dataInizio = LocalDate.parse("2033-02-05", formatter);

        int result = statisticsActivityMock.getIncassoRangeGiorni(dataInizio, ordiniM);
        assertEquals(0, result);
    }

    @Test
    public void testDataInizioIrrealistica() {
        ordiniM.add(new OrdineMock(3, "2023-05-04"));
        ordiniM.add(new OrdineMock(105, "2023-05-04"));
        ordiniM.add(new OrdineMock(72, "2023-02-04"));

        LocalDate dataInizio = LocalDate.parse("0133-02-05", formatter);

        int result = statisticsActivityMock.getIncassoRangeGiorni(dataInizio, ordiniM);
        assertEquals(180, result);
    }

    @Test
    public void testDataOrdiniInFormatoSbagliato(){
        ArrayList<OrdineMock> ordiniM_1 = new ArrayList<>();
        ArrayList<OrdineMock> ordiniM_2 = new ArrayList<>();
        ArrayList<OrdineMock> ordiniM_3 = new ArrayList<>();
        ArrayList<OrdineMock> ordiniM_4 = new ArrayList<>();

        ordiniM_1.add(new OrdineMock(3, "2023"));
        ordiniM_2.add(new OrdineMock(105, "02/05/2023"));
        ordiniM_3.add(new OrdineMock(72, "due-aprile-2023"));
        ordiniM_4.add(new OrdineMock(105, "2023-32-31"));


        LocalDate dataInizio = LocalDate.parse("2023-02-01", formatter);

        assertAll(
                () ->   assertThrows(DateTimeParseException.class,
                        () -> statisticsActivityMock.getIncassoRangeGiorni(dataInizio, ordiniM_1)
                ),
                () ->   assertThrows(DateTimeParseException.class,
                        () ->statisticsActivityMock.getIncassoRangeGiorni(dataInizio, ordiniM_2)
                ),
                () ->   assertThrows(DateTimeParseException.class,
                        () -> statisticsActivityMock.getIncassoRangeGiorni(dataInizio, ordiniM_3)
                ),
                () ->   assertThrows(DateTimeException.class,
                        () -> statisticsActivityMock.getIncassoRangeGiorni(dataInizio, ordiniM_4)
                )

        );
        ordiniM_1.clear();
        ordiniM_2.clear();
        ordiniM_3.clear();
        ordiniM_4.clear();
    }

}
