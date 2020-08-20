package com.akkeritech.android.travelogue;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class PlaceListViewFragment extends Fragment {

    private static final String TAG = "PlaceListViewFragment";

    private OnListFragmentInteractionListener mListener;
    private PlaceListAdapter placeListAdapter = new PlaceListAdapter(new ArrayList<Place>());
    private RecyclerView recyclerView;
    private PlaceListViewModel viewModel;

    public PlaceListViewFragment() {
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == AddPlaceActivity.EDIT_PLACE_REQUEST_CODE) {
                Log.d(TAG, "Result of edit place are OK");
            }
            else if (requestCode == AddPlaceActivity.NEW_PLACE_REQUEST_CODE) {
                Log.d(TAG, "Result of new place are OK");
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_place_list, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(PlaceListViewModel.class);

        recyclerView = (RecyclerView) view;
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setAdapter(placeListAdapter);

        viewModel.getAllPlaces().observe(this, new Observer<List<Place>>() {
            @Override
            public void onChanged(List<Place> places) {
                if (places != null && places instanceof List) {
                    recyclerView.setVisibility(View.VISIBLE); // may not be needed
                    placeListAdapter.updatePlacesList(places);
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(Place place);
    }

    private class PlaceListAdapter extends RecyclerView.Adapter<PlaceListAdapter.PlaceListHolder> {
        private ArrayList<Place> placesList;

        public PlaceListAdapter(ArrayList<Place> placesList) {
            this.placesList = placesList;
        }

        public void updatePlacesList(List<Place> updatedPlacesList) {
            placesList.clear();
            placesList.addAll(updatedPlacesList);
            notifyDataSetChanged();
        }

        @Override
        public PlaceListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new PlaceListHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(PlaceListHolder holder, int position) {
            holder.bind(position);
        }

        @Override
        public int getItemCount() {
            return placesList.size();
        }

        private class PlaceListHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            private final TextView placeNameTextView;
            private final TextView placeLocationTextView;
            private final ImageView placeImageView;
            private int position;
            private Place place;

            public PlaceListHolder(LayoutInflater inflater, ViewGroup parent) {
                super(inflater.inflate(R.layout.fragment_place, parent, false));
                placeNameTextView = itemView.findViewById(R.id.content);
                placeLocationTextView = itemView.findViewById(R.id.content_location);
                placeImageView = itemView.findViewById(R.id.place_list_photo);
                itemView.setOnClickListener(this);
            }

            public void bind(int position) {
                this.position = position;
                place = placesList.get(position);

                placeNameTextView.setText(place.placeName);

                if (place.placeReferencePhoto != null) {
                    Glide.with(itemView)
                            .load(place.placeReferencePhoto)
                            .centerCrop()
                            .into(placeImageView);
                }

                Resources res = getResources();
                String locationText = String.format(res.getString(R.string.lat_long_text),
                        Location.convert(place.placeLatitude, Location.FORMAT_DEGREES),
                        Location.convert(place.placeLongitude, Location.FORMAT_DEGREES));
                placeLocationTextView.setText(locationText);
            }

            @Override
            public void onClick(View view) {
                mListener.onListFragmentInteraction(place);
            }
        }
    }
}
