package com.example.progettoingsw2022_2.SingletonModels;

import com.example.progettoingsw2022_2.Models.Cameriere;
import com.example.progettoingsw2022_2.Models.Supervisore;

public class SupervisoreSingleton {

    private static SupervisoreSingleton instance;
    private Supervisore supervisore;

    private SupervisoreSingleton() {}

    public static SupervisoreSingleton getInstance() {
        if (instance == null) {
            instance = new SupervisoreSingleton();
        }
        return instance;
    }

    public Supervisore getAccount() {
        return supervisore;
    }

    public void setAccount(Supervisore supervisore) {
        this.supervisore = supervisore;
    }
}
