package com.akkeritech.android.travelogue;

import android.app.Application;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import java.io.File;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

public class PlaceDetailViewModel extends AndroidViewModel {
    public MutableLiveData<Place> thePlace = new MutableLiveData<Place>();

    private static final String TAG = "PlaceViewModel";

    public PlaceDetailViewModel(@NonNull Application application) {
        super(application);
    }

    public void setCurrentPlace(Place place) {
        thePlace.setValue(place);
    }

    public void insertPhoto(Place place, File photoFile) {
        PlaceRepository.getInstance(getApplication()).insertPhoto(place, photoFile);
        thePlace.setValue(place);
    }
}
