package com.example.progettoingsw2022_2.Models;

import java.util.List;

public class Ristorante {

    private Long codice_ristorante;
    private String nome;
    private int coperti;
    private String locazione;
    private Admin proprietario;
    private List<Cameriere> camerieri;
    private List<AddettoCucina> addettiCucina;
    private Supervisore supervisore;

    private List<Menu> menu;

    public Ristorante() {
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

    public List<Cameriere> getCamerieri() {
        return camerieri;
    }

    public void setCamerieri(List<Cameriere> camerieri) {
        this.camerieri = camerieri;
    }


    public List<AddettoCucina> getAddettiCucina() {
        return addettiCucina;
    }

    public void setAddettiCucina(List<AddettoCucina> addettiCucina) {
        this.addettiCucina = addettiCucina;
    }

    public Supervisore getSupervisore() {
        return supervisore;
    }

    public void setSupervisore(Supervisore supervisore) {
        this.supervisore = supervisore;
    }


    public List<Menu> getMenu() {
        return menu;
    }

    public void setMenu(List<Menu> menu) {
        this.menu = menu;
    }


}