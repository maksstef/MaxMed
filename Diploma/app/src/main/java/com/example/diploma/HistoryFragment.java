package com.example.diploma;

import android.app.AlertDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HistoryFragment extends Fragment {

    IAPI iapi;
    Integer appointment_id;
    ListView appointmentsList;
    private List<Tickets> tickets = new ArrayList();
    private List<Tickets> ticketsOn = new ArrayList();

    ListView appointmentsListOff;
    private List<Tickets> ticketsOff = new ArrayList();

    public static HistoryFragment newInstance() {
        return new HistoryFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_history, container, false);
        appointmentsList = (ListView) view.findViewById(R.id.current_appointments);
        appointmentsListOff = (ListView) view.findViewById(R.id.previous_appointments);
        iapi = RetrofitClient.getInstance().create(IAPI.class);

        iapi.getTicketsForUser(LoginActivity.user_enterence_id).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                JSONArray jsonarray = null;
                try {
                    tickets.clear();
                    ticketsOff.clear();
                    ticketsOn.clear();

                    jsonarray = new JSONArray(response.body());
                    for (int i = 0; i < jsonarray.length(); i++) {
                        JSONObject jsonobject = jsonarray.getJSONObject(i);

                        Tickets ticket = new Tickets(jsonobject.getInt("Id"),
                                jsonobject.getInt("DoctorId"),
                                jsonobject.getInt("PatientId"),
                                jsonobject.getString("DoctorName"),
                                jsonobject.getString("Date"),
                                jsonobject.getString("Time"));
                        tickets.add(ticket);
                    }

                    Date now = new Date();
                    //now.setTime(000000);

                    SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
                    Date today = new Date();

                    for(int j = 0; j < tickets.size();j++){
                        try {
                            Date todayWithZeroTime = formatter.parse(formatter.format(today));
                            now = todayWithZeroTime;

                            Date date = new SimpleDateFormat("dd.MM.yyyy").parse(tickets.get(j).getDate());

                            if(date.compareTo(now) > 0 || date.compareTo(now) == 0){
                            //if(date.compareTo(now) > 0){
                                ticketsOn.add(tickets.get(j));
                            }else{
                                ticketsOff.add(tickets.get(j));
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }

                    AppointmentsAdapter appointmentsAdapter = new AppointmentsAdapter(getActivity(), R.layout.list_item, ticketsOn);
                    appointmentsList.setAdapter(appointmentsAdapter);
                    AdapterView.OnItemClickListener itemListener = new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                            Tickets selectedState = (Tickets) parent.getItemAtPosition(position);
                            appointment_id = Integer.valueOf(selectedState.getId());

                            assert getFragmentManager() != null;
                            HistoryDetailsFragment detailsFragment = new HistoryDetailsFragment();
                            Bundle bundle = new Bundle();
                            bundle.putInt("appointment_id", appointment_id);
                            bundle.putString("doctor_name", selectedState.getDoctorName());
                            bundle.putInt("doctor_id", selectedState.getDoctorId());
                            bundle.putString("appointment_date", selectedState.getDate());
                            bundle.putString("appointment_time", selectedState.getTime());
                            detailsFragment.setArguments(bundle);

                            FragmentTransaction ft = getFragmentManager().beginTransaction();
                            ft.replace(R.id.bodyFragment, detailsFragment).addToBackStack( "tag" );
                            ft.commit();

                        }
                    };
                    appointmentsList.setOnItemClickListener(itemListener);


                    AppointmentsAdapter appointmentsAdapter1 = new AppointmentsAdapter(getActivity(), R.layout.list_item, ticketsOff);
                    appointmentsListOff.setAdapter(appointmentsAdapter1);
                    AdapterView.OnItemClickListener itemListener2 = new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                            Tickets selectedState = (Tickets) parent.getItemAtPosition(position);
                            appointment_id = Integer.valueOf(selectedState.getId());

                            assert getFragmentManager() != null;
                            HistoryDetailsFragment detailsFragment = new HistoryDetailsFragment();
                            Bundle bundle = new Bundle();
                            bundle.putInt("appointment_id", appointment_id);
                            bundle.putString("doctor_name", selectedState.getDoctorName());
                            bundle.putInt("doctor_id", selectedState.getDoctorId());
                            bundle.putString("appointment_date", selectedState.getDate());
                            bundle.putString("appointment_time", selectedState.getTime());
                            detailsFragment.setArguments(bundle);

                            FragmentTransaction ft = getFragmentManager().beginTransaction();
                            ft.replace(R.id.bodyFragment, detailsFragment).addToBackStack( "tag" );
                            ft.commit();
                        }
                    };
                    appointmentsListOff.setOnItemClickListener(itemListener2);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });

        return view;

    }

}
