package com.akkeritech.android.travelogue;

import android.app.Application;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

public class PlaceListViewModel extends AndroidViewModel {
    public MutableLiveData<List<Place>> placesList = new MutableLiveData<List<Place>>();

    private static final String TAG = "PlaceListViewModel";

    public PlaceListViewModel(@NonNull Application application) {
        super(application);
    }

    public void refresh() {
        ArrayList<Place> tempPlaceList = PlaceRepository.getInstance(getApplication()).retrievePlaces();
        placesList.setValue(tempPlaceList);
    }
}
