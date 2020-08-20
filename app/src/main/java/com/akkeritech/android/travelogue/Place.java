package com.akkeritech.android.travelogue;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "place_table")
public class Place implements Parcelable {
    public static final String PLACE_NAME = "PlaceName";

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "placeId")
    public int    placeId;

    @NonNull
    @ColumnInfo(name="placeName")
    public String placeName;

    @NonNull
    @ColumnInfo(name = "placeLocation")
    public String placeLocation;

    @NonNull
    @ColumnInfo(name = "placeNotes")
    public String placeNotes;

    @NonNull
    @ColumnInfo(name = "placeLatitude")
    public double placeLatitude;

    @NonNull
    @ColumnInfo(name = "placeLongitude")
    public double placeLongitude;

    @NonNull
    @ColumnInfo(name = "placeTime")
    public int    placeTime;

    @ColumnInfo(name = "placeReferencePhoto")
    public String placeReferencePhoto;

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
        parcel.writeString(placeReferencePhoto);
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
        placeReferencePhoto = in.readString();
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
                ", placeReferencePhoto=" + placeReferencePhoto +
                '}';
    }
}
