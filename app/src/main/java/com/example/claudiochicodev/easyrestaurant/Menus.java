package com.example.claudiochicodev.easyrestaurant;

import java.util.ArrayList;
import java.util.List;

public class Menus {

    private int _id;
    private String name;
    private List<Items> itemList = new ArrayList<Items>();

    //Constructors
    public Menus() {}

    public Menus(String name) {
        this.name = name;
    }

    public Menus(List<Items> itemList) {
        this.itemList = itemList;
    }

    public Menus(String name, ArrayList<Items> itemList) {
        this.name = name;
        this.itemList = itemList;
    }


    //Methods
    public void setItems(List<Items> itemList) { this.itemList = itemList;}

    public void setName(String menu) {
        this.name = menu;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public int get_id() {
        return _id;
    }

    public String getName() {
        return name;
    }

    public List<Items> getItems() {
        return itemList;
    }
}
