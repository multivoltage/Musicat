package com.multivoltage.musicat.recycleviewlist;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Filter;
import android.widget.Filterable;

import com.tonicartos.superslim.GridSLM;
import com.tonicartos.superslim.LinearSLM;
import com.multivoltage.musicat.R;
import com.multivoltage.musicat.entity.Item;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Diego on 03/07/2015.
 */
public abstract class AGenericAdapterRecycle<I> extends RecyclerView.Adapter<AGenericViewHolder> implements Filterable {

    private static String TAG = "TrackAdapterRecycle.TAG";
    protected static final int VIEW_TYPE_HEADER = 0x01;
    protected static final int VIEW_TYPE_CONTENT = 0x00;
    protected static final int VIEW_TYPE_FOOTER = 0x02;
    private static final int LINEAR = 0;
    private  List<LineItem> mLineItems;
    private List<Item> mItemsFiltered;
    private int mHeaderDisplay;
    private boolean mMarginsFixed;
    protected final Context mContext;
    private List<Item> mItems = new ArrayList<Item>();
    private int lastPosition = -1;

    public int getCount(){
        return mItems.size();
    }

    public AGenericAdapterRecycle(Context context, int headerMode, List<Item> tracks) {
        mContext = context;
        mHeaderDisplay = headerMode;
        mLineItems = new ArrayList<>();
        mItems = new ArrayList<>(tracks);

        //Insert headers into list of items.
        String lastHeader = "";
        int sectionManager = -1;
        int headerCount = 0;
        int sectionFirstPosition = 0;
        for (int i = 0; i < mItems.size(); i++) {
            String header = mItems.get(i).getFirstTitle().substring(0, 1);
            if (!TextUtils.equals(lastHeader, header)) {
                // Insert new header view and update section data.
                sectionManager = (sectionManager + 1) % 2;
                sectionFirstPosition = i + headerCount;
                lastHeader = header;
                headerCount += 1;

                mLineItems.add(new LineItem(mItems.get(i), true, sectionManager, sectionFirstPosition));
            }
            mLineItems.add(new LineItem(mItems.get(i), false, sectionManager, sectionFirstPosition));
        }
    }

    public boolean isItemHeader(int position) {
        return mLineItems.get(position).isHeader;
    }

    public String itemToString(int position) {
        return mLineItems.get(position).toString();
    }

    // in future this method is abstract
    @Override
    public abstract AGenericViewHolder onCreateViewHolder(ViewGroup parent, int viewType);

    @Override
    public void onBindViewHolder(AGenericViewHolder holder, int position) {
        final LineItem item = mLineItems.get(position);
        final View itemView = holder.itemView;

        holder.bindItem(item.item, item.isHeader);

        final GridSLM.LayoutParams lp = GridSLM.LayoutParams.from(itemView.getLayoutParams());
        // Overrides xml attrs, could use different layouts too.
        if (item.isHeader) {
            lp.headerDisplay = mHeaderDisplay;
            if (lp.isHeaderInline() || (mMarginsFixed && !lp.isHeaderOverlay())) {
                lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
            } else {
                lp.width = ViewGroup.LayoutParams.WRAP_CONTENT;
            }

            lp.headerEndMarginIsAuto = !mMarginsFixed;
            lp.headerStartMarginIsAuto = !mMarginsFixed;
        }
        lp.setSlm(LinearSLM.ID);//item.sectionManager == LINEAR ? LinearSLM.ID : GridSLM.ID);
        lp.setColumnWidth(mContext.getResources().getDimensionPixelSize(R.dimen.grid_column_width));
        lp.setFirstPosition(item.sectionFirstPosition);
        itemView.setLayoutParams(lp);

        setAnimation(holder.itemView, position);
    }

    private void setAnimation(View viewToAnimate, int position){
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(mContext, android.R.anim.slide_in_left);
            animation.setDuration(0);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    @Override
    public int getItemViewType(int position) {
        return mLineItems.get(position).isHeader ? VIEW_TYPE_HEADER : VIEW_TYPE_CONTENT;
    }

    @Override
    public int getItemCount() {
        return mLineItems.size();
    }

    public void setHeaderDisplay(int headerDisplay) {
        mHeaderDisplay = headerDisplay;
        notifyHeaderChanges();
    }

    public void setMarginsFixed(boolean marginsFixed) {
        mMarginsFixed = marginsFixed;
        notifyHeaderChanges();
    }

    public void update(List<Item> list){
        //Insert headers into list of items.
        mLineItems.clear();
        String lastHeader = "";
        int sectionManager = -1;
        int headerCount = 0;
        int sectionFirstPosition = 0;
        for (int i = 0; i < mItems.size(); i++) {
            String header = mItems.get(i).getFirstTitle().substring(0, 1);
            if (!TextUtils.equals(lastHeader, header)) {
                // Insert new header view and update section data.
                sectionManager = (sectionManager + 1) % 2;
                sectionFirstPosition = i + headerCount;
                lastHeader = header;
                headerCount += 1;

                mLineItems.add(new LineItem(mItems.get(i), true, sectionManager, sectionFirstPosition));
            }
            mLineItems.add(new LineItem(mItems.get(i), false, sectionManager, sectionFirstPosition));
        }
        this.mItems = list;
    }

    public void notifyHeaderChanges() {
        for (int i = 0; i < mLineItems.size(); i++) {
            LineItem item = mLineItems.get(i);
            if (item.isHeader) {
                notifyItemChanged(i);
            }
        }
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                final FilterResults oReturn = new FilterResults();
                final List<Item> results = new ArrayList<Item>();
                if (mItemsFiltered == null)
                    mItemsFiltered = mItems;
                if (constraint != null) {
                    if (mItemsFiltered != null && mItemsFiltered.size() > 0) {
                        for (final Item i : mItemsFiltered) {
                            if (i.getFirstTitle().toLowerCase().contains(constraint.toString())) {
                                results.add(i);
                            } else {
                                // check artist not null
                                String artist = i.getSecondTitle();
                                if(artist!=null){
                                    if(artist.toLowerCase().contains(constraint.toString()))
                                        results.add(i);
                                }
                            }
                        }
                    }
                    oReturn.values = results;
                }
                return oReturn;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                mItems = (ArrayList<Item>) results.values;
                update(mItems);
                notifyDataSetChanged();

            }
        };
    }


    public void removeAtPos(int pos){
       mLineItems.remove(pos);
       notifyItemRemoved(pos);
    }

    private static class LineItem<I> {

        public int sectionManager;
        public int sectionFirstPosition;
        public boolean isHeader;
        public I item;

        public LineItem(I item, boolean isHeader, int sectionManager,int sectionFirstPosition) {
            this.item = item;
            this.isHeader = isHeader;
            this.sectionManager = sectionManager;
            this.sectionFirstPosition = sectionFirstPosition;
        }

        public I getTrack(){ return item; }
    }


    public Object getItem(int pos){

        if(pos<=mLineItems.size())
            return mLineItems.get(pos).getTrack();
        else
            return mItemsFiltered.get(pos);
    }


    public interface OnButtonListener {
        void onButtonSelected(int id);
        void onOptionSelected(int id);
    }

}
