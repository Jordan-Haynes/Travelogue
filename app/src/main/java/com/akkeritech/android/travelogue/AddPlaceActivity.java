package com.akkeritech.android.travelogue;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;

public class AddPlaceActivity extends AppCompatActivity implements AddPlaceFragment.OnFragmentInteractionListener {

    private static final String TAG = "PlaceListActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_place);

        FragmentManager fm = getSupportFragmentManager();

        AddPlaceFragment fragment = (AddPlaceFragment) fm.findFragmentById(R.id.add_place_fragment_container);

        if (fragment == null) {
            fragment = new AddPlaceFragment();
            fm.beginTransaction()
                    .add(R.id.add_place_fragment_container, fragment)
                    .commit();
        }
    }

    public void onFragmentInteraction(Place place) {
        Intent intent = new Intent(this, PlaceDetailViewActivity.class);
        intent.putExtra(Place.PLACE_NAME, place);
        startActivity(intent);

        finish();
    }
}
