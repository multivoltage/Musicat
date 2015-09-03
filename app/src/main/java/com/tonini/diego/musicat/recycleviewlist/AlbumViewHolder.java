package com.tonini.diego.musicat.recycleviewlist;

import android.content.Context;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.tonini.diego.musicat.R;
import com.tonini.diego.musicat.Utils;
import com.tonini.diego.musicat.entity.Album;
import com.tonini.diego.musicat.events.EventClick;

import de.greenrobot.event.EventBus;

/**
 * ViewHolder used for AllSongFragment
 */
public class AlbumViewHolder extends AGenericViewHolder<Album> {

    private int colorSecondary;

    public AlbumViewHolder(View view, Context context) {
        super(view, context);
        colorSecondary = Utils.getSecondaryColor(mContext);
    }

    @Override
    public void bindItem(final Album album, boolean isHeader) {
        if (firstTitle != null)
            firstTitle.setText(isHeader ? album.getName().substring(0,1) : album.getName());

        if(!isHeader) {
            if (secondTitle != null)
                secondTitle.setText(album.getAuthor());


            if(imageView !=null)
                loadPicasso(imageView, album.getAlmbumUri());
        } else {
            if(firstTitle!=null)
                firstTitle.setTextColor(colorSecondary);
        }

        setUpPopUpMenu(R.menu.popup_menu_album,album);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new EventClick(LoaderRecycleAsynk.Type.ALBUM).withAlbum(album));
            }
        });
    }

    @Override
    protected void menuItemChosed(MenuItem menuItem, Album album) {
        Toast.makeText(mContext,menuItem.getTitle(),Toast.LENGTH_SHORT).show();
    }


}
