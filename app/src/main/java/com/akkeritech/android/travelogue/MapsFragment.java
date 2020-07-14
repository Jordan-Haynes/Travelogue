package com.akkeritech.android.travelogue;

import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsFragment extends SupportMapFragment {

    private static final String TAG = "MapsFragment";

    private GoogleMap mMap;
    Place place;

    private PlaceDetailViewFragment.OnFragmentInteractionListener mListener;

    public void setDetails(Place place) {
        this.place = place;
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;
                double placeLat = place.placeLatitude;
                double placeLong = place.placeLongitude;

                LatLng sydney = new LatLng(placeLat, placeLong);
                mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

                updateUI(placeLat, placeLong);
            }
        });
    }

    private void updateUI(double Lat, double Long) {
        if (mMap == null) {
            return;
        }

        LatLng myPoint = new LatLng(Lat, Long);

        LatLngBounds bounds = new LatLngBounds.Builder()
                .include(myPoint)
                .build();

        int margin = getResources().getDimensionPixelSize(R.dimen.map_inset_margin);
        CameraUpdate update = CameraUpdateFactory.newLatLngBounds(bounds, margin);
        mMap.animateCamera(update);
    }
}
