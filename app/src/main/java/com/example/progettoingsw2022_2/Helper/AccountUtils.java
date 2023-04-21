package com.example.progettoingsw2022_2.Helper;

import android.content.Context;

import com.example.progettoingsw2022_2.R;

import org.apache.commons.validator.routines.EmailValidator;

public class AccountUtils {

    public static boolean isCodiceFiscaleValidoAdE(String cf) {
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
    public static boolean isCodiceFiscaleValidoSimple(String cf) { return cf.matches("^[\\dA-Z]{16}$"); }

    public static String checkPassword(Context context, String pass){

        String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[#_!$%?.,;:])[\\w#_!$%?.,;:]{8,}$";
        pass = pass.replaceAll("\\s", "");
        if (pass.isEmpty()) {
            return context.getString(R.string.fieldRequired);
        } else if (pass.length()<8) {
            return context.getString(R.string.passwordShort);
        } else if (!pass.matches(regex)) {
            return context.getString(R.string.passwordSimple); }
        else return "OK";
    }

    public static String checkEmail(Context context, String email){
        EmailValidator validator = EmailValidator.getInstance();
        if (email.length() == 0) return context.getString(R.string.fieldRequired);
        else if(!validator.isValid(email)) return context.getString(R.string.emailError);
        else return "OK";
    }

    public static String checkPIVA(Context context, String pIva){
        if (pIva.length() == 0) return context.getString(R.string.fieldRequired);
        else if (!pIva.matches("^\\d{11}$")) return context.getString(R.string.fieldIncorrect);
        else return "OK";
    }


}
