package com.example.diploma;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class HistoryForDocNotesAdapter extends ArrayAdapter<HistoryForDocNotes> {

    private LayoutInflater inflater;
    private int layout;
    private List<HistoryForDocNotes> tickets;

    public HistoryForDocNotesAdapter(Context context, int resource, List<HistoryForDocNotes> tickets){
        super(context, resource, tickets );
        this.tickets = tickets;
        this.layout = resource;
        this.inflater = LayoutInflater.from(context);
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        View view=inflater.inflate(this.layout, parent, false);

        TextView nameView = (TextView) view.findViewById(R.id.doc_history_note_date);
        TextView nameView2 = (TextView) view.findViewById(R.id.doc_history_note_name);
        TextView nameView3 = (TextView) view.findViewById(R.id.doc_history_note_department);

        HistoryForDocNotes journalNote = tickets.get(position);

        nameView.setText("Дата: "+journalNote.getDate());
        nameView2.setText("Врач: "+journalNote.getDocName());
        nameView3.setText("Отделение: "+journalNote.getDepartment());
        return view;
    }

}
