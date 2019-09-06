package com.example.android.travelogue.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

/**
 * Created by jordanhaynes on 7/4/18.
 */

public class PlacesProvider extends ContentProvider {
    private final static String TAG = "PlacesProvider";

    private static PlacesDbHelper dbHelper = null;
    private static final int PLACES_TABLE = 1;
    private static final int PLACES_TABLE_ITEM = 2;
    private static final int PHOTOS_TABLE = 3;
    private static final int PHOTOS_TABLE_ITEM = 4;
    private static final int JUNCTION_TABLE = 5;
    private static final int JUNCTION_TABLE_ITEM = 6;

    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        uriMatcher.addURI(PlacesDatabase.AUTHORITY, PlacesDatabase.PLACES_DATABASE_PATH, PLACES_TABLE);
        uriMatcher.addURI(PlacesDatabase.AUTHORITY, PlacesDatabase.PLACES_DATABASE_PATH + "/#", PLACES_TABLE_ITEM);
        uriMatcher.addURI(PlacesDatabase.AUTHORITY, PlacesDatabase.PHOTOS_DATABASE_PATH, PHOTOS_TABLE);
        uriMatcher.addURI(PlacesDatabase.AUTHORITY, PlacesDatabase.PHOTOS_DATABASE_PATH + "/#", PHOTOS_TABLE_ITEM);
        uriMatcher.addURI(PlacesDatabase.AUTHORITY, PlacesDatabase.PHOTO_JUNCTION_PATH, JUNCTION_TABLE);
        uriMatcher.addURI(PlacesDatabase.AUTHORITY, PlacesDatabase.PHOTO_JUNCTION_PATH + "/#", JUNCTION_TABLE_ITEM);
    }

    public boolean onCreate() {
        if (dbHelper == null) {
            dbHelper = new PlacesDbHelper(getContext());
        }

        Log.d(TAG, "Creating Places database content provider");
        return true;
    }

    public Uri insert(Uri uri, ContentValues values) {

        String tableName;

        int uriType = uriMatcher.match(uri);
        switch (uriType) {
            case PLACES_TABLE:
                Log.d(TAG, "Inserting into places table of PlacesDatabase.");
                tableName = PlacesDatabase.PlacesDatabaseEntry.TABLE_NAME;
                break;

            case PLACES_TABLE_ITEM:
                throw new IllegalArgumentException("Unknown URI: " + uri);

            case PHOTOS_TABLE:
                Log.d(TAG, "Inserting into photos table of PlacesDatabase");
                tableName = PlacesDatabase.PhotosDatabaseEntry.TABLE_NAME;
                break;

            case PHOTOS_TABLE_ITEM:
                throw new IllegalArgumentException("Unknown URI: " + uri);

            case JUNCTION_TABLE:
                Log.d(TAG, "Inserting into junction table of PlacesDatabase");
                tableName = PlacesDatabase.PhotosJunctionEntry.TABLE_NAME;
                break;

            case JUNCTION_TABLE_ITEM:
                throw new IllegalArgumentException("Unknown URI: " + uri);

            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        long index = db.insert(tableName, null, values);

        // return Uri.parse(PLACES_TABLE + "/" + index);
        // TODO return the full URI plus the index
        return Uri.parse(uri + "/" + index);
    }

    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = null;

        int uriType = uriMatcher.match(uri);
        switch (uriType) {
            case PLACES_TABLE:
            case PLACES_TABLE_ITEM:
                Log.d(TAG, "Querying places table in database");
                cursor = db.query(
                        PlacesDatabase.PlacesDatabaseEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;

            case PHOTOS_TABLE:
                Log.d(TAG, "Querying photos table in database");
                SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
                queryBuilder.setTables("photosDatabase INNER JOIN photosJunctionDatabase ON photosDatabase._ID=photosJunctionDatabase.photoId");
                // queryBuilder.appendWhere(selection);
                cursor = queryBuilder.query(
                        db,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        null
                );
                break;

            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        return cursor;
    }

    // Return the number of rows updated
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }

    public int delete(Uri uri, String selection, String[] selectionArgs) {

        String tableName;

        int uriType = uriMatcher.match(uri);
        switch (uriType) {
            case PLACES_TABLE:
                throw new IllegalArgumentException("Unknown URI: " + uri);

            case PLACES_TABLE_ITEM:
                Log.d(TAG, "Querying places table.");
                tableName = PlacesDatabase.PlacesDatabaseEntry.TABLE_NAME;
                break;

            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        int deletedRows = db.delete(tableName, selection, selectionArgs);
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
