package com.example.diploma;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import io.reactivex.disposables.CompositeDisposable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AdminChangeDoctorFragment extends Fragment {

    Integer user_id;
    Integer doctor_id;
    private LinearLayout llContainer;
    private EditText etSearch;
    private ListView lvDoctors;

    IAPI iapi;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    private ArrayList<User> doctorsList = new ArrayList();
    private DoctorListAdapter adapter1;

    //создать новый адаптер для юзера+доктора и использовать его для типа юзер


    public static AdminChangeDoctorFragment newInstance() {
        return new AdminChangeDoctorFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_change_doctor, container, false);

        etSearch = (EditText) view.findViewById(R.id.profiles_doctors_search);
        lvDoctors = (ListView) view.findViewById(R.id.profiles_doctors_view);

        iapi = RetrofitClient.getInstance().create(IAPI.class);

        etSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Call back the Adapter with current character to Filter
                adapter1.setListDoctors(doctorsList);
                adapter1.getFilter().filter(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        iapi.getDoctors().enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        JSONArray jsonarray = null;
                        try {
                            doctorsList.clear();
                            jsonarray = new JSONArray(response.body());
                            for (int i = 0; i < jsonarray.length(); i++) {
                                JSONObject jsonobject = jsonarray.getJSONObject(i);

                                User user = new User(jsonobject.getInt("UserId"),
                                        jsonobject.getString("FullName"),
                                        jsonobject.getString("Login"),
                                        null,
                                        null);

                                Doctor doctor = new Doctor(jsonobject.getInt("DoctorId"),
                                        jsonobject.getInt("DoctorId"),
                                        jsonobject.getString("Building"),
                                        jsonobject.getString("Department"));

                                user.Doctors.add(0,doctor);
                                doctorsList.add(user);
                            }

                            adapter1 = new DoctorListAdapter(getActivity(), R.layout.row, doctorsList);
                            lvDoctors.setAdapter(adapter1);

                            AdapterView.OnItemClickListener itemListener = new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                                    User selectedState = (User) parent.getItemAtPosition(position);
                                    user_id = (Integer)selectedState.UserId;
                                    doctor_id = (Integer)selectedState.Doctors.get(0).DoctorId;

                                    assert getFragmentManager() != null;
                                    ChangeDoctorDetailsFragment detailsFragment = new ChangeDoctorDetailsFragment();
                                    Bundle bundle = new Bundle();
                                    bundle.putInt("a_change_user_id", user_id);
                                    bundle.putInt("a_change_doctor_id", doctor_id);
                                    bundle.putString("a_change_doctor_name", selectedState.FullName);
                                    bundle.putString("a_change_doctor_login", selectedState.Login);
                                    bundle.putString("a_change_doctor_building", selectedState.Doctors.get(0).Building);
                                    bundle.putString("a_change_doctor_department", selectedState.Doctors.get(0).Department);
                                    detailsFragment.setArguments(bundle);

                                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                                    ft.replace(R.id.fragment_admin_container, detailsFragment).addToBackStack( "tag2" );
                                    ft.commit();

                                }
                            };
                            lvDoctors.setOnItemClickListener(itemListener);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        //Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        return view;
    }

}
