package com.example.android.travelogue;

import android.content.Context;
import android.database.Cursor;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.travelogue.data.PlacesDatabase;


public class PlaceDetailViewFragment extends Fragment {

    private static final String TAG = "PlaceDetailViewFragment";

    private OnFragmentInteractionListener mListener;

    // TODO Replace with Place class
    String placeName;
    // String placeLocation;
    Double placeLong;
    Double placeLat;
    String placeNotes;

    private ImageButton mPhotoButton;
    private ImageView mPhotoView;

    public PlaceDetailViewFragment() {
    }

    public void setDetails(String name, Double longitude, Double latitude, String notes) {
        this.placeName = name;
        this.placeLong = longitude;
        this.placeLat = latitude;
        this.placeNotes = notes;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_place_detail_view, container, false);

        Log.d(TAG, "Running onCreateView now");

        Log.d(TAG, "Details are " + placeName + ", " + placeLong + ", " + placeLat + ", " + placeNotes);

        // TODO Retrieve details

        if (placeLong == null) {
            retrieveDetails();
        }

        // TODO Add instance variables for camera intent
        mPhotoButton = (ImageButton) view.findViewById(R.id.place_camera);
        mPhotoView = (ImageView) view.findViewById(R.id.place_photo);

        TextView nameWidget = (TextView) view.findViewById(R.id.place_name);
        nameWidget.setText(placeName);

        TextView locationWidget = (TextView) view.findViewById(R.id.place_location);
        locationWidget.setText(placeLong.toString());

        TextView locationWidget = (TextView) view.findViewById(R.id.place_location);
        locationWidget.setText(placeLat.toString());

        TextView notesWidget = (TextView) view.findViewById(R.id.place_notes);
        notesWidget.setText(placeNotes);

        return view;
    }


    private void retrieveDetails() {
        // TODO Make query to database to get a specific row
        String[] projection = { "placeName", "placeLongitude", "placeLatitude", "placeNotes" };

        Uri contentUri = Uri.parse("content://" + PlacesDatabase.AUTHORITY + "/" + PlacesDatabase.BASE_PATH);

        // TODO Define the selection
        String selection =  "placeName = " + placeName;

        Cursor cursor = null;
        try {

            cursor = getActivity().getContentResolver().query(contentUri, projection, selection, null, null);

            while (cursor.moveToNext()) {
                String place = cursor.getString(cursor.getColumnIndexOrThrow("placeName"));

                // placeList.add(place);
            }
        } catch (Exception e) {
            Log.e(TAG, "Error found: " + e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        // Log.d(TAG, "The query is as follows: " + placeList);
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
