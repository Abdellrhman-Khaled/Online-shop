package com.example.project;
public class CartItem {
    String name;
    double price;
    int quantity;

    public CartItem(String name, double price) {
        this.name = name;
        this.price = price;
        this.quantity = 1;
    }


    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        CartItem cartItem = (CartItem) obj;
        return name.equals(cartItem.name); // Compare based on name
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }


}













