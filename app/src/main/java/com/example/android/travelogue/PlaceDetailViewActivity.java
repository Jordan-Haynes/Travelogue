package com.example.android.travelogue;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.support.v7.widget.Toolbar;

public class PlaceDetailViewActivity extends AppCompatActivity implements PlaceDetailViewFragment.OnFragmentInteractionListener {

    private static final String TAG = "PlaceDetailViewActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_detail_view);
        // TODO Add back button to launch ListView
        Toolbar toolbar = findViewById(R.id.toolbar_detail_view);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
           @Override
            public void onClick(View v) {
               startActivity(new Intent(getApplicationContext(), PlaceListActivity.class));
           }
        });

        Intent intent = getIntent();
        Place place = (Place) intent.getParcelableExtra("PlaceName");

        FragmentManager fm = getSupportFragmentManager();

        Log.d(TAG, "Start of PlaceDetailViewActivity");

        PlaceDetailViewFragment fragment = (PlaceDetailViewFragment) fm.findFragmentById(R.id.detail_view_fragment_container);

        if (fragment == null) {
            fragment = new PlaceDetailViewFragment();
            fragment.setDetails(place);
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
