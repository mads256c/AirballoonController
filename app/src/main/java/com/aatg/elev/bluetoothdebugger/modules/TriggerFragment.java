package com.aatg.elev.bluetoothdebugger.modules;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.aatg.elev.bluetoothdebugger.BluetoothPacket;
import com.aatg.elev.bluetoothdebugger.IBluetoothController;
import com.aatg.elev.bluetoothdebugger.IControlFragment;
import com.aatg.elev.bluetoothdebugger.R;


public class TriggerFragment extends Fragment implements IControlFragment {
    private static BluetoothPacket packet = new BluetoothPacket(4, 100);

    private Button button;

    private IBluetoothController bluetoothController;

    public TriggerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_trigger, container, false);

        //Find the +1 button
        button = view.findViewById(R.id.button);

        button.setText("Hello world!");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bluetoothController != null) {
                    bluetoothController.sendPacket(packet);
                }
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof IBluetoothController) {
            bluetoothController = (IBluetoothController) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement IBluetoothController");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        bluetoothController = null;
    }


    @Override
    public Integer getPacketId() {
        return 4;
    }

    @Override
    public void handlePacket(BluetoothPacket packet) {

    }

    @Override
    public Fragment getFragment() {
        return this;
    }
}
