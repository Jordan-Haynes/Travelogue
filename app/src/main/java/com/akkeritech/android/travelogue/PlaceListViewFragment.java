package com.akkeritech.android.travelogue;

import android.content.Context;
import android.database.Cursor;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.akkeritech.android.travelogue.data.PlacesDatabase;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import static com.akkeritech.android.travelogue.data.PlacesDatabase.PlacesDatabaseEntry.COLUMN_PLACE_LATITUDE;
import static com.akkeritech.android.travelogue.data.PlacesDatabase.PlacesDatabaseEntry.COLUMN_PLACE_LOCATION;
import static com.akkeritech.android.travelogue.data.PlacesDatabase.PlacesDatabaseEntry.COLUMN_PLACE_LONGITUDE;
import static com.akkeritech.android.travelogue.data.PlacesDatabase.PlacesDatabaseEntry.COLUMN_PLACE_NAME;
import static com.akkeritech.android.travelogue.data.PlacesDatabase.PlacesDatabaseEntry.COLUMN_PLACE_NOTES;
import static com.akkeritech.android.travelogue.data.PlacesDatabase.PlacesDatabaseEntry.COLUMN_PLACE_TIMESTAMP;

public class PlaceListViewFragment extends Fragment {

    private static final String TAG = "PlaceListViewFragment";

    private final List<Place> placeList = new ArrayList<>();
    private final List<String> keyList = new ArrayList<>();

    private OnListFragmentInteractionListener mListener;
    private PlaceListAdapter placeListAdapter;
    private RecyclerView recyclerView;

    public PlaceListViewFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_place_list, container, false);

        retrievePlaces();

        Context context = view.getContext();
        recyclerView = (RecyclerView) view;

        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        placeListAdapter = new PlaceListAdapter();
        recyclerView.setAdapter(placeListAdapter);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
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

    private void updateUI() {
        if (placeListAdapter == null ) {
            placeListAdapter = new PlaceListAdapter();
            recyclerView.setAdapter(placeListAdapter);
        }
        else {
            placeListAdapter.notifyDataSetChanged();
        }
    }

    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(Place place);
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
            place = placeList.get(position);

            placeNameTextView.setText(place.placeName);

            if (place.photos != null && place.photos.size() > 0) {
                String photoFile = place.photos.get(0);
                if (photoFile == null) {
                } else {
                    Glide.with(itemView)
                            .load(photoFile)
                            .centerCrop()
                            .into(placeImageView);
                }
            }

            placeLocationTextView.setText(Location.convert(place.placeLatitude, Location.FORMAT_DEGREES) + "," +
                Location.convert(place.placeLongitude, Location.FORMAT_DEGREES));
        }

        @Override
        public void onClick(View view) {
            mListener.onListFragmentInteraction(place);
        }
    }

    private class PlaceListAdapter extends RecyclerView.Adapter<PlaceListHolder> {
        public PlaceListAdapter() {
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
            return placeList.size();
        }
    }

    private void retrievePlaces() {
        String[] projection = { PlacesDatabase.PlacesDatabaseEntry._ID, COLUMN_PLACE_NAME, COLUMN_PLACE_LOCATION, COLUMN_PLACE_NOTES,
                COLUMN_PLACE_LATITUDE, COLUMN_PLACE_LONGITUDE, COLUMN_PLACE_TIMESTAMP };
        Cursor cursor = null;
        try {
            cursor = getActivity().getContentResolver().query(PlacesDatabase.CONTENT_URI, projection, null, null, null);
            while (cursor.moveToNext()) {
                int    placeId = cursor.getInt(cursor.getColumnIndexOrThrow(PlacesDatabase.PlacesDatabaseEntry._ID));
                String placeName = cursor.getString(cursor.getColumnIndexOrThrow("placeName"));
                String placeLocation = cursor.getString(cursor.getColumnIndexOrThrow("placeLocation"));
                String placeNotes = cursor.getString(cursor.getColumnIndexOrThrow("placeNotes"));
                double placeLatitude = cursor.getDouble(cursor.getColumnIndexOrThrow("placeLatitude"));
                double placeLongitude = cursor.getDouble(cursor.getColumnIndexOrThrow("placeLongitude"));
                int    placeTime = cursor.getInt(cursor.getColumnIndexOrThrow("placeTime"));
                Place place = new Place(placeId, placeName, placeLocation, placeNotes, placeLatitude, placeLongitude, placeTime);

                retrievePhotos(place);

                placeList.add(place);
            }
        } catch (Exception e) {
            Log.e(TAG, "Error found: " + e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    private void retrievePhotos(Place place) {
        if (place != null) {
            Uri uri = Uri.parse("content://" + PlacesDatabase.AUTHORITY + "/" + PlacesDatabase.PHOTOS_DATABASE_PATH);
            String[] projection = {"photosJunctionDatabase.placeId", "photosJunctionDatabase.photoId", "photosDatabase.photoFilename"};
            String selection = "photosJunctionDatabase.placeId=?";
            String[] selectionArgs = {Integer.toString(place.placeId)};
            Cursor cursor = null;
            try {
                cursor = getActivity().getContentResolver().query(uri, projection, selection, selectionArgs, null);
                for (String column : cursor.getColumnNames()) {

                }
                while (cursor.moveToNext()) {
                    int placeId = cursor.getInt(cursor.getColumnIndexOrThrow("placeId"));
                    int photoId = cursor.getInt(cursor.getColumnIndexOrThrow("photoId"));
                    String photoFilename = cursor.getString(cursor.getColumnIndexOrThrow("photoFilename"));
                    place.addPhoto(photoFilename);
                }
            }
            catch (Exception e) {
                Log.e(TAG, "Database query error from photos database: " + e);
            }
            finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }

    }
}
