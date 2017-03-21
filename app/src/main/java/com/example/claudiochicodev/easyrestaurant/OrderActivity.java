package com.example.claudiochicodev.easyrestaurant;

import android.content.Context;
import android.content.DialogInterface;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintJob;
import android.print.PrintManager;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.Activity;
import android.text.InputType;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

public class OrderActivity extends AppCompatActivity {

    double total;
    private MyDBHandler dbHandler;
    ArrayList<OrderRow> ticket;
    TextView total_textView;
    TextView tableNumber;
    private WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ticket = new ArrayList<OrderRow>();
        total_textView = (TextView) findViewById(R.id.OR_total_textView);
        tableNumber = (TextView) findViewById(R.id.OR_tableNumber_TextView);
        tableNumber.setText("Table: " + SharedResources.tableNumber);
        dbHandler = SharedResources.dbHandler;
        populate_ticket();
    }

    public void clearClicked(View view){
        total = 0;
        dbHandler.clearTable(SharedResources.tableNumber);
        total_textView.setText("" + total + " " + SharedResources.coin);
        PlaceholderFragment.cleared = true;
        populate_ticket();
        Iterator it = SharedResources.gridMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            Object tgrid [][] = (Object[][]) pair.getValue();
            it.remove();
            try{
                for (int j = 0; j<tgrid.length; j++) {
                    TextView tv = (TextView) tgrid[j][0];
                    tv.setText("0");
                }
            }catch(Exception e){
                    System.err.println("ouch! - " + e.toString());
            }
        }
    }

    public void sendClicked(View view){
        //Send to server (same app, kitchen) Right now it'll just be printed.
        doWebViewPrint();
    }

    public void populate_ticket(){
        ticket = dbHandler.getTicketByTable(SharedResources.tableNumber);
        for (OrderRow ord : ticket){
            total += ord.getPrice()*ord.getCount();
        }
        total_textView.setText("" + total + " " + SharedResources.coin);
        final ListAdapter ticketAdapter = new CustomTicketAdapter(this, ticket);
        ListView ticketList = (ListView) findViewById(R.id.order_listView);
        ticketList.setAdapter(ticketAdapter);
        ticketList.setOnItemClickListener(
                new AdapterView.OnItemClickListener(){
                    @Override
                    public void onItemClick(AdapterView parent, View view, int position, long id){
                        final View v = view;
                        final OrderRow orderRow = (OrderRow) ticketAdapter.getItem(position);
                        final int oldCount = orderRow.getCount();

                        // Edit (extra, amount, price)
                        // Delete

                        AlertDialog.Builder alert = new AlertDialog.Builder(view.getContext());
                        alert.setTitle("Edit or Delete");
                        alert.setMessage("");

                        TableLayout alertLayout = new TableLayout(view.getContext());
                        alertLayout.setOrientation(LinearLayout.VERTICAL);

                        final EditText extra = new EditText(view.getContext());
                        extra.setHint("Type extras");
                        extra.setText("" + orderRow.getExtra());
                        final EditText price = new EditText(view.getContext());
                        price.setHint("Type item price");
                        price.setInputType(InputType.TYPE_CLASS_NUMBER);
                        price.setText("" + orderRow.getPrice());
                        final EditText amount = new EditText(view.getContext());
                        amount.setHint("Amount");
                        amount.setInputType(InputType.TYPE_CLASS_NUMBER);
                        amount.setText("" + oldCount);

                        TextView extraTv = new TextView(view.getContext());
                        extraTv.setText("Extras:");
                        TextView priceTv = new TextView(view.getContext());
                        priceTv.setText("Price:");
                        TextView amountTv = new TextView(view.getContext());
                        amountTv.setText("Amount:");

                        final float scale = view.getResources().getDisplayMetrics().density;
                        TableRow.LayoutParams tr_params = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
                        tr_params.setMargins(SharedResources.dpTopx(v, 5),SharedResources.dpTopx(v, 5),SharedResources.dpTopx(v, 5),SharedResources.dpTopx(v, 5));
                        TableRow.LayoutParams textView_params = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT,(int) (1 * scale + 0.1f));
                        TableRow.LayoutParams editText_params = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT,(int) (1 * scale + 0.9f));

                        extraTv.setLayoutParams(textView_params);
                        priceTv.setLayoutParams(textView_params);
                        amountTv.setLayoutParams(textView_params);
                        extra.setLayoutParams(editText_params);
                        price.setLayoutParams(editText_params);
                        amount.setLayoutParams(editText_params);

                        TableRow tr1 = new TableRow(view.getContext());
                        tr1.setLayoutParams(tr_params);
                        tr1.addView(extraTv);
                        tr1.addView(extra);
                        TableRow tr2 = new TableRow(view.getContext());
                        tr2.setLayoutParams(tr_params);
                        tr2.addView(priceTv);
                        tr2.addView(price);
                        TableRow tr3 = new TableRow(view.getContext());
                        tr3.setLayoutParams(tr_params);
                        tr3.addView(amountTv);
                        tr3.addView(amount);

                        alertLayout.addView(tr1);
                        alertLayout.addView(tr2);
                        alertLayout.addView(tr3);
                        alert.setView(alertLayout);

                        alert.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                String extraStr = extra.getText().toString();
                                float priceFloat = Float.parseFloat(price.getText().toString());
                                int amountInt = Integer.parseInt(amount.getText().toString());

                                //Update DB
                                dbHandler.removeAllOrderRowsLike(orderRow);
                                orderRow.setCount(amountInt);
                                orderRow.setExtra(extraStr);
                                for (int i=0; i<amountInt; i++){
                                    dbHandler.addOrder(orderRow);
                                }

                                //Update Screen
                                populate_ticket();
                            }
                        });

                        alert.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                Toast.makeText(OrderActivity.this, "Deleted!", Toast.LENGTH_SHORT).show();

                                //Update DB
                                dbHandler.removeAllOrderRowsLike(orderRow);

                                //Update Screen
                                populate_ticket();
                            }
                        });

                        alert.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                // Canceled.
                            }
                        });

                        alert.show();







                    }
                }
        );
    }

    private void doWebViewPrint() {
        // Create a WebView object specifically for printing
        WebView webView = new WebView(this);
        webView.setWebViewClient(new WebViewClient() {

            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                createWebPrintJob(view);
                mWebView = null;
            }
        });

        // Generate an HTML document:
        String htmlDoc = "<html>" +
                            "<body>" +
                                "<table border = \"1\">" +
                                    "<tr><th>" +
                                        "<h3> Table: "  + SharedResources.tableNumber + "</h3>" +
                                    "</th></tr>" +
                                    "<tr><th>"
                                        + DateFormat.getDateTimeInstance().format(new Date()) + "" +
                                    "</th></tr>" +
                                    "<tr><th>" +
                                        "<p align=\"left\">";

        String currMenu = "";
        ticket = dbHandler.getTicketByTable(SharedResources.tableNumber);
        for (OrderRow ord : ticket){
            total += ord.getPrice()*ord.getCount();
            if (!currMenu.equals(ord.getMenuName())){
                currMenu = ord.getMenuName();
                htmlDoc += "<br/> --- " + currMenu + " --- <br/><br/>";
            }
            htmlDoc += " x" + ord.getCount() + " - " + ord.getItemName() + " (" + ord.getExtra() +")<br>" ;
        }
        htmlDoc +=                      "</p>" +
                                    "</th></tr>" +
                                "</table>" +
                            "</body>" +
                        "</html>";

        webView.loadDataWithBaseURL(null, htmlDoc, "text/HTML", "UTF-8", null);

        // Keep a reference to WebView object until you pass the PrintDocumentAdapter
        // to the PrintManager
        mWebView = webView;
    }

    private void createWebPrintJob(WebView webView) {

        // Get a PrintManager instance
        PrintManager printManager = (PrintManager) this.getSystemService(Context.PRINT_SERVICE);

        // Get a print adapter instance
        PrintDocumentAdapter printAdapter = webView.createPrintDocumentAdapter();

        // Create a print job with name and adapter instance
        String jobName = getString(R.string.app_name) + " Document";
        PrintJob printJob = printManager.print(jobName, printAdapter,
                new PrintAttributes.Builder().build());
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_order, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
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
        if (id == R.id.tb_menu) {
            Intent intent = new Intent(this, MenuActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}