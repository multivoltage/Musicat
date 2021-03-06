package com.multivoltage.musicat.recycleviewlist;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.design.widget.Snackbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.multivoltage.musicat.Const;
import com.multivoltage.musicat.R;
import com.multivoltage.musicat.Utils;
import com.multivoltage.musicat.entity.PlayList;
import com.multivoltage.musicat.entity.PlayListManager;
import com.multivoltage.musicat.events.EventClick;
import com.multivoltage.musicat.events.EventCrud;

import java.util.Random;

import de.greenrobot.event.EventBus;

/**
 * ViewHolder used for AllSongFragment
 */
public class PlayListViewHolder extends AGenericViewHolder<PlayList> {

    private TextView tvRound;
    private int[] mColorChoices;
    private int index = 1;
    private int colorSecondary;

    public PlayListViewHolder(View view, Context context) {
        super(view, context);
        mColorChoices=null;
        String[] color_array = mContext.getResources().getStringArray(R.array.default_color_choice_values);

        if (color_array!=null && color_array.length>0) {
            mColorChoices = new int[color_array.length];
            for (int i = 0; i < color_array.length; i++) {
                mColorChoices[i] = Color.parseColor(color_array[i]);
            }
        }

        tvRound = (TextView) view.findViewById(R.id.tvRound);
        colorSecondary = Utils.getSecondaryColor(context);
    }

    @Override
    public void bindItem(final PlayList playList, boolean isHeader) {
        if (firstTitle != null)
            firstTitle.setText(isHeader ? playList.getTitle().substring(0,1) : playList.getTitle());

        if(!isHeader) {

            tvRound.setText(playList.getTitle().substring(0, 1));
            tvRound.setBackgroundResource(R.drawable.shape_round_textview);
            GradientDrawable drawable = (GradientDrawable) tvRound.getBackground();
            drawable.setColor(mColorChoices[new Random().nextInt(mColorChoices.length)]);

            index++;
            if (secondTitle != null)
                secondTitle.setText("id: "+String.valueOf(playList.getId()));
        }  else {
            if(firstTitle!=null)
                firstTitle.setTextColor(colorSecondary);
        }

        setUpPopUpMenu(R.menu.popup_menu_playlist,playList);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new EventClick(LoaderRecycleAsynk.Type.PLAYLIST).withPlayList(playList)); // used to notify fragment to show fabBack
            }
        });
    }

    @Override
    protected void menuItemChosed(MenuItem menuItem,PlayList playList) {
        switch (menuItem.getItemId()){
            case R.id.deletePlayList:
                deletePlaylist(playList);
                break;
            case R.id.sharePlayList:
                break;
        }
    }

    private void deletePlaylist(final PlayList playList){
        new MaterialDialog.Builder(mContext)
                .theme(Utils.getTheme(mContext)== Const.THEME_LIGHT ? Theme.LIGHT : Theme.DARK)
                .title(mContext.getString(R.string.delete_playlist)+" " + playList.getTitle() + " ?")
                .positiveText(mContext.getString(R.string.confirm))
                .negativeText(mContext.getString(R.string.back))
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onNegative(MaterialDialog dialog) {

                    }

                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        int rowDeleted = PlayListManager.deletePlaylist(mContext, playList.getId());
                        if (rowDeleted > 0) {
                            Snackbar.make(tvRound, "Playlist " + playList.getTitle() + " "+mContext.getString(R.string.playlist_delete_ok), Snackbar.LENGTH_LONG).show();
                            EventBus.getDefault().post(new EventCrud(Const.CRUD_DELETE_PLAYLIST));
                        } else
                            Snackbar.make(tvRound, mContext.getString(R.string.playlist_create_err), Snackbar.LENGTH_LONG).show();

                    }
                }).show();
    }


}
