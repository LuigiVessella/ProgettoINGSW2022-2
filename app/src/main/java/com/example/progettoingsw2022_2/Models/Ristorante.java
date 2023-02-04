package com.example.progettoingsw2022_2.Models;

import java.util.List;

public class Ristorante {

    private Long codice_ristorante;
    private String nome;
    private int coperti;
    private String locazione;
    //private Admin proprietario;

    private List camerieri;
    public Ristorante() {
    }

    public Long getCodice_ristorante() {
        return codice_ristorante;
    }

    public void setCodice_ristorante(Long codice_ristorante) {
        this.codice_ristorante = codice_ristorante;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getCoperti() {
        return coperti;
    }

    public void setCoperti(int coperti) {
        this.coperti = coperti;
    }

    public String getLocazione() {
        return locazione;
    }

    public void setLocazione(String locazione) {
        this.locazione = locazione;
    }

    public List getCamerieri() {
        return camerieri;
    }

    public void setCamerieri(List camerieri) {
        this.camerieri = camerieri;
    }
}
