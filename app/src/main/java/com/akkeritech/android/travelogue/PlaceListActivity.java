package com.akkeritech.android.travelogue;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.viewpager.widget.ViewPager;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.akkeritech.android.travelogue.settings.SettingsActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

public class PlaceListActivity extends AppCompatActivity implements PlaceListViewFragment.OnListFragmentInteractionListener, LoaderManager.LoaderCallbacks<Cursor> {

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
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
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

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
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

        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onListFragmentInteraction(Place place) {
        if (mTwoPane) {
            mPlace = place;

            viewPager = (ViewPager) findViewById(R.id.viewPager);
            tabLayout = (TabLayout) findViewById(R.id.tabLayout);
            adapter = new TabAdapter(getSupportFragmentManager());

            PlaceDetailViewFragment detailFragment = new PlaceDetailViewFragment();
            detailFragment.setDetails(place);
            adapter.addFragment(detailFragment, "Notes");

            PlacePhotosFragment photosFragment = new PlacePhotosFragment();
            photosFragment.setDetails(place);
            adapter.addFragment(photosFragment, "Photos");

            adapter.addFragment(new MapsFragment(), "Map");

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

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {

    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }
}
