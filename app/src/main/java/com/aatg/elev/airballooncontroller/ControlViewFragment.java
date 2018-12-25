package com.aatg.elev.airballooncontroller;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.aatg.elev.airballooncontroller.modules.PlotFragment;
import com.aatg.elev.airballooncontroller.modules.RangeFragment;
import com.aatg.elev.airballooncontroller.modules.ToggleFragment;
import com.aatg.elev.bluetoothdebugger.R;
import com.aatg.elev.airballooncontroller.dataconverters.TemperatureConverter;
import com.aatg.elev.airballooncontroller.dataconverters.UltraSonicDistanceConverter;
import com.aatg.elev.airballooncontroller.modules.IModuleFragment;
import com.aatg.elev.airballooncontroller.modules.PrintStatisticsFragment;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ControlViewFragment extends Fragment {

    public List<IModuleFragment> controlFragments = new ArrayList<>();

    private LinearLayout layoutControl;
    private FragmentManager fragmentManager;


    public ControlViewFragment() {
        // Required empty public constructor
    }

    public void addFragment(IModuleFragment controlFragment)
    {
        controlFragments.add(controlFragment);
    }

    public void removeFragment(IModuleFragment controlFragment)
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

        for (IModuleFragment controlFragment :
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

        //PrintValueFragment printValueFragment = PrintValueFragment.newInstance(6, 100, "Temperature (C)");
        //printValueFragment.dataConverter = new TemperatureConverter(217, TemperatureConverter.P100_TABLE, TemperatureConverter.P100_START, TemperatureConverter.TemperatureUnit.Celsius);
        //addFragment(printValueFragment);

        PrintStatisticsFragment printStatisticsFragment = PrintStatisticsFragment.newInstance(6, 100, "Temperature (C)");
        printStatisticsFragment.dataConverter = new TemperatureConverter(217, TemperatureConverter.P100_TABLE, TemperatureConverter.P100_START, TemperatureConverter.TemperatureUnit.Celsius);
        addFragment(printStatisticsFragment);

        PlotFragment plot = PlotFragment.newInstance(7, 1000, 100, "Distance (M)");
        plot.dataConverter = new UltraSonicDistanceConverter(UltraSonicDistanceConverter.DistanceUnit.Meters);
        addFragment(plot);


        updateFragments();
        return view;
    }

}
