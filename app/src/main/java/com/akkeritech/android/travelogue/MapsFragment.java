package com.akkeritech.android.travelogue;

import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by jordanhaynes on 10/21/19.
 */

public class MapsFragment extends SupportMapFragment {

    private static final String TAG = "MapsFragment";

    // MapView mMapView;
    private GoogleMap mMap;
    Place place;

    private PlaceDetailViewFragment.OnFragmentInteractionListener mListener;

    public void setDetails(Place place) {
        this.place = place;
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        Log.d(TAG, "MapsFragment has been created");


        getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;
                double placeLat = place.placeLatitude;
                double placeLong = place.placeLongitude;

                // Add a marker in Sydney, Australia, and move the camera.
                LatLng sydney = new LatLng(placeLat, placeLong);
                mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

                updateUI(placeLat, placeLong);
            }
        });
    }

    // Implementation to zoom in on the map
    private void updateUI(double Lat, double Long) {
        if (mMap == null) {
            //if (mMap == null || mMapImage == null) {
            return;
        }

        // LatLng itemPoint = new LatLng(mMapItem.getLat(), mMapItem.getLon());
        LatLng myPoint = new LatLng(Lat, Long);

        LatLngBounds bounds = new LatLngBounds.Builder()
                //.include(itemPoint)
                .include(myPoint)
                .build();

        int margin = getResources().getDimensionPixelSize(R.dimen.map_inset_margin);
        CameraUpdate update = CameraUpdateFactory.newLatLngBounds(bounds, margin);
        mMap.animateCamera(update);
    }
}
