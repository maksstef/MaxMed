package com.example.diploma;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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


public class JournalDetailsFragment extends Fragment {

    IAPI iapi;
    int patient_id;
    String patient_name;
    int selectedNote;
    ListView journalDetailsList;
    private List<JournalNotes> journalNotes = new ArrayList();
    private List<JournalNotes> journalNotesForPatient = new ArrayList();

    public static JournalDetailsFragment newInstance() {
        return new JournalDetailsFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_journal_details, container, false);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            patient_id = bundle.getInt("patient_id", 1);
            patient_name = bundle.getString("patient_name","default_patient");
        }

        TextView patient_name_text_view = v.findViewById(R.id.patient_name_in_journal);
        patient_name_text_view.setText(patient_name);

        journalDetailsList = (ListView) v.findViewById(R.id.journal_details_view);

        iapi = RetrofitClient.getInstance().create(IAPI.class);

        iapi.getJournal(patient_id).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                JSONArray jsonarray = null;
                try {
                    journalNotes.clear();

                    jsonarray = new JSONArray(response.body());
                    for (int i = 0; i < jsonarray.length(); i++) {
                        JSONObject jsonobject = jsonarray.getJSONObject(i);

                        JournalNotes journal = new JournalNotes(jsonobject.getInt("Id"),
                                jsonobject.getInt("DoctorId"),
                                jsonobject.getInt("PatientId"),
                                jsonobject.getString("Date"),
                                jsonobject.getString("Issue"),
                                jsonobject.getString("Decision"));
                        journalNotes.add(journal);
                    }

                    JournalNotesAdapter appointmentsAdapter = new JournalNotesAdapter(getActivity(), R.layout.list_item, journalNotes);
                    journalDetailsList.setAdapter(appointmentsAdapter);
                    AdapterView.OnItemClickListener itemListener = new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                            JournalNotes selectedState = (JournalNotes) parent.getItemAtPosition(position);
                            selectedNote = Integer.valueOf(selectedState.getPatientId());

                            assert getFragmentManager() != null;
                            JournalNoteFragment journalNoteFragment = new JournalNoteFragment();
                            Bundle bundle = new Bundle();
                            bundle.putInt("patient_id", patient_id);
                            bundle.putString("patient_name", patient_name);
                            bundle.putInt("journal_note_id", selectedState.getId());//также нужно будет положить айди записи
                            bundle.putString("journal_note_date", selectedState.getDate());
                            bundle.putString("journal_note_issue", selectedState.getIssue());
                            bundle.putString("journal_note_decision", selectedState.getDecision());

                            journalNoteFragment.setArguments(bundle);

                            FragmentTransaction ft = getFragmentManager().beginTransaction();
                            ft.replace(R.id.fragment_doc_container, journalNoteFragment).addToBackStack( "tag" );
                            ft.commit();
                        }
                    };
                    journalDetailsList.setOnItemClickListener(itemListener);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });

//        journalNotes.clear();
//        journalNotesForPatient.clear();
//
//        for (int i = 0; i < 6; i++) {
//            JournalNotes ticket = new JournalNotes(i+1,i+1,i+1,"11.02.2021","головная боль, жалобы на давление", "выписать анальгин 30мл, дать больничный 30 дней");
//            journalNotes.add(ticket);
//            JournalNotes ticket2 = new JournalNotes(i+10,i+1,i+1,"18.04.2021","рядовой поход к врачу, жалобы отсутствуют", "жалоб нет, прописывать ничего не нужно");
//            journalNotes.add(ticket2);
//        }
//
//        JournalNotes ticket2 = new JournalNotes(666,1,1,"22.04.2021","пациент решил проверить свое давление", "давление в норме, прописывать ничего не нужно");
//        journalNotes.add(ticket2);


//        for (int i = 0; i < journalNotes.size(); i++){
//            if(journalNotes.get(i).getPatientId() == patient_id ){
//                journalNotesForPatient.add(journalNotes.get(i));
//            }
//        }



        Button button = (Button) v.findViewById(R.id.createJournalNote);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                JournalCreateNoteFragment journalCreateNoteFragment = new JournalCreateNoteFragment();

                Bundle bundle = new Bundle();
                bundle.putInt("patient_id", patient_id); //также нужно будет положить айди записи
                bundle.putString("patient_name", patient_name);

                journalCreateNoteFragment.setArguments(bundle);

                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.fragment_doc_container, journalCreateNoteFragment);
                ft.commit();
            }
        });

        return v;
    }

}
