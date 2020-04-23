package com.akkeritech.android.travelogue;

import android.content.Intent;
import android.net.Uri;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

public class PlaceDetailViewActivity extends AppCompatActivity implements PlaceDetailViewFragment.OnFragmentInteractionListener {

    private static final String TAG = "PlaceDetailViewActivity";

    private TabAdapter adapter;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private FloatingActionButton fab;

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
        Double placeLat = place.placeLatitude;

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        fab = (FloatingActionButton) findViewById(R.id.detail_fab);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        adapter = new TabAdapter(getSupportFragmentManager());

        PlaceDetailViewFragment fragment = new PlaceDetailViewFragment();
        fragment.setDetails(place);
        adapter.addFragment(fragment, "Notes");

        PlacePhotosFragment photosFragment = new PlacePhotosFragment();
        photosFragment.setDetails(place);
        adapter.addFragment(photosFragment, "Photos");

        if (placeLat == 0) {
            NoLocationFragment mapFragment = new NoLocationFragment();
            adapter.addFragment(mapFragment, "Map");
        } else {
            MapsFragment mapFragment = new MapsFragment();
            mapFragment.setDetails(place);
            adapter.addFragment(mapFragment, "Map");
        }

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {

                Log.d(TAG, "Position is " + position);

                if (position == 0) {
                    fab.setImageResource(R.drawable.ic_mode_edit_white_24dp);
                    fab.show();
                } else if (position == 1) {
                    fab.setImageResource(R.drawable.ic_photo_white_24dp);
                    fab.show();
                } else {
                    fab.hide();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        // Handle the FloatingActionButton click event:
        /*
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = viewPager.getCurrentItem();
                if (position == 0) {
                    Log.d(TAG, "fab clickListener on tab 1");
                } else if (position == 1) {
                    Log.d(TAG, "fab clickListener on tab 2");
                }
            }
        });

         */

        // This is the Floating Action Button (FAB) which launches AddPlaceFragment
        /*
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

         */
    }

    public void onFragmentInteraction(Uri uri) {
    }
}
