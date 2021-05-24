package com.example.diploma;

import android.app.AlertDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.security.MessageDigest;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import dmax.dialog.SpotsDialog;
import es.dmoral.toasty.Toasty;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangeDoctorDetailsFragment extends Fragment {

    IAPI iapi;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    Integer doctor_id;
    Integer user_id;
    String doctor_name;
    String doctor_login;
    String doctor_building;
    String doctor_department;

    Integer current_building;
    Integer current_department;

    public static ChangeDoctorDetailsFragment newInstance() {
        return new ChangeDoctorDetailsFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_change_doctor_details, container, false);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            user_id = bundle.getInt("a_change_user_id", 1);
            doctor_id = bundle.getInt("a_change_doctor_id", 1);
            doctor_name = bundle.getString("a_change_doctor_name","default_doctor");
            doctor_login = bundle.getString("a_change_doctor_login","default_doctor");
            doctor_building = bundle.getString("a_change_doctor_building","default_doctor");
            doctor_department = bundle.getString("a_change_doctor_department","default_doctor");
        }

        String [] buildings = {"Маяковского, 73","Голодеда, 30","Проспет Независимости, 12","Кабушкина, 45","Рокоссовского, 109","Огинского, 94","Лелина, 5","Солнечная, 45",};
        final Spinner spinner = (Spinner) view.findViewById(R.id.cdd_buildings_Spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_spinner_item, buildings);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        for (int i = 0; i < buildings.length; i++) {
            if(buildings[i].equals(doctor_building)){
                //spinner.setSelection(i+1);
                current_building = i;
            }
        }
        spinner.setSelection(current_building);

        //spinner.setSelection(position);

        String [] departments = {"Общая практика","Аллергология","Стоматология","Терапевтия","Неврология","Урология","Оториноларингология","Офтальмология","Хирургия",};
        final Spinner spinner2 = (Spinner) view.findViewById(R.id.cdd_departmens_Spinner);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_spinner_item, departments);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter2);

        for (int i = 0; i < departments.length; i++) {
            if(departments[i].equals(doctor_department)){
                //spinner2.setSelection(i+1);
                current_department = i;
            }
        }
        spinner2.setSelection(current_department);

        final EditText doc_name = (EditText)view.findViewById(R.id.change_doc_fullname);
        doc_name.setText(doctor_name);
//        final EditText doc_building = (EditText)view.findViewById(R.id.change_doc_building);
//        doc_building.setText(doctor_building);
        final EditText doc_login = (EditText)view.findViewById(R.id.change_doc_login);
        doc_login.setText(doctor_login);
//        final EditText doc_department = (EditText)view.findViewById(R.id.change_doc_department);
//        doc_department.setText(doctor_department);
        final EditText doc_password = (EditText)view.findViewById(R.id.change_doc_password);
        doc_password.setText("InitialPassword");

        iapi = RetrofitClient.getInstance().create(IAPI.class);

        Button button = (Button) view.findViewById(R.id.changeDoctorBtn);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                doc_name.getText().toString();
                doc_login.getText().toString();
                doc_password.getText().toString();
//                doc_building.getText().toString();
//                doc_department.getText().toString();
                String building = spinner.getSelectedItem().toString();
                String department = spinner2.getSelectedItem().toString();
//                try{
//
//                    final AlertDialog dialog = new SpotsDialog.Builder()
//                            .setContext(getContext())
//                            .build();
//                    dialog.show();
//
//                    Doctor doctor = new Doctor(doctor_id,
//                            user_id,
//                            doc_building.getText().toString(),
//                            doc_department.getText().toString()
//                    );
//
//                    User user = new User(user_id,
//                            doc_name.getText().toString(),
//                            doc_login.getText().toString(),
//                            encrypt(doc_password.getText().toString().getBytes(), ("0123000000000215").getBytes()),
//                            "Doctor");
//
//                    user.Doctors.add(0,doctor);
//
//                    compositeDisposable.add(iapi.registerUser(user)
//                            .subscribeOn(Schedulers.io())
//                            .observeOn(AndroidSchedulers.mainThread())
//                            .subscribe(new Consumer<String>() {
//                                @Override
//                                public void accept(String s) throws Exception {
//                                    if(!s.equals("User is existing!"))
//                                    {
//                                        doc_name.setText("");
//                                        doc_login.setText("");
//                                        doc_password.setText("");
//                                        doc_building.setText("");
//                                        doc_department.setText("");
//                                        Toasty.success(getContext(), "Врач успешно обновлен!", Toast.LENGTH_SHORT, true).show();
//                                    }
//                                    dialog.dismiss();
//                                }
//                            }, new Consumer<Throwable>() {
//                                @Override
//                                public void accept(Throwable throwable) throws Exception {
//                                    dialog.dismiss();
//                                }
//                            }));
//
//                }
//                catch (Exception e){
//                    e.printStackTrace();
//                }

                if(doc_name.getText().toString().equals("")){
                    Toasty.error(getContext(), "Введите имя!", Toast.LENGTH_SHORT, true).show();
                }else if(doc_login.getText().toString().equals("")){
                    Toasty.error(getContext(), "Введите логин!", Toast.LENGTH_SHORT, true).show();
                }
                else if(doc_password.getText().toString().equals("")){
                    Toasty.error(getContext(), "Введите пароль!", Toast.LENGTH_SHORT, true).show();
                }
                else {
                    try {
                        Doctor doctor = new Doctor(0,
                                0,
                                building, //doc_building.getText().toString(),
                                department //doc_department.getText().toString()
                        );

                        User user = new User(user_id,
                                doc_name.getText().toString(),
                                doc_login.getText().toString(),
                                encrypt(doc_password.getText().toString().getBytes(), ("0123000000000215").getBytes()),
                                "Doctor");

                        user.Doctors.add(0, doctor);

                        //Call<User> call= iapi.updateDoctor(user);
                        Call<Void> call = iapi.updateDoctor(user);
                        call.enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                Toasty.success(getContext(), "Врач успешно обновлен!", Toast.LENGTH_SHORT, true).show();

                                AdminChangeDoctorFragment detailsFragment = new AdminChangeDoctorFragment();
                                FragmentTransaction ft = getFragmentManager().beginTransaction();
                                ft.replace(R.id.fragment_admin_container, detailsFragment);
                                ft.commit();
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable throwable) {
                                Log.d("tag", throwable.getMessage());
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
//                Toasty.success(getContext(), "Врач успешно обновлен!", Toast.LENGTH_SHORT, true).show();
//
//                AdminChangeDoctorFragment detailsFragment = new AdminChangeDoctorFragment();
//                FragmentTransaction ft = getFragmentManager().beginTransaction();
//                ft.replace(R.id.fragment_admin_container, detailsFragment);
//                ft.commit();

            }
        });

        Button delete = view.findViewById(R.id.deleteDoctorBtn);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Call<Void> call = iapi.deleteDoctor(user_id);
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        Log.d("tag2", response.message()+"");

                        Toasty.success(getContext(), "Врач успешно удален!", Toast.LENGTH_SHORT, true).show();

                        AdminChangeDoctorFragment detailsFragment = new AdminChangeDoctorFragment();
                        FragmentTransaction ft = getFragmentManager().beginTransaction();
                        ft.replace(R.id.fragment_admin_container, detailsFragment);
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


    private static String encrypt(byte[] key, byte[] clear) throws Exception
    {
        MessageDigest md = MessageDigest.getInstance("md5");
        byte[] digestOfPassword = md.digest(key);

        SecretKeySpec skeySpec = new SecretKeySpec(digestOfPassword, "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS7Padding");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        byte[] encrypted = cipher.doFinal(clear);
        return Base64.encodeToString(encrypted,Base64.DEFAULT);
    }

}
