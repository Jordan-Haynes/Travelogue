package com.akkeritech.android.travelogue;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface PhotoDao {
    // Allow the insert of the same place by specifying a conflict resolution strategy
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Photo photo);

    @Delete
    void delete(Photo photo);

    @Update
    void update(Photo photo);

    @Query("SELECT * FROM photo_table WHERE placeId LIKE :currentPlaceId")
    List<Photo> getPhotos(int currentPlaceId);

    @Query("DELETE FROM photo_table")
    void deleteAll();

}
