package com.tonini.diego.musicat;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.google.common.io.ByteStreams;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.melnykov.fab.FloatingActionButton;
import com.mpatric.mp3agic.ID3v1;
import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.Mp3File;
import com.squareup.picasso.Picasso;
import com.tonini.diego.musicat.entity.GoogleCoverLoader;
import com.tonini.diego.musicat.entity.ImageWebAdapterDialog;
import com.tonini.diego.musicat.entity.Track;
import com.tonini.diego.musicat.gcs.Item;

import org.apache.http.Header;
import org.cmc.music.metadata.MusicMetadata;
import org.cmc.music.metadata.MusicMetadataSet;
import org.cmc.music.myid3.MyID3;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class EditActivity extends AppCompatActivity implements View.OnClickListener{

    private ScrollView scrollView;
    private EditText editTitle,editAlbum,editArtist;
    private ImageView imgCover;
    private FloatingActionButton fabEditConfirm;
    private Track track = null;
    static final String TAG = "edit.TAG";
    static final String RETAGGED_EXTENSION = ".ret";
    private byte[]  arrayImageFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        track = getIntent().getParcelableExtra(Const.KEY_EDIT_TRACK);

        imgCover = (ImageView) findViewById(R.id.imgCover);
        editTitle = (EditText) findViewById(R.id.editTitle);
        editAlbum = (EditText) findViewById(R.id.editAlbum);
        editArtist= (EditText) findViewById(R.id.editArtist);
        fabEditConfirm = (FloatingActionButton) findViewById(R.id.fabEditConfirm);
        scrollView = (ScrollView) findViewById(R.id.scrollView);
        fabEditConfirm.setOnClickListener(this);

        imgCover.setOnClickListener(this);

        ListView l;
        ImageWebAdapterDialog ad;


        initData();
    }

    private void initData(){
        if(track!=null){
            try {
                Mp3File mp3File = new Mp3File(new File(track.getTrackUri().toString()));
                if(mp3File.hasId3v2Tag()){
                    ID3v2 tag = mp3File.getId3v2Tag();
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                    Bitmap yourSelectedImage = BitmapFactory.decodeByteArray(tag.getAlbumImage(), 0, tag.getAlbumImage().length,options);
                    imgCover.setImageBitmap(yourSelectedImage);
                }
            } catch (Exception e) {
                Log.i(EditActivity.TAG,e.toString());
            }
            editTitle.setText(track.getTitle());
            editArtist.setText(track.getArtist());
            editAlbum.setText(track.getAlbum());

        } else {
            imgCover.setImageResource(R.mipmap.unknow_cover);
        }

        Log.i(EditActivity.TAG, "------------------------------------------------/nmetadata from library:");
        MusicMetadataSet src_set = null;
        try {
            src_set = new MyID3().read(new File(track.getTrackUri().toString())); // read metadata
        } catch (IOException e) {
            Log.i(EditActivity.TAG, "Exception .read(): " + e.toString());
        }
        if(src_set==null){
            Log.i(EditActivity.TAG, "there is no metadata");
        } else {
            MusicMetadata metadata = (MusicMetadata) src_set.getSimplified();
            Log.i(EditActivity.TAG,"title: "+metadata.getSongTitle());
            Log.i(EditActivity.TAG,"album" + metadata.getAlbum());
            Log.i(EditActivity.TAG, "artist" + metadata.getArtist());

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fabEditConfirm : saveNewMetadataMp3Agic();
                break;

            case R.id.imgCover :
                new MaterialDialog.Builder(this)
                        .title("Choose image source")
                        .items(R.array.items_option_image)
                        .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
                            @Override
                            public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                                if(which==0){
                                    Log.i(EditActivity.TAG,"chose gallery");
                                    // startActivityForResult
                                } else if(which==1){
                                    Log.i(EditActivity.TAG, "chose web");
                                    //new DownloadImageAsynk(EditActivity.this,track.getTitle()).execute();
                                    //new GoogleCoverLoader(track.getTitle()).getUrlImage();
                                    new InitLIstViewAsynk(EditActivity.this,track.getTitle()).execute();
                                }
                                return true;
                            }
                        })
                        .show();
                break;
        }
    }


    MediaScannerConnection  scanner;
    private void saveNewMetadataMyId3Lib(){

        String newTitle = editTitle.getText().toString().length()==0 ? editTitle.getHint().toString() : editTitle.getText().toString();
        String newAlbum = editAlbum.getText().toString().length()==0 ? editAlbum.getHint().toString() : editAlbum.getText().toString();
        String newArtist = editArtist.getText().toString().length()==0 ? editArtist.getHint().toString() : editArtist.getText().toString();

        File src = new File(track.getTrackUri().toString());
        MusicMetadataSet src_set = null;
        try {
            src_set = new MyID3().read(src);
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            Log.i(EditActivity.TAG, e1.toString());
            Snackbar.make(scrollView,"There was an errore while editing",Snackbar.LENGTH_SHORT).show();
        } // read metadata

        if (src_set == null){
            Log.i(EditActivity.TAG, "there is no metadata");
        } else {
            try{
                MusicMetadata metadata = (MusicMetadata) src_set.getSimplified();
                metadata.setSongTitle(newTitle);
                metadata.setAlbum(newAlbum);
                metadata.setArtist(newArtist);
                new MyID3().update(src, src_set, metadata);


                scan.connect();
            }catch (Exception e) {
                Log.i(EditActivity.TAG,"Exception .getSimplified(): "+e.toString());
                Snackbar.make(scrollView,"There was an errore while editing",Snackbar.LENGTH_SHORT).show();
            }
        }
    }

    private void saveNewMetadataMp3Agic(){
        String newTitle = editTitle.getText().toString().length()==0 ? editTitle.getHint().toString() : editTitle.getText().toString();
        String newAlbum = editAlbum.getText().toString().length()==0 ? editAlbum.getHint().toString() : editAlbum.getText().toString();
        String newArtist = editArtist.getText().toString().length()==0 ? editArtist.getHint().toString() : editArtist.getText().toString();

        try {

            String path = track.getTrackUri().toString();
            File originFile = new File(path);
            File retaggedFile = new File(path+RETAGGED_EXTENSION);

            Mp3File mp3File = new Mp3File(new File(track.getTrackUri().toString()));
            if(mp3File.hasId3v1Tag()){
                ID3v1 tag = mp3File.getId3v1Tag();
                if(tag!=null){
                    tag.setArtist(newArtist);
                    tag.setAlbum(newAlbum);
                    tag.setTitle(newTitle);
                    mp3File.save(retaggedFile.getAbsolutePath());
                    originFile.delete();
                    retaggedFile.renameTo(originFile);
                    scan.connect();
                    getContentResolver().notifyChange(Uri.parse("content://media"), null);
                    Log.i(EditActivity.TAG,"set metadata on tag v1 !=null");
                }
            } else if(mp3File.hasId3v2Tag()){
                ID3v2 tag = mp3File.getId3v2Tag();
                tag.setArtist(newArtist);
                tag.setAlbum(newAlbum);
                tag.setTitle(newTitle);
                String mimeType = tag.getAlbumImageMimeType();
                // user want to update image also
                if(arrayImageFile!=null){
                    Log.i(EditActivity.TAG,"want to update image also with !null file");

                    File tempFile = new File(Environment.getExternalStorageDirectory().getPath()+File.separator+"Musicat"+File.separator+"try.jpg");
                    tempFile.createNewFile();
                    FileOutputStream fos = new FileOutputStream(tempFile.getAbsolutePath());
                    fos.write(arrayImageFile);
                    fos.close();

                    // update MEDIASTORE
                    /*Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");
                    int deleted = getContentResolver().delete(ContentUris.withAppendedId(sArtworkUri, track.getAlbumId()), null, null);
                    Log.i(EditActivity.TAG, "delete " + deleted + " row for albumId");
                    ContentValues values = new ContentValues();
                    values.put("album_id", track.getAlbumId());
                    values.put("_data", track.getTrackUri().toString());*/

                    Utils.updateArtWorkMediaStore(getApplicationContext(),track.getArtUri(),track.getAlbumId(),new File(track.getTrackUri().toString()).getAbsolutePath());

                    tag.setAlbumImage(arrayImageFile, "image/jpeg");
                } else {
                    Log.i(EditActivity.TAG,"want to update image also with null file");
                }
                mp3File.save(retaggedFile.getAbsolutePath());
                originFile.delete();
                retaggedFile.renameTo(originFile);
                scan.connect();
                getContentResolver().notifyChange(Uri.parse("content://media"), null);

                Log.i(EditActivity.TAG, "set metadata on tag v2 !=null");
            }
            scan.connect();

        } catch (Exception e) {
            Log.i(EditActivity.TAG,e.toString());
        }


    }

    MediaScannerConnection scan = new MediaScannerConnection(EditActivity.this,
            new MediaScannerConnection.MediaScannerConnectionClient() {
                @Override
                public void onMediaScannerConnected() {
                    Log.i(EditActivity.TAG, "disconnect() called");
                    Snackbar.make(scrollView,"Editing was succesfully",Snackbar.LENGTH_SHORT).show();
                }

                @Override
                public void onScanCompleted(String path, Uri uri) {
                    scanner.scanFile(track.getTrackUri().toString(), "audio/*");
                    Log.i(EditActivity.TAG,"scanFile() called");
                }
            });


    class InitLIstViewAsynk extends AsyncTask<Void,Void,List<Item>> {

        private ProgressDialog dialog;
        private String q;
        private Context context;

        public InitLIstViewAsynk(Context context, String query){
            q = query;
            this.context = context;
            dialog = new ProgressDialog(this.context);
        }
        protected void onPreExecute() {
            dialog.setMessage("Searching images");
            dialog.show();
        }
        @Override
        protected List<Item> doInBackground(Void... params) {

            //return new ArrayList<Item>();
            return new GoogleCoverLoader(q).getUrlImage();
        }

        @Override
        protected void onPostExecute(final List<Item> list){
            dialog.dismiss();
            // set up listview i new material dialog
            new MaterialDialog.Builder(EditActivity.this)
                    .theme(Utils.getTheme(EditActivity.this)==Const.THEME_LIGHT ? Theme.LIGHT : Theme.DARK)
                    .title("Chose one pictures")
                    .adapter(new ImageWebAdapterDialog(EditActivity.this, R.layout.row_dialog_images, list), new MaterialDialog.ListCallback() {
                        @Override
                        public void onSelection(final MaterialDialog materialDialog, View view, int i, CharSequence charSequence) {
                            AsyncHttpClient client = new AsyncHttpClient();
                            client.get(list.get(i).getLink(), new FileAsyncHttpResponseHandler(context) {
                                @Override
                                public void onFailure(int statusCode, Header[] headers, Throwable throwable, File file) {
                                    Snackbar.make(scrollView, "Error downloading image, check interner connection", Snackbar.LENGTH_LONG);
                                    materialDialog.dismiss();
                                }

                                @Override
                                public void onSuccess(int statusCode, Header[] headers, File file) {
                                    Log.i(EditActivity.TAG, "statusCode: " + statusCode);
                                    if (statusCode == 200 && file.exists()) {
                                        // we can set directly to imageView
                                        Picasso.with(context)
                                                .load(file)
                                                .into(imgCover);

                                        try {
                                            arrayImageFile = ByteStreams.toByteArray(new FileInputStream(file));

                                        } catch (IOException e) {
                                            Log.i(EditActivity.TAG, e.toString());
                                            Snackbar.make(scrollView, "Error while download image", Snackbar.LENGTH_LONG);
                                        }
                                    }
                                    materialDialog.dismiss();
                                }
                            });
                        }
                    }).show();

        }

    }

}
