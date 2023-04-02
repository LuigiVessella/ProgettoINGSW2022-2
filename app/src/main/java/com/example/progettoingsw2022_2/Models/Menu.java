package com.example.progettoingsw2022_2.Models;

import java.util.List;

public class Menu {
    private Long id_menu;

    private Ristorante ristorante;

    //Menu ha una lista di piatti
    private List<Piatto> portate;

    public Menu() {
    }

    public Long getId_menu() {
        return id_menu;
    }

    public void setId_menu(Long id_menu) {
        this.id_menu = id_menu;
    }

    public Ristorante getRistorante() {
        return ristorante;
    }

    public void setRistorante(Ristorante ristorante) {
        this.ristorante = ristorante;
    }

    public List<Piatto> getPortate() {
        return portate;
    }

    public void setPortate(List<Piatto> portate) {
        this.portate = portate;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getLingua() {
        return lingua;
    }

    public void setLingua(String lingua) {
        this.lingua = lingua;
    }

    private String tipo;
    private String lingua;

}
