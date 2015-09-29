package com.multivoltage.musicat.custom;

import android.content.Context;
import android.preference.CheckBoxPreference;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import com.multivoltage.musicat.Const;
import com.multivoltage.musicat.R;
import com.multivoltage.musicat.Utils;

/**
 * Created by multivoltage on 9/27/15.
 */
public class ColorCheckBoxPreference extends CheckBoxPreference {

    public ColorCheckBoxPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public ColorCheckBoxPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ColorCheckBoxPreference(Context context) {
        super(context);
    }

    @Override
    protected void onBindView(View view) {
        super.onBindView(view);

        TextView title = (TextView) view.findViewById(android.R.id.title);
        TextView summary = (TextView) view.findViewById(android.R.id.summary);

        int primaryColor = Utils.getPrimaryColor(getContext());
        int secondaryCol = Utils.getSecondaryColor(getContext());

        title.setTextColor(primaryColor);
        title.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        int theme = Utils.getTheme(getContext());
        if(theme== Const.THEME_DARK){
            summary.setTextColor(getContext().getResources().getColor(R.color.grey_100));
        } else {
            summary.setTextColor(getContext().getResources().getColor(R.color.grey_800));
        }
    }
}
