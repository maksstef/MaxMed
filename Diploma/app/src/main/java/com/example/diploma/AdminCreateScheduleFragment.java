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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import dmax.dialog.SpotsDialog;
import es.dmoral.toasty.Toasty;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AdminCreateScheduleFragment extends Fragment {

    private ListView lvDoctors;

    IAPI iapi;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    private ArrayList<User> doctorsList = new ArrayList();

    public static AdminCreateScheduleFragment newInstance() {
        return new AdminCreateScheduleFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_admin_create_schedule, container, false);

        final EditText date = (EditText)v.findViewById(R.id.cs_schedule_date);

        final String [] values = {};
        final ArrayList<String> values2 = new ArrayList();
        final Map<String, Integer> valuesIds = new HashMap<String, Integer>();
        final Spinner spinner = (Spinner) v.findViewById(R.id.cs_doctors_Spinner);
        iapi = RetrofitClient.getInstance().create(IAPI.class);

        iapi.getDoctors().enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                JSONArray jsonarray = null;
                try {
                    doctorsList.clear();
                    jsonarray = new JSONArray(response.body());

                    for (int i = 0; i < jsonarray.length(); i++) {
                        JSONObject jsonobject = jsonarray.getJSONObject(i);
                        values2.add(jsonobject.getString("FullName"));
                        valuesIds.put(jsonobject.getString("FullName"), jsonobject.getInt("DoctorId"));
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, values2);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner.setAdapter(adapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                //Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


//        for(int i = 0; i < doctorsList.size(); i++){
//            values[i] = doctorsList.get(i).FullName;
//        }
//
        //String [] values = {"Лермонтова Изольда","Весенин Константин","Лесовский Петр","Долницын Василий","Шмидт Ингрид","Андропов Дмитрий"};
//        Spinner spinner = (Spinner) v.findViewById(R.id.cs_doctors_Spinner);
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(v.getContext(), android.R.layout.simple_spinner_item, values2);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinner.setAdapter(adapter);


        String [] values3 = {"10:00","10:30","11:00","11:30","12:00","12:30","14:00","14:30","15:00","15:30","16:00","16:30","17:00","17:30","18:00"};
        final Spinner spinner3 = (Spinner) v.findViewById(R.id.cs_times_Spinner);
        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(v.getContext(), android.R.layout.simple_spinner_item, values3);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner3.setAdapter(adapter3);

        //получать из сервера нужно не только список фамилий но и айди доктора сразу
        //перед созданием делать гет на сервер с этими данными чтобы чекнуть есь ли такое уже расписание
        //возможно у врача или админа выводить все расписание доктора
        //дату сделать выплывающим календарем

        Button button = (Button) v.findViewById(R.id.createSchedule);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = spinner.getSelectedItem().toString();
                int doctorId = valuesIds.get(name);


//                for(int i = 0; i < doctorsList.size(); i++ ){
//                    if(doctorsList.get(i).FullName == name){
//                        doctorId = doctorsList.get(i).Doctors.get(0).DoctorId;
//                    }
//                }

                String time = spinner3.getSelectedItem().toString();

                if(!date.getText().toString().trim().matches("^[0-9]{1,2}(.)[0-9]{1,2}(.)[0-9]{4}$")){
                    //Toast.makeText(this, "Проблемы с email", Toast.LENGTH_SHORT).show();
                    Toasty.error(getContext(), "Проблемы с датой!", Toast.LENGTH_SHORT, true).show();
                }
                else{
                    try{

                        final AlertDialog dialog = new SpotsDialog.Builder()
                                .setContext(getContext())
                                .build();
                        dialog.show();

                        int id = UniqueID();

                        Tickets ticket = new Tickets(id, doctorId, 1, name, date.getText().toString(), time);

                        compositeDisposable.add(iapi.createSchedule(ticket)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Consumer<String>() {
                                    @Override
                                    public void accept(String s) throws Exception {
                                        Toasty.info(getContext(), s.replace("\"",""), Toast.LENGTH_SHORT, true).show();
                                        dialog.dismiss();
                                    }
                                }, new Consumer<Throwable>() {
                                    @Override
                                    public void accept(Throwable throwable) throws Exception {
                                        Toasty.error(getContext(), throwable.getMessage(), Toasty.LENGTH_SHORT);
                                        dialog.dismiss();
                                        //Toast.makeText(RegisterPage.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }));

                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                }

            }
        });

        return v;
    }

    public int UniqueID(){
        Date now = new Date();
        int id = Integer.parseInt(new SimpleDateFormat("MMddHHmmss",  Locale.US).format(now));
        return id;
    }

}
