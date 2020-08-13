package com.akkeritech.android.travelogue;

import android.app.Application;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class PlaceListViewModel extends AndroidViewModel {
    private static final String TAG = "PlaceListViewModel";

    private LiveData<List<Place>> m_placesList;
    private PlaceRepository repository;

    public PlaceListViewModel(@NonNull Application application) {
        super(application);
        repository = new PlaceRepository(application);
        m_placesList = repository.getAllPlaces();
    }

    public LiveData<List<Place>> getAllPlaces() {
        return m_placesList;
    }
}
