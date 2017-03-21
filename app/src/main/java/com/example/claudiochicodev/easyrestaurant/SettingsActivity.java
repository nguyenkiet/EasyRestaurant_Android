package com.example.claudiochicodev.easyrestaurant;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static android.R.id.input;

public class SettingsActivity extends AppCompatActivity {

    EditText numTables;
    EditText itemName;
    EditText itemPrice;
    MyDBHandler dbHandler;
    Spinner menu_spinner;
    int currentSelection;
    TableLayout tableLayout;
    List<Items> itemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        SharedResources.menuList = new ArrayList<Menus>();
        dbHandler = SharedResources.dbHandler;
        tableLayout = (TableLayout) findViewById(R.id.set_tableLayout);
        numTables = (EditText) findViewById(R.id.set_numberTables_editText);
        itemName = (EditText) findViewById(R.id.set_entryName_EditText);
        itemPrice = (EditText) findViewById(R.id.set_price_editText);
        menu_spinner = (Spinner) findViewById(R.id.set_spinner);
        currentSelection = menu_spinner.getSelectedItemPosition();

        menu_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (currentSelection != i){
                    // Your code here
                    populateItems(tableLayout);
                }
                currentSelection = i;
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }
        });

        if (!SharedResources.menuList.isEmpty())
            SharedResources.menuList.clear();
        SharedResources.menuList = dbHandler.getAllMenus(true);
        populateSpinner(SharedResources.menuList);
    }

    public void populateSpinner(List<Menus> menusList){
        List<String> list = new ArrayList<String>();
        for (Menus m : menusList){
            list.add(m.getName());
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        menu_spinner.setAdapter(dataAdapter);
    }

    public void deleteMenuClicked(View view){

        if (menu_spinner.getSelectedItem() !=null){

        dbHandler.deleteMenu(menu_spinner.getSelectedItem().toString());
        int menuId = dbHandler.getMenuIdByMenuName(menu_spinner.getSelectedItem().toString());
        dbHandler.deleteMenuItemsByMenu("" + menuId);
        if (!SharedResources.menuList.isEmpty())   //Useless... but pretty.
            SharedResources.menuList.clear();
            SharedResources.menuList = dbHandler.getAllMenus(true);
            SectionsPagerAdapter.doNotifyDataSetChangedOnce = true;
            populateSpinner(SharedResources.menuList);
            populateItems(tableLayout);
        } else{
            Toast.makeText(this, "You haven't selected a menu to delete.", Toast.LENGTH_SHORT).show();
        }
    }

    public void setTablesClicked(View view){
        String numberTables = numTables.getText().toString();
        if (numberTables.matches("")){
            Toast.makeText(this, "You need to enter a number to set the amount of tables.", Toast.LENGTH_SHORT).show();
        }else {
            dbHandler.deleteAllTablesRows();
            dbHandler.addTables(Integer.parseInt(numberTables));
            Toast.makeText(this, "The number has been tables updated.", Toast.LENGTH_SHORT).show();
        }
    }

    public void addToMenuClicked(View view){
        if(TextUtils.isEmpty(itemName.getText().toString())){
            Toast.makeText(this, "Item must have a name ", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(itemPrice.getText().toString())){
            Toast.makeText(this, "Item must have a price ", Toast.LENGTH_SHORT).show();
        } else {
            //Insert ITEMS needed before MENU_ITEMS
            Items item = new Items(itemName.getText().toString(), Float.valueOf(itemPrice.getText().toString()));
            dbHandler.addItem(item);
            //Insert MENU_ITEMS
            Menus m = SharedResources.menuList.get(menu_spinner.getSelectedItemPosition());
            dbHandler.addMenu_Item(m, item);
            Toast.makeText(this, "Added", Toast.LENGTH_SHORT).show();
            itemName.setText("");
            itemPrice.setText("");
            //Refresh
            if (!SharedResources.menuList.isEmpty())
                SharedResources.menuList.clear();
            SharedResources.menuList = dbHandler.getAllMenus(true);
            populateItems(tableLayout);
        }
    }

    public void createNewMenuClicked(View view){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Create new menu");
        final EditText input_name = new EditText(this);
        input_name.setHint("Menu name");
        alert.setView(input_name);
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                if (!dbHandler.menuExists(input_name.getText().toString())){
                    String menuName = input_name.getText().toString();
                    Menus menu = new Menus("" + menuName);
                    dbHandler.addMenu(menu);
                    SharedResources.menuList = new ArrayList<Menus>(); //Just in case.
                    if (!SharedResources.menuList.isEmpty())
                        SharedResources.menuList.clear();
                    SharedResources.menuList = dbHandler.getAllMenus(true);
                    SectionsPagerAdapter.doNotifyDataSetChangedOnce = true;
                    populateSpinner(SharedResources.menuList);
                } else {
                    Toast.makeText(SettingsActivity.this, "There is a menu with that name already. \nPlease choose a diferent one.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }
        });
        alert.show();
    }

    public int pxToDp(int px){
        float dp = getResources().getDisplayMetrics().density*px;
        return Math.round(dp);
    }

    public int dpTopx(int dp) {
        DisplayMetrics displayMetrics = this.getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    public void populateItems(TableLayout tableLayout){
        tableLayout.removeAllViews();
        final int menuId = dbHandler.getMenuIdByMenuName(menu_spinner.getSelectedItem().toString());
        itemList = dbHandler.getItemListByMenuId(menuId);
        TableRow.LayoutParams trp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
        trp.setMargins(pxToDp(5), pxToDp(5), pxToDp(5), pxToDp(5));
        trp.gravity = Gravity.CENTER_VERTICAL;
        int row = 0;
        for (final Items item : itemList){
            //Add row
            TableRow tableRow = new TableRow(this);
            tableRow.setLayoutParams(new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.WRAP_CONTENT,
                    1.0f));
            tableRow.setGravity(Gravity.CENTER);

            if ((row % 2) == 0) {
                tableRow.setBackgroundColor(Color.parseColor("#bbc1cc"));
            } else {
                tableRow.setBackgroundColor(Color.parseColor("#eaeff7"));
            }
            row++;
            tableLayout.addView(tableRow);
            //Fill row
            final TextView itemName_textView = new TextView(this);
            itemName_textView.setLayoutParams(trp);
            itemName_textView.setGravity(Gravity.LEFT);
            itemName_textView.setMaxWidth(getResources().getDisplayMetrics().widthPixels/2 - dpTopx(20));
            itemName_textView.setMinWidth(getResources().getDisplayMetrics().widthPixels/2 - dpTopx(20));
            itemName_textView.setText("" + item.getName());
            tableRow.addView(itemName_textView);
            itemName_textView.setTextAppearance(this, android.R.style.TextAppearance);
            TextView itemPrice_textView = new TextView(this);
            itemPrice_textView.setLayoutParams(trp);
            itemPrice_textView.setGravity(Gravity.LEFT);
            itemPrice_textView.setMaxWidth(getResources().getDisplayMetrics().widthPixels/2 - dpTopx(20));
            itemPrice_textView.setMinWidth(getResources().getDisplayMetrics().widthPixels/2 - dpTopx(20));
            itemPrice_textView.setText("" + item.getPrice());
            tableRow.addView(itemPrice_textView);
            itemPrice_textView.setTextAppearance(this, android.R.style.TextAppearance);

            tableRow.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
                    alert.setTitle(itemName_textView.getText().toString());

                    alert.setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            dbHandler.deleteMenuItem(menuId, item.get_id());
                            if (!SharedResources.menuList.isEmpty())
                                SharedResources.menuList.clear();
                            SharedResources.menuList = dbHandler.getAllMenus(true);
                            TableLayout tl = (TableLayout) findViewById(R.id.set_tableLayout);
                            populateItems(tl);
                        }
                    });
                    alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            // Canceled.
                        }
                    });
                    alert.show();
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, SelectTableActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        if (id == R.id.tb_table) {
            Intent intent = new Intent(this, SelectTableActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}