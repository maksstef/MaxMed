package com.example.diploma;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class ReceiptsAdapter extends ArrayAdapter<Receipt> {

    private LayoutInflater inflater;
    private int layout;
    private List<Receipt> receipts;

    public ReceiptsAdapter(Context context, int resource, List<Receipt> receipts){
        super(context, resource, receipts );
        this.receipts = receipts;
        this.layout = resource;
        this.inflater = LayoutInflater.from(context);
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        View view=inflater.inflate(this.layout, parent, false);

        TextView nameView = (TextView) view.findViewById(R.id.appointment_date);
        TextView nameView2 = (TextView) view.findViewById(R.id.appointment_doctor);

        Receipt receipt = receipts.get(position);

        nameView.setText(receipt.Date);
        nameView2.setText(receipt.DrugName);
        return view;
    }

}
