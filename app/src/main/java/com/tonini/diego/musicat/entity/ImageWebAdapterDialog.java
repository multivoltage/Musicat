package com.tonini.diego.musicat.entity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.tonini.diego.musicat.R;

import java.util.List;

/**
 * Created by Diego on 27/08/2015.
 */
public class ImageWebAdapterDialog extends ArrayAdapter<com.tonini.diego.musicat.gcs.Item> {

    private LayoutInflater inflater;
    private int resource;

    public ImageWebAdapterDialog(Context context, int resourceId, List objects) {
        super(context, resourceId, objects);
        resource = resourceId;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View v, ViewGroup parent) {

        // Recuperiamo l'oggetti che dobbiamo inserire a questa posizione
        com.tonini.diego.musicat.gcs.Item item = getItem(position);

        ViewHolder holder;

        if (v == null) {
            v = inflater.inflate(resource, parent, false);
            holder = new ViewHolder();
            holder.imgDialog = (ImageView) v.findViewById(R.id.imgDialog);

            v.setTag(holder);
        } else {
            holder = (ViewHolder) v.getTag();
        }

        Picasso.with(getContext())
                .load(item.getLink())
                .resize(150,150)
                .into(holder.imgDialog);

        return v;
    }

    private static class ViewHolder {
        ImageView imgDialog;
    }

}
