package com.akkeritech.android.travelogue;

import android.app.Activity;
import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

public class AddPlaceViewModel extends AndroidViewModel {
    private static final String TAG = "PlaceListViewModel";

    public AddPlaceViewModel(@NonNull Application application) {
        super(application);
    }

    public int insertPlace(Place place) {
        return PlaceRepository.getInstance(getApplication()).insertPlace(place);
    }
}
