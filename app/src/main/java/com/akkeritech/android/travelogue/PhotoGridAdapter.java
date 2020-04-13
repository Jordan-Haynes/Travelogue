package com.akkeritech.android.travelogue;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PhotoGridAdapter extends RecyclerView.Adapter<PhotoGridAdapter.PhotoGridHolder> {

    private static final String TAG = "PhotoGridAdapter";
    private static List<String> mPhotoPaths;

    public PhotoGridAdapter(List<String> paths) {
        mPhotoPaths = paths;
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

    static final class PhotoGridHolder extends RecyclerView.ViewHolder {
        private final ImageView mImageView;
        private int position;

        public PhotoGridHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.photo_grid_item, parent, false));
            mImageView = itemView.findViewById(R.id.photo_grid_item);
        }

        public void bind(int position) {
            this.position = position;
            String imageData = mPhotoPaths.get(position);
            Glide.with(itemView)
                    .load(imageData)
                    .centerCrop()
                    .into(mImageView);
        }
    }
}
