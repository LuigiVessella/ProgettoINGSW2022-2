package com.example.progettoingsw2022_2;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.content.Context;

import com.example.progettoingsw2022_2.Helper.AccountUtils;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class CodiceFiscaleTest {

    @Mock
    Context mockContext;


    public void codiceFiscaleIsCorrect(){
        assertTrue(AccountUtils.isCodiceFiscaleValidoSimple("VSSLGU01D05G596V"));

        assertFalse(AccountUtils.isCodiceFiscaleValidoSimple("AB45"));
    }

    @Test
    public void checkIfPasswordCorrect(){
        MockitoAnnotations.initMocks(this);
        assertEquals( "OK", AccountUtils.checkPassword(mockContext, "Pas"));
    }

}
