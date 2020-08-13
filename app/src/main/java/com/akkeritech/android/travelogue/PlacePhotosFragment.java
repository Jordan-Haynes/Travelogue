package com.akkeritech.android.travelogue;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PlacePhotosFragment extends Fragment {

    private static final String TAG = "PhotosFragment";

    private PhotoGridAdapter mPhotoGridAdapter = new PhotoGridAdapter(new ArrayList<Photo>());
    private RecyclerView mRecyclerView;
    private PlaceDetailViewModel viewModel;

    PlacePhotosFragment(PlaceDetailViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_place_photos, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Pass the view model in from the parent activity. See constructor.
        // viewModel = new ViewModelProvider(getActivity()).get(PlaceDetailViewModel.class);

        mRecyclerView = view.findViewById(R.id.photo_grid);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3, RecyclerView.VERTICAL, false));
        mRecyclerView.setAdapter(mPhotoGridAdapter);

        viewModel.getAllPhotos().observe(this, new Observer<List<Photo>>() {
            @Override
            public void onChanged(List<Photo> photos) {
                if (photos != null && photos instanceof List) {
                    mRecyclerView.setVisibility(View.VISIBLE); // may not be needed
                    mPhotoGridAdapter.updatePhotoList(photos);
                }
            }
        });
    }

    public class PhotoGridAdapter extends RecyclerView.Adapter<PhotoGridAdapter.PhotoGridHolder> {

        private final String TAG = "PhotoGridAdapter";
        private ArrayList<Photo> mPhotoPaths;

        public PhotoGridAdapter(ArrayList<Photo> paths) {
            mPhotoPaths = paths;
        }

        public void updatePhotoList(List<Photo> updatedPhotoList) {
            mPhotoPaths.clear();
            mPhotoPaths.addAll(updatedPhotoList);
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public PhotoGridAdapter.PhotoGridHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
            return new PhotoGridHolder(inflater, viewGroup);
        }

        @Override
        public int getItemCount() {
            return mPhotoPaths.size();
        }

        @Override
        public void onBindViewHolder(@NonNull PhotoGridAdapter.PhotoGridHolder holder, int position) {
            holder.bind(position);
        }

        public class PhotoGridHolder extends RecyclerView.ViewHolder {
            private final ImageView mImageView;
            private int position;

            public PhotoGridHolder(LayoutInflater inflater, ViewGroup parent) {
                super(inflater.inflate(R.layout.photo_grid_item, parent, false));
                mImageView = itemView.findViewById(R.id.photo_grid_item);
            }

            public void bind(int position) {
                this.position = position;
                String imageData = mPhotoPaths.get(position).photoFilename;
                Glide.with(itemView)
                        .load(imageData)
                        .centerCrop()
                        .into(mImageView);
            }
        }
    }
}
