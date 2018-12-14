package com.aatg.elev.bluetoothdebugger;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ControlViewFragment extends Fragment {

    public List<IControlFragment> controlFragments = new ArrayList<>();

    private LinearLayout layoutControl;
    private FragmentManager fragmentManager;


    public ControlViewFragment() {
        // Required empty public constructor
    }

    public void addFragment(IControlFragment controlFragment)
    {
        controlFragments.add(controlFragment);
    }

    public void removeFragment(IControlFragment controlFragment)
    {
        controlFragments.remove(controlFragment);
    }

    public void clearFragments()
    {
        controlFragments.clear();
    }

    public void updateFragments()
    {
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        for (Fragment fragment:fragmentManager.getFragments()) {
            if (fragment != null) {
                transaction.remove(fragment);
            }
        }

        for (IControlFragment controlFragment :
                controlFragments) {
            transaction.add(layoutControl.getId(), controlFragment.getFragment());
            transaction.add(layoutControl.getId(), new SpacerFragment());
        }

        transaction.commit();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_control_view, container, false);
        layoutControl = view.findViewById(R.id.layout_control);
        fragmentManager = getChildFragmentManager();

        clearFragments();
        addFragment(ToggleFragment.newInstance(4, 0, 1, "LED"));
        addFragment(RangeFragment.newInstance(5, 0, 180, "Servo"));

        updateFragments();
        return view;
    }

}
