package com.example.android.travelogue;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class PlaceDetailViewActivity extends AppCompatActivity implements PlaceDetailViewFragment.OnFragmentInteractionListener {

    private static final String TAG = "PlaceDetailViewActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_detail_view);

        Intent intent = getIntent();
        String placeName = intent.getStringExtra("PlaceName");
        Double placeLongitude = intent.getDoubleExtra("PlaceLongitude", 0);
        Double placeLatitude = intent.getDoubleExtra("PlaceLatitude", 0);
        String placeNotes = intent.getStringExtra("PlaceNotes");

        FragmentManager fm = getSupportFragmentManager();

        Log.d(TAG, "Start of PlaceDetailViewActivity");

        PlaceDetailViewFragment fragment = (PlaceDetailViewFragment) fm.findFragmentById(R.id.detail_view_fragment_container);

        if (fragment == null) {
            fragment = new PlaceDetailViewFragment();
            fragment.setDetails(placeName, placeLongitude, placeLatitude, placeNotes);
            fm.beginTransaction()
                    .add(R.id.detail_view_fragment_container, fragment)
                    .commit();
        }
    }

    public void onFragmentInteraction(Uri uri) {
        /*
        Intent intent = new Intent(this, PlaceDetailViewActivity.class);
        intent.putExtra(PLACE_DETAILS, position);
        startActivity(intent);
        */
    }
}
