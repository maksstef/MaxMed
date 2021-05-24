package com.example.diploma;

import android.app.AlertDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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


public class ReceiptCreationFragment extends Fragment {

    IAPI iapi;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    private ArrayList<User> patientsList = new ArrayList();

    public static ReceiptCreationFragment newInstance() {
        return new ReceiptCreationFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_receipt_creation, container, false);

        final EditText drugName = v.findViewById(R.id.rc_drugName);
        final EditText lastDate = v.findViewById(R.id.rc_lastDate);

        final ArrayList<String> values = new ArrayList();
        final Map<String, Integer> valuesIds = new HashMap<String, Integer>();
        final Spinner spinner = (Spinner) v.findViewById(R.id.rc_patientNameSpinner);
        iapi = RetrofitClient.getInstance().create(IAPI.class);

        iapi.getPatients().enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                JSONArray jsonarray = null;
                try {
                    patientsList.clear();
                    jsonarray = new JSONArray(response.body());

                    for (int i = 0; i < jsonarray.length(); i++) {
                        JSONObject jsonobject = jsonarray.getJSONObject(i);
                        values.add(jsonobject.getString("FullName"));
                        valuesIds.put(jsonobject.getString("FullName"), jsonobject.getInt("UserId"));
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, values);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner.setAdapter(adapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });

//        String [] values = {"John Sam Washington","Kira Bush","Smith Collins","George Madison","Jack Wolfskin","Lela Potter"};
//        Spinner spinner = (Spinner) v.findViewById(R.id.rc_patientNameSpinner);
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(v.getContext(), android.R.layout.simple_spinner_item, values);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinner.setAdapter(adapter);

        Button button = (Button) v.findViewById(R.id.rc_create_receipt);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = spinner.getSelectedItem().toString();
                int patientId = valuesIds.get(name);

                if(drugName.getText().toString().equals("") || lastDate.getText().toString().equals("") ) {
                    Toasty.error(getContext(), "Заполните все поля!", Toast.LENGTH_SHORT, true).show();
                }
                else if(!lastDate.getText().toString().trim().matches("^[0-9]{1,2}(.)[0-9]{1,2}(.)[0-9]{4}$")){ //matches("^(0[1-9]|1[0-2])(.)(0[1-9]|1\\d|2\\d|3[01])(.)(19|20)\\d{2}$")){
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

                        Receipt receipt = new Receipt(id, patientId, drugName.getText().toString(), lastDate.getText().toString());

                        compositeDisposable.add(iapi.createReceipt(receipt)
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
