package com.example.progettoingsw2022_2.Helper;

import android.content.Context;
import android.view.View;

import com.example.progettoingsw2022_2.R;

import org.apache.commons.validator.routines.EmailValidator;

import java.util.ArrayList;

public class AccountUtils {

    public static boolean isCodiceFiscaleValido(String cf) {
        if(cf == null) return false;
        if( ! cf.matches("^[\\dA-Z]{16}$") )
            return false;
        int s = 0;
        String even_map = "BAFHJNPRTVCESULDGIMOQKWZYX";
        for(int i = 0; i < 15; i++){
            int c = cf.charAt(i);
            int n;
            if( '0' <= c && c <= '9' )
                n = c - '0';
            else
                n = c - 'A';
            if( (i & 1) == 0 )
                n = even_map.charAt(n) - 'A';
            s += n;
        }
        return s % 26 + 'A' == cf.charAt(15);
    }
    public static ArrayList<Integer> checkCredentials(String mail, String pswrd){
        ArrayList<Integer> errors = new ArrayList<>();
        String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[#_!$%?.,;:])[\\w#_!$%?.,;:]{8,}$";
        if(pswrd == null) errors.add(9);
        else {
            pswrd = pswrd.replaceAll("\\s", "");
            if (pswrd.isEmpty()) {
                errors.add(9);
            } else if (pswrd.length() < 8) {
                errors.add(10);
            } else if (!pswrd.matches(regex)) {
                errors.add(11);
            }
        }
        EmailValidator validator = EmailValidator.getInstance();
        if (mail == null || mail.length() == 0) errors.add(12);
        else if(!validator.isValid(mail)) errors.add(13);

        return errors;
    }


    public static ArrayList<Integer> getRegistrationFieldsErrors(String nome, String cognome, String pIva, String cf, String pass, String email){
        ArrayList<Integer> errors = new ArrayList<>();

        if (nome.length() <= 3) errors.add(1);
        if (nome.length() == 0) errors.add(2);
        if (cognome.length() <= 3) errors.add(3);
        if (cognome.length() == 0) errors.add(4);

        int errorIva = checkPIVA(pIva);
        if (errorIva == 1) errors.add(5);
        if (errorIva == 2) errors.add(6);
        //Check codice fiscale

        if (cf.length() == 0) errors.add(7);
        else if (!isCodiceFiscaleValido(cf)) errors.add(8);

        errors.addAll(checkCredentials(email, pass));

        return errors;
    }


    public static int checkPIVA(String pIva){
        if (pIva.length() == 0) return 1;
        else if (!pIva.matches("^\\d{11}$")) return 2;
        else return 0;
    }

        public static ArrayList<Integer> getRestaurantFieldsErrors(String nome, String coperti, String locazione, String numeroTelefono){
            ArrayList<Integer> codici_errore = new ArrayList<>();
            String regex = "^[^.,;:\\-_#\\[\\](){}|!=?'^<>§&%]*$";
            if (nome.length() == 1) codici_errore.add(1);
            if (nome.length() == 0) codici_errore.add(2);
            if (coperti.length() == 0) {
                codici_errore.add(3);
            }else {
                try {
                    int numCoperti = Integer.parseInt(coperti);
                    if (numCoperti > 1000 || numCoperti < 5) codici_errore.add(4);
                }
                 catch (NumberFormatException ex) {
                    codici_errore.add(9);
                }
            }
            if (locazione.length() == 0) codici_errore.add(5);
            else if (locazione.length() < 5) codici_errore.add(6);
            else if (!locazione.matches(regex)) codici_errore.add(10);

            if (numeroTelefono.length() == 0) codici_errore.add(7);
            else if (!numeroTelefono.matches("^\\d{10}$")) codici_errore.add(8);

            return codici_errore;
        }
}

