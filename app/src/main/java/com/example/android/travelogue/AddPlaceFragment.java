package com.example.android.travelogue;

import android.Manifest;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android.travelogue.data.PlacesDatabase;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

/*
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.location.places.PlaceLikelihoodBufferResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
*/


public class AddPlaceFragment extends Fragment {

    private static final String TAG = "AddPlaceFragment";
    private static final String[] LOCATION_PERMISSIONS = new String[] {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };
    private static final int REQUEST_LOCATION_PERMISSION = 2;

    private OnFragmentInteractionListener mListener;

    private FusedLocationProviderClient client;

    /*
    // TODO Declare Firebase DB reference
    private DatabaseReference database;
    */

    private String newPlaceName;
    private String newPlaceLocation;
    private String newPlaceNotes;
    private View addPlaceView;

    private boolean gotNewLocation;
    private Location newLocation;

    public AddPlaceFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        addPlaceView = inflater.inflate(R.layout.fragment_add_place, container, false);

        Log.d(TAG, "onCreateView for AddPlaceFragment is running");

        gotNewLocation = false;

        /*
        // TODO Set reference to Firebase database
        database = FirebaseDatabase.getInstance().getReference();
        */

        // TODO Get current place
        /*
        Task<PlaceLikelihoodBufferResponse> placeResult = mPlaceDetectionClient.getCurrentPlace(null);
        placeResult.addOnCompleteListener(new OnCompleteListener<PlaceLikelihoodBufferResponse>() {
            @Override
            public void onComplete(@NonNull Task<PlaceLikelihoodBufferResponse> task) {
                PlaceLikelihoodBufferResponse likelyPlaces = task.getResult();
                for (PlaceLikelihood placeLikelihood : likelyPlaces) {
                    Log.i(TAG, String.format("Place '%s' has likelihood: %g",
                            placeLikelihood.getPlace().getName(),
                            placeLikelihood.getLikelihood()));
                }
                likelyPlaces.release();
            }
        });
        */


        Button button = addPlaceView.findViewById(R.id.submit_button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                // TODO replace with Firebase database write function

                EditText editPlaceName = (EditText) addPlaceView.findViewById(R.id.place_name_input);
                newPlaceName = editPlaceName.getText().toString();

                // TODO Replace with Take Current Position widget
                EditText editPlaceLocation = (EditText) addPlaceView.findViewById(R.id.place_location_input);
                newPlaceLocation = editPlaceLocation.getText().toString();

                EditText editPlaceNotes = (EditText) addPlaceView.findViewById(R.id.place_notes_input);
                newPlaceNotes = editPlaceNotes.getText().toString();

                // Toast for when button is clicked
                // TODO Remove toast when submit button functionality is finished
                Toast.makeText(getContext(), "You have clicked the Submit button!", Toast.LENGTH_SHORT).show();

                Log.d(TAG, "Submit button clicked, fields added to database.");

                ContentValues values = new ContentValues();
                values.put(PlacesDatabase.PlacesDatabaseEntry.COLUMN_PLACE_NAME, newPlaceName);
                values.put(PlacesDatabase.PlacesDatabaseEntry.COLUMN_PLACE_LOCATION, newPlaceLocation);
                values.put(PlacesDatabase.PlacesDatabaseEntry.COLUMN_PLACE_NOTES, newPlaceNotes);
                values.put(PlacesDatabase.PlacesDatabaseEntry.COLUMN_PLACE_LATITUDE, newLocation.getLatitude());
                values.put(PlacesDatabase.PlacesDatabaseEntry.COLUMN_PLACE_LONGITUDE, newLocation.getLongitude());
                values.put(PlacesDatabase.PlacesDatabaseEntry.COLUMN_PLACE_TIMESTAMP, newLocation.getTime());
                Log.d(TAG, "row saved consists of " + newPlaceName + ", " + newPlaceLocation + ", " + newPlaceNotes);

                // Insert a new row with values
                Uri contentUri = Uri.parse("content://" + PlacesDatabase.AUTHORITY + "/" + PlacesDatabase.PLACES_DATABASE_PATH);
                Uri returnedUri = getActivity().getContentResolver().insert(contentUri, values);
                Log.d(TAG, "Finished insert for " + returnedUri);

                // Get the id of the new record
                int newPlaceId = (int) ContentUris.parseId(returnedUri);

                // TODO Write data to Firebase Realtime Database
                // writeNewPlace(newPlaceName, newPlaceLocation, newPlaceNotes);

                // TODO Launch intent to open PlaceListActivity
                mListener.onFragmentInteraction(new Place(newPlaceId, newPlaceName, newPlaceLocation, newPlaceNotes,
                        newLocation.getLatitude(), newLocation.getLongitude(), (int) newLocation.getTime()));

                // TODO Add Snackbar message notifying user of data saved
            }
        });

        Log.d(TAG, "Get the location of this place");
        // TODO this returns a Location class which is what should be saved in Place
        if (hasLocationPermission()) {
            client = LocationServices.getFusedLocationProviderClient(getActivity());
            client.getLastLocation()
                    .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                Log.i(TAG, "Got location " + location);
                                newLocation = location;
                                gotNewLocation = true;
                            }
                        }
                    });
        }
        else {
            requestPermissions(LOCATION_PERMISSIONS, REQUEST_LOCATION_PERMISSION);
        }

        return addPlaceView;
    }

    /*
    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction();
        }
    }
    */

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
        // void onFragmentInteraction(Uri uri);
        void onFragmentInteraction(Place place);
    }

    /*
    private void writeNewPlace(String name, String location, String notes) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("/places/");

        Place place = new Place(name, location, notes);

        DatabaseReference pushedRef = myRef.push();
        pushedRef.setValue(place);
    }
    */

    // PULL REQUEST 2: get location or image from gallery
    private boolean hasLocationPermission() {
        int result = ContextCompat.checkSelfPermission(getActivity(), LOCATION_PERMISSIONS[0]);
        return result == PackageManager.PERMISSION_GRANTED;
    }
}
