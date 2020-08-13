package com.akkeritech.android.travelogue;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

public class AddPlaceViewModel extends AndroidViewModel {
    private static final String TAG = "PlaceListViewModel";

    private PlaceRepository repository;

    public AddPlaceViewModel(@NonNull Application application) {
        super(application);
        repository = new PlaceRepository(application);
    }

    public void insertPlace(Place place) {
        repository.insert(place);
    }
}
