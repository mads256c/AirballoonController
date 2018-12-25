package com.aatg.elev.airballooncontroller;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.aatg.elev.bluetoothdebugger.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class SpacerFragment extends Fragment {


    public SpacerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_spacer, container, false);
    }

}
