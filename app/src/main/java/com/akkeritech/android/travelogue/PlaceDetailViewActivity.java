package com.akkeritech.android.travelogue;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.akkeritech.android.travelogue.data.PlacesDatabase;
import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PlaceDetailViewActivity extends AppCompatActivity implements PlaceDetailViewFragment.OnFragmentInteractionListener {

    private static final String TAG = "PlaceDetailViewActivity";

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    // PULL REQUEST 2: get location or image from gallery
    /*
    private static final int REQUEST_LOCATION_PERMISSION = 2;
    */
    private static final int GRAB_IMAGE_GALLERY = 3;
    /*
    private static final String[] LOCATION_PERMISSIONS = new String[] {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };
    */

    private TabAdapter adapter;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private FloatingActionButton fab;

    /*
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_CAPTURE) {
                if (photoFile == null || !photoFile.exists()) {
                    mPhotoView.setImageDrawable(null);
                }
                else {
                    Glide.with(this)
                            .load(photoFile.getPath())
                            .into(mPhotoView);

                    // Save the filename of the photo to the database
                    ContentValues values = new ContentValues();
                    values.put(PlacesDatabase.PhotosDatabaseEntry.COLUMN_PHOTO_FILENAME, photoFile.getPath());
                    Uri contentUri = Uri.parse("content://" + PlacesDatabase.AUTHORITY + "/" + PlacesDatabase.PHOTOS_DATABASE_PATH);
                    Uri returnedUri = getActivity().getContentResolver().insert(contentUri, values);
                    Log.d(TAG, "Finished insert for photo filename " + returnedUri);
                    int newPhotoFilenameId = (int) ContentUris.parseId(returnedUri);

                    // Save the mapping of the photo to the place
                    ContentValues junctionValues = new ContentValues();
                    junctionValues.put(PlacesDatabase.PhotosJunctionEntry.COLUMN_PLACE_INDEX, place.placeId);
                    junctionValues.put(PlacesDatabase.PhotosJunctionEntry.COLUMN_PHOTO_INDEX, newPhotoFilenameId);
                    Uri junctionUri = Uri.parse("content://" + PlacesDatabase.AUTHORITY + "/" + PlacesDatabase.PHOTO_JUNCTION_PATH);
                    Uri returnedJunctionUri = getActivity().getContentResolver().insert(junctionUri, junctionValues);
                    Log.d(TAG, "Finished insert for juntion table " + returnedJunctionUri);
                }

            }
        }
    }

     */

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

        // Initialize FAB icon
        fab.setImageResource(R.drawable.ic_mode_edit_white_24dp);

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

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = viewPager.getCurrentItem();
                if (position == 0) {
                    Log.d(TAG, "fab clickListener on tab 1");

                    Intent intent = new Intent(getApplicationContext(), AddPlaceActivity.class);
                    // intent.putExtra(Place.PLACE_NAME, place);
                    startActivity(intent);
                } else if (position == 1) {
                    Log.d(TAG, "fab clickListener on tab 2");

                    // Take a picture
                    /*
                    @Override
                    public void onClick(View v) {
                        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {

                            try {
                                photoFile = createImageFile();
                            }
                            catch (IOException exception) {
                                Log.e(TAG, "Exception when creating photo filename: " + exception);
                            }

                            if (photoFile != null) {
                                Uri photoURI = FileProvider.getUriForFile(getActivity(),
                                        "com.akkeritech.android.travelogue.fileprovider",
                                        photoFile);
                                Log.d(TAG, "URI for photo file is " + photoURI);
                                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                            }
                        }
                    }

                     */
                }
            }
        });


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

    /*
    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + ".jpg";
        File storageDir = getContext().getFilesDir();
        Log.d(TAG, "Created filename for saving image: " + imageFileName);
        return new File(storageDir, imageFileName);
    }

     */
}
