package com.example.progettoingsw2022_2.Models;


import android.content.Context;
import android.util.Log;

import com.example.progettoingsw2022_2.R;

import org.apache.commons.validator.routines.EmailValidator;

public abstract class Lavoratore {
    private String codiceFiscale;
    private String nome;
    private String cognome;
    private String email;
    private String ruolo;

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
        if (pass.isEmpty()) return context.getString(R.string.fieldRequired);
        else if (pass.length()<6) return context.getString(R.string.passwordShort);
       // else if (!pass.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]+$")) { Log.i("Check regex","DIOCANE"); return context.getString(R.string.passwordSimple); }
        else return "OK";
    }

    public static String checkEmail(Context context, String email){
        EmailValidator validator = EmailValidator.getInstance();
        if (email.length() == 0) return context.getString(R.string.fieldRequired);
        else if(!validator.isValid(email)) return context.getString(R.string.emailError);
        else return "OK";
    }

    public String getCodiceFiscale() {
        return this.codiceFiscale;
    }

    public void setCodiceFiscale(String codiceFiscale) {
        this.codiceFiscale = codiceFiscale;
    }

    public String getNome() {
        return this.nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCognome() {
        return this.cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRuolo() {
        return ruolo;
    }

    public void setRuolo(String ruolo) {
        this.ruolo = ruolo;
    }
}
