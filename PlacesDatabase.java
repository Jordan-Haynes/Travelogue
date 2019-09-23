package com.example.android.travelogue.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public final class PlacesDatabase {
    public static final String AUTHORITY = "com.example.android.travelogue.data.provider";

    public static final String BASE_PATH = "placesDatabase";

    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH);

    public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd.com.example.android.travelogue.data.provider.table";

    public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/vnd.com.example.android.travelogue.data.provider.table_item";

    public static class PlacesDatabaseEntry implements BaseColumns {
        public static final String TABLE_NAME = "placesDatabase";
        public static final String COLUMN_PLACE_NAME = "placeName";
        public static final String COLUMN_PLACE_LONGITUDE = "placeLongitude";
        public static final String COLUMN_PLACE_LATITUDE = "placeLatitude";
        public static final String COLUMN_PLACE_NOTES = "placeNotes";
    }
}
