package com.akkeritech.android.travelogue;

import android.content.res.Resources;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
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

    private PlaceDetailViewModel viewModel;
    private File photoFile = null;
    private ImageView mPhotoView;
    private LinearLayout mDetailView;
    private BottomSheetBehavior mBottomSheetBehavior;
    private TextView m_nameWidget;
    private TextView m_locationWidget;
    private TextView m_notesWidget;
    private View m_bottomSheet;

    public PlaceDetailViewFragment(PlaceDetailViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_place_detail_view, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Pass the view model in from the parent activity. See constructor.
        // viewModel = new ViewModelProvider(getActivity()).get(PlaceDetailViewModel.class);

        mPhotoView = view.findViewById(R.id.place_photo);
        mDetailView = view.findViewById(R.id.detail_card_view);
        m_nameWidget = view.findViewById(R.id.place_name);
        m_locationWidget = view.findViewById(R.id.place_location);
        m_notesWidget = view.findViewById(R.id.place_notes);
        m_bottomSheet = view.findViewById(R.id.bottom_sheet);

        mBottomSheetBehavior = BottomSheetBehavior.from(m_bottomSheet);
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

        viewModel.getCurrentPlace().observe(getViewLifecycleOwner(), new Observer<Place>() {
            @Override
            public void onChanged(Place place) {
                if (place != null) {
                    m_nameWidget.setText(place.placeName);
                    Resources res = getResources();
                    String locationText = String.format(res.getString(R.string.lat_long_text),
                            Location.convert(place.placeLatitude, Location.FORMAT_DEGREES),
                            Location.convert(place.placeLongitude, Location.FORMAT_DEGREES));
                    m_locationWidget.setText(locationText);
                    m_notesWidget.setText("Notes \n\n" + place.placeNotes);
                    if (place.placeReferencePhoto != null) {
                        Glide.with(view)
                                .load(place.placeReferencePhoto)
                                .centerCrop()
                                .into(mPhotoView);
                    }
            }
            }
        });
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
