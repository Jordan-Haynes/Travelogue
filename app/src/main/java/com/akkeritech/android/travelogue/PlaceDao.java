package com.akkeritech.android.travelogue;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface PlaceDao {
    // Allow the insert of the same place by specifying a conflict resolution strategy
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Place place);

    @Delete
    void delete(Place place);

    @Update
    void update(Place place);

    @Query("SELECT * from place_table")
    LiveData<List<Place>> getPlaces();

    @Query("DELETE FROM place_table")
    void deleteAll();

    @Query("SELECT * FROM place_table WHERE placeId LIKE :pid")
    Place setPlace(final int pid);

    @Query("UPDATE place_table SET placeReferencePhoto = :photoFilename WHERE placeId = :pid")
    int updatePhoto(final int pid, String photoFilename);
}
