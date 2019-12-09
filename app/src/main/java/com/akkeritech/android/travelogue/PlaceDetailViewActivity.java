package com.akkeritech.android.travelogue;

import android.content.Intent;
import android.net.Uri;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
// import android.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

public class PlaceDetailViewActivity extends AppCompatActivity implements PlaceDetailViewFragment.OnFragmentInteractionListener {

    private static final String TAG = "PlaceDetailViewActivity";

    // For the ViewPager
    private TabAdapter adapter;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_detail_view);

        Toolbar toolbar = findViewById(R.id.tool_bar);
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

        // TODO Add ViewPager
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        adapter = new TabAdapter(getSupportFragmentManager());

        // Throw Parcelable from AddFragment to DetailsViewFragment
        PlaceDetailViewFragment fragment = new PlaceDetailViewFragment();
        fragment.setDetails(place);
        adapter.addFragment(fragment, "Notes");

        PlacePhotosFragment photosFragment = new PlacePhotosFragment();
        photosFragment.setDetails(place);
        adapter.addFragment(photosFragment, "Photos");


        adapter.addFragment(new MapsFragment(), "Map");

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        // FragmentManager fm = getSupportFragmentManager();

        // This is the Floating Action Button (FAB) which launches AddPlaceFragment
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.detail_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();

                // TODO When clicked, should bring up user's camera or photo gallery
                Intent intent = new Intent(view.getContext(), AddPlaceActivity.class);
                startActivity(intent);
            }
        });

        Log.d(TAG, "Start of PlaceDetailViewActivity");

        /*
        PlaceDetailViewFragment fragment = (PlaceDetailViewFragment) fm.findFragmentById(R.id.detail_view_fragment_container);

        if (fragment == null) {
            fragment = new PlaceDetailViewFragment();
            fragment.setDetails(place);
            fm.beginTransaction()
                    .add(R.id.detail_view_fragment_container, fragment)
                    .commit();
        }
        */
    }

    public void onFragmentInteraction(Uri uri) {
        /*
        Intent intent = new Intent(this, PlaceDetailViewActivity.class);
        intent.putExtra(PLACE_DETAILS, position);
        startActivity(intent);
        */
    }
}
