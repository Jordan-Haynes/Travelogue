package com.example.android.travelogue;

import android.content.Context;
import android.database.Cursor;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class PlaceListViewFragment extends Fragment {

    private static final String TAG = "PlaceListViewFragment";

    private final List<String> placeList = new ArrayList<>();
    private final List<String> keyList = new ArrayList<>();

    private OnListFragmentInteractionListener mListener;
    private PlaceListAdapter placeListAdapter;

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
        RecyclerView recyclerView = (RecyclerView) view;

        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        placeListAdapter = new PlaceListAdapter();
        recyclerView.setAdapter(placeListAdapter);

        return view;
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
        void onListFragmentInteraction(String place, String location, String notes);
    }

    private class PlaceListHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView placeNameTextView;
        private int position;
        private String placeName;


        public PlaceListHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.fragment_place, parent, false));
            placeNameTextView = itemView.findViewById(R.id.content);
            itemView.setOnClickListener(this);
        }

        public void bind(int position) {
            this.position = position;

            placeName = placeList.get(position);
            placeNameTextView.setText(placeName);
        }

        @Override
        public void onClick(View view) {
            Log.d(TAG, "This has been clicked!");

            //TODO Make Query here?
            final String placeLocation = "Null";
            // String placeNotes = "Null";

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            String placeKey = keyList.get(position);
            DatabaseReference myRef = database.getReference("/places/" + placeKey);

            Log.d(TAG, "Database reference for onClick!");

            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String placeNotes = dataSnapshot.child("notes").getValue(String.class);
                    // placeList.add(place);
                    Log.d(TAG, "onClick for place: " + placeName + " notes: " + placeNotes);
                    // placeListAdapter.notifyDataSetChanged();

                    mListener.onListFragmentInteraction(placeName, placeLocation, placeNotes);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            // TODO Change text in Toast
            // Toast.makeText(getActivity(), "Recipe title", Toast.LENGTH_SHORT).show();
            // Log.d(TAG, "Listed onClick: " + placeLocation + " and " + placeNotes);

            // mListener.onListFragmentInteraction(placeName, placeLocation, placeNotes);
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

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("/places");

        Log.d(TAG, "Database created!");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds: dataSnapshot.getChildren()) {
                    String placeKey = ds.getKey();
                    String place = ds.child("name").getValue(String.class);
                    Log.d(TAG, "Key for place " + place + " is: " + placeKey);

                    keyList.add(placeKey);
                    placeList.add(place);
                }
                Log.d(TAG, "Starting list of places " + placeList);
                placeListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Log.d(TAG, "The query is as follows: " + placeList);
    }
}
