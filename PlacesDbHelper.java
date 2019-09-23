package com.example.android.travelogue.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.android.travelogue.data.PlacesDatabase;


/**
 * Created by jordanhaynes on 7/4/18.
 */

public class PlacesDbHelper extends SQLiteOpenHelper {
    private static final String TAG = "PlacesDbHelper";

    private static final String DATABASE_NAME = "savedplaces.db";

    private static final int DATABASE_VERSION = 1;

    public PlacesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Log.d(TAG, "onCreate: SQLiteDatabase");
        final String SQL_CREATE_PLACES_TABLE = "CREATE TABLE " + PlacesDatabase.PlacesDatabaseEntry.TABLE_NAME +
                " (" + PlacesDatabase.PlacesDatabaseEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                PlacesDatabase.PlacesDatabaseEntry.COLUMN_PLACE_NAME + " TEXT NOT NULL," +
                PlacesDatabase.PlacesDatabaseEntry.COLUMN_PLACE_LONGITUDE + " DOUBLE NOT NULL," +
                PlacesDatabase.PlacesDatabaseEntry.COLUMN_PLACE_LATITUDE + " DOUBLE NOT NULL," +
                PlacesDatabase.PlacesDatabaseEntry.COLUMN_PLACE_NOTES + " TEXT NOT NULL)";

        sqLiteDatabase.execSQL(SQL_CREATE_PLACES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + PlacesDatabase.PlacesDatabaseEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
