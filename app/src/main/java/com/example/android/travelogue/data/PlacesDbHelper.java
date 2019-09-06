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

    private static final int DATABASE_VERSION = 2;

    public PlacesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Log.d(TAG, "onCreate: SQLiteDatabase");

        final String SQL_CREATE_PLACES_TABLE =
                "create table " +
                PlacesDatabase.PlacesDatabaseEntry.TABLE_NAME + " (" +
                PlacesDatabase.PlacesDatabaseEntry._ID + " integer primary key autoincrement, " +
                PlacesDatabase.PlacesDatabaseEntry.COLUMN_PLACE_NAME + " text not null, " +
                PlacesDatabase.PlacesDatabaseEntry.COLUMN_PLACE_LOCATION + " text not null, " +
                PlacesDatabase.PlacesDatabaseEntry.COLUMN_PLACE_NOTES + " text not null, "+
                PlacesDatabase.PlacesDatabaseEntry.COLUMN_PLACE_LATITUDE + " double not null, " +
                PlacesDatabase.PlacesDatabaseEntry.COLUMN_PLACE_LONGITUDE + " double not null, " +
                PlacesDatabase.PlacesDatabaseEntry.COLUMN_PLACE_TIMESTAMP + " integer not null)";
        sqLiteDatabase.execSQL(SQL_CREATE_PLACES_TABLE);

        final String SQL_CREATE_PHOTOS_TABLE =
                "create table " +
                PlacesDatabase.PhotosDatabaseEntry.TABLE_NAME + " (" +
                PlacesDatabase.PhotosDatabaseEntry._ID + " integer primary key autoincrement, " +
                PlacesDatabase.PhotosDatabaseEntry.COLUMN_PHOTO_FILENAME + " text not null)";
        sqLiteDatabase.execSQL(SQL_CREATE_PHOTOS_TABLE);

        final String SQL_CREATE_JUNCTION_TABLE =
                "create table " +
                PlacesDatabase.PhotosJunctionEntry.TABLE_NAME + " (" +
                PlacesDatabase.PhotosJunctionEntry._ID + " integer primary key autoincrement," +
                PlacesDatabase.PhotosJunctionEntry.COLUMN_PLACE_INDEX + " integer not null, " +
                PlacesDatabase.PhotosJunctionEntry.COLUMN_PHOTO_INDEX + " integer not null)";
        sqLiteDatabase.execSQL(SQL_CREATE_JUNCTION_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("drop table if exists " + PlacesDatabase.PlacesDatabaseEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("drop table if exists " + PlacesDatabase.PhotosDatabaseEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("drop table if exists " + PlacesDatabase.PhotosJunctionEntry.TABLE_NAME);

        onCreate(sqLiteDatabase);
    }
}
