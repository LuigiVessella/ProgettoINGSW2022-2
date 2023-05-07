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
    public void testGetIncassoRangeGiorni() throws ParseException {
        ordiniM.add(new OrdineMock(3, "2023-05-04"));
        ordiniM.add(new OrdineMock(105, "2023-05-04"));
        ordiniM.add(new OrdineMock(72, "2023-05-04"));
        ordiniM.add(new OrdineMock(8, "2023-05-05"));
        ordiniM.add(new OrdineMock(80, "2023-05-05"));
        ordiniM.add(new OrdineMock(1, "2023-05-06"));
        ordiniM.add(new OrdineMock(3, "2023-05-06"));
        ordiniM.add(new OrdineMock(83, "2023-05-06"));
        ordiniM.add(new OrdineMock(11, "2023-05-06"));
        ordiniM.add(new OrdineMock(44, "2023-05-06"));
        ordiniM.add(new OrdineMock(89, "2023-05-06"));

        Calendar cal = Calendar.getInstance();
        cal.set(2023, 4, 4); // Mese inizia da 0, quindi 4 rappresenta maggio
        Date dataInizio = cal.getTime();

        statisticsActivityMock.setOrdini(ordiniM);

        int result = statisticsActivityMock.getIncassoRangeGiorni(dataInizio, ordiniM);
        assertEquals(499, result);
    }
}
