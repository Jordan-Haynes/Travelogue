package com.example.android.travelogue;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class AddPlaceActivity extends AppCompatActivity implements AddPlaceFragment.OnFragmentInteractionListener {

    private static final String TAG = "PlaceListActivity";
    private static final String PLACE_NAME = "PlaceName";
    private static final String PLACE_LONGITUDE = "PlaceLongitude";
    private static final String PLACE_LATITUDE = "PlaceLatitude";
    private static final String PLACE_NOTES = "PlaceNotes";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_place);

        //TODO Add fragment manager to launch AddPlaceFragment
        FragmentManager fm = getSupportFragmentManager();

        Log.d(TAG, "Start of AddPlaceActivity");

        AddPlaceFragment fragment = (AddPlaceFragment) fm.findFragmentById(R.id.add_place_fragment_container);

        if (fragment == null) {
            fragment = new AddPlaceFragment();
            fm.beginTransaction()
                    .add(R.id.add_place_fragment_container, fragment)
                    .commit();
        }
    }

    // Callback to AddPlaceFragment
    public void onFragmentInteraction(String placeName, Double placeLong, Double placeLat, String placeNotes) {
        Intent intent = new Intent(this, PlaceDetailViewActivity.class);
        intent.putExtra(PLACE_NAME, placeName);
        intent.putExtra(PLACE_LONGITUDE, placeLong);
        intent.putExtra(PLACE_LATITUDE, placeLat);
        intent.putExtra(PLACE_NOTES, placeNotes);
        startActivity(intent);
    }
}
