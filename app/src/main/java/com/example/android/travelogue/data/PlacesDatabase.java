package com.example.android.travelogue.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public final class PlacesDatabase {
    public static final String AUTHORITY = "com.example.android.travelogue.data.provider";

    public static final String PLACES_DATABASE_PATH = "placesDatabase";
    public static final String PHOTOS_DATABASE_PATH = "photosDatabase";
    public static final String PHOTO_JUNCTION_PATH = "photosJunctionDatabase";

    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + PLACES_DATABASE_PATH);

    public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd.com.example.android.travelogue.data.provider.table";

    public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/vnd.com.example.android.travelogue.data.provider.table_item";

    // This table holds a complete description of a place
    public static class PlacesDatabaseEntry implements BaseColumns {
        public static final String TABLE_NAME = PLACES_DATABASE_PATH;
        public static final String COLUMN_PLACE_NAME = "placeName";
        public static final String COLUMN_PLACE_LOCATION = "placeLocation";
        public static final String COLUMN_PLACE_NOTES = "placeNotes";
        public static final String COLUMN_PLACE_LATITUDE = "placeLatitude";
        public static final String COLUMN_PLACE_LONGITUDE = "placeLongitude";
        public static final String COLUMN_PLACE_TIMESTAMP = "placeTime";
    }

    // This table holds the filename for a photo
    public static class PhotosDatabaseEntry implements BaseColumns {
        public static final String TABLE_NAME = PHOTOS_DATABASE_PATH;
        public static final String COLUMN_PHOTO_FILENAME = "photoFilename";
    }

    // This table maps a photo to a place. A place may have multiple photos.
    public static class PhotosJunctionEntry implements BaseColumns {
        public static final String TABLE_NAME = PHOTO_JUNCTION_PATH;
        public static final String COLUMN_PLACE_INDEX = "placeId";
        public static final String COLUMN_PHOTO_INDEX = "photoId";
    }
}
