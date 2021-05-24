package com.example.diploma;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
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


public class ScheduleFragment extends Fragment {

    public static ScheduleFragment newInstance() {
        return new ScheduleFragment();
    }

    IAPI iapi;
    Integer appointment_id;
    ListView appointmentsList;
    private List<Tickets> tickets = new ArrayList();
    private List<Tickets> ticketsOn = new ArrayList();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_schedule, container, false);

        iapi = RetrofitClient.getInstance().create(IAPI.class);

        appointmentsList = (ListView) view.findViewById(R.id.schedule_view);

        iapi.getTicketsForDoctor(LoginActivity.user_enterence_id).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                JSONArray jsonarray = null;
                try {
                    tickets.clear();
                    ticketsOn.clear();

                    jsonarray = new JSONArray(response.body());
                    for (int i = 0; i < jsonarray.length(); i++) {
                        JSONObject jsonobject = jsonarray.getJSONObject(i);

                        Tickets ticket = new Tickets(jsonobject.getInt("Id"),
                                0,
                                0,
                                jsonobject.getString("FullName"),
                                jsonobject.getString("Date"),
                                jsonobject.getString("Time"));
                        tickets.add(ticket);
                    }

                    Date now = new Date();
                    SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
                    Date today = new Date();

                    for(int j = 0; j < tickets.size();j++){
                        try {
                            Date todayWithZeroTime = formatter.parse(formatter.format(today));
                            now = todayWithZeroTime;

                            Date date = new SimpleDateFormat("dd.MM.yyyy").parse(tickets.get(j).getDate());

                            if(date.compareTo(now) > 0 || date.compareTo(now) == 0){
                                ticketsOn.add(tickets.get(j));
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

                            assert getFragmentManager() != null;
                            ScheduleDetailsFragment detailsFragment = new ScheduleDetailsFragment();
                            Bundle bundle = new Bundle();
                            bundle.putString("patient_name", selectedState.getDoctorName());
                            bundle.putString("appointment_date", selectedState.getDate());
                            bundle.putString("appointment_time", selectedState.getTime());
                            detailsFragment.setArguments(bundle);

                            FragmentTransaction ft = getFragmentManager().beginTransaction();
                            ft.replace(R.id.fragment_doc_container, detailsFragment).addToBackStack( "tag" );
                            ft.commit();

                        }
                    };
                    appointmentsList.setOnItemClickListener(itemListener);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });



//        tickets.clear();
//
//        for (int i = 0; i < 4; i++) {
//            Tickets ticket = new Tickets(1,1,1, "Овсянников", "21.12.2021","16:30");
//            tickets.add(ticket);
//        }
//
//        appointmentsList = (ListView) view.findViewById(R.id.schedule_view);
//        AppointmentsAdapter appointmentsAdapter = new AppointmentsAdapter(getActivity(), R.layout.list_item, tickets);
//        appointmentsList.setAdapter(appointmentsAdapter);
//        AdapterView.OnItemClickListener itemListener = new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
//                Tickets selectedState = (Tickets) parent.getItemAtPosition(position);
//                appointment_id = Integer.valueOf(selectedState.getId());
//                Toast.makeText(getActivity(), "id: "+appointment_id, Toast.LENGTH_SHORT).show();
//                //View view = new View(getApplicationContext()); попробовтаь как вью выше
//
//
//            }
//        };
//        appointmentsList.setOnItemClickListener(itemListener);

        return view;

    }



}
