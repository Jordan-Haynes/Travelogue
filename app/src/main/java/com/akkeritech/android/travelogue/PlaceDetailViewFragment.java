package com.akkeritech.android.travelogue;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;

import androidx.cardview.widget.CardView;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.renderscript.Allocation;
import androidx.renderscript.Element;
import androidx.renderscript.RenderScript;
import androidx.renderscript.ScriptIntrinsicBlur;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
// import com.google.android.gms.location.FusedLocationProviderClient;

import com.akkeritech.android.travelogue.data.PlacesDatabase;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.app.Activity.RESULT_OK;


public class PlaceDetailViewFragment extends Fragment {

    private static final String TAG = "PlaceDetailViewFragment";
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

    /*
    private FusedLocationProviderClient client;
    */

    // private OnFragmentInteractionListener mListener;

    // TODO Replace with Place class
    Place place;

    private File photoFile = null;

    private ImageView mPhotoView;
    private LinearLayout mDetailView;

    private BottomSheetBehavior mBottomSheetBehavior;

    public PlaceDetailViewFragment() {
        place = null;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_CAPTURE) {
                // FEATURE: SNAP PICTURE
                // This is where the image is returned from the camera application.
                /* Original method before using a photo file
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                mPhotoView.setImageBitmap(imageBitmap);
                */
                if (photoFile == null || !photoFile.exists()) {
                    mPhotoView.setImageDrawable(null);
                }
                else {
                    // Bitmap bitmap = PictureUtilities.getScaledBitmap(photoFile.getPath(), getActivity());
                    // mPhotoView.setImageBitmap(bitmap);

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
            /*
            else if (requestCode == GRAB_IMAGE_GALLERY) {
                try {
                    final Uri imageUri = data.getData();
                    final InputStream imageStream = getActivity().getContentResolver().openInputStream(imageUri);
                    final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                    mPhotoView.setImageBitmap(selectedImage);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    // String errorString = "Something went wrong trying to load picture " + data.getDataString();
                    // Toast.makeText(this, errorString, Toast.LENGTH_LONG).show();
                }
            }
            */
        }
    }

    public void setDetails(Place place) {
        this.place = place;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_place_detail_view, container, false);

        /*
        ImageButton photoButton = (ImageButton) view.findViewById(R.id.place_camera);
        photoButton.setOnClickListener(new View.OnClickListener(){
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
        });

         */

        mPhotoView = (ImageView) view.findViewById(R.id.place_photo);
        if (place.photos != null && place.photos.size() > 0) {
            String photoFile = place.photos.get(0);
            if (photoFile == null) {
                // TODO Display a default bitmap
            } else {
                // TODO User must choose a default image
                Glide.with(view)
                        .load(photoFile)
                        .centerCrop()
                        .into(mPhotoView);
            }
        }
        mDetailView = (LinearLayout) view.findViewById(R.id.detail_card_view);

        TextView nameWidget = (TextView) view.findViewById(R.id.place_name);
        nameWidget.setText(place.placeName);

        TextView locationWidget = (TextView) view.findViewById(R.id.place_location);
        locationWidget.setText(Location.convert(place.placeLatitude, Location.FORMAT_DEGREES) + "," +
                Location.convert(place.placeLongitude, Location.FORMAT_DEGREES));

        TextView notesWidget = (TextView) view.findViewById(R.id.place_notes);
        notesWidget.setText("Notes \n\n" + place.placeNotes);

        View bottomSheet = view.findViewById(R.id.bottom_sheet);

        mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        mBottomSheetBehavior.setPeekHeight(150);
        // mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

        ViewTreeObserver viewTreeObserver = view.getViewTreeObserver();
        if (viewTreeObserver.isAlive()) {
            viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    int viewWidth = mPhotoView.getMeasuredWidth();
                    int viewHeight = mPhotoView.getMeasuredHeight();
                    Log.d(TAG, "GlobalLayout Width = " + viewWidth + ", GlobalLayout Height = " + viewHeight);
                    frostCardView(mPhotoView, mDetailView);
                }
            });
        }

        return view;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
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

    private void frostCardView(View parentView, View cardView) {
        if (getContext() == null)
            return;

        Bitmap image = Bitmap.createBitmap(parentView.getMeasuredWidth(),
                parentView.getMeasuredHeight(),
                Bitmap.Config.ARGB_4444); //reduce quality
        Canvas canvas = new Canvas(image);
        parentView.draw(canvas);

        Paint paint = new Paint();
        paint.setXfermode(
                new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        ColorFilter filter =
                new LightingColorFilter(0xFFFFFFFF, 0x00222222); // lighten
        paint.setColorFilter(filter);
        canvas.drawBitmap(image, 0, 0, paint);

        Matrix matrix = new Matrix();
        matrix.setScale(0.5f, 0.5f);
        Bitmap bitmap = Bitmap.createBitmap(image, (int) cardView.getX(), (int) cardView.getY(),
                cardView.getMeasuredWidth(), cardView.getMeasuredHeight(), matrix, true);

        if (bitmap != null) {
            ImageHelper.blurBitmapWithRenderscript(
                    RenderScript.create(getContext()),
                    bitmap);

            // Then display
            Drawable d = new BitmapDrawable(getResources(), bitmap);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                mDetailView.setBackground(d);
            }
        }
    }
}
