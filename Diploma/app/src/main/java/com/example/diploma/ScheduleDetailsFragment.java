package com.example.diploma;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

public class ScheduleDetailsFragment extends Fragment {

//    Integer appointment_id;
//    Integer doctor_id;
    String patient_name;
    String appointment_date;
    String appointment_time;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_schedule_details, container, false);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
//            appointment_id = bundle.getInt("appointment_id", 1);
//            doctor_id = bundle.getInt("doctor_id", 1);
            patient_name = bundle.getString("patient_name","default_patient");
            appointment_date = bundle.getString("appointment_date","default_patient");
            appointment_time = bundle.getString("appointment_time","default_patient");
        }

        EditText patient_name_text = (EditText)v.findViewById(R.id.sd_patient_name);
        patient_name_text.setText(patient_name);
        EditText appointment_date_text = (EditText)v.findViewById(R.id.sd_appointment_date);
        appointment_date_text.setText(appointment_date);
        EditText appointment_time_text = (EditText)v.findViewById(R.id.sd_appointment_time);
        appointment_time_text.setText(appointment_time);

        return v;
    }


}
