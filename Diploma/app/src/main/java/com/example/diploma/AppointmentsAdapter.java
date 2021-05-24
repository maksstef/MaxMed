package com.example.diploma;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;

public class AppointmentsAdapter extends ArrayAdapter<Tickets>{

    private LayoutInflater inflater;
    private int layout;
    private List<Tickets> tickets;

    public AppointmentsAdapter(Context context, int resource, List<Tickets> tickets){
        super(context, resource, tickets );
        this.tickets = tickets;
        this.layout = resource;
        this.inflater = LayoutInflater.from(context);
    }

    public AppointmentsAdapter(@NonNull Context context, int resource) {
        super(context, resource);
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        View view=inflater.inflate(this.layout, parent, false);

        TextView nameView = (TextView) view.findViewById(R.id.appointment_date);
        TextView nameView2 = (TextView) view.findViewById(R.id.appointment_doctor);

        Tickets ticket = tickets.get(position);

        nameView.setText(ticket.getDoctorName());
        nameView2.setText(ticket.getDate());
        return view;
    }
}
