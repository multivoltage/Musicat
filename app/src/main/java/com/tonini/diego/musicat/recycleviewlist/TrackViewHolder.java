package com.tonini.diego.musicat.recycleviewlist;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.squareup.picasso.Picasso;
import com.tonini.diego.musicat.Const;
import com.tonini.diego.musicat.EditActivity;
import com.tonini.diego.musicat.PlayerService;
import com.tonini.diego.musicat.R;
import com.tonini.diego.musicat.Utils;
import com.tonini.diego.musicat.custom.RoundedTransformation;
import com.tonini.diego.musicat.entity.LoadImageFileAsynk;
import com.tonini.diego.musicat.entity.PlayList;
import com.tonini.diego.musicat.entity.PlayListManager;
import com.tonini.diego.musicat.entity.Track;
import com.tonini.diego.musicat.entity.TrackFinder;
import com.tonini.diego.musicat.events.EventClick;
import com.tonini.diego.musicat.events.EventCrud;

import java.io.File;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * ViewHolder used for AllSongFragment
 */
public class TrackViewHolder extends AGenericViewHolder<Track> {

    int colorSecondary;
    boolean load = false;

    public TrackViewHolder(View view, Context context) {
        super(view, context);
        colorSecondary = Utils.getSecondaryColor(mContext);

    }

    @Override
    public void bindItem(final Track track, boolean isHeader) {

        if(imageViewOver!=null && firstTitle!=null && secondTitle!=null){
            imageViewOver.setImageResource(R.drawable.ic_menu_moreoverflow_holo_dark);
            // LIGHT
            switch (theme){
                case Const.THEME_LIGHT :
                    if(!isHeader)
                        firstTitle.setTextColor(mContext.getResources().getColor(android.R.color.black));
                    // LOLLIPOP OR NEW
                    if (Build.VERSION.SDK_INT >= 21){
                        imageViewOver.setImageDrawable(mContext.getDrawable(R.drawable.ic_menu_moreoverflow_material));
                    } else {
                        // PRE LOLLIPOP
                        imageViewOver.setImageResource(R.drawable.ic_menu_moreoverflow_holo_light);
                    }
                    break;

                case Const.THEME_DARK :
                    if(!isHeader)
                        firstTitle.setTextColor(mContext.getResources().getColor(android.R.color.white));
                    // LOLLIPOP
                    if (Build.VERSION.SDK_INT >= 21){
                        imageViewOver.setImageDrawable(mContext.getDrawable(R.drawable.ic_menu_moreoverflow_material));
                        imageViewOver.getDrawable().setTint(mContext.getResources().getColor(android.R.color.white));
                        // PRE LOLLIPOP
                    } else {
                        imageViewOver.setImageResource(R.drawable.ic_menu_moreoverflow_holo_dark);
                    }
                    break;

            }
        }

        if (firstTitle != null)
            firstTitle.setText(isHeader ? track.getTitle().substring(0, 1) : track.getTitle());

        if(!isHeader) {
            if (secondTitle != null)
                secondTitle.setText(track.getArtist());
            if(!load){
                load = true;
            }
            if(imageView !=null) {
                imageView.setImageResource(R.mipmap.unknow_cover);
                new LoadImageFileAsynk(new File(track.getTrackUri().toString()), mContext,2) {
                    @Override
                    protected void onPostExecute(File fileImage) {{
                            if(fileImage!= null && fileImage.exists()){
                                Picasso.with(mContext)
                                        .load(fileImage)
                                        .transform(new RoundedTransformation(90, 10))
                                        .resize(dimPixel, dimPixel)
                                        .centerInside()
                                        .placeholder(R.mipmap.unknow_cover)
                                        .into(imageView);
                            } else {
                                imageView.setImageResource(R.mipmap.unknow_cover);
                            }
                        }
                    }
                }.execute();
            }
        } else {
            if(firstTitle!=null)
                firstTitle.setTextColor(colorSecondary);
        }

        setUpPopUpMenu(R.menu.popup_menu_track, track);
        // play selected track
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new EventClick(LoaderRecycleAsynk.Type.TRACK).withTrack(track)); // used to notify fragment to show fabBack
                Intent intent = new Intent(mContext, PlayerService.class).setAction(Const.ACTION_PLAY_TRACK);
                intent.putExtra(Const.KEY_SELECTED_TRACK, track);
                mContext.startService(intent);
            }
        });
    }

    @Override
    protected void menuItemChosed(MenuItem menuItem,Track track) {
        switch (menuItem.getItemId()){
            case R.id.addToPlayListTrack :  addToPlayList(track);
                break;
            case R.id.addToQueueTrack:      addToQueue(track);
                break;
            case R.id.shareTrack:           shareTrack(track);
                break;
            case R.id.editTrack:            editMetadata(track);
                break;
        }

        Toast.makeText(mContext,menuItem.getTitle(),Toast.LENGTH_SHORT).show();
    }

    private void editMetadata(Track track){
        Intent intent = new Intent(mContext, EditActivity.class);
        intent.putExtra(Const.KEY_EDIT_TRACK,track);
        ((Activity)mContext).startActivityForResult(intent, Const.REQUEST_CODE_EDIT);
    }

    private void shareTrack(Track track){
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,track.getTitle()+" - "+track.getArtist());
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Listen this song man!");
        mContext.startActivity(sharingIntent);
    }
    private void addToQueue(Track track){
        mContext.startService(new Intent(mContext, PlayerService.class).setAction(Const.ACTION_ADD_TO_QUEUE).putExtra(Const.KEY_ADD_QUEUE_TRACK, track));
    }
    private void addToPlayList(final Track track){
        // want to load existing playlist and give possibility to create new one
        final List<PlayList> list = new TrackFinder(mContext).getPlayList();
        final CharSequence[] namesPlayList = new CharSequence[list.size()];
        for(int i=0;i<list.size();i++){
            namesPlayList[i] = list.get(i).getTitle();
        }

        new MaterialDialog.Builder(mContext)
                .theme(Theme.LIGHT)
                .title("Playlist")
                .items(namesPlayList)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog materialDialog, View view, int i, CharSequence charSequence) {
                        // want to save list[i].id
                        long idPlayList = list.get(i).getId();
                        int res = PlayListManager.saveTrackOnPlayList(mContext, track, idPlayList);
                        if (res != -1)
                            Snackbar.make(itemView, "Added " + track.getTitle() + " to " + list.get(i).getTitle(), Snackbar.LENGTH_LONG).show();
                        else
                            Snackbar.make(itemView, "I 'm sorry. Please contact me to fix error instead put negative review", Snackbar.LENGTH_LONG).show();
                    }
                })
                .neutralText("Create new Playlist")
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onNeutral(MaterialDialog dialog) {
                        // tap on "create new playlist
                        // create small Dialog
                        new MaterialDialog.Builder(mContext)
                                .theme(Theme.LIGHT)
                                .title("Create playlist")
                                .inputType(InputType.TYPE_CLASS_TEXT)
                                .input("Insert name", "My playlist", new MaterialDialog.InputCallback() {
                                    @Override
                                    public void onInput(MaterialDialog materialDialog, CharSequence titlePlayList) {
                                        // called when user click on OK button.
                                        // OK button is automatically setted
                                        String newPlayList = titlePlayList.toString();
                                        String start = newPlayList.substring(0, 1).toUpperCase();
                                        newPlayList = start.concat(newPlayList.substring(1));
                                        long newIdPlayLIst = PlayListManager.createPlayList(mContext, newPlayList, System.currentTimeMillis());
                                        if (newIdPlayLIst != -1) {
                                            int res = PlayListManager.saveTrackOnPlayList(mContext, track, newIdPlayLIst);
                                            if (res != -1) {
                                                Snackbar.make(itemView, "Added " + track.getTitle() + " to " + newPlayList, Snackbar.LENGTH_LONG).show();
                                                EventBus.getDefault().post(new EventCrud(Const.CRUD_ADD_TRACK_TO_PLAYLIST));
                                            } else
                                                Snackbar.make(itemView, "I 'm sorry. Please contact me to fix error instead put negative review", Snackbar.LENGTH_LONG).show();
                                        } else {
                                            Snackbar.make(itemView, "I 'm sorry. Please contact me to fix error instead put negative review", Snackbar.LENGTH_LONG).show();
                                        }
                                    }
                                })
                                .show();
                        super.onNeutral(dialog);
                    }
                })
                .show();
    }

}
