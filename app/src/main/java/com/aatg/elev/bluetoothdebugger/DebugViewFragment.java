package com.aatg.elev.bluetoothdebugger;


import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.aatg.elev.bluetoothdebugger.bluetooth.BluetoothPacket;


/**
 * A simple {@link Fragment} subclass.
 */
public class DebugViewFragment extends Fragment implements IPacketHookListener {

    private IBluetoothController bluetoothController;

    private TextView deviceTextView;

    private EditText packetIdEditText;
    private EditText packetContentEditText;

    private EditText inputEditText;



    public DebugViewFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_debug_view, container, false);

        deviceTextView = view.findViewById(R.id.textViewDevice);

        packetIdEditText = view.findViewById(R.id.editTextPacketId);

        packetIdEditText.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count){}

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,int after){}

            //Right after the text is changed
            @Override
            public void afterTextChanged(Editable s)
            {
                //Store the text on a String
                String text = s.toString();

                //Get the length of the String
                int length = s.length();

                /*If the String length is bigger than zero and it's not
                composed only by the following characters: A to F and/or 0 to 9 */
                if(length > 0 && !text.matches("[a-fA-F0-9]+"))
                {
                    //Delete the last character
                    s.delete(length - 1, length);
                }
            }
        });

        packetContentEditText = view.findViewById(R.id.editTextPacketContent);

        inputEditText = view.findViewById(R.id.editTextInput);

        FloatingActionButton fab = view.findViewById(R.id.fab_send);

        if (bluetoothController.isFake())
        {
            deviceTextView.setText("Fake device -  AA:BB:CC:DD:EE");

            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (isPacketValid())
                    {
                        Snackbar.make(view, "Sending packet", Snackbar.LENGTH_SHORT)
                                .setAction("Action", null).show();
                    }
                    else
                    {
                        Snackbar.make(view, "Invalid packet", Snackbar.LENGTH_SHORT)
                                .setAction("Action", null).show();
                    }
                }
            });

            return view;
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (isPacketValid())
                {
                    Snackbar.make(view, "Sending packet", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();

                    BluetoothPacket packet = new BluetoothPacket(Integer.parseInt(packetIdEditText.getText().toString(), 16), Long.parseLong(packetContentEditText.getText().toString()));

                    bluetoothController.sendPacket(packet);
                }
                else
                {
                    Snackbar.make(view, "Invalid packet", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                }


            }
        });

        deviceTextView.setText(bluetoothController.getDeviceName() + " - " + bluetoothController.getDeviceAddress());

        return view;
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);

        if (context instanceof IBluetoothController) {
            bluetoothController = (IBluetoothController) context;
            bluetoothController.setPacketHook(this);
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement IBluetoothController");
        }

    }

    @Override
    public void onDetach()
    {
        super.onDetach();
        bluetoothController.removePacketHook(this);
        bluetoothController = null;
    }

    private boolean isPacketValid()
    {
        String packetId = packetIdEditText.getText().toString();
        String packetContent = packetContentEditText.getText().toString();

        if (packetId.length() == 0) return false;
        if (packetContent.length() == 0) return false;

        try {
            Integer.parseInt(packetId, 16);
            Long.parseLong(packetContent);

            return true;
        }
        catch (NumberFormatException e)
        {
            return false;
        }
    }

    @Override
    public void getHookedPacket(BluetoothPacket packet) {
        inputEditText.setText("ID: " + packet.id + " Data: " + packet.data);
    }
}
