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

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

public class PlaceListActivity extends AppCompatActivity implements PlaceListViewFragment.OnListFragmentInteractionListener {

    private static final String TAG = "PlaceListActivity";

    private boolean mTwoPane;

    private TabAdapter adapter;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    int position;
    Place mPlace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_list);
        Toolbar toolbar = findViewById(R.id.toolbar);

        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;

        // if(findViewById(R.id.places_list_linear_layout) != null) {
        if (dpWidth < 0) {
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
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_place_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    public void onListFragmentInteraction(Place place) {
        if (mTwoPane) {
            mPlace = place;

            PlaceDetailViewModel viewModel = new ViewModelProvider(this).get(PlaceDetailViewModel.class);
            viewModel.setCurrentPlace(place);

            viewPager = findViewById(R.id.viewPager);
            tabLayout = findViewById(R.id.tabLayout);
            adapter = new TabAdapter(getSupportFragmentManager());

            PlaceDetailViewFragment detailFragment = new PlaceDetailViewFragment(viewModel);
            adapter.addFragment(detailFragment, "Notes");

            PlacePhotosFragment photosFragment = new PlacePhotosFragment(viewModel);
            adapter.addFragment(photosFragment, "Photos");

            MapsFragment mapsFragment = new MapsFragment(viewModel);
            adapter.addFragment(mapsFragment, "Map");

            viewPager.setAdapter(adapter);
            tabLayout.setupWithViewPager(viewPager);
        } else {
            Intent intent = new Intent(this, PlaceDetailViewActivity.class);
            intent.putExtra(Place.PLACE_NAME, place);
            startActivity(intent);
        }
    }

    public void onFragmentInteraction(Uri uri) {
    }
}
