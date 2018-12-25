package com.aatg.elev.airballooncontroller.selectpaireddevice;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aatg.elev.airballooncontroller.R;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link PairedDeviceItem} and makes a call to the
 * specified {@link PairedDeviceItemListFragment.OnPairedDeviceItemClickedListener}.
 */
public final class PairedDeviceItemRecyclerViewAdapter extends RecyclerView.Adapter<PairedDeviceItemRecyclerViewAdapter.ViewHolder> {

    private final List<PairedDeviceItem> mValues;
    private final PairedDeviceItemListFragment.OnPairedDeviceItemClickedListener mListener;

    PairedDeviceItemRecyclerViewAdapter(List<PairedDeviceItem> items, PairedDeviceItemListFragment.OnPairedDeviceItemClickedListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mIdView.setText(mValues.get(position).name);
        holder.mContentView.setText(mValues.get(position).mac);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onPairedDeviceItemClicked(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public final class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        public PairedDeviceItem mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.item_number);
            mContentView = (TextView) view.findViewById(R.id.content);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
