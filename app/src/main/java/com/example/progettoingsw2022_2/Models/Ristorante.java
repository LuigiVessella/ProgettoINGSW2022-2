package com.example.progettoingsw2022_2.Models;

import java.io.Serializable;
import java.util.List;

public class Ristorante implements Serializable {

    private Long codice_ristorante;
    private String nome;
    private int coperti;
    private String locazione;
    private Admin proprietario;
    private List<Cameriere> dipendenti;

    public List<Menu> getMenu() {
        return menu;
    }

    public void setMenu(List<Menu> menu) {
        this.menu = menu;
    }

    private List<Menu> menu;

    public Ristorante() {
    }

    public List<Cameriere> getCamerieri() {
        return dipendenti;
    }

    public void setCamerieri(List<Cameriere> camerieri) {
        this.dipendenti = camerieri;
    }

    public Admin getProprietario() {
        return proprietario;
    }

    public void setProprietario(Admin proprietario) {
        this.proprietario = proprietario;
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


    public Long getCodice_ristorante() {
        return codice_ristorante;
    }

    public void setCodice_ristorante(Long codice_ristorante) {
        this.codice_ristorante = codice_ristorante;
    }


}