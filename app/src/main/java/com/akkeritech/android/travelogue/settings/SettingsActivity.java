package com.akkeritech.android.travelogue.settings;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.util.Log;

import com.akkeritech.android.travelogue.R;

public class SettingsActivity extends AppCompatActivity {

    private static final String TAG = "SettingsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        FragmentManager fm = getSupportFragmentManager();

        Log.d(TAG, "Start of SettingsActivity");

        SettingsFragment fragment = (SettingsFragment) fm.findFragmentById(R.id.detail_view_fragment_container);

        if (fragment == null) {
            fragment = new SettingsFragment();
            fm.beginTransaction()
                    .add(R.id.detail_view_fragment_container, fragment)
                    .commit();
        }
    }
}
