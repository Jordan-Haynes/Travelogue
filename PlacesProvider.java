package com.example.android.travelogue.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import com.example.android.travelogue.data.PlacesDatabase;

import static com.example.android.travelogue.data.PlacesDatabase.PlacesDatabaseEntry.TABLE_NAME;

/**
 * Created by jordanhaynes on 7/4/18.
 */

public class PlacesProvider extends ContentProvider {
    private final static String TAG = "PlacesProvider";

    private static PlacesDbHelper dbHelper = null;
    private static final int PLACES_TABLE = 1;
    private static final int PLACES_TABLE_ITEM = 2;

    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        uriMatcher.addURI(PlacesDatabase.AUTHORITY, PlacesDatabase.BASE_PATH, PLACES_TABLE);
        uriMatcher.addURI(PlacesDatabase.AUTHORITY, PlacesDatabase.BASE_PATH + "/#", PLACES_TABLE_ITEM);
    }

    public boolean onCreate() {
        if (dbHelper == null) {
            dbHelper = new PlacesDbHelper(getContext());
        }

        Log.d(TAG, "Creating Places database content provider");
        return true;
    }

    public Uri insert(Uri uri, ContentValues values) {

        int uriType = uriMatcher.match(uri);
        switch (uriType) {
            case PLACES_TABLE:
                Log.d(TAG, "Inserting into PlacesDatabase.");
                break;

            case PLACES_TABLE_ITEM:
                throw new IllegalArgumentException("Unknown URI: " + uri);

            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        db.insert(TABLE_NAME, null, values);

        return uri;
    }

    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        int uriType = uriMatcher.match(uri);
        switch (uriType) {
            case PLACES_TABLE:
                Log.d(TAG, "Querying places table.");
                break;

            case PLACES_TABLE_ITEM:
                Log.d(TAG, "Querying row of places table.");
                break;

            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }


        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(
                "placesDatabase",
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        return cursor;
    }

    // Return the number of rows updated
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }

    public int delete(Uri uri, String selection, String[] selectionArgs) {

        int uriType = uriMatcher.match(uri);
        switch (uriType) {
            case PLACES_TABLE:
                throw new IllegalArgumentException("Unknown URI: " + uri);

            case PLACES_TABLE_ITEM:
                Log.d(TAG, "Querying places table.");
                break;

            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        int deletedRows = db.delete(TABLE_NAME, selection, selectionArgs);
        Log.d(TAG, "Deleted " + deletedRows + " rows via ContentProvider");
        return deletedRows;
    }

    public String getType(Uri uri) {
        int uriType = uriMatcher.match(uri);
        switch (uriType) {
            case PLACES_TABLE:
                return PlacesDatabase.CONTENT_TYPE;

            case PLACES_TABLE_ITEM:
                return PlacesDatabase.CONTENT_ITEM_TYPE;

            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }
}
