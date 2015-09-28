package com.tonini.diego.musicat.recycleviewlist;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.text.InputType;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.google.common.io.ByteStreams;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.Mp3File;
import com.squareup.picasso.Picasso;
import com.tonini.diego.musicat.Const;
import com.tonini.diego.musicat.MainActivity;
import com.tonini.diego.musicat.PlayerService;
import com.tonini.diego.musicat.R;
import com.tonini.diego.musicat.Utils;
import com.tonini.diego.musicat.entity.GoogleCoverLoader;
import com.tonini.diego.musicat.entity.ImageWebAdapterDialog;
import com.tonini.diego.musicat.entity.PlayList;
import com.tonini.diego.musicat.entity.PlayListManager;
import com.tonini.diego.musicat.entity.Track;
import com.tonini.diego.musicat.entity.TrackFinder;
import com.tonini.diego.musicat.events.EventClick;
import com.tonini.diego.musicat.events.EventCrud;
import com.tonini.diego.musicat.gcs.Item;

import org.apache.http.Header;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * ViewHolder used for AllSongFragment
 */
public class TrackViewHolder extends AGenericViewHolder<Track> {

    int colorSecondary;
    boolean load = false;
    private byte[]  arrayImageFile;

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
                        imageViewOver.getDrawable().setTint(mContext.getResources().getColor(android.R.color.darker_gray));
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
                loadPicasso(imageView,track.getArtUri());

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
            /*case R.id.addToQueueTrack:      addToQueue(track);
                break;*/
            case R.id.shareTrack:           shareTrack(track);
                break;
            case R.id.downloadCoverArt:     new FetchWebImagesAsynk(mContext,track).execute();
                break;
        }

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

    class FetchWebImagesAsynk extends AsyncTask<Void,Void,List<Item>> {

        private ProgressDialog dialog;
        private String q;
        private Context context;
        private Track track;

        public FetchWebImagesAsynk(Context context, Track track){
            // use title as query
            q = track.getTitle();
            this.context = context;
            this.track = track;
            dialog = new ProgressDialog(this.context);
        }
        protected void onPreExecute() {
            dialog.setMessage("Searching images");
            dialog.show();
        }
        @Override
        protected List<Item> doInBackground(Void... params) {
            return new GoogleCoverLoader(q).getUrlImage();
        }

        @Override
        protected void onPostExecute(final List<Item> list){
            dialog.dismiss();
            // set up listview i new material dialog
            new MaterialDialog.Builder(mContext)
                    .theme(Utils.getTheme(mContext)==Const.THEME_LIGHT ? Theme.LIGHT : Theme.DARK)
                    .title("Chose one pictures")
                    .adapter(new ImageWebAdapterDialog(mContext, R.layout.row_dialog_images, list), new MaterialDialog.ListCallback() {
                        @Override
                        public void onSelection(final MaterialDialog materialDialog, View view, int i, CharSequence charSequence) {
                            AsyncHttpClient client = new AsyncHttpClient();
                            client.get(list.get(i).getLink(), new FileAsyncHttpResponseHandler(context) {
                                @Override
                                public void onFailure(int statusCode, Header[] headers, Throwable throwable, File file) {
                                    Snackbar.make(itemView,"Error downloading image, check interner connection", Snackbar.LENGTH_LONG).show();
                                    materialDialog.dismiss();
                                }

                                @Override
                                public void onSuccess(int statusCode, Header[] headers, File file) {
                                    if (statusCode == 200 && file.exists()) {
                                        Picasso.with(mContext)
                                                .load(file)
                                                .resize(dimPixel,dimPixel)
                                                .into(imageView);

                                        Log.i(MainActivity.TAG, "set image downloaded");
                                        try {
                                            arrayImageFile = ByteStreams.toByteArray(new FileInputStream(file));
                                            saveNewMetadataMp3Agic(track);
                                            Snackbar.make(itemView,"Edit Cover Ok",Snackbar.LENGTH_SHORT).show();
                                        } catch (IOException e) {
                                            Log.e(MainActivity.TAG, e.toString());
                                            Snackbar.make(itemView, "Error while download image", Snackbar.LENGTH_LONG);
                                        }
                                    }
                                    materialDialog.dismiss();
                                }

                            });
                        }
                    })
                    .show();
        }

    }


    private void saveNewMetadataMp3Agic(Track track){

        try {

            final String RETAGGED_EXTENSION = ".ret";
            File originFile = new File(track.getTrackUri().toString());
            File retaggedFile = new File(track.getTrackUri().toString()+RETAGGED_EXTENSION);

            Mp3File mp3File = new Mp3File(new File(track.getTrackUri().toString()));
            if(mp3File.hasId3v2Tag()){
                ID3v2 tag = mp3File.getId3v2Tag();

                // user want to update image also
                if(arrayImageFile!=null){
                    Log.i(MainActivity.TAG, "want to update image also with !null file");

                    File folder = new File(Environment.getExternalStorageDirectory().getPath()+File.separator+"Musicat");
                    if(!folder.exists())
                        folder.mkdirs();

                    File tempFile = new File(Environment.getExternalStorageDirectory().getPath()+File.separator+"Musicat"+File.separator+"try.jpg");
                    tempFile.createNewFile();
                    FileOutputStream fos = new FileOutputStream(tempFile.getAbsolutePath());
                    fos.write(arrayImageFile);
                    fos.close();
                    tag.setAlbumImage(arrayImageFile, "image/jpeg");
                } else {
                    Log.i(MainActivity.TAG,"want to update image also with null file");
                }
                mp3File.save(retaggedFile.getAbsolutePath());
                originFile.delete();
                retaggedFile.renameTo(originFile);

                Log.i(MainActivity.TAG, "set metadata on tag v2 !=null");
            }

        } catch (Exception e) {
            Log.i(MainActivity.TAG,e.toString());
        }


    }

}
