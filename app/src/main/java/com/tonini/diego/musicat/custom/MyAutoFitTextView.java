package com.tonini.diego.musicat.custom;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import me.grantland.widget.AutofitTextView;

public class MyAutoFitTextView extends TextView {

    public MyAutoFitTextView(Context context) {
        super(context);
        setDefaultFont();
    }

    public MyAutoFitTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setDefaultFont();
    }

    public MyAutoFitTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setDefaultFont();
    }

    private void setDefaultFont() {
        Typeface font = Typeface.createFromAsset(getContext().getAssets(),
                "fonts/Roboto_Light.ttf");
        setTypeface(font);
    }
}
