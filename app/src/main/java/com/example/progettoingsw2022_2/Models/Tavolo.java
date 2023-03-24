package com.example.progettoingsw2022_2.Models;

import java.io.Serializable;

public class Tavolo implements Serializable {
    private String orderName;
    private int tableNumber;
    private int progress;

    public Tavolo(String orderName, int tableNumber, int progress) {
        this.orderName = orderName;
        this.tableNumber = tableNumber;
        this.progress = progress;
    }

    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    public int getTableNumber() {
        return tableNumber;
    }

    public void setTableNumber(int tableNumber) {
        this.tableNumber = tableNumber;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }
}
