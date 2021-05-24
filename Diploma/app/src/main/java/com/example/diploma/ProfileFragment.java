package com.example.diploma;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import es.dmoral.toasty.Toasty;
import io.reactivex.disposables.CompositeDisposable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ProfileFragment extends Fragment {

    IAPI iapi;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        iapi = RetrofitClient.getInstance().create(IAPI.class);

        final EditText fullName = (EditText) view.findViewById(R.id.edit_fullname);
        final EditText dateOfBirth = (EditText) view.findViewById(R.id.edit_birthdate);
        final EditText address = (EditText) view.findViewById(R.id.edit_address);
        final EditText phone = (EditText) view.findViewById(R.id.edit_phone);
        final EditText email = (EditText) view.findViewById(R.id.edit_email);

        iapi.getPatient(LoginActivity.user_enterence_id).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                JSONArray jsonarray = null;
                JSONObject jsonObject = null;
                try {
                    jsonarray = new JSONArray(response.body());
                    for (int i = 0; i < jsonarray.length(); i++) {
                        JSONObject jsonobject = jsonarray.getJSONObject(i);

                        fullName.setText(jsonobject.getString("FullName"));
                        dateOfBirth.setText(jsonobject.getString("BirthDate"));
                        address.setText(jsonobject.getString("Address"));
                        phone.setText(jsonobject.getString("Phone"));
                        email.setText(jsonobject.getString("Email"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                //Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

//        fullName.setText("Стефаненко Максим Сергеевич");
//        dateOfBirth.setText("11.11.1999");
//        address.setText("ул. Героев 54/33");
//        phone.setText("+375123456789");
//        email.setText("randomemail@gmail.com");

        Button button = (Button) view.findViewById(R.id.changeProfileDataBtn);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(address.getText().toString().trim().equals("") || dateOfBirth.getText().toString().trim().equals("") ||
                        fullName.getText().toString().trim().equals("") || phone.getText().toString().trim().equals("") ||
                        email.getText().toString().trim().equals("")){
                    Toasty.error(getContext(), "Заполните все поля!", Toast.LENGTH_SHORT, true).show();
                }
                else{
                    try{
                        Patient patient = new Patient(0,
                                LoginActivity.user_enterence_id,
                                dateOfBirth.getText().toString(),
                                address.getText().toString(),
                                phone.getText().toString(),
                                email.getText().toString()
                        );

                        User user = new User(LoginActivity.user_enterence_id,
                                fullName.getText().toString(),
                                null,
                                null,
                                "Patient");

                        user.Patients.add(0,patient);

                        //Call<User> call= iapi.updateDoctor(user);
                        Call<Void> call= iapi.updatePatient(user);
                        call.enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                Toasty.success(getContext(), "Профиль успешно обновлен!", Toast.LENGTH_SHORT, true).show();

                                ProfileFragment detailsFragment = new ProfileFragment();
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

        return view;
    }
}
