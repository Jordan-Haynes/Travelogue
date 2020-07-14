package com.akkeritech.android.travelogue;

import android.Manifest;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.akkeritech.android.travelogue.data.PlacesDatabase;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

public class AddPlaceFragment extends Fragment {

    private static final String TAG = "AddPlaceFragment";
    private static final String[] LOCATION_PERMISSIONS = new String[] {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };
    private static final int REQUEST_LOCATION_PERMISSION = 2;

    private OnFragmentInteractionListener mListener;

    private FusedLocationProviderClient client;

    private String newPlaceName;
    private String newPlaceLocation;
    private String newPlaceNotes;
    private View addPlaceView;

    private boolean gotNewLocation;
    private Location newLocation;

    public AddPlaceFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        addPlaceView = inflater.inflate(R.layout.fragment_add_place, container, false);

        gotNewLocation = false;

        Button button = addPlaceView.findViewById(R.id.submit_button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                EditText editPlaceName = addPlaceView.findViewById(R.id.place_name_input);
                newPlaceName = editPlaceName.getText().toString();

                EditText editPlaceNotes = addPlaceView.findViewById(R.id.place_notes_input);
                newPlaceNotes = editPlaceNotes.getText().toString();

                if (newLocation == null) {
                } else {
                    ContentValues values = new ContentValues();
                    values.put(PlacesDatabase.PlacesDatabaseEntry.COLUMN_PLACE_NAME, newPlaceName);
                    values.put(PlacesDatabase.PlacesDatabaseEntry.COLUMN_PLACE_LOCATION, newPlaceLocation);
                    values.put(PlacesDatabase.PlacesDatabaseEntry.COLUMN_PLACE_NOTES, newPlaceNotes);
                    values.put(PlacesDatabase.PlacesDatabaseEntry.COLUMN_PLACE_LATITUDE, newLocation.getLatitude());
                    values.put(PlacesDatabase.PlacesDatabaseEntry.COLUMN_PLACE_LONGITUDE, newLocation.getLongitude());
                    values.put(PlacesDatabase.PlacesDatabaseEntry.COLUMN_PLACE_TIMESTAMP, newLocation.getTime());

                    Uri contentUri = Uri.parse("content://" + PlacesDatabase.AUTHORITY + "/" + PlacesDatabase.PLACES_DATABASE_PATH);
                    Uri returnedUri = getActivity().getContentResolver().insert(contentUri, values);

                    int newPlaceId = (int) ContentUris.parseId(returnedUri);

                    mListener.onFragmentInteraction(new Place(newPlaceId, newPlaceName, newPlaceLocation, newPlaceNotes,
                            newLocation.getLatitude(), newLocation.getLongitude(), (int) newLocation.getTime()));
                }
            }
        });

        Button cancelButton = addPlaceView.findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(new View.OnClickListener() {
              public void onClick(View v) {
                    getActivity().finish();
              }
        });

        if (hasLocationPermission()) {
            client = LocationServices.getFusedLocationProviderClient(getActivity());
            client.getLastLocation()
                    .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                newLocation = location;
                                gotNewLocation = true;

                                TextView currentLocation = addPlaceView.findViewById(R.id.currentLocation);
                                currentLocation.setText(Location.convert(newLocation.getLatitude(), Location.FORMAT_DEGREES) + "," +
                                        Location.convert(newLocation.getLongitude(), Location.FORMAT_DEGREES));
                            }
                        }
                    });
        }
        else {
            requestPermissions(LOCATION_PERMISSIONS, REQUEST_LOCATION_PERMISSION);
        }

        TextView currentLocation = addPlaceView.findViewById(R.id.currentLocation);
        if (newLocation == null) {
            currentLocation.setText(R.string.placeholder_current);
        } else {
            currentLocation.setText(newLocation.getLatitude() + ", " + newLocation.getLongitude());
        }

        return addPlaceView;
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

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Place place);
    }

    private boolean hasLocationPermission() {
        int result = ContextCompat.checkSelfPermission(getActivity(), LOCATION_PERMISSIONS[0]);
        return result == PackageManager.PERMISSION_GRANTED;
    }
}
