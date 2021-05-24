package com.example.diploma;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class JournalNoteFragment extends Fragment {

    IAPI iapi;
    Integer journal_note_id;
    Integer patient_id;
    String journal_note_date;
    String journal_note_issue;
    String journal_note_decision;
    String patient_name;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_journal_note, container, false);

        iapi = RetrofitClient.getInstance().create(IAPI.class);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            patient_id = bundle.getInt("patient_id",1);
            patient_name = bundle.getString("patient_name","default");
            journal_note_id = bundle.getInt("journal_note_id",1);
            journal_note_date = bundle.getString("journal_note_date","default");
            journal_note_issue = bundle.getString("journal_note_issue","default");
            journal_note_decision = bundle.getString("journal_note_decision","default");
        }

        final EditText ed_journal_note_date = (EditText) view.findViewById(R.id.journal_note_date);
        final EditText ed_journal_note_issue = (EditText) view.findViewById(R.id.journal_note_issue);
        final EditText ed_journal_note_decision = (EditText) view.findViewById(R.id.journal_note_decision);

        ed_journal_note_date.setText(journal_note_date);
        ed_journal_note_issue.setText(journal_note_issue);
        ed_journal_note_decision.setText(journal_note_decision);

        Button button = (Button) view.findViewById(R.id.changeJournalNote);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ed_journal_note_date.getText().toString();
                ed_journal_note_issue.getText().toString();
                ed_journal_note_decision.getText().toString();

                try{

                    JournalNotes journalNotes = new JournalNotes(journal_note_id,
                            0,
                            0,
                            ed_journal_note_date.getText().toString(),
                            ed_journal_note_issue.getText().toString(),
                            ed_journal_note_decision.getText().toString());

                    Call<Void> call= iapi.updateJournal(journalNotes);
                    call.enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            Toasty.success(getContext(), "Запись успешно обновлена!", Toast.LENGTH_SHORT, true).show();

                            JournalDetailsFragment journalDetailsFragment = new JournalDetailsFragment();

                            Bundle bundle = new Bundle();
                            bundle.putInt("patient_id", patient_id); //также нужно будет положить айди записи
                            bundle.putString("patient_name", patient_name);

                            journalDetailsFragment.setArguments(bundle);

                            FragmentTransaction ft = getFragmentManager().beginTransaction();
                            ft.replace(R.id.fragment_doc_container, journalDetailsFragment);
                            ft.commit();
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable throwable) {
                            Log.d("tag", throwable.getMessage());
                        }
                    });
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        Button delete = view.findViewById(R.id.deleteJournalNote);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Call<Void> call = iapi.deleteJournal(journal_note_id);
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        Log.d("tag2", response.message()+"");

                        Toasty.success(getContext(), "Запись успешно удалена!", Toast.LENGTH_SHORT, true).show();

                        JournalDetailsFragment journalDetailsFragment = new JournalDetailsFragment();

                        Bundle bundle = new Bundle();
                        bundle.putInt("patient_id", patient_id); //также нужно будет положить айди записи
                        bundle.putString("patient_name", patient_name);

                        journalDetailsFragment.setArguments(bundle);

                        FragmentTransaction ft = getFragmentManager().beginTransaction();
                        ft.replace(R.id.fragment_doc_container, journalDetailsFragment);//.addToBackStack( "tag" );
                        ft.commit();
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable throwable) {
                        Log.d("tag2", throwable.getMessage());
                    }
                });
            }
        });

        return view;
    }

}
