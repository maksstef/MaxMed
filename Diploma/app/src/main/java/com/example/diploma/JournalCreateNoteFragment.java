package com.example.diploma;

import android.app.AlertDialog;
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
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import dmax.dialog.SpotsDialog;
import es.dmoral.toasty.Toasty;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class JournalCreateNoteFragment extends Fragment {

    CompositeDisposable compositeDisposable = new CompositeDisposable();
    IAPI iapi;
    String patient_name;
    String journal_note_issue;
    String journal_note_decision;
    Integer patient_id;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_journal_create_note, container, false);

        iapi = RetrofitClient.getInstance().create(IAPI.class);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            patient_name = bundle.getString("patient_name","default");
            patient_id = bundle.getInt("patient_id",1);
        }

        TextView textView = (TextView)view.findViewById(R.id.jcn_patient_name);
        textView.setText("Пациент: "+patient_name);

        final EditText ed_journal_note_date = (EditText) view.findViewById(R.id.jcn_journal_note_date);
        final EditText ed_journal_note_issue = (EditText) view.findViewById(R.id.jcn_journal_note_issue);
        final EditText ed_journal_note_decision = (EditText) view.findViewById(R.id.jcn_journal_note_decision);


        Button button = (Button) view.findViewById(R.id.jcn_createNote);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                try{

//                    final AlertDialog dialog = new SpotsDialog.Builder()
//                            .setContext(getContext())
//                            .build();
//                    dialog.show();

                    JournalNotes journal = new JournalNotes(UniqueID(),
                            LoginActivity.user_enterence_id,
                            patient_id,
                            ed_journal_note_date.getText().toString(),
                            ed_journal_note_issue.getText().toString(),
                            ed_journal_note_decision.getText().toString());

                    compositeDisposable.add(iapi.createJournal(journal)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<String>() {
                            @Override
                            public void accept(String s) throws Exception {

                                //Toasty.success(getContext(), "Запись успешно создана!", Toast.LENGTH_SHORT, true).show();

                                Toasty.info(getContext(), s.replace("\"",""), Toast.LENGTH_SHORT, true).show();
                                //dialog.dismiss();

                                JournalDetailsFragment journalDetailsFragment = new JournalDetailsFragment();

                                Bundle bundle = new Bundle();
                                bundle.putInt("patient_id", patient_id); //также нужно будет положить айди записи
                                bundle.putString("patient_name", patient_name);

                                journalDetailsFragment.setArguments(bundle);

                                FragmentTransaction ft = getFragmentManager().beginTransaction();
                                ft.replace(R.id.fragment_doc_container, journalDetailsFragment).addToBackStack( "tag" );
                                ft.commit();
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                Toasty.error(getContext(), throwable.getMessage(), Toasty.LENGTH_SHORT);
                                //dialog.dismiss();
                                //Toast.makeText(RegisterPage.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }));
                }
                catch (Exception e){
                    e.printStackTrace();
                }




            }
        });


        return view;
    }

    public int UniqueID(){
        Date now = new Date();
        int id = Integer.parseInt(new SimpleDateFormat("MMddHHmmss",  Locale.US).format(now));
        return id;
    }

}
