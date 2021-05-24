package com.example.diploma;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AdminActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_admin_container, AdminCreateScheduleFragment.newInstance()).commit();

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.admin_menu);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.a_create_schedule:
                    loadFragment(AdminCreateScheduleFragment.newInstance());
                    return true;
                case R.id.a_change_doctor:
                    loadFragment(AdminChangeDoctorFragment.newInstance());
                    return true;
                case R.id.a_create_doctor:
                    loadFragment(AdminCreateDoctorFragment.newInstance());
                    return true;
            }
            return false;
        }
    };

    private void loadFragment(Fragment fragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_admin_container, fragment);
        ft.commit();
    }


}
