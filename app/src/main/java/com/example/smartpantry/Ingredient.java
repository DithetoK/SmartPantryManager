package com.example.smartpantry;

public class Ingredient {
    public int id;
    public String name;
    public double quantity;
    public String unit;
    public String expiry;

    public Ingredient(int id, String name, double quantity, String unit, String expiry){
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.unit = unit;
        this.expiry = expiry;
    }

    public String quantityText() {
        String number;
        if(quantity == (long) quantity){
            number = String.valueOf((long) quantity);
        } else{
            number = String.valueOf(quantity);
        }
        return number + " " + unit;
    }
}
