package com.aatg.elev.bluetoothdebugger;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;



/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PlotFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PlotFragment extends Fragment implements IControlFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_ID = "arg_id";
    private static final String ARG_DELAY = "arg_delay";
    private static final String ARG_MAXPOINTS = "arg_maxpoints";
    private static final String ARG_LABEL = "arg_label";

    public IDataConverter dataConverter;

    // TODO: Rename and change types of parameters
    private int id;
    private long delay;
    private int maxpoints;
    private String label;

    private long current = 0;

    private GraphView graph;
    private LineGraphSeries<DataPoint> series;


    private IBluetoothController bluetoothController;



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
        graph.setTitle(label);


        series = new LineGraphSeries<>();
        graph.addSeries(series);

        view.postDelayed(new Runnable() {
            @Override
            public void run() {

                sendPacket();

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

        series.appendData(new DataPoint(current, data.doubleValue()), true, maxpoints);
        graph.removeSeries(series);
        graph.addSeries(series);

        graph.getViewport().setMinX(current - maxpoints);
        graph.getViewport().setMaxX(current);
        graph.getViewport().setXAxisBoundsManual(true);

        current++;
    }

    @Override
    public Fragment getFragment() {
        return this;
    }



    private void sendPacket()
    {
        BluetoothPacket packet = new BluetoothPacket(id, 1);

        packet.sendPacket(bluetoothController.getOutputStream());
    }
}
