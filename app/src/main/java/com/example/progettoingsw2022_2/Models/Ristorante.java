package com.example.progettoingsw2022_2.Models;

import java.util.List;

public class Ristorante {

    private Long codice_ristorante;
    private String nome;
    private int coperti;
    private String locazione;
    private Admin proprietario;
    private List<Cameriere> camerieri;

    private List<Avviso> avvisi;

    private AddettoCucina addettoCucina;
    private Supervisore supervisore;
    private Menu menu;

    public Ristorante() {}


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

    public Supervisore getSupervisore() {
        return supervisore;
    }

    public void setSupervisore(Supervisore supervisore) {
        this.supervisore = supervisore;
    }

    public Menu getMenu() {
        return menu;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    public AddettoCucina getAddettoCucina() {
        return addettoCucina;
    }

    public void setAddettoCucina(AddettoCucina addettoCucina) {
        this.addettoCucina = addettoCucina;
    }

    public List<Avviso> getAvvisi() {
        return avvisi;
    }

    public void setAvvisi(List<Avviso> avvisi) {
        this.avvisi = avvisi;
    }

}