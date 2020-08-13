package com.akkeritech.android.travelogue;

import android.content.Intent;
import android.net.Uri;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PlaceDetailViewActivity extends AppCompatActivity implements PlaceDetailViewFragment.OnFragmentInteractionListener {

    private static final String TAG = "PlaceDetailViewActivity";

    private static final int REQUEST_IMAGE_CAPTURE = 1;

    Place place;

    PlaceDetailViewModel viewModel;

    private TabAdapter adapter;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private FloatingActionButton fab;

    private File photoFile = null;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_CAPTURE) {
                if (photoFile != null && photoFile.exists()) {
                    viewModel.insertPhoto(place, photoFile.getPath());
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_detail_view);

        Intent intent = getIntent();
        place = intent.getParcelableExtra("PlaceName");

        // Get a new or existing ViewModel from the ViewModelProvider.
        // viewModel = ViewModelProviders.of(this).get(PlaceDetailViewModel.class);
        viewModel = new ViewModelProvider(this).get(PlaceDetailViewModel.class);
        viewModel.setCurrentPlace(place);

        Toolbar toolbar = findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
           @Override
            public void onClick(View v) {
               startActivity(new Intent(getApplicationContext(), PlaceListActivity.class));
           }
        });

        viewPager = findViewById(R.id.viewPager);
        fab = findViewById(R.id.detail_fab);
        tabLayout = findViewById(R.id.tabLayout);
        adapter = new TabAdapter(getSupportFragmentManager());

        PlaceDetailViewFragment fragment = new PlaceDetailViewFragment(viewModel);
        adapter.addFragment(fragment, "Notes");

        PlacePhotosFragment photosFragment = new PlacePhotosFragment(viewModel);
        adapter.addFragment(photosFragment, "Photos");

        Double placeLat = place.placeLatitude;
        if (placeLat == 0) {
            NoLocationFragment mapFragment = new NoLocationFragment();
            adapter.addFragment(mapFragment, "Map");
        } else {
            MapsFragment mapFragment = new MapsFragment(viewModel);
            mapFragment.setDetails(place);
            adapter.addFragment(mapFragment, "Map");
        }

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        fab.setImageResource(R.drawable.ic_mode_edit_white_24dp);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {

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

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = viewPager.getCurrentItem();
                if (position == 0) {
                    Intent intent = new Intent(getApplicationContext(), AddPlaceActivity.class);
                    startActivity(intent);
                } else if (position == 1) {
                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (takePictureIntent.resolveActivity(getApplicationContext().getPackageManager()) != null) {

                        try {
                            photoFile = createImageFile();
                        }
                        catch (IOException exception) {
                            Log.e(TAG, "Exception when creating photo filename: " + exception);
                        }

                        if (photoFile != null) {
                            Uri photoURI = FileProvider.getUriForFile(getApplicationContext(),
                                    "com.akkeritech.android.travelogue.fileprovider",
                                    photoFile);
                            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                        }
                    }

                }
            }
        });

    }

    public void onFragmentInteraction(Uri uri) {
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + ".jpg";
        File storageDir = getApplicationContext().getFilesDir();
        return new File(storageDir, imageFileName);
    }
}
