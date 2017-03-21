package com.example.claudiochicodev.easyrestaurant;

/**
 * Created by claud on 11/03/2017.
 */

public class OrderRow {

    int _id = 0;    // 0 means not asigned by SQLite autoincrement.
    int tableId;
    int menuIndex = 0;
    String menuName =  "";
    String itemName = "";
    String extra = "";
    float price = 0.f;

    int count = 0;

    //Constructors
    public OrderRow (){
    }

    public OrderRow(int tableId, int menuIndex, String menuName, String itemName, float price) {
        this.tableId = tableId;
        this.menuIndex = menuIndex;
        this.menuName = menuName;
        this.itemName = itemName;
        this.price = price;
    }

    public OrderRow(int tableId, int menuIndex, String menuName, String itemName, String extra, float price) {
        this.tableId = tableId;
        this.menuIndex = menuIndex;
        this.menuName = menuName;
        this.itemName = itemName;
        this.extra = extra;
        this.price = price;
    }

    //Methods
    public int get_id() {
        return _id;
    }

    public int getTableId() {
        return tableId;
    }

    public int getMenuIndex() {
        return menuIndex;
    }

    public String getMenuName() {
        return menuName;
    }

    public String getItemName() {
        return itemName;
    }

    public String getExtra() {
        return extra;
    }

    public float getPrice() {
        return price;
    }

    public int getCount() {
        return count;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public void setTableId(int tableId) {
        this.tableId = tableId;
    }

    public void setMenuIndex(int menuIndex) {
        this.menuIndex = menuIndex;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public void setCount(int count) {
        this.count = count;
    }

}