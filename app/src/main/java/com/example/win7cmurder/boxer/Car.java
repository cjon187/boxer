package com.example.win7cmurder.boxer;

/**
 * Created by win7Cmurder on 7/26/2015.
 */
public class Car {

    private int drawableId;
    private String model;
    private String color;
    private double price;

    public Car(int drawableId, String model, String color, double price) {
        super();
        this.drawableId = drawableId;
        this.model = model;
        this.color = color;
        this.price = price;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getModel() {
        return model;
    }

    public String getColor() {
        return color;
    }

    public double getPrice() {
        return price;
    }

    public int getDrawableId() {
        return drawableId;
    }

    public void setDrawableId(int drawableId) {
        this.drawableId = drawableId;
    }

}