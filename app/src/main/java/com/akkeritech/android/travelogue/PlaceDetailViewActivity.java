package com.akkeritech.android.travelogue;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.akkeritech.android.travelogue.data.PlacesDatabase;
import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.viewpager.widget.ViewPager;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PlaceDetailViewActivity extends AppCompatActivity implements PlaceDetailViewFragment.OnFragmentInteractionListener {

    private static final String TAG = "PlaceDetailViewActivity";

    private static final int REQUEST_IMAGE_CAPTURE = 1;

    Place place;
    private TabAdapter adapter;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private FloatingActionButton fab;

    private File photoFile = null;
    private ImageView mPhotoView;

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

                    ContentValues values = new ContentValues();
                    values.put(PlacesDatabase.PhotosDatabaseEntry.COLUMN_PHOTO_FILENAME, photoFile.getPath());
                    Uri contentUri = Uri.parse("content://" + PlacesDatabase.AUTHORITY + "/" + PlacesDatabase.PHOTOS_DATABASE_PATH);
                    Uri returnedUri = getApplicationContext().getContentResolver().insert(contentUri, values);
                    int newPhotoFilenameId = (int) ContentUris.parseId(returnedUri);

                    ContentValues junctionValues = new ContentValues();
                    junctionValues.put(PlacesDatabase.PhotosJunctionEntry.COLUMN_PLACE_INDEX, place.placeId);
                    junctionValues.put(PlacesDatabase.PhotosJunctionEntry.COLUMN_PHOTO_INDEX, newPhotoFilenameId);
                    Uri junctionUri = Uri.parse("content://" + PlacesDatabase.AUTHORITY + "/" + PlacesDatabase.PHOTO_JUNCTION_PATH);
                    Uri returnedJunctionUri = getApplicationContext().getContentResolver().insert(junctionUri, junctionValues);
                }

            }
        }
    }

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
        Place place = intent.getParcelableExtra("PlaceName");
        Double placeLat = place.placeLatitude;

        viewPager = findViewById(R.id.viewPager);
        fab = findViewById(R.id.detail_fab);
        tabLayout = findViewById(R.id.tabLayout);
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
