package com.multivoltage.musicat.custom;

import android.content.Context;
import android.preference.ListPreference;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import com.multivoltage.musicat.Const;
import com.multivoltage.musicat.R;
import com.multivoltage.musicat.Utils;

/**
 * Created by multivoltage on 9/28/15.
 */
public class ColorListPreference extends ListPreference {

    public ColorListPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ColorListPreference(Context context) {
        super(context);
    }

    @Override
    protected void onBindView(View view) {
        super.onBindView(view);

        TextView viewTitle = (TextView) view.findViewById(android.R.id.title);
        TextView summaary = (TextView) view.findViewById(android.R.id.summary);

        int theme = Utils.getTheme(getContext());

        viewTitle.setTextColor(Utils.getPrimaryColor(getContext()));
        viewTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        if(theme== Const.THEME_DARK){
            summaary.setTextColor(getContext().getResources().getColor(R.color.grey_100));
        } else {
            summaary.setTextColor(getContext().getResources().getColor(R.color.grey_800));
        }

    }
}
