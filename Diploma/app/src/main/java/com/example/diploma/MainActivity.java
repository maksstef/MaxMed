package com.example.diploma;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        final ImageButton historyBtn = (ImageButton) findViewById(R.id.historybtn);
//        final ImageButton appointmentBtn = (ImageButton) findViewById(R.id.appointmentbtn);
//        final ImageButton receiptsBtn = (ImageButton) findViewById(R.id.receiptsbtn);
//        final ImageButton profileBtn = (ImageButton) findViewById(R.id.profilebtn);
//
//        final ReceiptsFragment receipts = new ReceiptsFragment();
//        final ProfileFragment profile = new ProfileFragment();
//        final AppointmentsFragment appointments = new AppointmentsFragment();
//        final HistoryFragment history = new HistoryFragment();
//
//        getSupportFragmentManager().beginTransaction().replace(R.id.bodyFragment, history).commit();
//        historyBtn.setColorFilter(Color.argb(128, 128, 128, 128));
//
//        historyBtn.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View _view) {
//                historyBtn.setColorFilter(Color.argb(128, 128, 128, 128));
//                profileBtn.setColorFilter(Color.argb(255, 255, 255, 255));
//                appointmentBtn.setColorFilter(Color.argb(255, 255, 255, 255));
//                receiptsBtn.setColorFilter(Color.argb(255, 255, 255, 255));
//
//                getSupportFragmentManager().beginTransaction().replace(R.id.bodyFragment, history).commit();
//            }
//        });
//
//        appointmentBtn.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View _view) {
//                appointmentBtn.setColorFilter(Color.argb(128, 128, 128, 128));
//                profileBtn.setColorFilter(Color.argb(255, 255, 255, 255));
//                receiptsBtn.setColorFilter(Color.argb(255, 255, 255, 255));
//                historyBtn.setColorFilter(Color.argb(255, 255, 255, 255));
//
//                getSupportFragmentManager().beginTransaction().replace(R.id.bodyFragment, appointments).commit();
//            }
//        });
//
//        profileBtn.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View _view) {
//                profileBtn.setColorFilter(Color.argb(128, 128, 128, 128));
//                receiptsBtn.setColorFilter(Color.argb(255, 255, 255, 255));
//                appointmentBtn.setColorFilter(Color.argb(255, 255, 255, 255));
//                historyBtn.setColorFilter(Color.argb(255, 255, 255, 255));
//
//                getSupportFragmentManager().beginTransaction().replace(R.id.bodyFragment, profile).commit();
//            }
//        });
//
//        receiptsBtn.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View _view) {
//                receiptsBtn.setColorFilter(Color.argb(128, 128, 128, 128));
//                profileBtn.setColorFilter(Color.argb(255, 255, 255, 255));
//                appointmentBtn.setColorFilter(Color.argb(255, 255, 255, 255));
//                historyBtn.setColorFilter(Color.argb(255, 255, 255, 255));
//
//                getSupportFragmentManager().beginTransaction().replace(R.id.bodyFragment, receipts).commit();
//            }
//        });
//    }

//    public void ChangeProfileData(View view) {
//        Toast.makeText(this, "hey", Toast.LENGTH_SHORT).show();
//    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.p_patient_history:
                    loadFragment(HistoryFragment.newInstance());
                    return true;
                case R.id.p_make_appointment:
                    loadFragment(AppointmentsFragment.newInstance());
                    return true;
                case R.id.p_receipts:
                    loadFragment(ReceiptsFragment.newInstance());
                    return true;
                case R.id.p_account:
                    loadFragment(ProfileFragment.newInstance());
                    return true;
            }
            return false;
        }
    };

    private void loadFragment(Fragment fragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.bodyFragment, fragment);
        ft.commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d("taggg","id "+LoginActivity.user_enterence_id);

        getSupportFragmentManager().beginTransaction().replace(R.id.bodyFragment, HistoryFragment.newInstance()).commit();

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.main_navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

}
