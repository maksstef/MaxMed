package com.example.diploma;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;

public class JournalNotesAdapter extends ArrayAdapter<JournalNotes> {
    private LayoutInflater inflater;
    private int layout;
    private List<JournalNotes> tickets;

    public JournalNotesAdapter(Context context, int resource, List<JournalNotes> tickets){
        super(context, resource, tickets );
        this.tickets = tickets;
        this.layout = resource;
        this.inflater = LayoutInflater.from(context);
    }

    public JournalNotesAdapter(@NonNull Context context, int resource) {
        super(context, resource);
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        View view=inflater.inflate(this.layout, parent, false);

        TextView nameView = (TextView) view.findViewById(R.id.appointment_doctor);

        JournalNotes journalNote = tickets.get(position);

        nameView.setText(journalNote.getDate());
        return view;
    }
}
