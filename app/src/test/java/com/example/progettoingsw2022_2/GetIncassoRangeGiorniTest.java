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
   CLASSI DI EQUIVALENZA:

      DATA_INIZIO: {VALIDA, NULL}
        - Valida: a sua volta può essere
             1.1) Verosimile e precedente agli ordini
             1.2) Futura agli ordini
             1.3) Inverosimilmente precedente agli ordini

      ORDINI: {VALIDI, NULL, LISTA VUOTA, CON DATA SBAGLIATA}

 --------------------------------------------------------------------------------

   STRATEGIE DI TESTING UTILIZZATE:
      BlackBox secondo il criterio WECT

   CASI DI TESTING RITENUTI NECESSARI:
     {VALIDA , VALIDI} : 1 Caso
     {VALIDA , NULL} : 1 Caso
     {VALIDA , LISTA VUOTA} : 1 Caso
     {VALIDA , CON DATA SBAGLIATA} : 4 Casi
     {DATA FUTURA , VALIDI} : 1 Caso
     {DATA INVEROSIMILMENTE PRECEDENTE , VALIDI} : 1 Caso
     {NULL , VALIDI} : 1 Caso

 ---------------------------------------------------------------------------- */

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
