package com.akkeritech.android.travelogue;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import android.util.DisplayMetrics;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

public class PlaceListActivity extends AppCompatActivity implements PlaceListViewFragment.OnListFragmentInteractionListener {

    private static final String TAG = "PlaceListActivity";

    // These are required for two pane view
    private boolean mTwoPane;
    private PlaceDetailViewModel twoPaneViewModel = null;
    private TabAdapter adapter;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private PlaceDetailViewFragment detailFragment;
    private PlacePhotosFragment photosFragment;
    private MapsFragment mapsFragment;
    private Place twoPaneSavedPlace = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;

        if(findViewById(R.id.places_list_linear_layout) != null) {
            mTwoPane = true;
            if(savedInstanceState == null) {
                FragmentManager fm = getSupportFragmentManager();

                PlaceListViewFragment fragment = (PlaceListViewFragment) fm.findFragmentById(R.id.fragment_container);

                if (fragment == null) {
                    fragment = new PlaceListViewFragment();
                    fm.beginTransaction()
                            .add(R.id.fragment_container, fragment)
                            .commit();
                }
            }
        } else {
            mTwoPane = false;

            FragmentManager fm = getSupportFragmentManager();

            PlaceListViewFragment fragment = (PlaceListViewFragment) fm.findFragmentById(R.id.fragment_container);

            if (fragment == null) {
                fragment = new PlaceListViewFragment();
                fm.beginTransaction()
                        .add(R.id.fragment_container, fragment)
                        .commit();
            }
        }

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), AddPlaceActivity.class);
                startActivityForResult(intent, AddPlaceActivity.NEW_PLACE_REQUEST_CODE);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mTwoPane) {
            return false;
        }
        getMenuInflater().inflate(R.menu.menu_place_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (mTwoPane) {
            if (id == R.id.action_delete) {
                twoPaneViewModel.deletePlace(twoPaneSavedPlace);
                twoPaneViewModel.deletePlacePhotos(twoPaneSavedPlace.placeId);
                Toast.makeText(PlaceListActivity.this, "Place Deleted", Toast.LENGTH_SHORT).show();
                finish();
                return true;
            }
            else if (id == R.id.action_edit) {
                if (twoPaneSavedPlace != null) {
                    Intent intent = new Intent(getApplicationContext(), AddPlaceActivity.class);
                    intent.putExtra(Place.PLACE_NAME, twoPaneSavedPlace);
                    startActivityForResult(intent, AddPlaceActivity.EDIT_PLACE_REQUEST_CODE);
                    // finish();
                }
                return true;
            }
            else if (id == R.id.action_take_picture) {
                Toast.makeText(PlaceListActivity.this, "Take a picture", Toast.LENGTH_SHORT).show();
                finish();
                return true;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    public void onListFragmentInteraction(Place place) {
        if (mTwoPane) {
            twoPaneSavedPlace = place;
            if (twoPaneViewModel == null) {
                twoPaneViewModel = new ViewModelProvider(this).get(PlaceDetailViewModel.class);
                twoPaneViewModel.setCurrentPlace(place);

                viewPager = findViewById(R.id.viewPager);
                tabLayout = findViewById(R.id.tabLayout);
                adapter = new TabAdapter(getSupportFragmentManager());

                detailFragment = new PlaceDetailViewFragment(twoPaneViewModel);
                adapter.addFragment(detailFragment, "Notes");

                photosFragment = new PlacePhotosFragment(twoPaneViewModel);
                adapter.addFragment(photosFragment, "Photos");

                mapsFragment = new MapsFragment(twoPaneViewModel);
                mapsFragment.setDetails(place);
                adapter.addFragment(mapsFragment, "Map");

                viewPager.setAdapter(adapter);
                tabLayout.setupWithViewPager(viewPager);
            }
            else {
                twoPaneViewModel.setCurrentPlace(place);
                mapsFragment.setDetails(place);
            }
        }
        else {
            Intent intent = new Intent(this, PlaceDetailViewActivity.class);
            intent.putExtra(Place.PLACE_NAME, place);
            startActivity(intent);
        }
    }

    public void onFragmentInteraction(Uri uri) {
    }
}
