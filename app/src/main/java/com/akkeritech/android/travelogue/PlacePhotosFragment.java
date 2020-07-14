package com.akkeritech.android.travelogue;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class PlacePhotosFragment extends Fragment {

    private static final String TAG = "PhotosFragment";

    private PhotoGridAdapter mPhotoGridAdapter;
    private RecyclerView mRecyclerView;
    private Place place;

    public void setDetails(Place place) {
        this.place = place;
    }

    private void updateUI() {
        if (mPhotoGridAdapter == null) {
            mPhotoGridAdapter = new PhotoGridAdapter(place.photos);
            mRecyclerView.setAdapter(mPhotoGridAdapter);
        } else {
            mPhotoGridAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_place_photos, container, false);

        mRecyclerView = view.findViewById(R.id.photo_grid);

        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3, RecyclerView.VERTICAL, false));

        updateUI();
        return view;
    }
}
