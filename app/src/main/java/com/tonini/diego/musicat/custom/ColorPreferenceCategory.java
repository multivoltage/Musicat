package com.tonini.diego.musicat.custom;

import android.content.Context;
import android.graphics.Typeface;
import android.preference.CheckBoxPreference;
import android.preference.PreferenceCategory;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import com.tonini.diego.musicat.Utils;

import org.w3c.dom.Text;

/**
 * Created by multivoltage on 9/27/15.
 */
public class ColorPreferenceCategory extends PreferenceCategory {


    public ColorPreferenceCategory(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ColorPreferenceCategory(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ColorPreferenceCategory(Context context) {
        super(context);
    }

    @Override
    protected void onBindView(View view) {
        super.onBindView(view);

        TextView title = (TextView) view.findViewById(android.R.id.title);

        int primaryColor = Utils.getPrimaryColor(getContext());

        title.setTextColor(primaryColor);
        title.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        title.setTypeface(null, Typeface.BOLD);
    }
}