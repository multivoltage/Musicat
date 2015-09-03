package com.tonini.diego.musicat.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tonini.diego.musicat.R;


public class FloatingBubble extends RelativeLayout {

    public FloatingBubble(Context context) {
        super(context);
        init();
    }

    public FloatingBubble(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FloatingBubble(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.layout_bubble, this);
    }

    public void setFirstTitle(String firstTitle){
        TextView textView =(TextView) findViewById(R.id.tvTitleBubble);
        textView.setText(firstTitle);
        textView.setSelected(true);
    }
    public void setSubTitle(String subTitle){
        TextView textView = ((TextView) findViewById(R.id.tvSubTitleBubble));
        textView.setText(subTitle);
        textView.setSelected(true);
    }
    public void setImageRes(int imgRes){
        Picasso.with(getContext())
                .load(imgRes)
                .into((ImageView) findViewById(R.id.imgBubble));
    }

}
