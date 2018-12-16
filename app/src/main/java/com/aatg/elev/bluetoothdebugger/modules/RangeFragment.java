package com.aatg.elev.bluetoothdebugger.modules;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.aatg.elev.bluetoothdebugger.BluetoothPacket;
import com.aatg.elev.bluetoothdebugger.IBluetoothController;
import com.aatg.elev.bluetoothdebugger.IControlFragment;
import com.aatg.elev.bluetoothdebugger.R;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RangeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RangeFragment extends Fragment implements IControlFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_ID = "arg_id";
    private static final String ARG_MIN = "arg_min";
    private static final String ARG_MAX = "arg_max";
    private static final String ARG_LABEL = "arg_label";

    // TODO: Rename and change types of parameters
    private int id;
    private long min;
    private long max;
    private String label;

    private TextView textViewLabel;
    private SeekBar seekBar;

    private IBluetoothController bluetoothController;

    public RangeFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static RangeFragment newInstance(int id, long min, long max, String label) {
        RangeFragment fragment = new RangeFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_ID, id);
        args.putLong(ARG_MIN, min);
        args.putLong(ARG_MAX, max);
        args.putString(ARG_LABEL, label);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            id = getArguments().getInt(ARG_ID);
            min = getArguments().getLong(ARG_MIN);
            max = getArguments().getLong(ARG_MAX);
            label = getArguments().getString(ARG_LABEL);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_range, container, false);

        textViewLabel = view.findViewById(R.id.label);
        textViewLabel.setText(label);

        seekBar = view.findViewById(R.id.seekBar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                double prog = (double)progress / (double)100;
                long multiplier = max - min;

                long finalValue = (long)((prog * multiplier) + min);

                sendPacket(finalValue);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

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
        return null;
    }

    @Override
    public void handlePacket(BluetoothPacket packet) {

    }

    @Override
    public Fragment getFragment() {
        return this;
    }

    private void sendPacket(long progress)
    {
        BluetoothPacket packet = new BluetoothPacket(id, progress);
        bluetoothController.sendPacket(packet);
    }
}
