package com.example.progettoingsw2022_2;

import com.example.progettoingsw2022_2.Driver.OrdineMock;
import com.example.progettoingsw2022_2.Driver.StatisticsActivityMock;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class getIncassoRangeGiorniTest {

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

    /*
    @Test
    public void testDataOrdiniInDiversoFormato() {
        ordiniM.add(new OrdineMock(3, "04-19-2023"));
        ordiniM.add(new OrdineMock(105, "30-30-2023"));
        ordiniM.add(new OrdineMock(72, "2023-02-31"));


        LocalDate dataInizio = LocalDate.parse("2023-02-01", formatter);

        int result = statisticsActivityMock.getIncassoRangeGiorni(dataInizio, ordiniM);
        assertEquals(180, result);
    }

     */

    @Test
    public void testDataOrdiniInFormatoSbagliato() throws ParseException{
        ArrayList<OrdineMock> ordiniM_1 = new ArrayList<>();
        ArrayList<OrdineMock> ordiniM_2 = new ArrayList<>();
        ArrayList<OrdineMock> ordiniM_3 = new ArrayList<>();
        ArrayList<OrdineMock> ordiniM_4 = new ArrayList<>();

        ordiniM_1.add(new OrdineMock(3, "2023"));
        ordiniM_2.add(new OrdineMock(105, "05-2023"));
        ordiniM_3.add(new OrdineMock(72, "due-aprile-2023"));
        ordiniM_4.add(new OrdineMock(105, "31-22-2023"));


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
                () ->   assertThrows(DateTimeParseException.class,
                        () -> statisticsActivityMock.getIncassoRangeGiorni(dataInizio, ordiniM_4)
                )

        );
        ordiniM_1.clear();
        ordiniM_2.clear();
        ordiniM_3.clear();
        ordiniM_4.clear();
    }
}
