package com.example.android.travelogue;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

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

        adapter.addFragment(fragment, "Details");

        adapter.addFragment(new PlacePhotosFragment(), "Photos");
        // adapter.addFragment(new Tab3Fragment(), "Map");
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        // FragmentManager fm = getSupportFragmentManager();

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
