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

public class ReceiptsFragment extends Fragment {

    Integer appointment_id;
    IAPI iapi;
    ListView receipsList;
    private List<Receipt> receipts = new ArrayList();
    private List<Receipt> receiptsWithRightDates = new ArrayList();

    public static ReceiptsFragment newInstance() {
        return new ReceiptsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_receipts, container, false);

        receipsList = (ListView) view.findViewById(R.id.receipts);
        iapi = RetrofitClient.getInstance().create(IAPI.class);

        //tickets.clear();

        iapi.getReceipts(LoginActivity.user_enterence_id).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                JSONArray jsonarray = null;
                try {
                    receipts.clear();
                    jsonarray = new JSONArray(response.body());
                    for (int i = 0; i < jsonarray.length(); i++) {
                        JSONObject jsonobject = jsonarray.getJSONObject(i);

                        Receipt receipt = new Receipt(jsonobject.getInt("Id"),
                                jsonobject.getInt("PatientId"),
                                jsonobject.getString("DrugName"),
                                jsonobject.getString("Date"));

                        receipts.add(receipt);
                    }

                    Date now = new Date();
                    SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
                    Date today = new Date();

                    for(int j = 0; j < receipts.size();j++){
                        try {
                            Date todayWithZeroTime = formatter.parse(formatter.format(today));
                            now = todayWithZeroTime;

                            Date date = new SimpleDateFormat("dd.MM.yyyy").parse(receipts.get(j).Date);

                            if(date.compareTo(now) > 0 || date.compareTo(now) == 0){
                                receiptsWithRightDates.add(receipts.get(j));
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }

                    ReceiptsAdapter receiptsAdapter = new ReceiptsAdapter(getActivity(), R.layout.list_item, receiptsWithRightDates);
                    receipsList.setAdapter(receiptsAdapter);

//                    adapter1 = new DoctorListAdapter(getActivity(), R.layout.row, doctorsList);
//                    lvDoctors.setAdapter(adapter1);

//                    AdapterView.OnItemClickListener itemListener = new AdapterView.OnItemClickListener() {
//                        @Override
//                        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
//                            User selectedState = (User) parent.getItemAtPosition(position);
//                            user_id = (Integer)selectedState.UserId;
//                            doctor_id = (Integer)selectedState.Doctors.get(0).DoctorId;
//
//                            assert getFragmentManager() != null;
//                            ChangeDoctorDetailsFragment detailsFragment = new ChangeDoctorDetailsFragment();
//                            Bundle bundle = new Bundle();
//                            bundle.putInt("a_change_user_id", user_id);
//                            bundle.putInt("a_change_doctor_id", doctor_id);
//                            bundle.putString("a_change_doctor_name", selectedState.FullName);
//                            bundle.putString("a_change_doctor_login", selectedState.Login);
//                            bundle.putString("a_change_doctor_building", selectedState.Doctors.get(0).Building);
//                            bundle.putString("a_change_doctor_department", selectedState.Doctors.get(0).Department);
//                            detailsFragment.setArguments(bundle);
//
//                            FragmentTransaction ft = getFragmentManager().beginTransaction();
//                            ft.replace(R.id.fragment_admin_container, detailsFragment).addToBackStack( "tag2" );
//                            ft.commit();
//
//                        }
//                    };
//                    lvDoctors.setOnItemClickListener(itemListener);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                //Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

//        for (int i = 0; i < 5; i++) {
//            Tickets ticket = new Tickets(1,1,1, "Ибупрофен 30мг", "21.12.2021","16:30");
//            tickets.add(ticket);
//        }

//        appointmentsList = (ListView) view.findViewById(R.id.receipts);
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
