package com.company;

public class Type {
    final int type;
    int price;
    final String name;
    int quantity;

    public Type(int type, int price, String name, int quantity) {
        this.type = type;
        this.price = price;
        this.name = name;
        this.quantity = quantity;
    }

    public Type(int type, String name) {
        this.type = type;
        this.name = name;
        quantity = 0;
        price = 0;
    }

    public int getType() {
        return type;
    }

    public int getPrice() {
        return price;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
