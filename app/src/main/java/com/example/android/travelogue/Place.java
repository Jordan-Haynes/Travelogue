package com.example.android.travelogue;

import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jordanhaynes on 7/10/18.
 */

public class Place implements Parcelable {
    public static final String PLACE_NAME = "PlaceName";

    // TODO public String placeKey;
    public int    placeId;
    public String placeName;
    public String placeLocation;
    public String placeNotes;
    public double placeLatitude;
    public double placeLongitude;
    public int    placeTime;

    public List<String> photos = null;

    public Place() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Place(int id, String name, String location, String notes, double latitude, double longitude, int time) {
        this.placeId = id;
        this.placeName = name;
        this.placeLocation = location;
        this.placeNotes = notes;
        this.placeLatitude = latitude;
        this.placeLongitude = longitude;
        this.placeTime = time;
        photos = new ArrayList<>();
    }

    public void addPhoto(String photo) {
        if (photos == null) {
            photos = new ArrayList<>();
        }

        photos.add(photo);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(placeId);
        parcel.writeString(placeName);
        parcel.writeString(placeLocation);
        parcel.writeString(placeNotes);
        parcel.writeDouble(placeLatitude);
        parcel.writeDouble(placeLongitude);
        parcel.writeInt(placeTime);
        parcel.writeList(photos);
    }

    // All Parcelables must have a CREATOR that implements these two methods. This is
    // used to regenerate the object
    public static final Parcelable.Creator<Place> CREATOR = new Parcelable.Creator<Place>() {
        public Place createFromParcel(Parcel in) {
            return new Place(in);
        }

        public Place[] newArray(int size) {
            return new Place[size];
        }
    };

    // Constructor that takes a Parcel and gives you an object populated with it's values
    private Place(Parcel in) {
        placeId = in.readInt();
        placeName = in.readString();
        placeLocation = in.readString();
        placeNotes = in.readString();
        placeLatitude = in.readDouble();
        placeLongitude = in.readDouble();
        placeTime = in.readInt();
        if (photos == null) {
            photos = new ArrayList<>();
        }
        in.readList(photos, List.class.getClassLoader());
    }

    @Override
    public String toString() {
        return "Place{" +
                "placeId=" + placeId +
                ", placeName='" + placeName + '\'' +
                ", placeLocation='" + placeLocation + '\'' +
                ", placeNotes='" + placeNotes + '\'' +
                ", placeLatitude=" + placeLatitude +
                ", placeLongitude=" + placeLongitude +
                ", placeTime=" + placeTime +
                ", photos=" + photos +
                '}';
    }
}
