package com.akkeritech.android.travelogue;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.renderscript.RenderScript;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.io.File;

public class PlaceDetailViewFragment extends Fragment {

    private static final String TAG = "PlaceDetailViewFragment";

    Place place;

    private File photoFile = null;

    private ImageView mPhotoView;
    private LinearLayout mDetailView;

    private BottomSheetBehavior mBottomSheetBehavior;

    public PlaceDetailViewFragment() {
        place = null;
    }

    public void setDetails(Place place) {
        this.place = place;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_place_detail_view, container, false);

        mPhotoView = view.findViewById(R.id.place_photo);
        if (place.photos != null && place.photos.size() > 0) {
            String photoFile = place.photos.get(0);
            if (photoFile == null) {
                // TODO Display a default bitmap
            } else {
                Glide.with(view)
                        .load(photoFile)
                        .centerCrop()
                        .into(mPhotoView);
            }
        }
        mDetailView = view.findViewById(R.id.detail_card_view);

        TextView nameWidget = view.findViewById(R.id.place_name);
        nameWidget.setText(place.placeName);

        TextView locationWidget = view.findViewById(R.id.place_location);
        locationWidget.setText(Location.convert(place.placeLatitude, Location.FORMAT_DEGREES) + "," +
                Location.convert(place.placeLongitude, Location.FORMAT_DEGREES));

        TextView notesWidget = view.findViewById(R.id.place_notes);
        notesWidget.setText("Notes \n\n" + place.placeNotes);

        View bottomSheet = view.findViewById(R.id.bottom_sheet);

        mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        mBottomSheetBehavior.setPeekHeight(150);

        ViewTreeObserver viewTreeObserver = view.getViewTreeObserver();
        if (viewTreeObserver.isAlive()) {
            viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    int viewWidth = mPhotoView.getMeasuredWidth();
                    int viewHeight = mPhotoView.getMeasuredHeight();
                    frostCardView(mPhotoView, mDetailView);
                }
            });
        }

        return view;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    private void frostCardView(View parentView, View cardView) {
        if (getContext() == null)
            return;

        Bitmap image = Bitmap.createBitmap(parentView.getMeasuredWidth(),
                parentView.getMeasuredHeight(),
                Bitmap.Config.ARGB_4444); //reduce quality
        Canvas canvas = new Canvas(image);
        parentView.draw(canvas);

        Paint paint = new Paint();
        paint.setXfermode(
                new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        ColorFilter filter =
                new LightingColorFilter(0xFFFFFFFF, 0x00222222); // lighten
        paint.setColorFilter(filter);
        canvas.drawBitmap(image, 0, 0, paint);

        Matrix matrix = new Matrix();
        matrix.setScale(0.5f, 0.5f);
        Bitmap bitmap = Bitmap.createBitmap(image, (int) cardView.getX(), (int) cardView.getY(),
                cardView.getMeasuredWidth(), cardView.getMeasuredHeight(), matrix, true);

        if (bitmap != null) {
            ImageHelper.blurBitmapWithRenderscript(
                    RenderScript.create(getContext()),
                    bitmap);

            // Then display
            Drawable d = new BitmapDrawable(getResources(), bitmap);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                mDetailView.setBackground(d);
            }
        }
    }
}
