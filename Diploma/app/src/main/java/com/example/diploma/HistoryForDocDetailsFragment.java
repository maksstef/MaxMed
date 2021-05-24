package com.example.diploma;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HistoryForDocDetailsFragment extends Fragment {

    IAPI iapi;
    int patient_id;
    String patient_name;
    int selectedNote;
    ListView journalDetailsList;
    private List<HistoryForDocNotes> journalNotes = new ArrayList();
    private List<HistoryForDocNotes> journalNotesForPatient = new ArrayList();

    public static HistoryForDocDetailsFragment newInstance() {
        return new HistoryForDocDetailsFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_history_for_doc_details, container, false);

        iapi = RetrofitClient.getInstance().create(IAPI.class);

        journalDetailsList = (ListView) v.findViewById(R.id.doc_history_details_view);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            patient_id = bundle.getInt("dc_patient_id", 1);
            patient_name = bundle.getString("dc_patient_name","default_patient");
        }

        TextView patient_name_text_view = v.findViewById(R.id.patient_name_in_doc_history);
        patient_name_text_view.setText(patient_name);

        for (int i = 0; i < journalNotes.size(); i++){
            if(journalNotes.get(i).getPatientId() == patient_id ){
                journalNotesForPatient.add(journalNotes.get(i));
            }
        }

        iapi.getTicketsForHistory(patient_id).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                JSONArray jsonarray = null;
                try {
                    journalNotes.clear();
                    journalNotesForPatient.clear();

                    jsonarray = new JSONArray(response.body());
                    for (int i = 0; i < jsonarray.length(); i++) {
                        JSONObject jsonobject = jsonarray.getJSONObject(i);

                        HistoryForDocNotes ticket = new HistoryForDocNotes(jsonobject.getInt("Id"),
                                jsonobject.getInt("PatientId"),
                                jsonobject.getString("Date"),
                                jsonobject.getString("DoctorName"),
                                jsonobject.getString("Department"));
                        journalNotes.add(ticket);
                    }

                    Date now = new Date();
//                    try{
//                        Date nowDate = new SimpleDateFormat("dd.MM.yyyy").parse(now.toString());
//                    }catch (ParseException e){
//
//                    }

                    SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
                    Date today = new Date();


                    for(int j = 0; j < journalNotes.size();j++){
                        try {

                            Date todayWithZeroTime = formatter.parse(formatter.format(today));
                            now = todayWithZeroTime;
                            Date date = new SimpleDateFormat("dd.MM.yyyy").parse(journalNotes.get(j).getDate());
                            if(!(date.compareTo(now) > 0 || date.compareTo(now) == 0)){
                                journalNotesForPatient.add(journalNotes.get(j));
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }

                    HistoryForDocNotesAdapter appointmentsAdapter = new HistoryForDocNotesAdapter(getActivity(), R.layout.doc_history_note, journalNotesForPatient);
                    journalDetailsList.setAdapter(appointmentsAdapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });

//        for (int i = 0; i < 6; i++) {
//            HistoryForDocNotes ticket = new HistoryForDocNotes(i+1,i+1,"11.02.2021","Евтушенко Александр", "педиатрия");
//            journalNotes.add(ticket);
//            HistoryForDocNotes ticket2 = new HistoryForDocNotes(i+10,i+1,"18.04.2021","Крассовский Аркадий", "кардиология");
//            journalNotes.add(ticket2);
//        }
//
//        HistoryForDocNotes ticket2 = new HistoryForDocNotes(666,1,"22.04.2021","Агутин Леонид", "урология");
//        journalNotes.add(ticket2);
//
//        HistoryForDocNotes ticket3 = new HistoryForDocNotes(667,1,"27.04.2021","Лемберг Анастасия", "инфекционка");
//        journalNotes.add(ticket3);
//
//        HistoryForDocNotes ticket4 = new HistoryForDocNotes(668,1,"03.05.2021","Крассовский Аркадий", "кардиология");
//        journalNotes.add(ticket4);
//
//        HistoryForDocNotes ticket5 = new HistoryForDocNotes(669,1,"17.05.2021","Дольберг Геннадий", "приемное отжеление");
//        journalNotes.add(ticket5);
//
//        HistoryForDocNotes ticket6 = new HistoryForDocNotes(670,1,"23.05.2021","Вишневска Анна", "травматология");
//        journalNotes.add(ticket6);
//
//        HistoryForDocNotes ticket7 = new HistoryForDocNotes(671,1,"26.05.2021","Зеленина Екатерина", "терапевтическое отделение");
//        journalNotes.add(ticket7);
//
//
//
//        journalDetailsList = (ListView) v.findViewById(R.id.doc_history_details_view);
//        HistoryForDocNotesAdapter appointmentsAdapter = new HistoryForDocNotesAdapter(getActivity(), R.layout.doc_history_note, journalNotesForPatient);
//        journalDetailsList.setAdapter(appointmentsAdapter);



//        AdapterView.OnItemClickListener itemListener = new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
//                JournalNotes selectedState = (JournalNotes) parent.getItemAtPosition(position);
//                selectedNote = Integer.valueOf(selectedState.getPatientId());
//                //Toast.makeText(getActivity(), "patient id: "+selectedNote, Toast.LENGTH_SHORT).show();
//                //View view = new View(getApplicationContext()); попробовтаь как вью выше
//
//                assert getFragmentManager() != null;
//                JournalNoteFragment journalNoteFragment = new JournalNoteFragment();
//                Bundle bundle = new Bundle();
//                bundle.putInt("patient_id", patient_id); //также нужно будет положить айди записи
//                bundle.putString("journal_note_date", selectedState.getDate());
//                bundle.putString("journal_note_issue", selectedState.getIssue());
//                bundle.putString("journal_note_decision", selectedState.getDecision());
//
//                journalNoteFragment.setArguments(bundle);
//
//                FragmentTransaction ft = getFragmentManager().beginTransaction();
//                ft.replace(R.id.fragment_doc_container, journalNoteFragment).addToBackStack( "tag" );
//                ft.commit();
//            }
//        };
//        journalDetailsList.setOnItemClickListener(itemListener);
//
//        Button button = (Button) v.findViewById(R.id.createJournalNote);
//        button.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View v)
//            {
//                JournalNoteFragment journalNoteFragment = new JournalNoteFragment();
//                FragmentTransaction ft = getFragmentManager().beginTransaction();
//                ft.replace(R.id.fragment_doc_container, journalNoteFragment);
//                ft.commit();
//            }
//        });

        return v;
    }

}
