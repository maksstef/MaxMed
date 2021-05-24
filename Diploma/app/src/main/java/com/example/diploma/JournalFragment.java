package com.example.diploma;

import android.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class JournalFragment extends Fragment {

    Integer patient_id;
    private LinearLayout llContainer;
    private EditText etSearch;
    private ListView lvPatients;
    IAPI iapi;

    private ArrayList<JournalPatients> patientsList = new ArrayList();
    private JournalPatientAdapter adapter1;


    public static JournalFragment newInstance() {
        return new JournalFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_journal, container, false);

        iapi = RetrofitClient.getInstance().create(IAPI.class);

        etSearch = (EditText) view.findViewById(R.id.journal_search);
        lvPatients = (ListView) view.findViewById(R.id.journal_view);

        // Add Text Change Listener to EditText
        etSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Call back the Adapter with current character to Filter
                adapter1.setListPatients(patientsList);
                adapter1.getFilter().filter(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        iapi.getPatients().enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                JSONArray jsonarray = null;
                try {
                    patientsList.clear();

                    jsonarray = new JSONArray(response.body());
                    for (int i = 0; i < jsonarray.length(); i++) {
                        JSONObject jsonobject = jsonarray.getJSONObject(i);

                        JournalPatients patient = new JournalPatients(jsonobject.getInt("PatientId"),
                                jsonobject.getString("FullName"),
                                jsonobject.getString("BirthDate"));

                        patientsList.add(patient);
                    }

                    adapter1 = new JournalPatientAdapter(getActivity(), R.layout.row_journal, patientsList);
                    lvPatients.setAdapter(adapter1);

                    AdapterView.OnItemClickListener itemListener = new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                            JournalPatients selectedState = (JournalPatients) parent.getItemAtPosition(position);
                            patient_id = (Integer)selectedState.getPatientId();

                            assert getFragmentManager() != null;
                            JournalDetailsFragment detailsFragment = new JournalDetailsFragment();
                            Bundle bundle = new Bundle();
                            bundle.putInt("patient_id", patient_id);
                            bundle.putString("patient_name", selectedState.getPatientName());
                            detailsFragment.setArguments(bundle);

                            FragmentTransaction ft = getFragmentManager().beginTransaction();
                            ft.replace(R.id.fragment_doc_container, detailsFragment).addToBackStack( "tag" );
                            ft.commit();

                        }
                    };
                    lvPatients.setOnItemClickListener(itemListener);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });

//        etSearch = (EditText) view.findViewById(R.id.journal_search);
//        lvPatients = (ListView) view.findViewById(R.id.journal_view);
//
//        // Add Text Change Listener to EditText
//        etSearch.addTextChangedListener(new TextWatcher() {
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                // Call back the Adapter with current character to Filter
//                adapter1.setListPatients(mProductArrayList);
//                adapter1.getFilter().filter(s.toString());
//            }
//
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//            }
//        });
//
//        mProductArrayList.clear();
//        mProductArrayList.add(new JournalPatients(1, "John Sam Washington", "12.12.1987"));
//        mProductArrayList.add(new JournalPatients(2, "Kira Bush", "23.02.1994"));
//        mProductArrayList.add(new JournalPatients(3, "Smith Collins", "12.03.1999"));
//        mProductArrayList.add(new JournalPatients(4, "George Madison", "05.12.1997"));
//        mProductArrayList.add(new JournalPatients(5, "Jack Wolfskin", "09.09.1997"));
//        mProductArrayList.add(new JournalPatients(6, "Lela Potter", "17.11.1993"));
//
//        adapter1 = new JournalPatientAdapter(getActivity(), R.layout.row, mProductArrayList);
//        //adapter1 = new JournalPatientAdapter(getActivity(), mProductArrayList);
//        lvPatients.setAdapter(adapter1);
//
//        AdapterView.OnItemClickListener itemListener = new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
//                JournalPatients selectedState = (JournalPatients) parent.getItemAtPosition(position);
//                patient_id = (Integer)selectedState.getPatientId(); //Integer.valueOf(selectedState.getPatientId());
//                //Toast.makeText(getActivity(), "id: "+patient_id, Toast.LENGTH_SHORT).show();
//                //View view = new View(getApplicationContext()); попробовтаь как вью выше
//
//                assert getFragmentManager() != null;
//                JournalDetailsFragment detailsFragment = new JournalDetailsFragment();
//                Bundle bundle = new Bundle();
//                bundle.putInt("patient_id", patient_id);
//                bundle.putString("patient_name", selectedState.getPatientName());
//                detailsFragment.setArguments(bundle);
//
//                FragmentTransaction ft = getFragmentManager().beginTransaction();
//                ft.replace(R.id.fragment_doc_container, detailsFragment).addToBackStack( "tag" );
//                ft.commit();
//
//            }
//        };
//        lvPatients.setOnItemClickListener(itemListener);


        return view;
    }


    @Override
    public void onResume () {

        super.onResume();
//
//        mProductArrayList.add(new JournalPatients(1, "John Sam Washington", "12.12.1987"));
//        mProductArrayList.add(new JournalPatients(2, "Kira Bush", "23.02.1994"));
//        mProductArrayList.add(new JournalPatients(3, "Smith Collins", "12.03.1999"));
//        mProductArrayList.add(new JournalPatients(4, "George Madison", "05.12.1997"));
//        mProductArrayList.add(new JournalPatients(5, "Jack Wolfskin", "09.09.1997"));
//        mProductArrayList.add(new JournalPatients(6, "Lela Potter", "17.11.1993"));
//
//        adapter1 = new JournalPatientAdapter(getActivity(), mProductArrayList);
//        lvProducts.setAdapter(adapter1);
//
//        AdapterView.OnItemClickListener itemListener = new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
//                JournalPatients selectedState = (JournalPatients) parent.getItemAtPosition(position);
//                appointment_id = Integer.valueOf(selectedState.getPatientId());
//                Toast.makeText(getActivity(), "id: "+appointment_id, Toast.LENGTH_SHORT).show();
//                //View view = new View(getApplicationContext()); попробовтаь как вью выше
//
//            }
//        };
//        lvProducts.setOnItemClickListener(itemListener);

    }


}

