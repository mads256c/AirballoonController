package com.aatg.elev.airballooncontroller.selectpaireddevice;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.aatg.elev.airballooncontroller.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnPairedDeviceItemClickedListener}
 * interface.
 */
public final class PairedDeviceItemListFragment extends Fragment {

    private OnPairedDeviceItemClickedListener itemClickedListener;

    private RecyclerView recyclerView;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public PairedDeviceItemListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_paired_device_item_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));

            updateView(new ArrayList<PairedDeviceItem>());
        }
        return view;
    }

    public void updateView(List<PairedDeviceItem> pairedDeviceItems)
    {
        recyclerView.setAdapter(new PairedDeviceItemRecyclerViewAdapter(pairedDeviceItems, itemClickedListener));
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnPairedDeviceItemClickedListener) {
            itemClickedListener = (OnPairedDeviceItemClickedListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnPairedDeviceItemClickedListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        itemClickedListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnPairedDeviceItemClickedListener {
        void onPairedDeviceItemClicked(PairedDeviceItem item);
    }
}
