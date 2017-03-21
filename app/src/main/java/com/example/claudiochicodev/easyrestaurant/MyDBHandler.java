package com.example.claudiochicodev.easyrestaurant;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

class MyDBHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 505;
    private static final String DATABASE_NAME = "easyRest_01.db";

    private static final String TABLE_TABLES = "tables";
    private static final String TABLES_COLUMN_ID = "_id";
    private static final String TABLES_COLUMN_NUMBER = "number";

    private static final String TABLE_USERS = "users";
    private static final String USERS_COLUMN_ID = "_id";
    private static final String USERS_COLUMN_EMAIL = "email";
    private static final String USERS_COLUMN_PASSWORD = "password";
    private static final String USERS_COLUMN_CREATEDATE = "create_date";

    private static final String TABLE_MENUS = "menus";
    private static final String MENUS_COLUMN_ID = "_id";
    private static final String MENUS_COLUMN_NAME = "name";

    private static final String TABLE_ITEMS = "items";
    private static final String ITEMS_COLUMN_ID = "_id";
    private static final String ITEMS_COLUMN_NAME = "name";
    private static final String ITEMS_COLUMN_PRICE = "price";

    private static final String TABLE_MENU_ITEMS = "menu_items";
    private static final String MENU_ITEMS_COLUMN_ID = "_id";
    private static final String MENU_ITEMS_COLUMN_MENU_ID = "menu_id";
    private static final String MENU_ITEMS_COLUMN_ITEM_ID = "item_id";

    private static final String TABLE_ORDERS = "orders";
    private static final String ORDERS_COLUMN_ID = "_id";
    private static final String ORDERS_COLUMN_TABLEID = "tableId";
    private static final String ORDERS_COLUMN_MENUINDEX = "menuIndex";
    private static final String ORDERS_COLUMN_MENUNAME = "menuName";
    private static final String ORDERS_COLUMN_ITEMNAME = "itemName";
    private static final String ORDERS_COLUMN_EXTRA = "extra";
    private static final String ORDERS_COLUMN_ITEMPRICE = "itemPrice";

    public MyDBHandler(Context context, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String query = "CREATE TABLE " + TABLE_USERS + "(" +
                USERS_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                USERS_COLUMN_EMAIL + " TEXT, " +
                USERS_COLUMN_PASSWORD + " TEXT, " + //Hash
                USERS_COLUMN_CREATEDATE + " TEXT " +
                ");";
        db.execSQL(query);

        query = "CREATE TABLE " + TABLE_TABLES + "(" +
                TABLES_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TABLES_COLUMN_NUMBER + " INTEGER " +
                ");";
        db.execSQL(query);

        query = "CREATE TABLE " + TABLE_MENUS + "(" +
                MENUS_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                MENUS_COLUMN_NAME + " TEXT " +
                ");";
        db.execSQL(query);

        query = "CREATE TABLE " + TABLE_ITEMS + "(" +
                ITEMS_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ITEMS_COLUMN_NAME + " TEXT, " +
                ITEMS_COLUMN_PRICE + " REAL " +
                ");";
        db.execSQL(query);

        query = "CREATE TABLE " + TABLE_MENU_ITEMS + "(" +
                MENU_ITEMS_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                MENU_ITEMS_COLUMN_MENU_ID + " INTEGER, " +
                MENU_ITEMS_COLUMN_ITEM_ID + " INTEGER " +
                ");";
        db.execSQL(query);

        query = "CREATE TABLE " + TABLE_ORDERS + "(" +
                ORDERS_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ORDERS_COLUMN_TABLEID + " INTEGER, " +
                ORDERS_COLUMN_MENUINDEX + " INTEGER, " +
                ORDERS_COLUMN_MENUNAME + " TEXT, " +
                ORDERS_COLUMN_ITEMNAME + " TEXT, " +
                ORDERS_COLUMN_EXTRA + " TEXT, " +
                ORDERS_COLUMN_ITEMPRICE + " REAL " +
                ");";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TABLES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MENUS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEMS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MENU_ITEMS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDERS);
        onCreate(db);
    }

    //INSERTS
    public void  addTables(int numberTables){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TABLES_COLUMN_NUMBER, numberTables);
        db.insert(TABLE_TABLES, null, values);
        db.close();
    }

    public void  addUser(String email, String passwordHash){
        String sql = "INSERT INTO " + TABLE_USERS + "  (" +
                USERS_COLUMN_EMAIL + ", " + USERS_COLUMN_PASSWORD + ")" +
                "VALUES (?, ?)";

        SQLiteDatabase db = getWritableDatabase();
        SQLiteStatement statement = db.compileStatement(sql);
        statement.bindString(1, "" + email);
        statement.bindString(2, "" + passwordHash);
        long rowId = statement.executeInsert();
        db.close();
    }

    public void  addOrder(OrderRow o){
        String sql = "INSERT INTO " + TABLE_ORDERS + "  (" +
                ORDERS_COLUMN_TABLEID +", " + ORDERS_COLUMN_MENUINDEX + ", " + ORDERS_COLUMN_MENUNAME + ", " +
                ORDERS_COLUMN_ITEMNAME+ ", " + ORDERS_COLUMN_EXTRA + ", " +
                ORDERS_COLUMN_ITEMPRICE+")" +
                "VALUES (?, ?, ?, ?, ?, ?)";
        SQLiteDatabase db = getWritableDatabase();
        SQLiteStatement statement = db.compileStatement(sql);
        statement.bindLong(1, o.getTableId());
        statement.bindLong(2, o.getMenuIndex());
        statement.bindString(3, o.getMenuName());
        statement.bindString(4, o.getItemName());
        statement.bindString(5, o.getExtra());
        statement.bindDouble(6, o.getPrice());
        long rowId = statement.executeInsert();
        db.close();
    }

    public void addMenu(Menus menu){
        ContentValues values = new ContentValues();
        values.put(MENUS_COLUMN_NAME, menu.getName());
        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_MENUS, null, values);
        db.close();
    }

    public void addItem(Items item){
        ContentValues values = new ContentValues();
        values.put(ITEMS_COLUMN_NAME, item.getName());
        values.put(ITEMS_COLUMN_PRICE, item.getPrice());
        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_ITEMS, null, values);
        db.close();
    }

    public void addMenu_Item(Menus menu, Items item){
        ContentValues values = new ContentValues();
        values.put(MENU_ITEMS_COLUMN_MENU_ID, menu.get_id());
        values.put(MENU_ITEMS_COLUMN_ITEM_ID, getItemIdByName(item.getName()));
        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_MENU_ITEMS, null, values);
        db.close();
    }

    //DELETES
    public void deleteMenuItem(int menuId, int itemId){
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_MENU_ITEMS, MENU_ITEMS_COLUMN_MENU_ID + " = ? AND " + MENU_ITEMS_COLUMN_ITEM_ID + " = ?",new String[]{""+menuId, ""+itemId});
        db.close();
    }



    public void deleteAllTablesRows(){
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_TABLES, TABLES_COLUMN_ID, null);
        db.close();
    }

    public void deleteMenu(String name){
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_MENUS,"name = ?",new String[]{name});
        db.close();
    }

    public void deleteMenuItemsByMenu(String menuId){
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_MENU_ITEMS,MENU_ITEMS_COLUMN_MENU_ID + " = ?",new String[]{menuId});
        db.close();
    }

    public void removeOneOrder(OrderRow ord){
        SQLiteDatabase db = getWritableDatabase();

        String whereClause = ORDERS_COLUMN_ID + " IN ( SELECT " + ORDERS_COLUMN_ID + " FROM " + TABLE_ORDERS + " WHERE " + ORDERS_COLUMN_TABLEID + " = ? AND " +  ORDERS_COLUMN_ITEMNAME + " = ? AND " +
                ORDERS_COLUMN_EXTRA + " = ? AND " + ORDERS_COLUMN_ITEMPRICE + " = ? LIMIT 1)";

        String[] whereArgs = new String[] { "" + ord.getTableId(), "" + ord.getItemName(), "" + ord.getExtra(), "" + ord.getPrice() };
        db.delete(TABLE_ORDERS, whereClause, whereArgs);
        db.close();
    }

    public void removeAllOrderRowsLike(OrderRow ord){
        SQLiteDatabase db = getWritableDatabase();
        String whereClause = ORDERS_COLUMN_ID + " IN ( SELECT " + ORDERS_COLUMN_ID + " FROM " + TABLE_ORDERS + " WHERE " + ORDERS_COLUMN_TABLEID + " = ? AND " +  ORDERS_COLUMN_ITEMNAME + " = ? AND " +
                ORDERS_COLUMN_EXTRA + " = ? AND " + ORDERS_COLUMN_ITEMPRICE + " = ?)";
        String[] whereArgs = new String[] { "" + ord.getTableId(), "" + ord.getItemName(), "" + ord.getExtra(), "" + ord.getPrice() };
        db.delete(TABLE_ORDERS, whereClause, whereArgs);
        db.close();
    }

    public void clearTable(int tableNumber){
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_ORDERS, ORDERS_COLUMN_TABLEID + " = ?",new String[]{"" + tableNumber});
        db.close();
    }

    //SELECTS

    public boolean isUserInDB(String userName, String password) {

        String hashedPassword = SharedResources.hashString(password);

        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE "+ USERS_COLUMN_EMAIL + " LIKE ? AND " + USERS_COLUMN_PASSWORD + " LIKE ? LIMIT 1",
                new String[] { userName, hashedPassword });
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        db.close();
        return exists;
    }

    public boolean isUserInDB(String userName) {
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE "+ USERS_COLUMN_EMAIL + " LIKE ? LIMIT 1",
                new String[] { userName });
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        db.close();
        return exists;
    }

    public int getNumberOfTables(){
        int num = 0;
        String selectQuery = "SELECT * FROM " + TABLE_TABLES;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()){
            num = Integer.parseInt(cursor.getString(cursor.getColumnIndex(TABLES_COLUMN_NUMBER)));
        }
        cursor.close();
        db.close();
        return num;
    }

    public boolean menuExists(String name) {
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_MENUS + " WHERE "+ MENUS_COLUMN_NAME + " LIKE ? LIMIT 1",
                new String[] { name });
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        db.close();
        return exists;
    }

    public ArrayList<ArrayList<OrderRow>> tablesWithOrders(){
        ArrayList<ArrayList<OrderRow>> tablesList = new ArrayList<ArrayList<OrderRow>>();
        for ( int i = 1;  i <= getNumberOfTables(); i++){
            ArrayList<OrderRow> aux = new ArrayList<OrderRow>();
            aux = getTicketByTable(i);
            if (aux.size() > 0){
                tablesList.add(aux);
            }
        }
        return tablesList;
    }

    public int getItemCountByTableAndItemName(int tableId, String itemName){
        int count = 0;
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT (" + ORDERS_COLUMN_ITEMNAME +") AS COUNT FROM " + TABLE_ORDERS + " WHERE " + ORDERS_COLUMN_TABLEID + " = ? AND " + ORDERS_COLUMN_ITEMNAME +
                " LIKE ?", new String[] {"" + tableId, "" + itemName});
        if(cursor.moveToFirst()) {
            count = (Integer.parseInt(cursor.getString(cursor.getColumnIndex("COUNT"))));
        }
        cursor.close();
        db.close();
        return count;
    }

    public ArrayList<OrderRow> getTicketByTable(int table){
        ArrayList<OrderRow> ticket = new ArrayList<OrderRow>();
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT *, COUNT (" + ORDERS_COLUMN_ITEMNAME +") AS COUNT FROM " + TABLE_ORDERS + " WHERE " + ORDERS_COLUMN_TABLEID + " = ? GROUP BY " + ORDERS_COLUMN_ITEMNAME + ", " + ORDERS_COLUMN_EXTRA + " ORDER BY " + ORDERS_COLUMN_MENUINDEX, new String[] {"" + table});

        int indexes[] = new int[5];
        if(cursor.moveToFirst()) {

            int _id = 0;
            String menuName = "";
            String itemName = "";
            String extra = "";
            float itemPrice = 0.f;
            int count = 0;

            do {
                OrderRow orderRow = new OrderRow();
                _id = (Integer.parseInt(cursor.getString(cursor.getColumnIndex(ORDERS_COLUMN_ID))));
                menuName = ((cursor.getString(cursor.getColumnIndex(ORDERS_COLUMN_MENUNAME))));
                itemName = ((cursor.getString(cursor.getColumnIndex(ORDERS_COLUMN_ITEMNAME))));
                extra = ((cursor.getString(cursor.getColumnIndex(ORDERS_COLUMN_EXTRA))));
                itemPrice = (Float.parseFloat(cursor.getString(cursor.getColumnIndex(ORDERS_COLUMN_ITEMPRICE))));
                count = (Integer.parseInt(cursor.getString(cursor.getColumnIndex("COUNT"))));

                orderRow.set_id(_id);
                orderRow.setTableId(table);
                orderRow.setMenuName(menuName);
                orderRow.setItemName(itemName);
                orderRow.setPrice(itemPrice);
                orderRow.setExtra(extra);
                orderRow.setCount(count);
                ticket.add(orderRow);

            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return ticket;
    }

    public String getMenuNameById (int menuId){
        String result = "";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + MENUS_COLUMN_NAME + " FROM " + TABLE_MENUS + " WHERE " + MENUS_COLUMN_ID + " = ?", new String[] {Integer.toString(menuId)});
        if (cursor.moveToFirst()) {
            result = "" + cursor.getString(cursor.getColumnIndex(MENUS_COLUMN_NAME));
        }
        cursor.close();
        db.close();
        return result;
    }

    public String getItemNameById (int itemId){
        String result = "";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + ITEMS_COLUMN_NAME + " FROM " + TABLE_ITEMS + " WHERE " + ITEMS_COLUMN_ID + " = ?", new String[] {Integer.toString(itemId)});
        if (cursor.moveToFirst()) {
            result = "" + cursor.getString(cursor.getColumnIndex(ITEMS_COLUMN_NAME));
        }
        cursor.close();
        db.close();
        return result;
    }

    public String getItemPriceById (int itemId){
        String result = "";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + ITEMS_COLUMN_PRICE + " FROM " + TABLE_ITEMS + " WHERE " + ITEMS_COLUMN_ID + " = ?", new String[] {Integer.toString(itemId)});
        if (cursor.moveToFirst()) {
            result = "" + cursor.getString(cursor.getColumnIndex(ITEMS_COLUMN_PRICE));
        }
        cursor.close();
        db.close();
        return result;
    }

    public float getPriceByItemName (String itemName){
        float result = 0.f;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + ITEMS_COLUMN_PRICE + " FROM " + TABLE_ITEMS + " WHERE " + ITEMS_COLUMN_NAME + " LIKE ? LIMIT 1", new String[] {itemName});
        if (cursor.moveToFirst()) {
            result = cursor.getInt(cursor.getColumnIndex(ITEMS_COLUMN_ID));
        }
        cursor.close();
        db.close();
        return result;
    }

    public int getMenuCount(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(DISTINCT " + MENUS_COLUMN_ID + ") AS number_menus FROM " + TABLE_MENUS, new String[] {});
        cursor.moveToFirst();
        int count= cursor.getInt(0);
        cursor.close();
        db.close();
        return count;
    }

    public int getItemIdByName(String name){
        int result = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + ITEMS_COLUMN_ID + " FROM " + TABLE_ITEMS + " WHERE " + ITEMS_COLUMN_NAME + " LIKE ? LIMIT 1", new String[] {name});
        if (cursor.moveToFirst()) {
            result = cursor.getInt(cursor.getColumnIndex(ITEMS_COLUMN_ID));
        }
        cursor.close();
        db.close();
        return result;
    }

    public int getMenuIdByMenuName(String menuName){
        int result = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + MENUS_COLUMN_ID + " FROM " + TABLE_MENUS + " WHERE " + MENUS_COLUMN_NAME + " LIKE ? LIMIT 1", new String[] {menuName});
        if (cursor.moveToFirst()) {
            result = cursor.getInt(cursor.getColumnIndex(ITEMS_COLUMN_ID));
        }
        cursor.close();
        db.close();
        return result;
    }

    public List<Items> getItemListByMenuId(int mId) {
        List<Items> itemList = new ArrayList<Items>();
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_ITEMS + " WHERE " + ITEMS_COLUMN_ID +" IN(" +
                "SELECT " + MENU_ITEMS_COLUMN_ITEM_ID + " FROM " + TABLE_MENU_ITEMS + " WHERE " + MENU_ITEMS_COLUMN_MENU_ID + " = ?)", new String[] {Integer.toString(mId)});
        if(cursor.moveToFirst()) {
            do {
                Items item = new Items();
                item.set_id(Integer.parseInt(cursor.getString(cursor.getColumnIndex("_id"))));
                item.setName(cursor.getString(cursor.getColumnIndex("name")));
                item.setPrice(Float.parseFloat(cursor.getString(cursor.getColumnIndex("price"))));
                itemList.add(item);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return itemList;
    }

    public List<Menus> getAllMenus(boolean getItemList) {
        List<Menus> menuList = new ArrayList<Menus>();
        String selectQuery = "SELECT * FROM " + TABLE_MENUS;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Menus menu = new Menus();
                menu.set_id(Integer.parseInt(cursor.getString(cursor.getColumnIndex(MENUS_COLUMN_ID))));
                menu.setName(cursor.getString(cursor.getColumnIndex(MENUS_COLUMN_NAME)));

                if(getItemList){
                    List<Items> itemList;
                    itemList = getItemListByMenuId(menu.get_id());
                    menu.setItems(itemList);
                }
                // Adding menu to list
                menuList.add(menu);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return menuList;
    }

    public List<Items> getItemsByMenu(Menus menu) { //Right now just getting the ID
        List<Items> itemList = new ArrayList<Items>();
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_MENU_ITEMS + " WHERE name = ?", new String[] {Integer.toString(menu.get_id())});

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Items item = new Items();
                item.set_id(Integer.parseInt(cursor.getString(cursor.getColumnIndex(ITEMS_COLUMN_ID))));
                item.setName(cursor.getString(cursor.getColumnIndex(ITEMS_COLUMN_NAME)));
                item.set_id(Integer.parseInt(cursor.getString(cursor.getColumnIndex(ITEMS_COLUMN_PRICE))));
                // Adding menu to list
                itemList.add(item);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return itemList;
    }

    public String databaseToString(){
        String dbString = "";
        String query;
        SQLiteDatabase db = getWritableDatabase();
        Cursor c;

//        dbString += "-- TABLE MENUS -- \n";
//
//        query = "SELECT * FROM " + TABLE_MENUS + " WHERE 1";
//        //Cursor points to a location in your results
//        c = db.rawQuery(query, null);
//        //Move to the first row in your results
//        c.moveToFirst();
//        //Position after the last row means the end of the results
//        while (!c.isAfterLast()) {
//            if (c.getString(c.getColumnIndex(MENUS_COLUMN_ID)) != null) {
//                dbString += cursorToString(c);
//            }
//            c.moveToNext();
//        }

        dbString += "\n\n -- TABLE USERS -- \n";

        query = "SELECT * FROM " + TABLE_USERS + " WHERE 1";
        //Cursor points to a location in your results
        c = db.rawQuery(query, null);
        //Move to the first row in your results
        c.moveToFirst();
        //Position after the last row means the end of the results
        while (!c.isAfterLast()) {
            if (c.getString(c.getColumnIndex(USERS_COLUMN_ID)) != null) {
                dbString += cursorToString(c);
            }
            c.moveToNext();
        }

//        dbString += "\n\n -- TABLE ITEMS -- \n";
//
//        query = "SELECT * FROM " + TABLE_ITEMS + " WHERE 1";
//        //Cursor points to a location in your results
//        c = db.rawQuery(query, null);
//        //Move to the first row in your results
//        c.moveToFirst();
//        //Position after the last row means the end of the results
//        while (!c.isAfterLast()) {
//            if (c.getString(c.getColumnIndex(ITEMS_COLUMN_ID)) != null) {
//                dbString += cursorToString(c);
//            }
//            c.moveToNext();
//        }
//
//        dbString += "\n\n -- TABLE MENU_ITEMS -- \n";
//
//        query = "SELECT * FROM " + TABLE_MENU_ITEMS + " WHERE 1";
//        //Cursor points to a location in your results
//        c = db.rawQuery(query, null);
//        //Move to the first row in your results
//        c.moveToFirst();
//        //Position after the last row means the end of the results
//        while (!c.isAfterLast()) {
//            if (c.getString(c.getColumnIndex(MENU_ITEMS_COLUMN_ID)) != null) {
//                dbString += cursorToString(c);
//            }
//            c.moveToNext();
//        }
//
//        dbString += "\n\n -- TABLE ACTIVE_ORDERS -- \n";
//
//        query = "SELECT * FROM " + TABLE_ORDERS + " WHERE 1";
//        //Cursor points to a location in your results
//        c = db.rawQuery(query, null);
//        //Move to the first row in your results
//        c.moveToFirst();
//        //Position after the last row means the end of the results
//        while (!c.isAfterLast()) {
//            if (c.getString(c.getColumnIndex(ORDERS_COLUMN_ID)) != null) {
//                dbString += cursorToString(c);
//            }
//            c.moveToNext();
//        }
//        c.close();
//        db.close();
        return dbString;
    }

    public String cursorToString (Cursor c){
        String result = "";
        if (c.moveToFirst()) {
            do {
                for(int i=0; i<c.getColumnCount();i++)
                {
                    result += c.getString(i);
                    result += " - ";
                }
                result += "\n";
            } while (c.moveToNext());
        }
        result = result.substring(0, result.length()-1);
        return result;
    }

}