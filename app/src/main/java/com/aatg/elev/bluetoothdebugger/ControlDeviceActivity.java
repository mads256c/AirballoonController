package com.aatg.elev.bluetoothdebugger;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

import static com.aatg.elev.bluetoothdebugger.SelectDeviceActivity.EXTRA_MESSAGE;

public class ControlDeviceActivity extends AppCompatActivity implements TriggerFragment.OnFragmentInteractionListener {


    private BluetoothDevice device;
    private BluetoothSocket socket;
    private OutputStream outputStream;


    private Activity activity;

    private BottomNavigationView navigation;

    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_add:

                    return true;


                case R.id.navigation_control:


                    return true;


                case R.id.navigation_debug:
                    navigation.setSelectedItemId(R.id.navigation_control);
                    Intent intent = new Intent(activity, DebugDeviceActivity.class);
                    intent.putExtra(EXTRA_MESSAGE, device);

                    startActivity(intent);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control_device);

        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setSelectedItemId(R.id.navigation_control);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        Intent intent = getIntent();
        device = intent.getParcelableExtra(EXTRA_MESSAGE);

        activity = this;

        connect();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        try {
            outputStream.close();
            socket.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onFragmentInteraction(BluetoothPacket packet) {
        Toast.makeText(this, packet.toString(), Toast.LENGTH_SHORT).show();
        packet.sendPacket(outputStream);
    }



    private void connect()
    {
        try {
            socket = device.createRfcommSocketToServiceRecord(MY_UUID);
            outputStream = socket.getOutputStream();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
