package com.tonini.diego.musicat.custom;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.preference.SwitchPreference;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import com.tonini.diego.musicat.Const;
import com.tonini.diego.musicat.R;
import com.tonini.diego.musicat.Utils;

import org.w3c.dom.Text;

/**
 * Created by multivoltage on 9/27/15.
 */
public class ColorSwitchPreference extends SwitchPreference {


    public ColorSwitchPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ColorSwitchPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ColorSwitchPreference(Context context) {
        super(context);
    }

    @Override
    protected void onBindView(View view) {
        super.onBindView(view);

        TextView viewTitle = (TextView) view.findViewById(android.R.id.title);
        TextView summaary = (TextView) view.findViewById(android.R.id.summary);
        //viewTitle.setTextColor(Utils.getDarkerColor(Utils.getPrimaryColor(getContext()),0.8f));
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