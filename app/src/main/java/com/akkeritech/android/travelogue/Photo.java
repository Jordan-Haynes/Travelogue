package com.akkeritech.android.travelogue;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "photo_table")
public class Photo {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "photoId")
    public int    photoId;

    @ColumnInfo(name = "placeId")
    @NonNull
    public int    placeId;

    @ColumnInfo(name = "photoFilename")
    @NonNull
    public String photoFilename;

    public Photo() {

    }

    public Photo(int id, int placeId, String photoFilename) {
        this.photoId = id;
        this.placeId = placeId;
        this.photoFilename = photoFilename;
    }
}
