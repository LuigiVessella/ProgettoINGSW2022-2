package com.example.progettoingsw2022_2.SingletonModels;
import com.example.progettoingsw2022_2.Models.AddettoCucina;

public class AddettoCucinaSingleton {
    private static AddettoCucinaSingleton instance;
    private AddettoCucina addettoCucina;

    private AddettoCucinaSingleton() {}

    public static AddettoCucinaSingleton getInstance() {
        if (instance == null) {
            instance = new AddettoCucinaSingleton();
        }
        return instance;
    }

    public AddettoCucina getAccount() {
        return addettoCucina;
    }

    public void setAccount(AddettoCucina addettoCucina) {
        this.addettoCucina = addettoCucina;
    }
}
