package com.akkeritech.android.travelogue;

import android.app.Application;
import android.database.Cursor;
import android.net.Uri;

import java.io.File;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class PlaceDetailViewModel extends AndroidViewModel {
    private static final String TAG = "PlaceViewModel";

    private LiveData<List<Photo>> m_photosList;
    private LiveData<Place> m_currentPlace;
    private PlaceRepository repository;

    public PlaceDetailViewModel(@NonNull Application application) {
        super(application);
        repository = new PlaceRepository(application);
    }

    public LiveData<List<Photo>> getAllPhotos() {
        if (m_photosList == null) {
            m_photosList = repository.getAllPhotos();
        }
        return m_photosList;
    }

    public LiveData<Place> getCurrentPlace() {
        if (m_currentPlace == null) {
            m_currentPlace = repository.getCurrentPlace();
        }
        return m_currentPlace;
    }

    public void setCurrentPlace(Place place) {
        repository.setPlace(place.placeId);
    }

    public void insertPhoto(Place place, String photoFile) {
        Photo photo = new Photo(0, place.placeId, photoFile);
        repository.insert(photo); // TODO does this update the photos list?
        repository.updatePhoto(place.placeId, photoFile); // TODO use menu to select this
    }

    public void deletePlace(Place place) {repository.deletePlace(place);}

    public void deletePlacePhotos(int placeId) {repository.deletePlacePhotos(placeId);}
}
