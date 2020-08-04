package com.akkeritech.android.travelogue;

import android.app.Activity;
import android.app.Application;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.akkeritech.android.travelogue.data.PlacesDatabase;

import java.io.File;
import java.util.ArrayList;

import static com.akkeritech.android.travelogue.data.PlacesDatabase.PlacesDatabaseEntry.COLUMN_PLACE_LATITUDE;
import static com.akkeritech.android.travelogue.data.PlacesDatabase.PlacesDatabaseEntry.COLUMN_PLACE_LOCATION;
import static com.akkeritech.android.travelogue.data.PlacesDatabase.PlacesDatabaseEntry.COLUMN_PLACE_LONGITUDE;
import static com.akkeritech.android.travelogue.data.PlacesDatabase.PlacesDatabaseEntry.COLUMN_PLACE_NAME;
import static com.akkeritech.android.travelogue.data.PlacesDatabase.PlacesDatabaseEntry.COLUMN_PLACE_NOTES;
import static com.akkeritech.android.travelogue.data.PlacesDatabase.PlacesDatabaseEntry.COLUMN_PLACE_TIMESTAMP;

public class PlaceRepository {
    private static final String TAG = "PlaceRepository";
    private static PlaceRepository INSTANCE = null;
    private static Application app;

    private PlaceRepository(Application app) {
        this.app = app;
    };

    public static PlaceRepository getInstance(Application app) {
        if (INSTANCE == null) {
            INSTANCE = new PlaceRepository(app);
        }
        return(INSTANCE);
    }

    public static int insertPlace(Place place) {
        ContentValues values = new ContentValues();
        values.put(PlacesDatabase.PlacesDatabaseEntry.COLUMN_PLACE_NAME, place.placeName);
        values.put(PlacesDatabase.PlacesDatabaseEntry.COLUMN_PLACE_LOCATION, place.placeLocation);
        values.put(PlacesDatabase.PlacesDatabaseEntry.COLUMN_PLACE_NOTES, place.placeNotes);
        values.put(PlacesDatabase.PlacesDatabaseEntry.COLUMN_PLACE_LATITUDE, place.placeLatitude);
        values.put(PlacesDatabase.PlacesDatabaseEntry.COLUMN_PLACE_LONGITUDE, place.placeLongitude);
        values.put(PlacesDatabase.PlacesDatabaseEntry.COLUMN_PLACE_TIMESTAMP, place.placeTime);

        Uri contentUri = Uri.parse("content://" + PlacesDatabase.AUTHORITY + "/" + PlacesDatabase.PLACES_DATABASE_PATH);
        Uri returnedUri = app.getContentResolver().insert(contentUri, values);

        // return (int) ContentUris.parseId(returnedUri);
        int placeId = (int) ContentUris.parseId(returnedUri);
        return placeId;
    }

    public static void insertPhoto(Place place, File photoFile) {
        // Insert the filename of the photo in the the photo filename table
        ContentValues values = new ContentValues();
        values.put(PlacesDatabase.PhotosDatabaseEntry.COLUMN_PHOTO_FILENAME, photoFile.getPath());
        Uri contentUri = Uri.parse("content://" + PlacesDatabase.AUTHORITY + "/" + PlacesDatabase.PHOTOS_DATABASE_PATH);
        Uri returnedUri = app.getContentResolver().insert(contentUri, values);
        int newPhotoFilenameId = (int) ContentUris.parseId(returnedUri);

        // Using the returned id into the photo filename table, assign that id to the place
        ContentValues junctionValues = new ContentValues();
        junctionValues.put(PlacesDatabase.PhotosJunctionEntry.COLUMN_PLACE_INDEX, place.placeId);
        junctionValues.put(PlacesDatabase.PhotosJunctionEntry.COLUMN_PHOTO_INDEX, newPhotoFilenameId);
        Uri junctionUri = Uri.parse("content://" + PlacesDatabase.AUTHORITY + "/" + PlacesDatabase.PHOTO_JUNCTION_PATH);
        Uri returnedJunctionUri = app.getContentResolver().insert(junctionUri, junctionValues);
    }

    public static ArrayList<Place> retrievePlaces() {
        ArrayList<Place> tempPlaceList = new ArrayList<>();

        String[] projection = { PlacesDatabase.PlacesDatabaseEntry._ID, COLUMN_PLACE_NAME, COLUMN_PLACE_LOCATION, COLUMN_PLACE_NOTES,
                COLUMN_PLACE_LATITUDE, COLUMN_PLACE_LONGITUDE, COLUMN_PLACE_TIMESTAMP };
        Cursor cursor = null;
        try {
            cursor = app.getContentResolver().query(PlacesDatabase.CONTENT_URI, projection, null, null, null);
            while (cursor.moveToNext()) {
                int    placeId = cursor.getInt(cursor.getColumnIndexOrThrow(PlacesDatabase.PlacesDatabaseEntry._ID));
                String placeName = cursor.getString(cursor.getColumnIndexOrThrow("placeName"));
                String placeLocation = cursor.getString(cursor.getColumnIndexOrThrow("placeLocation"));
                String placeNotes = cursor.getString(cursor.getColumnIndexOrThrow("placeNotes"));
                double placeLatitude = cursor.getDouble(cursor.getColumnIndexOrThrow("placeLatitude"));
                double placeLongitude = cursor.getDouble(cursor.getColumnIndexOrThrow("placeLongitude"));
                int    placeTime = cursor.getInt(cursor.getColumnIndexOrThrow("placeTime"));
                Place place = new Place(placeId, placeName, placeLocation, placeNotes, placeLatitude, placeLongitude, placeTime);

                retrievePhotos(place);

                tempPlaceList.add(place);
            }
        } catch (Exception e) {
            Log.e(TAG, "Error found: " + e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return tempPlaceList;
    }

    private static void retrievePhotos(Place place) {
        if (place != null) {
            Uri uri = Uri.parse("content://" + PlacesDatabase.AUTHORITY + "/" + PlacesDatabase.PHOTOS_DATABASE_PATH);
            String[] projection = {"photosJunctionDatabase.placeId", "photosJunctionDatabase.photoId", "photosDatabase.photoFilename"};
            String selection = "photosJunctionDatabase.placeId=?";
            String[] selectionArgs = {Integer.toString(place.placeId)};
            Cursor cursor = null;
            try {
                cursor = app.getContentResolver().query(uri, projection, selection, selectionArgs, null);
                for (String column : cursor.getColumnNames()) {

                }
                while (cursor.moveToNext()) {
                    int placeId = cursor.getInt(cursor.getColumnIndexOrThrow("placeId"));
                    int photoId = cursor.getInt(cursor.getColumnIndexOrThrow("photoId"));
                    String photoFilename = cursor.getString(cursor.getColumnIndexOrThrow("photoFilename"));
                    place.addPhoto(photoFilename);
                }
            }
            catch (Exception e) {
                Log.e(TAG, "Database query error from photos database: " + e);
            }
            finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }
    }
}
