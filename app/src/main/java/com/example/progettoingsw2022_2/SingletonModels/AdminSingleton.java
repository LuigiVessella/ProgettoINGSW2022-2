package com.example.progettoingsw2022_2.SingletonModels;

import com.example.progettoingsw2022_2.Models.Admin;

public class AdminSingleton {

    private static AdminSingleton instance;
    private Admin admin;

    private AdminSingleton() {}

    public static AdminSingleton getInstance() {
        if (instance == null) {
            instance = new AdminSingleton();
        }
        return instance;
    }

    public Admin getAccount() {
        return admin;
    }

    public void setAccount(Admin admin) {
        this.admin = admin;
    }
}
