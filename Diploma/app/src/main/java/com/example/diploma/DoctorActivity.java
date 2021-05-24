package com.example.diploma;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;


import com.google.android.material.bottomnavigation.BottomNavigationView;


public class DoctorActivity extends AppCompatActivity {

    boolean mKeyboardVisible = false;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.d_doctor_schedule:
                    loadFragment(ScheduleFragment.newInstance());
                    return true;
                case R.id.d_patients_journal:
                    loadFragment(JournalFragment.newInstance());
                    return true;
                case R.id.d_patient_history:
                    loadFragment(HistoryForDocFragment.newInstance());
                    return true;
                case R.id.d_create_receipt:
                    loadFragment(ReceiptCreationFragment.newInstance());
                    return true;
            }
            return false;
        }
    };

    private void loadFragment(Fragment fragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_doc_container, fragment);
        ft.commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_doc_container, ScheduleFragment.newInstance()).addToBackStack( "tag2" ).commit();

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }


}

