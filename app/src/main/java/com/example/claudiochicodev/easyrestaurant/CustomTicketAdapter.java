package com.example.claudiochicodev.easyrestaurant;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by claud on 12/03/2017.
 */

class CustomTicketAdapter extends ArrayAdapter<OrderRow> {

    public CustomTicketAdapter(Context context, ArrayList<OrderRow> orderRows) {
        super(context, R.layout.order_row_layout, orderRows);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        View customView = layoutInflater.inflate(R.layout.order_row_layout, parent, false);

        String itemName = getItem(position).getItemName();
        int count = getItem(position).getCount();
        float subTotal = getItem(position).getPrice()*count;

        TextView extra = (TextView) customView.findViewById(R.id.OR_extra_textView);
        if (!getItem(position).getExtra().equals("")){
            extra.setText("Extra: " + getItem(position).getExtra());
            TableRow tr = (TableRow) customView.findViewById(R.id.OR_normal_tableRow);
            tr.setBackgroundColor(Color.parseColor("#5691ef"));
        }   else {
            extra.setVisibility(View.GONE);
        }

        TextView itemName_textView = (TextView) customView.findViewById(R.id.OR_ItemName_textView);
        TextView count_textView = (TextView) customView.findViewById(R.id.OR_amount_textView);
        TextView subTotal_textView = (TextView) customView.findViewById(R.id.OR_subTotal_textView);
        itemName_textView.setText(itemName);
        count_textView.setText("" + count);
        subTotal_textView.setText("" + subTotal + " " + SharedResources.coin);

        return customView;
    }

    @Nullable
    @Override
    public OrderRow getItem(int position) {
        return super.getItem(position);
    }
}