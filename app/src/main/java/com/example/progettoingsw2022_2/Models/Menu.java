package com.example.progettoingsw2022_2.Models;

public class Menu {

    private Long id_menu;
    private String nome_piatto;
    private String descrizione;
    private String allergeni;
    private String contiene;
    private String prezzo;
    private Ristorante ristorante;



    private String tipo; //primo, secondo, dessert, ecc
    private String tipoPietanza; //pesce, carne, ecc
    //quindi primo di pesce, secondo di carne, ecc

    public Menu() {
    }

    public Long getId_menu() {
        return id_menu;
    }

    public void setId_menu(Long id_menu) {
        this.id_menu = id_menu;
    }

    public String getNome_piatto() {
        return nome_piatto;
    }

    public void setNome_piatto(String nome_piatto) {
        this.nome_piatto = nome_piatto;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public String getAllergeni() {
        return allergeni;
    }

    public void setAllergeni(String allergeni) {
        this.allergeni = allergeni;
    }

    public String getContiene() {
        return contiene;
    }

    public void setContiene(String contiene) {
        this.contiene = contiene;
    }

    public String getPrezzo() {
        return prezzo;
    }

    public void setPrezzo(String prezzo) {
        this.prezzo = prezzo;
    }

    public Ristorante getRistorante() {
        return ristorante;
    }

    public void setRistorante(Ristorante ristorante) {
        this.ristorante = ristorante;
    }


    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getTipoPietanza() {
        return tipoPietanza;
    }

    public void setTipoPietanza(String tipoPietanza) {
        this.tipoPietanza = tipoPietanza;
    }
}
