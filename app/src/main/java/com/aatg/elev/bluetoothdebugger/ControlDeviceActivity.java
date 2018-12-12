package com.aatg.elev.bluetoothdebugger;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.UUID;

import static com.aatg.elev.bluetoothdebugger.SelectDeviceActivity.EXTRA_MESSAGE;

public class ControlDeviceActivity extends AppCompatActivity {

    private EditText packetIdEditText;
    private EditText packetContentEditText;
    private EditText inputEditText;

    private BluetoothDevice device;
    private BluetoothSocket socket;
    private OutputStream outputStream;
    private ConnectedThread connectedThread;

    private Handler bluetoothHandler;

    private CountDownTimer countDownTimer;

    private StringBuilder recDataString = new StringBuilder();

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

            sendPacket((byte)Integer.parseInt(packetIdEditText.getText().toString(), 16), Long.parseLong(packetContentEditText.getText().toString()));
            }
        });
        packetIdEditText = findViewById(R.id.editTextPacketId);

        //Assign a TextWatcher to the EditText
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
                if(!text.matches("[a-fA-F0-9]+") && length > 0)
                {
                    //Delete the last character
                    s.delete(length - 1, length);
                }
            }
        });

        packetContentEditText = findViewById(R.id.editTextPacketContent);

        inputEditText = findViewById(R.id.editTextInput);

        TextView deviceView = findViewById(R.id.textViewDevice);

        Intent intent = getIntent();
        device = intent.getParcelableExtra(EXTRA_MESSAGE);

        deviceView.setText(device.getName() + " - " + device.getAddress());

        connect();



        bluetoothHandler = new Handler() {
            public void handleMessage(android.os.Message msg) {
                inputEditText.setText((String)msg.obj);

            }
        };

//        InputStream stream = null;
//        try {
//            stream = socket.getInputStream();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        while(true)
//        {
//            try {
//
//                int t = stream.read();
//
//                int i = stream.read();
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }

        connectedThread = new ConnectedThread(socket);
        connectedThread.start();
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
            outputStream.write(0xFF);
            outputStream.write(bytes);
        }
        catch (IOException e)
        {
            Toast toast = Toast.makeText(getBaseContext(), "Error could not write to output stream", Toast.LENGTH_LONG);
            toast.show();
            finish();
        }

    }

    private class ConnectedThread extends Thread {
        private InputStream inputStream;

        public ConnectedThread(BluetoothSocket socket)
        {
            try{
                inputStream = socket.getInputStream();
            }
            catch (IOException e)
            {
                Toast toast = Toast.makeText(getBaseContext(), "Error could not create input stream", Toast.LENGTH_LONG);
                toast.show();
                finish();
            }

        }

        private void setText(final String value){
            inputEditText.post(new Runnable() {
                @Override
                public void run() {
                    inputEditText.setText(value);
                }
            });
        }

        private long result = 0;
        private byte id = 0;

        @Override
        public void run() {
            Looper.prepare();
            try {

                while(true)
                {
                    while (inputStream.read() != 0xFF) {}

                    id = (byte)inputStream.read();

                    int[] bytes = new int[8];

                    for (byte i = 0; i < 8; i++)
                    {
                        bytes[i] = inputStream.read();
                    }

                    result = 0;
                    for (byte i = 7; i >= 0; i--)
                    {
                        result <<= 8;
                        result |= (long)bytes[i];
                    }

                    Toast.makeText(getBaseContext(), "ID: " + id + " Data: " + result, Toast.LENGTH_LONG).show();



                    setText("ID: " + id + " Data: " + result);



                    bluetoothHandler.obtainMessage(0, "ID: " + id + " Data: " + result + "\n");
                }


            }
            catch (IOException e)
            {
                Toast toast = Toast.makeText(getBaseContext(), "Error could not read input stream", Toast.LENGTH_LONG);
                toast.show();
                finish();
            }



        }
    }
}
