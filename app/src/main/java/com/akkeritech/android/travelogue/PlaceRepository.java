package com.akkeritech.android.travelogue;

import android.app.Application;
import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class PlaceRepository {
    private static final String TAG = "PlaceRepository";
    private static Application app; // TODO remove this...

    private PlaceDao m_placeDao;
    private LiveData<List<Place>> m_allPlaces;
    private PhotoDao m_photoDao;
    private MutableLiveData<List<Photo>> m_allPhotos = new MutableLiveData<>();
    private MutableLiveData<Place> m_currentPlace = new MutableLiveData<>();

    // Note that in order to unit test the repository, you have to remove the Application
    // dependency.
    PlaceRepository(Application application) {
        PlaceRoomDatabase db = PlaceRoomDatabase.getDatabase(application);
        m_placeDao = db.placeDao();
        m_allPlaces = m_placeDao.getPlaces();
        m_photoDao = db.photoDao();
    }

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    LiveData<List<Place>> getAllPlaces() {
        return m_allPlaces;
    }
    LiveData<List<Photo>> getAllPhotos() { return m_allPhotos; }
    LiveData<Place> getCurrentPlace() { return m_currentPlace; }

    // You must call the following methods on a non-UI thread or your app will throw an exception.
    // Room ensures that there are not any long running operations on the main thread, blocking the UI.

    void insert(final Place place) {
        PlaceRoomDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                m_placeDao.insert(place);
            }
        });
    }

    void update(final Place place) {
        PlaceRoomDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                m_placeDao.update(place);
                m_currentPlace.postValue(place);
            }
        });
    }

    void insert(final Photo photo) {
        PlaceRoomDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                m_photoDao.insert(photo);
            }
        });
    }

    void setPlace(final int placeId) {
        PlaceRoomDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                Place place = m_placeDao.setPlace(placeId);
                m_currentPlace.postValue(place);
                List<Photo> photos = m_photoDao.getPhotos(placeId);
                m_allPhotos.postValue(photos);
            }
        });
    }

    void updatePhoto(final int placeId, final String photoFilename) {
        PlaceRoomDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                m_placeDao.updatePhoto(placeId, photoFilename);
                List<Photo> photos = m_photoDao.getPhotos(placeId);
                m_allPhotos.postValue(photos);
            }
        });
    }

    public void deletePlace(final Place place) {
        PlaceRoomDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                m_placeDao.delete(place);
            }
        });
    }

    public void deletePlacePhotos(final int placeId) {
        PlaceRoomDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                m_photoDao.deleteById(placeId);
            }
        });
    }
}
