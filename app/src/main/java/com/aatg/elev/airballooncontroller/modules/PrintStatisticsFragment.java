package com.aatg.elev.airballooncontroller.modules;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aatg.elev.airballooncontroller.dataconverters.BaseDataConverter;
import com.aatg.elev.airballooncontroller.IBluetoothController;
import com.aatg.elev.airballooncontroller.R;
import com.aatg.elev.airballooncontroller.bluetooth.BluetoothPacket;

import java.text.NumberFormat;
import java.util.Random;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PrintValueFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PrintStatisticsFragment extends Fragment implements IModuleFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_ID = "arg_id";
    private static final String ARG_DELAY = "arg_delay";
    private static final String ARG_LABEL = "arg_label";

    // TODO: Rename and change types of parameters
    private int id;
    private long delay;
    private String label;

    private IBluetoothController bluetoothController;
    private Random random = new Random();
    public BaseDataConverter dataConverter;

    private TextView labelTextView;
    private TextView minValueTextView;
    private TextView avgValueTextView;
    private TextView maxValueTextView;
    private TextView curValueTextView;

    private Number min;
    private Number avg;
    private Number max;

    private Number acc = 0;
    private long items = 0;

    private NumberFormat numberFormat = NumberFormat.getNumberInstance();


    public PrintStatisticsFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static PrintStatisticsFragment newInstance(int id, long delay, String label) {
        PrintStatisticsFragment fragment = new PrintStatisticsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_ID, id);
        args.putLong(ARG_DELAY, delay);
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
            label = getArguments().getString(ARG_LABEL);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_print_statistics, container, false);

        labelTextView = view.findViewById(R.id.label);

        labelTextView.setText(label + ": ");

        minValueTextView = view.findViewById(R.id.min);
        avgValueTextView = view.findViewById(R.id.avg);
        maxValueTextView = view.findViewById(R.id.max);
        curValueTextView = view.findViewById(R.id.cur);

        numberFormat.setMaximumFractionDigits(2);

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
            data = dataConverter.getData(data.longValue());

        items++;
        acc = acc.doubleValue() + data.doubleValue();

        if (min == null || data.doubleValue() < min.doubleValue())
        {
            min = data;
            minValueTextView.setText("Min: " + numberFormat.format(min.doubleValue()));
        }

        if (avg == null)
        {
            avg = data;

        }

        avg = acc.doubleValue() / (double)items;

        avgValueTextView.setText("Avg: " + numberFormat.format(avg.doubleValue()));

        if (max == null || data.doubleValue() > max.doubleValue())
        {
            max = data;
            maxValueTextView.setText("Max: " + numberFormat.format(max.doubleValue()));
        }

        curValueTextView.setText("Cur: " + numberFormat.format(data.doubleValue()));
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
