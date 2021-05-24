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
import android.widget.Toast;

import dmax.dialog.SpotsDialog;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HistoryDetailsFragment extends Fragment {

    IAPI iapi;

    Integer appointment_id;
    Integer doctor_id;
    String doctor_name;
    String appointment_date;
    String appointment_time;

    public static HistoryDetailsFragment newInstance() {
        return new HistoryDetailsFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_history_details, container, false);

        iapi = RetrofitClient.getInstance().create(IAPI.class);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            appointment_id = bundle.getInt("appointment_id", 1);
            doctor_id = bundle.getInt("doctor_id", 1);
            doctor_name = bundle.getString("doctor_name","default_patient");
            appointment_date = bundle.getString("appointment_date","default_patient");
            appointment_time = bundle.getString("appointment_time","default_patient");
        }

        EditText doctor_name_text = (EditText)v.findViewById(R.id.hd_doc_name);
        doctor_name_text.setText(doctor_name);
        EditText appointment_date_text = (EditText)v.findViewById(R.id.hd_appointment_date);
        appointment_date_text.setText(appointment_date);
        EditText appointment_time_text = (EditText)v.findViewById(R.id.hd_appointment_time);
        appointment_time_text.setText(appointment_time);


        Button button = (Button) v.findViewById(R.id.unSignFromAppointment);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                try{

                    Tickets ticket = new Tickets(appointment_id, doctor_id, 1, null, null, null);

                    Call<Void> call= iapi.updateTicket(ticket);
                    call.enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            Toasty.success(getContext(), "Вы успешно выписаны!", Toast.LENGTH_SHORT, true).show();


                            HistoryFragment detailsFragment = new HistoryFragment();
                            FragmentTransaction ft = getFragmentManager().beginTransaction();
                            ft.replace(R.id.bodyFragment, detailsFragment);
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


        return v;
    }

}
