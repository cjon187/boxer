package com.example.win7cmurder.boxer;

/**
 * Created by win7Cmurder on 7/26/2015.
 */
public class Car {

    private int drawableId;
    private String sender;
    private String message;
    private String time;
    private String side;

    public Car(int drawableId, String sender, String message, String time, String side) {
        super();
        this.drawableId = drawableId;
        this.sender = sender;
        this.message = message;
        this.time= time;
        this.side = side;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }
    public void setSide(String side) {
        this.side = side;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getSender() {
        return sender;
    }
    public String getSide() {
        return side;
    }

    public String getMessage() {
        return message;
    }

    public String getTime() {
        return time;
    }

    public int getDrawableId() {
        return drawableId;
    }

    public void setDrawableId(int drawableId) {
        this.drawableId = drawableId;
    }

}