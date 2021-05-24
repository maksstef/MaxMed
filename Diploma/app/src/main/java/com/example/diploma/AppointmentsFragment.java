package com.example.diploma;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import dmax.dialog.SpotsDialog;
import es.dmoral.toasty.Toasty;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AppointmentsFragment extends Fragment {

    IAPI iapi;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    private ArrayList<User> doctorsList = new ArrayList();

    public static AppointmentsFragment newInstance() {
        return new AppointmentsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_appointments, container, false);

        iapi = RetrofitClient.getInstance().create(IAPI.class);

        final ArrayList<String> values2 = new ArrayList();
        final ArrayList<String> dates = new ArrayList();
        final ArrayList<String> rightDates = new ArrayList();
        final ArrayList<String> times = new ArrayList();
        final Map<String, Integer> valuesIds = new HashMap<String, Integer>();
        final Map<String, Integer> valuesTicketIds = new HashMap<String, Integer>();
        final Spinner spinner3 = (Spinner) v.findViewById(R.id.doctorSpinner);
        final Spinner spinner4 = (Spinner) v.findViewById(R.id.dateSpinner);
        final Spinner spinner5 = (Spinner) v.findViewById(R.id.timeSpinner);

        String [] buildings = {"Маяковского, 73","Голодеда, 30","Проспет Независимости, 12","Кабушкина, 45","Рокоссовского, 109","Огинского, 94","Лелина, 5","Солнечная, 45",};
        final Spinner spinner = (Spinner) v.findViewById(R.id.buildingSpinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(v.getContext(), android.R.layout.simple_spinner_item, buildings);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        String [] departmens = {"Общая практика","Аллергология","Стоматология","Терапевтия","Неврология","Урология","Оториноларингология","Офтальмология","Хирургия",};
        final Spinner spinner2 = (Spinner) v.findViewById(R.id.departmentSpinner);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(v.getContext(), android.R.layout.simple_spinner_item, departmens);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter2);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String building = spinner.getSelectedItem().toString();
                String department = spinner2.getSelectedItem().toString();

                values2.clear();
                valuesIds.clear();


                Doctor doctor = new Doctor(0,
                        0,
                        building,
                        department
                );

                try {

                    compositeDisposable.add(iapi.getDoctorsForTicket(doctor)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Consumer<String>() {
                                @Override
                                public void accept(String s) throws Exception {
                                    JSONArray jsonarray = null;
                                    try {
                                        doctorsList.clear();
                                        jsonarray = new JSONArray(s);

                                        for (int i = 0; i < jsonarray.length(); i++) {
                                            JSONObject jsonobject = jsonarray.getJSONObject(i);
                                            values2.add(jsonobject.getString("FullName"));
                                            valuesIds.put(jsonobject.getString("FullName"), jsonobject.getInt("DoctorId"));
                                        }

                                        Set<String> ss = new LinkedHashSet<>(values2);
                                        values2.clear();
                                        values2.addAll(ss);

                                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, values2);
                                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                        spinner3.setAdapter(adapter);

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, new Consumer<Throwable>() {
                                @Override
                                public void accept(Throwable throwable) throws Exception {

                                }
                            }));
                }catch(Exception e){

                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        //get doctors by building and department
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String building = spinner.getSelectedItem().toString();
                String department = spinner2.getSelectedItem().toString();

                values2.clear();
                valuesIds.clear();

//                final AlertDialog dialog = new SpotsDialog.Builder()
//                        .setContext(getContext())
//                        .build();
//                dialog.show();


                Doctor doctor = new Doctor(0,
                        0,
                        building,
                        department
                );

                try {

                    compositeDisposable.add(iapi.getDoctorsForTicket(doctor)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Consumer<String>() {
                                @Override
                                public void accept(String s) throws Exception {
                                    JSONArray jsonarray = null;
                                    try {
                                        doctorsList.clear();
                                        jsonarray = new JSONArray(s);

                                        for (int i = 0; i < jsonarray.length(); i++) {
                                            JSONObject jsonobject = jsonarray.getJSONObject(i);
                                            values2.add(jsonobject.getString("FullName"));
                                            valuesIds.put(jsonobject.getString("FullName"), jsonobject.getInt("DoctorId"));
                                        }

                                        //еще такая же вставка в спинере 1
                                        //нужна для чистки дубликатов имен врачей
                                        //дубликаты появляются потому что при клике
                                        //и на учреждение и на отделение делается гет
                                        //очистка списков не помогла
                                        Set<String> ss = new LinkedHashSet<>(values2);
                                        values2.clear();
                                        values2.addAll(ss);

                                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, values2);
                                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                        spinner3.setAdapter(adapter);

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                    //  dialog.dismiss();
                                }
                            }, new Consumer<Throwable>() {
                                @Override
                                public void accept(Throwable throwable) throws Exception {
                                    //  dialog.dismiss();
                                }
                            }));
                }catch(Exception e){

                }



            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //get date by doctorId
        spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String name = spinner3.getSelectedItem().toString();
                int doctorId = valuesIds.get(name);

//                final AlertDialog dialog = new SpotsDialog.Builder()
//                        .setContext(getContext())
//                        .build();
//                dialog.show();

                iapi.getTickets(doctorId).enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        JSONArray jsonarray = null;
                        try {
                            dates.clear();
                            jsonarray = new JSONArray(response.body());
                            for (int i = 0; i < jsonarray.length(); i++) {
                                JSONObject jsonobject = jsonarray.getJSONObject(i);
                                dates.add(jsonobject.getString("Date"));
                            }

                            Set<String> s = new LinkedHashSet<>(dates);
                            dates.clear();
                            dates.addAll(s);
                            rightDates.clear();

                            Date now = new Date();
                            for(int j = 0; j < dates.size();j++){
                                try {
                                    Date date = new SimpleDateFormat("dd.MM.yyyy").parse(dates.get(j));

                                    if(date.compareTo(now) > 0 || date.compareTo(now) == 0){
                                        rightDates.add(dates.get(j));
                                    }
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }

                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, rightDates); //dates
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinner4.setAdapter(adapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        //Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

                //dialog.dismiss();
                //сделать обычный гет с айди как рецепты по айди например
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //get time by doctorId and date
        spinner4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String name = spinner3.getSelectedItem().toString();
                int doctorId = valuesIds.get(name);

                String date = spinner4.getSelectedItem().toString();

//                final AlertDialog dialog = new SpotsDialog.Builder()
//                        .setContext(getContext())
//                        .build();
//                dialog.show();

                Tickets ticket = new Tickets(0,doctorId,0,null,date,null);

                try {
                    compositeDisposable.add(iapi.getTimesForTicket(ticket)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<String>() {
                            @Override
                            public void accept(String s) throws Exception {
                                JSONArray jsonarray = null;
                                try {
                                    times.clear();
                                    jsonarray = new JSONArray(s);

                                    for (int i = 0; i < jsonarray.length(); i++) {
                                        JSONObject jsonobject = jsonarray.getJSONObject(i);
                                        times.add(jsonobject.getString("Time"));
                                        valuesTicketIds.put(jsonobject.getString("Time"), jsonobject.getInt("Id"));
                                    }

                                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, times);
                                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    spinner5.setAdapter(adapter);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                //dialog.dismiss();
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                             //   dialog.dismiss();
                            }
                        }));
                }catch(Exception e){

                }

                //сделать такой же пост как приполучении докторов,
                //только используя айди и дату получить доступные времена

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //селектить только те тикеты где значение пациент айди пустое,
        //нужно делать и при селекте дат и при селекте времени

        //обработать клик кнопки записаться
        //сделать пут в тикет контроллер и обновить айди пациента,
        //для этого нужно в предыдущих шагах вернуть айди запииси
        //например считать времена и айди записей, и затем как по имени врача получали айди
        //таким же образом по времени получить айди
        //постараться очистить все поля (мб как при смене отделения, то есть отправить гет на сервер с пустым отделением)
        //и дать месагу что вы успешно записаны

        //затем на первом экране у пациента делать два гета на получение записей куда записан пациент
        //один варик для будущих приемов, один для прошлых, соритровать на сервере в удобном порядке для просмотра
        //одни от большего к меньшему, другие наооборот

        Button button = (Button) v.findViewById(R.id.signUpInAppointment);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String name;
                int doctorId;
                String date;
                String time;
                int ticketId;

                try {
                    name = spinner3.getSelectedItem().toString();
                    doctorId = valuesIds.get(name);
                }
                catch (Exception e){
                    name = "";
                    doctorId = 0;
                }

                try{
                    date = spinner4.getSelectedItem().toString();
                    time = spinner5.getSelectedItem().toString();
                    ticketId = valuesTicketIds.get(time);
                }
                catch (Exception e){
                    date = "";
                    time = "";
                    ticketId = 0;
                }


                if(name.equals("")){
                    Toasty.error(getContext(), "Выберите врача!", Toast.LENGTH_SHORT, true).show();
                }else if(date.equals("")){
                    Toasty.error(getContext(), "Выберите дату!", Toast.LENGTH_SHORT, true).show();
                }
                else if(time.equals("")){
                    Toasty.error(getContext(), "Выберите время!", Toast.LENGTH_SHORT, true).show();
                }
                else{
                    try{

                        final AlertDialog dialog = new SpotsDialog.Builder()
                                .setContext(getContext())
                                .build();
                        dialog.show();


                        Tickets ticket = new Tickets(ticketId, doctorId, LoginActivity.user_enterence_id, name, date, time);

                        Call<Void> call= iapi.updateTicket(ticket);
                        call.enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                Toasty.success(getContext(), "Вы успешно записаны!", Toast.LENGTH_SHORT, true).show();

                                dialog.dismiss();

                                AppointmentsFragment detailsFragment = new AppointmentsFragment();
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


            }
        });


        return v;
    }



}
