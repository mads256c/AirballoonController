package com.aatg.elev.bluetoothdebugger.modules;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;

import com.aatg.elev.bluetoothdebugger.bluetooth.BluetoothPacket;
import com.aatg.elev.bluetoothdebugger.IBluetoothController;
import com.aatg.elev.bluetoothdebugger.IControlFragment;
import com.aatg.elev.bluetoothdebugger.R;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ToggleFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ToggleFragment extends Fragment implements IControlFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_ID = "arg_id";
    private static final String ARG_OFF_DATA = "arg_off_data";
    private static final String ARG_ON_DATA = "arg_on_data";
    private static final String ARG_LABEL = "arg_label";

    // TODO: Rename and change types of parameters
    private int id;
    private long offData;
    private long onData;
    private String label;

    private IBluetoothController bluetoothController;

    private Switch aSwitch;


    public ToggleFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static ToggleFragment newInstance(int id, long offData, long onData, String label) {
        ToggleFragment fragment = new ToggleFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_ID, id);
        args.putLong(ARG_OFF_DATA, offData);
        args.putLong(ARG_ON_DATA, onData);
        args.putString(ARG_LABEL, label);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            id = getArguments().getInt(ARG_ID);
            offData = getArguments().getLong(ARG_OFF_DATA);
            onData = getArguments().getLong(ARG_ON_DATA);
            label = getArguments().getString(ARG_LABEL);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_toggle, container, false);

        aSwitch = view.findViewById(R.id.switch_toggle);
        aSwitch.setText(label);
        aSwitch.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                if (aSwitch.isChecked()) sendOnPacket();
                else sendOffPacket();
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

    private void sendOffPacket()
    {
        BluetoothPacket packet = new BluetoothPacket(id, offData);
        bluetoothController.sendPacket(packet);
    }

    private void sendOnPacket()
    {
        BluetoothPacket packet = new BluetoothPacket(id, onData);
        bluetoothController.sendPacket(packet);
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
}
