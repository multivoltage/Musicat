package com.tonini.diego.musicat.recycleviewlist;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.LruCache;
import android.view.ViewGroup;

/**
 * Created by Diego on 27/08/2015.
 */
public  class AbstractCachingRecyclerAdapter extends RecyclerView.Adapter<AGenericViewHolder> {

    private LruCache<String, Bitmap> mIconCache;

    public AbstractCachingRecyclerAdapter() {
        // set up the icon cacher
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        final int cacheSize = maxMemory / 8;
        mIconCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getByteCount() / 1024;
            }
        };
    }

    @Override
    public AGenericViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(AGenericViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public LruCache<String, Bitmap> getIconCache() {
        return this.mIconCache;
    }

    public Bitmap getBitmapFromMemCache(String key) {
        return mIconCache.get(key);
    }

    /*public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public TextView description;
        public ImageView icon;
        public View progressBar;
        public int position;

        public ViewHolder(View item) {
            super(item);

            title = (TextView) item.findViewById(R.id.title);
            description = (TextView) item.findViewById(R.id.publisher);
            icon = (ImageView) item.findViewById(R.id.icon);
            progressBar = item.findViewById(R.id.loading);
        }
    }*/
}
