package com.example.claudiochicodev.easyrestaurant;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

import java.util.ArrayList;

public class SelectTableActivity extends AppCompatActivity {

    public TableLayout table;
    public int numTables;
    private int currNumTables = numTables;
    public int num_rows = 0;
    public static int NUM_COLS = 4;
    Button tabGrid [];
    public MyDBHandler dbHandler;
    public ArrayList<ArrayList<OrderRow>> tablesWithOrders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        SharedResources sh = new SharedResources(this); //Initiated here since this is currently the launching app and just in case.

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_table);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tablesWithOrders = new ArrayList<>();
        dbHandler = SharedResources.dbHandler;
        numTables = dbHandler.getNumberOfTables();
        if (numTables == 0){
            dbHandler.addTables(40);
            numTables = dbHandler.getNumberOfTables();
            Toast.makeText(this, "" + numTables, Toast.LENGTH_SHORT).show();
        }
        num_rows = (int) Math.ceil((double)numTables/NUM_COLS);
        tabGrid = new Button[numTables+1];
        table = (TableLayout) findViewById(R.id.selectTable_tableLayout);
        populateTable(table);
        colorUsedTables();
    }

    @Override
    protected void onResume() {
        super.onResume();
        numTables = dbHandler.getNumberOfTables();
        if (numTables > 0){
            if (currNumTables != numTables){
                currNumTables = numTables;
                table.removeAllViews();
                populateTable(table);
            }
            colorUsedTables();
        }
    }

    public void colorUsedTables(){
        tablesWithOrders.clear();
        tablesWithOrders = dbHandler.tablesWithOrders();
        for (ArrayList<OrderRow> arr : tablesWithOrders){
            if (arr.get(0).getTableId() > numTables){
                break;
            }
            Button b = (Button) tabGrid[arr.get(0).getTableId()];
            b.setBackgroundResource(R.drawable.table2);
        }
    }

    @Override
    public void onBackPressed() {
    //Do nothing.
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_table, menu);
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
        return super.onOptionsItemSelected(item);
    }

    public void populateTable(TableLayout table){

        for (int row = 0; row < num_rows; row++) {
            TableRow tableRow = new TableRow(this);
            tableRow.setLayoutParams(new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.WRAP_CONTENT,
                    1.0f));
            tableRow.setGravity(Gravity.CENTER_HORIZONTAL);
            table.addView(tableRow);

            for (int col = 0; col < NUM_COLS; col++) {
                final int FINAL_COL = col;
                final int FINAL_ROW = row;
                final int buttonNum = FINAL_ROW*NUM_COLS + FINAL_COL + 1;
                if (buttonNum > numTables){
                    break;
                }
                Button button = new Button(this);
                int width = (getResources().getDisplayMetrics().widthPixels/NUM_COLS - dpTopx(30));
                int height = width;
                TableRow.LayoutParams trpbutton = new TableRow.LayoutParams(width, height);
                trpbutton.gravity = Gravity.CENTER_VERTICAL;
                trpbutton.setMargins(dpTopx(12),dpTopx(12),dpTopx(12),dpTopx(12));
                button.setLayoutParams(trpbutton);
                button.setPadding(5,5,5,5);
                button.setText("" + buttonNum);
                button.setTextSize(20);
                button.setBackgroundResource(R.drawable.table1);

                //Listener
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        gridButtonClicked(FINAL_COL, FINAL_ROW, buttonNum);
                    }
                });

                tableRow.addView(button);
                tabGrid[buttonNum] = button;
            }
        }
    }

    private void gridButtonClicked(int col, int row, int num){
        SharedResources.tableNumber = num;
        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent);
    }

    public int pxToDp(int px){
        float dp = getResources().getDisplayMetrics().density*px;
        return Math.round(dp);
    }

    public int dpTopx(int dp) {
        DisplayMetrics displayMetrics = this.getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }


}


