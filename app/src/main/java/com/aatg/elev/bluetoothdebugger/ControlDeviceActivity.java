package com.aatg.elev.bluetoothdebugger;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.UUID;

import static com.aatg.elev.bluetoothdebugger.SelectDeviceActivity.EXTRA_MESSAGE;

public class ControlDeviceActivity extends AppCompatActivity {

    private EditText packetIdEditText;
    private EditText packetContentEditText;

    private BluetoothDevice device;
    private BluetoothSocket socket;
    private OutputStream outputStream;

    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control_device);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_send);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Sending packet", Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();

            sendPacket(Byte.parseByte(packetIdEditText.getText().toString()), Long.parseLong(packetContentEditText.getText().toString()));
            }
        });
        packetIdEditText = findViewById(R.id.editTextPacketId);
        packetContentEditText = findViewById(R.id.editTextPacketContent);

        TextView deviceView = findViewById(R.id.textViewDevice);

        Intent intent = getIntent();
        device = intent.getParcelableExtra(EXTRA_MESSAGE);

        deviceView.setText(device.getName() + " - " + device.getAddress());

        connect();
    }

    private void connect()
    {
        try {
            socket = device.createRfcommSocketToServiceRecord(MY_UUID);
        }
        catch (IOException e)
        {
            Toast toast = Toast.makeText(getBaseContext(), "Error could not create socket", Toast.LENGTH_LONG);
            toast.show();
            finish();
        }

        try {
            socket.connect();
        }
        catch (IOException e)
        {
            Toast toast = Toast.makeText(getBaseContext(), "Error could connect to socket", Toast.LENGTH_LONG);
            toast.show();
            finish();
        }

        try {
            outputStream = socket.getOutputStream();
        }
        catch (IOException e)
        {
            Toast toast = Toast.makeText(getBaseContext(), "Error could not create output stream", Toast.LENGTH_LONG);
            toast.show();
            finish();
        }


    }

    private void sendPacket(byte id, long content)
    {
        byte[] bytes = new byte[9];

        bytes[0] = id;

        bytes[1] = (byte) (content & 0xFF);
        bytes[2] = (byte) ((content >> 8) & 0xFF);
        bytes[3] = (byte) ((content >> 16) & 0xFF);
        bytes[4] = (byte) ((content >> 24) & 0xFF);
        bytes[5] = (byte) ((content >> 32) & 0xFF);
        bytes[6] = (byte) ((content >> 40) & 0xFF);
        bytes[7] = (byte) ((content >> 48) & 0xFF);
        bytes[8] = (byte) ((content >> 56) & 0xFF);
        //bytes[9] = (byte) ((content >> 64) & 0xFF);

        try {
            outputStream.write(bytes);
        }
        catch (IOException e)
        {
            Toast toast = Toast.makeText(getBaseContext(), "Error could not write to output stream", Toast.LENGTH_LONG);
            toast.show();
            finish();
        }

    }
}
