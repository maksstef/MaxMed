package com.example.diploma;

import android.app.AlertDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import dmax.dialog.SpotsDialog;
import es.dmoral.toasty.Toasty;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class AdminCreateDoctorFragment extends Fragment {

    IAPI iapi;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    public static AdminCreateDoctorFragment newInstance() {
        return new AdminCreateDoctorFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_admin_create_doctor, container, false);

        iapi = RetrofitClient.getInstance().create(IAPI.class);

        String [] buildings = {"Маяковского, 73","Голодеда, 30","Проспет Независимости, 12","Кабушкина, 45","Рокоссовского, 109","Огинского, 94","Лелина, 5","Солнечная, 45",};
        final Spinner spinner = (Spinner) view.findViewById(R.id.cd_buildings_Spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_spinner_item, buildings);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        String [] departmens = {"Общая практика","Аллергология","Стоматология","Терапевтия","Неврология","Урология","Оториноларингология","Офтальмология","Хирургия",};
        final Spinner spinner2 = (Spinner) view.findViewById(R.id.cd_departmens_Spinner);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_spinner_item, departmens);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter2);

        Button button = (Button) view.findViewById(R.id.createDoctorBtn);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                final EditText fullname = (EditText)view.findViewById(R.id.cd_doc_fullname);
//                final EditText building = (EditText)view.findViewById(R.id.cd_doc_building);
//                final EditText department = (EditText)view.findViewById(R.id.cd_doc_department);
                final EditText login = (EditText)view.findViewById(R.id.cd_doc_login);
                final EditText password = (EditText)view.findViewById(R.id.cd_doc_password);

                String building = spinner.getSelectedItem().toString();
                String department = spinner2.getSelectedItem().toString();


                if(fullname.getText().toString().equals("")){
                    Toasty.error(getContext(), "Введите имя!", Toast.LENGTH_SHORT, true).show();
                }else if(login.getText().toString().equals("")){
                    Toasty.error(getContext(), "Введите логин!", Toast.LENGTH_SHORT, true).show();
                }
                else if(password.getText().toString().equals("")){
                    Toasty.error(getContext(), "Введите пароль!", Toast.LENGTH_SHORT, true).show();
                }
                else {
                    try {

                        final AlertDialog dialog = new SpotsDialog.Builder()
                                .setContext(getContext())
                                .build();
                        dialog.show();

                        int userID = UniqueID();
                        int doctorID = userID;
                        Doctor doctor = new Doctor(doctorID,
                                userID,
                                building,
                                department
                        );

                        User user = new User(userID,
                                fullname.getText().toString(),
                                login.getText().toString(),
                                encrypt(password.getText().toString().getBytes(), ("0123000000000215").getBytes()),
                                "Doctor");

                        user.Doctors.add(0, doctor);

                        compositeDisposable.add(iapi.registerUser(user)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Consumer<String>() {
                                    @Override
                                    public void accept(String s) throws Exception {
                                        if (!s.equals("User is existing!")) {
                                            //finish();

                                            fullname.setText("");
                                            login.setText("");
                                            password.setText("");
                                            //building.setText("");
                                            //department.setText("");
                                            Toasty.success(getContext(), "Врач успешно создан!", Toast.LENGTH_SHORT, true).show();
                                            //test
                                            //getActivity().finish();
                                        }
                                        //Toast.makeText(RegisterPage.this, s, Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();
                                    }
                                }, new Consumer<Throwable>() {
                                    @Override
                                    public void accept(Throwable throwable) throws Exception {
                                        dialog.dismiss();
                                        //Toast.makeText(RegisterPage.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }));


                        //Toast.makeText(getApplicationContext(), "Вы успешно зарегестрированы!", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
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

    public int UniqueID(){
        Date now = new Date();
        int id = Integer.parseInt(new SimpleDateFormat("MMddHHmmss",  Locale.US).format(now));
        return id;
    }

}
