package com.example.claudiochicodev.easyrestaurant;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment {

    public static boolean cleared = false;
    private int section;
    private int menuIndex;
    public int num_rows = 1;
    public final int NUM_COLS = 5;  // COUNT, NAME, Button 1, Button 2, Button 3
    TableLayout table;
    Object tabGrid [][];
    MyDBHandler dbHandler;
    private static final String ARG_SECTION_NUMBER = "section_number";
    View rootView;

    public PlaceholderFragment() {
    }

    // Returns a new instance of this fragment for the given section number.
    public static PlaceholderFragment newInstance(int sectionNumber) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_menu, container, false);
        initiate(rootView);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        initiate(rootView);
    }

    private void initiate(View view){
        //All dynamic layout begins here
        section = getArguments().getInt(ARG_SECTION_NUMBER);    //Use this insetead of selectedTab, for reasons.
        menuIndex = section-1;

        TextView menuSection_textView = (TextView) view.findViewById(R.id.menuSection_textView);
        menuSection_textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
        dbHandler = SharedResources.dbHandler;

        SharedResources.menuList = dbHandler.getAllMenus(true);
        if(checkListIndex(SharedResources.menuList, menuIndex)){ //Check that there is a menu for the section.
            num_rows = SharedResources.menuList.get(menuIndex).getItems().size();
            menuSection_textView.setText((CharSequence) SharedResources.menuList.get(menuIndex).getName());
            tabGrid = new Object[num_rows][NUM_COLS];
            table = (TableLayout) view.findViewById(R.id.tab_tableLayout);
            table.removeAllViews();
            populateTable(table);
            SharedResources.gridMap.put(Integer.toString(menuIndex), tabGrid);
        }else {
            menuSection_textView.setText("There is no menu for the section " + section);
        }
    }

    public void populateTable(TableLayout table) {
        for (int row = 0; row < num_rows; row++) {
            TableRow tableRow = new TableRow(getActivity());
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
            table.addView(tableRow);

            for (int col = 0; col < NUM_COLS; col++) {
                TableRow.LayoutParams trp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
                trp.setMargins(pxToDp(5), pxToDp(5), pxToDp(5), pxToDp(5));
                trp.gravity = Gravity.CENTER_VERTICAL;

                if (col == 0) {   //Counter - COL 0
                    TextView textView = new TextView(getActivity());
                    textView.setLayoutParams(trp);
                    textView.setGravity(Gravity.CENTER);
                    //db get item count by table and item name
                    int numItems = dbHandler.getItemCountByTableAndItemName(SharedResources.tableNumber, SharedResources.menuList.get(menuIndex).getItems().get(row).getName());
                    textView.setText("" + numItems);
                    tableRow.addView(textView);
                    tabGrid[row][col] = textView;
                    tableRow.getChildAt(col).setBackgroundResource(R.drawable.circle);
                } else if (col == 1) {   //Name - COL 1
                    TextView textView = new TextView(getActivity());
                    textView.setLayoutParams(trp);
                    textView.setGravity(Gravity.LEFT);
                    textView.setMaxWidth(getResources().getDisplayMetrics().widthPixels - dpTopx(220));
                    textView.setMinWidth(getResources().getDisplayMetrics().widthPixels - dpTopx(220));
                    textView.setText(SharedResources.menuList.get(menuIndex).getItems().get(row).getName());
                    tableRow.addView(textView);
                    textView.setTextAppearance(getActivity(), android.R.style.TextAppearance);
                    tabGrid[row][col] = textView;
                } else {
                    final int FINAL_COL = col;
                    final int FINAL_ROW = row;
                    Button button = new Button(getActivity());
                    int width = pxToDp(50);
                    int height = pxToDp(50);
                    TableRow.LayoutParams trpbutton = new TableRow.LayoutParams(width, height);
                    trpbutton.gravity = Gravity.CENTER_VERTICAL;
                    button.setLayoutParams(trpbutton);
                    switch (col) {
                        case 2:
                            button.setBackgroundResource(R.drawable.plusbutton_edited);
                            break;
                        case 3:
                            button.setBackgroundResource(R.drawable.minusbutton_edited);
                            break;
                        case 4:
                            button.setBackgroundResource(R.drawable.extrabutton_edited);
                            break;
                        default:
                            break;
                    }

                    //Listener
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            gridButtonClicked(FINAL_COL, FINAL_ROW);
                        }
                    });

                    tableRow.addView(button);
                    tabGrid[row][col] = button;
                }
            }
        }
    }

    private void gridButtonClicked(int col, int row){

        if (col == 2){
            addButton(col, row);
        }
        else if (col == 3){
            substractButton(col, row);
        }
        else if (col == 4){
            extraButton(col, row);
        }
    }

    private void addButton(int col, int row){

        //Update in screen
        TextView count = (TextView) tabGrid [row][0];
        int counter = Integer.parseInt(count.getText().toString());
        counter++;
        count.setText(Integer.toString(counter));

        //Update DB
        int num = Integer.parseInt(count.getText().toString());

        TextView iName = (TextView) tabGrid[row][1];
        OrderRow orderRow = new OrderRow(SharedResources.tableNumber, menuIndex, SharedResources.menuList.get(menuIndex).getName(), iName.getText().toString(), SharedResources.menuList.get(menuIndex).getItems().get(row).getPrice());    //table number, menu name, item name, extras, item price.
        dbHandler.addOrder(orderRow);
    }

    private void substractButton(int col, int row){
        TextView count = (TextView) tabGrid [row][0];
        int counter = Integer.parseInt(count.getText().toString());
        if (counter > 0){
            //Update in screen
            counter--;
            count.setText(Integer.toString(counter));

            //Update in DB
            TextView iName = (TextView) tabGrid[row][1];
            OrderRow orderRow = new OrderRow(SharedResources.tableNumber, menuIndex, SharedResources.menuList.get(menuIndex).getName(), iName.getText().toString(), SharedResources.menuList.get(menuIndex).getItems().get(row).getPrice());    //table number, menu name, item name, extras, item price.
            dbHandler.removeOneOrder(orderRow);
        }
    }

    private void extraButton(int col, int row){
        final int ROW = row;
        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        TextView tv = (TextView) tabGrid[row][1];
        alert.setTitle(tv.getText().toString());
        alert.setMessage("Add extra:");

        LinearLayout alertLayout = new LinearLayout(getActivity());
        alertLayout.setOrientation(LinearLayout.VERTICAL);

        final EditText input = new EditText(getActivity());
        input.setHint("Type extras");
        final EditText amount = new EditText(getActivity());
        amount.setHint("Amount");
        amount.setInputType(InputType.TYPE_CLASS_NUMBER);
        amount.setText("1");




        alertLayout.addView(input);
        alertLayout.addView(amount);
        alert.setView(alertLayout);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String extra = input.getText().toString();
                int number = Integer.parseInt(amount.getText().toString());

                Toast.makeText(getActivity(), extra, Toast.LENGTH_SHORT).show();

                //Update in screen
                TextView count = (TextView) tabGrid [ROW][0];
                int counter = Integer.parseInt(count.getText().toString());
                counter += number;
                count.setText(Integer.toString(counter));

                //Update DB
                int num = Integer.parseInt(count.getText().toString());
                TextView iName = (TextView) tabGrid[ROW][1];
                OrderRow orderRow = new OrderRow(SharedResources.tableNumber, menuIndex, SharedResources.menuList.get(menuIndex).getName(), iName.getText().toString(), extra, SharedResources.menuList.get(menuIndex).getItems().get(ROW).getPrice());    //table number, menu name, item name, extras, item price.
                for (int i = 0; i<number; i++){
                    dbHandler.addOrder(orderRow);
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

    public boolean checkListIndex (List l, int index) {
        try {
            l.get(index);
            return true;
        } catch (IndexOutOfBoundsException e) {
            return false;
        }
    }

    public int pxToDp(int px){
        float dp = getResources().getDisplayMetrics().density*px;
        return Math.round(dp);
    }

    public int dpTopx(int dp) {
        DisplayMetrics displayMetrics = getActivity().getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }
}
