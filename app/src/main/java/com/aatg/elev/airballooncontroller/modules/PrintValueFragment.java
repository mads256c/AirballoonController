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
import com.aatg.elev.bluetoothdebugger.R;
import com.aatg.elev.airballooncontroller.bluetooth.BluetoothPacket;

import java.util.Random;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PrintValueFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PrintValueFragment extends Fragment implements IModuleFragment {
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
    private TextView valueTextView;

    public PrintValueFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static PrintValueFragment newInstance(int id, long delay, String label) {
        PrintValueFragment fragment = new PrintValueFragment();
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
        final View view = inflater.inflate(R.layout.fragment_print_value, container, false);

        labelTextView = view.findViewById(R.id.label);

        labelTextView.setText(label + ": ");

        valueTextView = view.findViewById(R.id.value);

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

        valueTextView.setText(data.toString());
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
