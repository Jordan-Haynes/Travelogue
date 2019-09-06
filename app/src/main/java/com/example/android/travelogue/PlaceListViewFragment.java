package com.example.android.travelogue;

import android.content.Context;
import android.database.Cursor;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.travelogue.data.PlacesDatabase;

import java.util.ArrayList;
import java.util.List;

import static com.example.android.travelogue.data.PlacesDatabase.PlacesDatabaseEntry.COLUMN_PLACE_LATITUDE;
import static com.example.android.travelogue.data.PlacesDatabase.PlacesDatabaseEntry.COLUMN_PLACE_LOCATION;
import static com.example.android.travelogue.data.PlacesDatabase.PlacesDatabaseEntry.COLUMN_PLACE_LONGITUDE;
import static com.example.android.travelogue.data.PlacesDatabase.PlacesDatabaseEntry.COLUMN_PLACE_NAME;
import static com.example.android.travelogue.data.PlacesDatabase.PlacesDatabaseEntry.COLUMN_PLACE_NOTES;
import static com.example.android.travelogue.data.PlacesDatabase.PlacesDatabaseEntry.COLUMN_PLACE_TIMESTAMP;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class PlaceListViewFragment extends Fragment {

    private static final String TAG = "PlaceListViewFragment";

    private final List<Place> placeList = new ArrayList<>();
    private final List<String> keyList = new ArrayList<>();

    private OnListFragmentInteractionListener mListener;
    private PlaceListAdapter placeListAdapter;
    private RecyclerView recyclerView;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
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

        Log.d(TAG, "onCreateView is running");

        retrievePlaces();

        Context context = view.getContext();
        // LATEST RecyclerView recyclerView = (RecyclerView) view;
        recyclerView = (RecyclerView) view;

        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        placeListAdapter = new PlaceListAdapter();
        recyclerView.setAdapter(placeListAdapter);

        return view;
    }

    // LATEST
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

    // LATEST
    private void updateUI() {
        if (placeListAdapter == null ) {
            placeListAdapter = new PlaceListAdapter();
            recyclerView.setAdapter(placeListAdapter);
        }
        else {
            // TODO Did the data set change? Was it added to the place list?
            placeListAdapter.notifyDataSetChanged();
            // or notifyItemChanged(int)
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(Place place);
    }

    private class PlaceListHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView placeIdTextView;
        private final TextView placeNameTextView;
        private final TextView placeLocationTextView;
        private int position;
        private Place place;

        public PlaceListHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.fragment_place, parent, false));
            placeIdTextView = itemView.findViewById(R.id.position);
            placeNameTextView = itemView.findViewById(R.id.content);
            placeLocationTextView = itemView.findViewById(R.id.location);
            itemView.setOnClickListener(this);
        }

        public void bind(int position) {
            this.position = position;
            place = placeList.get(position);

            // placeIdTextView.setText(Integer.toString(this.position+1));
            placeIdTextView.setText(Integer.toString(place.placeId));
            placeNameTextView.setText(place.placeName);
            /*
            Location location = new Location("");
            location.setLatitude(place.placeLatitude);
            location.setLongitude(place.placeLongitude);
            placeLocationTextView.setText(location.toString());
            */
            placeLocationTextView.setText(Location.convert(place.placeLatitude, Location.FORMAT_DEGREES) + "," +
                Location.convert(place.placeLongitude, Location.FORMAT_DEGREES));
        }

        @Override
        public void onClick(View view) {
            Log.d(TAG, "This has been clicked!");

            mListener.onListFragmentInteraction(place);
        }
    }

    // RecyclerView Adapter
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
            Log.d(TAG, "Number of place = " + placeList.size());
            return placeList.size();
        }
    }

    // Retrieve the list of places from the database
    private void retrievePlaces() {
        // Query the database to get a list of places

        // Uri contentUri = Uri.parse("content://" + PlacesDatabase.AUTHORITY + "/" + PlacesDatabase.BASE_PATH);
        // String[] projection = { "placeName", "placeLocation", "placeNotes" };
        String[] projection = { PlacesDatabase.PlacesDatabaseEntry._ID, COLUMN_PLACE_NAME, COLUMN_PLACE_LOCATION, COLUMN_PLACE_NOTES,
                COLUMN_PLACE_LATITUDE, COLUMN_PLACE_LONGITUDE, COLUMN_PLACE_TIMESTAMP };
        Cursor cursor = null;
        try {
            // TODO use the alias names for column names
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
                Log.d(TAG, "Retrieved place name: " + place.placeName);

                // Retrieve the photos for this place
                retrievePhotos(place);

                // Finally add the place to the list of places
                placeList.add(place);
                Log.d(TAG, "Add place to list: " + place);
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
        // Now query to get the list of images
        if (place != null) {
            Uri uri = Uri.parse("content://" + PlacesDatabase.AUTHORITY + "/" + PlacesDatabase.PHOTOS_DATABASE_PATH);
            String[] projection = {"photosJunctionDatabase.placeId", "photosJunctionDatabase.photoId", "photosDatabase.photoFilename"};
            String selection = "photosJunctionDatabase.placeId=?";
            String[] selectionArgs = {Integer.toString(place.placeId)};
            Cursor cursor = null;
            try {
                cursor = getActivity().getContentResolver().query(uri, projection, selection, selectionArgs, null);
                for (String column : cursor.getColumnNames()) {
                    Log.d(TAG, "Cursor column name " + column);
                }
                while (cursor.moveToNext()) {
                    /* was including table name with column, but this was causing problems
                    int placeId = cursor.getInt(cursor.getColumnIndexOrThrow("photosJunctionDatabase.placeId"));
                    int photoId = cursor.getInt(cursor.getColumnIndexOrThrow("photosJunctionDatabase.photoId"));
                    String photoFilename = cursor.getString(cursor.getColumnIndexOrThrow("photosDatabase.photoFilename"));
                    */
                    int placeId = cursor.getInt(cursor.getColumnIndexOrThrow("placeId"));
                    int photoId = cursor.getInt(cursor.getColumnIndexOrThrow("photoId"));
                    String photoFilename = cursor.getString(cursor.getColumnIndexOrThrow("photoFilename"));
                    Log.d(TAG, "Retrieved photo for place id: " + placeId + " and photo id: " + photoId + " and filename: " + photoFilename);
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
