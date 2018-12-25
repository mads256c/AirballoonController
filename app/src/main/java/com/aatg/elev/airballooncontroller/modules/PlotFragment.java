package com.aatg.elev.airballooncontroller.modules;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aatg.elev.airballooncontroller.bluetooth.BluetoothPacket;
import com.aatg.elev.airballooncontroller.IBluetoothController;
import com.aatg.elev.airballooncontroller.dataconverters.BaseDataConverter;
import com.aatg.elev.bluetoothdebugger.R;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.Random;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PlotFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PlotFragment extends Fragment implements IModuleFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_ID = "arg_id";
    private static final String ARG_DELAY = "arg_delay";
    private static final String ARG_MAXPOINTS = "arg_maxpoints";
    private static final String ARG_LABEL = "arg_label";

    public BaseDataConverter dataConverter;

    // TODO: Rename and change types of parameters
    private int id;
    private long delay;
    private int maxpoints;
    private String label;

    private long current = delay;
    private double minY = 0.0;
    private double maxY = 0.0;

    private GraphView graph;
    private LineGraphSeries<DataPoint> series;


    private IBluetoothController bluetoothController;

    private Random random = new Random();

    public PlotFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static PlotFragment newInstance(int id, long delay, int maxpoints, String label) {
        PlotFragment fragment = new PlotFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_ID, id);
        args.putLong(ARG_DELAY, delay);
        args.putInt(ARG_MAXPOINTS, maxpoints);
        args.putString(ARG_LABEL, label);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            id = getArguments().getInt(ARG_ID);
            delay = getArguments().getLong(ARG_DELAY);
            maxpoints = getArguments().getInt(ARG_MAXPOINTS);
            label = getArguments().getString(ARG_LABEL);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_plot, container, false);

        graph = (GraphView) view.findViewById(R.id.graph);
        //graph.setTitle(label);
        graph.getGridLabelRenderer().setHorizontalAxisTitle("Seconds");
        graph.getGridLabelRenderer().setVerticalAxisTitle(label);
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setYAxisBoundsManual(true);


        series = new LineGraphSeries<>();
        graph.addSeries(series);

        view.postDelayed(new Runnable() {
            @Override
            public void run() {

                //If bluetoothController is null we are detaching and so we need to cleanup and stop the loop
                if (bluetoothController == null) return;

                if (!bluetoothController.isFake()) sendPacket();

                else handlePacket(new BluetoothPacket(id, random.nextInt(500)));

                view.postDelayed(this, delay);
            }
        }, delay);

        return view;
    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);

        if (context instanceof IBluetoothController) {
            bluetoothController = (IBluetoothController) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement IBluetoothController");
        }
    }

    @Override
    public void onDetach()
    {
        super.onDetach();

        bluetoothController = null;
    }

    @Override
    public Integer getPacketId() {
        return id;
    }

    @Override
    public void handlePacket(BluetoothPacket packet) {

        Number data = packet.data;

        if (dataConverter != null)
            data = dataConverter.getData(packet.data);

        if (data.doubleValue() < minY)
            minY = data.doubleValue();

        if (data.doubleValue() > maxY)
            maxY = data.doubleValue();

        series.appendData(new DataPoint((double)current / 1000.0, data.doubleValue()), false, maxpoints);

        graph.getViewport().setMinX(((double)current - ((double)maxpoints * delay)) / 1000.0);
        graph.getViewport().setMaxX((double)current / 1000.0);
        graph.getViewport().setMinY(minY);
        graph.getViewport().setMaxY(maxY);


        current += delay;
    }

    @Override
    public Fragment getFragment() {
        return this;
    }



    private void sendPacket()
    {
        BluetoothPacket packet = new BluetoothPacket(id, 1);

        bluetoothController.sendPacket(packet);
    }
}
