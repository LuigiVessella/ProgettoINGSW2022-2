package com.example.progettoingsw2022_2.SingletonModels;

import com.example.progettoingsw2022_2.Models.Admin;
import com.example.progettoingsw2022_2.Models.Cameriere;

public class CameriereSingleton {

    private static CameriereSingleton instance;
    private Cameriere cameriere;

    private CameriereSingleton() {}

    public static CameriereSingleton getInstance() {
        if (instance == null) {
            instance = new CameriereSingleton();
        }
        return instance;
    }

    public Cameriere getAccount() {
        return cameriere;
    }

    public void setAccount(Cameriere cameriere) {
        this.cameriere = cameriere;
    }
}
