package com.example.claudiochicodev.easyrestaurant;

public class Items {

    private int _id;
    private String name = "";
    private float price = 0;

    //Constructors

    public Items() {
    }

    public Items(String name, float price) {
        this.name = name;
        this.price = price;
    }

    //Methods
    public void set_id(int _id) {
        this._id = _id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int get_id() {
        return _id;
    }

    public String getName() {
        return name;
    }

    public float getPrice() {
        return price;
    }
}
