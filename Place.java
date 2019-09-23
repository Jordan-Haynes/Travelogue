package com.example.android.travelogue;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by jordanhaynes on 7/10/18.
 */

@IgnoreExtraProperties
public class Place {
    public String placeKey;
    public String placeName;
    public String placeLocation;
    public String placeNotes;

    public Place() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Place(String name, String location, String notes) {
        this.placeName = name;
        this.placeLocation = location;
        this.placeNotes = notes;
    }
}
