package com.aatg.elev.bluetoothdebugger;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import static com.aatg.elev.bluetoothdebugger.SelectDeviceActivity.EXTRA_MESSAGE;

public class DebugDeviceActivity extends AppCompatActivity {

    private EditText packetIdEditText;
    private EditText packetContentEditText;
    private EditText inputEditText;

    private BluetoothDevice device;
    private BluetoothSocket socket;
    private OutputStream outputStream;
    private ConnectedThread connectedThread;

    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debug_device);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_send);

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

        if (device == null)
        {
            deviceView.setText("Fake device -  AA:BB:CC:DD:EE");

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

            return;
        }


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (isPacketValid())
                {
                    Snackbar.make(view, "Sending packet", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();

                    BluetoothPacket packet = new BluetoothPacket(Integer.parseInt(packetIdEditText.getText().toString(), 16), Long.parseLong(packetContentEditText.getText().toString()));

                    packet.sendPacket(outputStream);
                }
                else
                {
                    Snackbar.make(view, "Invalid packet", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                }


            }
        });

        deviceView.setText(device.getName() + " - " + device.getAddress());

        connect();

        connectedThread = new ConnectedThread(socket);
        connectedThread.activity = this;
        connectedThread.start();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();

        connectedThread.shouldRun = false;

        try {
            connectedThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            outputStream.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private class ConnectedThread extends Thread {
        private InputStream inputStream;

        public boolean shouldRun = true;

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

        private void displayError(final String value){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (!displayErrors) return;

                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(activity);
                    dialogBuilder.setMessage(value)
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })

                            .setNegativeButton("Ignore future errors", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    displayErrors = false;
                                }
                            }).create().show();
                }
            });
        }

        private boolean displayErrors = true;
        Activity activity;

        private BluetoothPacket packet;

        @Override
        public void run() {
            Looper.prepare();


            while(shouldRun)
            {
                try {
                    if (inputStream.available() >= 9)
                    {
                        packet = BluetoothPacket.readPacket(inputStream);

                        if (packet == null){
                            displayError("Could not read packet");
                            continue;
                        }

                        if (packet.id == 2) //error
                        {
                            displayError("The air-balloon sent an error:\n" + BluetoothPacket.ErrorIdToString((int)packet.data));
                        }

                        setText("ID: " + packet.id + " Data: " + packet.data);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }

            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
