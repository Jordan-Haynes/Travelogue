package com.akkeritech.android.travelogue;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;
import java.util.List;

public class Place implements Parcelable {
    public static final String PLACE_NAME = "PlaceName";

    public int    placeId;
    public String placeName;
    public String placeLocation;
    public String placeNotes;
    public double placeLatitude;
    public double placeLongitude;
    public int    placeTime;

    public ArrayList<String> photos = null;

    public Place() {
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

    public static final Parcelable.Creator<Place> CREATOR = new Parcelable.Creator<Place>() {
        public Place createFromParcel(Parcel in) {
            return new Place(in);
        }

        public Place[] newArray(int size) {
            return new Place[size];
        }
    };

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
