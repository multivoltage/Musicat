package com.tonini.diego.musicat.entity;

import android.graphics.Camera;
import android.graphics.Matrix;
import android.view.View;

import com.ToxicBakery.viewpager.transforms.DefaultTransformer;

public class RotatePageTransformer extends DefaultTransformer {
    private static final Matrix OFFSET_MATRIX = new Matrix();
    private static final Camera OFFSET_CAMERA = new Camera();
    private static final float[] OFFSET_TEMP_FLOAT = new float[2];
    @Override
    public void transformPage(View page, float position) {
       /*if (position > -1 && position < 1) {
            CircleImageView imageView = (CircleImageView) page.findViewById(R.id.profile_image_header);
            if (imageView != null) {
                imageView.setRotation(180 * position);
            }

            //FloatingActionButton fab = (FloatingActionButton) ((CoordinatorLayout) page.getParent().getParent()).findViewById(R.id.fltPP);
       }*/
    }

}
