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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HistoryForDocFragment extends Fragment {

    Integer patient_id;
    private LinearLayout llContainer;
    private EditText etSearch;
    private ListView lvPatients;

    IAPI iapi;

    private ArrayList<JournalPatients> patientsList = new ArrayList();
    private JournalPatientAdapter adapter1;

    public static HistoryForDocFragment newInstance() {
        return new HistoryForDocFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history_for_doc, container, false);

        iapi = RetrofitClient.getInstance().create(IAPI.class);

        etSearch = (EditText) view.findViewById(R.id.doc_history_search);
        lvPatients = (ListView) view.findViewById(R.id.doc_history_view);

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
                                null);

                        patientsList.add(patient);
                    }

                    adapter1 = new JournalPatientAdapter(getActivity(), R.layout.row, patientsList);
                    lvPatients.setAdapter(adapter1);

                    AdapterView.OnItemClickListener itemListener = new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                            JournalPatients selectedState = (JournalPatients) parent.getItemAtPosition(position);
                            patient_id = (Integer)selectedState.getPatientId();

                            assert getFragmentManager() != null;
                            HistoryForDocDetailsFragment detailsFragment = new HistoryForDocDetailsFragment();
                            Bundle bundle = new Bundle();
                            bundle.putInt("dc_patient_id", patient_id);
                            bundle.putString("dc_patient_name", selectedState.getPatientName());
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

        return view;
    }


}
