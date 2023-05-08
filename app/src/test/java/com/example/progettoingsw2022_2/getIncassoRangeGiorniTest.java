package com.example.progettoingsw2022_2;

import com.example.progettoingsw2022_2.Driver.OrdineMock;
import com.example.progettoingsw2022_2.Driver.StatisticsActivityMock;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class getIncassoRangeGiorniTest {

    StatisticsActivityMock statisticsActivityMock;
    ArrayList<OrdineMock> ordiniM;

    @Before
    public void setUp(){
        statisticsActivityMock = new StatisticsActivityMock();
        ordiniM = new ArrayList<>();
    }

    @AfterEach
    public void clearArrayList(){
        ordiniM.clear();
    }


    @Test
    public void testGetIncassoRangeGiorni() throws ParseException{
        ordiniM.add(new OrdineMock(3, "2023-05-04"));
        ordiniM.add(new OrdineMock(105, "2023-05-04"));
        ordiniM.add(new OrdineMock(72, "2023-02-04"));


        Calendar cal = Calendar.getInstance();
        cal.set(2023, 4, 4); // Mese inizia da 0, quindi 4 rappresenta maggio
        Date dataInizio = cal.getTime();

        int result = statisticsActivityMock.getIncassoRangeGiorni(dataInizio, ordiniM);
        assertEquals(180, result);
    }

    @Test
    public void testZeroOrdini() throws ParseException {
        Calendar cal = Calendar.getInstance();
        cal.set(2023, 4, 4); // Mese inizia da 0, quindi 4 rappresenta maggio
        Date dataInizio = cal.getTime();

        int result = statisticsActivityMock.getIncassoRangeGiorni(dataInizio, ordiniM);
        assertEquals(0, result);

    }

    @Test
    public void testDataOrdiniInDiversoFormato() throws ParseException{
        ordiniM.add(new OrdineMock(3, "04-19-2023"));
        ordiniM.add(new OrdineMock(105, "30-30-2023"));
        ordiniM.add(new OrdineMock(72, "2023-02-31"));


        Calendar cal = Calendar.getInstance();
        cal.set(2023, 4, 4); // Mese inizia da 0, quindi 4 rappresenta maggio
        Date dataInizio = cal.getTime();

        int result = statisticsActivityMock.getIncassoRangeGiorni(dataInizio, ordiniM);
        assertEquals(180, result);
    }

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


        Calendar cal = Calendar.getInstance();
        cal.set(2023, 4, 4); // Mese inizia da 0, quindi 4 rappresenta maggio
        Date dataInizio = cal.getTime();

        assertAll(
                () ->   assertThrows(ParseException.class,
                        () -> statisticsActivityMock.getIncassoRangeGiorni(dataInizio, ordiniM_1)
                ),
                () ->   assertThrows(ParseException.class,
                        () ->statisticsActivityMock.getIncassoRangeGiorni(dataInizio, ordiniM_2)
                ),
                () ->   assertThrows(ParseException.class,
                        () -> statisticsActivityMock.getIncassoRangeGiorni(dataInizio, ordiniM_3)
                ),
                () ->   assertThrows(ParseException.class,
                        () -> statisticsActivityMock.getIncassoRangeGiorni(dataInizio, ordiniM_4)
                )

        );
        ordiniM_1.clear();
        ordiniM_2.clear();
        ordiniM_3.clear();
    }
}
