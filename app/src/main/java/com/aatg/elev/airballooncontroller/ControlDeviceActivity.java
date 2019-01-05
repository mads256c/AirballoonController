package com.aatg.elev.airballooncontroller;

import android.app.AlertDialog;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.aatg.elev.airballooncontroller.R;
import com.aatg.elev.airballooncontroller.bluetooth.BluetoothPacket;
import com.aatg.elev.airballooncontroller.bluetooth.InputThread;
import com.aatg.elev.airballooncontroller.bluetooth.OutputThread;
import com.aatg.elev.airballooncontroller.modules.IModuleFragment;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.aatg.elev.airballooncontroller.selectpaireddevice.SelectPairedDeviceActivity.INTENT_MESSAGE_DEVICE;

public class ControlDeviceActivity extends AppCompatActivity implements IBluetoothController, IPacketHandler {


    private BluetoothDevice device;
    private BluetoothSocket socket;

    private AddViewFragment addViewFragment = new AddViewFragment();
    private ControlViewFragment controlViewFragment = new ControlViewFragment();
    private DebugViewFragment debugViewFragment = new DebugViewFragment();

    private FragmentManager fragmentManager;

    private ConnectAsyncTask connectAsyncTask;

    private InputThread inputThread;
    private OutputThread outputThread;

    private List<IPacketHookListener> packetHookListeners = new ArrayList<>();

    private boolean ignoreErrors = false;

    private static final UUID BLUETOOTH_SERIAL_COMMUNICATION_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    private Snackbar connectionStatusSnackbar;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_add:
                    showAddFragment();
                    return true;


                case R.id.navigation_control:
                    showControlFragment();
                    return true;


                case R.id.navigation_debug:
                    showDebugFragment();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control_device);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setSelectedItemId(R.id.navigation_control);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().add(R.id.master_layout, controlViewFragment).commit();

        Intent intent = getIntent();
        device = intent.getParcelableExtra(INTENT_MESSAGE_DEVICE);

        if (isFake()) return;

        inputThread = new InputThread(this);

        outputThread = new OutputThread();

        connect();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (isFake()) return;

        if (connectAsyncTask.getStatus() != AsyncTask.Status.FINISHED)
        {
            connectAsyncTask.cancel(true);
            connectAsyncTask = null;
        }

        inputThread.stopThread();

        try {
            inputThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        outputThread.stopThread();

        try {
            outputThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            if (socket != null)
                socket.close();
        }
        catch (IOException e)
        {
            showError("Could not close output stream and socket", false);
        }
    }

    private void showAddFragment(){
        fragmentManager.beginTransaction().replace(R.id.master_layout, addViewFragment).commit();

    }

    private void showControlFragment(){
        fragmentManager.beginTransaction().replace(R.id.master_layout, controlViewFragment).commit();
    }

    private void showDebugFragment(){
        fragmentManager.beginTransaction().replace(R.id.master_layout, debugViewFragment).commit();
    }


    private void connect()
    {
        if (isFake()) return;

        connectAsyncTask = new ConnectAsyncTask(this);
        connectAsyncTask.execute(device);

        connectionStatusSnackbar = Snackbar.make(findViewById(R.id.master_layout), "Connecting...", Snackbar.LENGTH_INDEFINITE)
                .setAction("Action", null);
        connectionStatusSnackbar.show();
    }

    private AlertDialog showError(String message) {
        return showError(message, true);
    }

    private AlertDialog showError(String message, final boolean ignorable){

        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);

        alertBuilder.setTitle(R.string.error)
                .setMessage(message)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (!ignorable) finish();
                    }
                });

        if (ignorable)
        {
            if (!ignoreErrors) {

                alertBuilder.setNegativeButton(R.string.ignore_future_errors, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ignoreErrors = true;
                    }
                });
            }
        }

        return alertBuilder.show();
    }

    @Override
    public void handlePacket(final BluetoothPacket packet)
    {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                for (IPacketHookListener listener :
                        packetHookListeners) {
                    listener.getHookedPacket(packet);
                }

                if (packet.id == 2){

                    showError("Air balloon reported error: " + BluetoothPacket.ErrorIdToString((int)packet.data));

                    return;
                }

                for (IModuleFragment fragment :
                        controlViewFragment.controlFragments) {
                    if (fragment.getPacketId() != null && fragment.getPacketId() == packet.id)
                    {
                        fragment.handlePacket(packet);
                    }
                }
            }
        });
    }

    @Override
    public boolean isFake() {
        return device == null;
    }

    @Override
    public String getDeviceName() {
        if (isFake()) return "Fake Device";

        return device.getName();
    }

    @Override
    public String getDeviceAddress() {
        if (isFake()) return "AA:BB:CC:DD:EE:FF";

        return device.getAddress();
    }

    @Override
    public void sendPacket(BluetoothPacket packet) {
        if (!isFake()) outputThread.add(packet);
    }

    @Override
    public void setPacketHook(IPacketHookListener listener) {
        packetHookListeners.add(listener);
    }

    @Override
    public void removePacketHook(IPacketHookListener listener) {
        packetHookListeners.remove(listener);
    }

    //Connects to the socket using async, so we don't hang the UI thread waiting for the connection.
    private static class ConnectAsyncTask extends AsyncTask<BluetoothDevice, Void, BluetoothSocket> {

        private WeakReference<ControlDeviceActivity> weakReference;

        ConnectAsyncTask(ControlDeviceActivity activity)
        {
            weakReference = new WeakReference<>(activity);
        }

        @Override
        protected BluetoothSocket doInBackground(BluetoothDevice... bluetoothDevices) {
            try {
                BluetoothSocket socket = bluetoothDevices[0].createInsecureRfcommSocketToServiceRecord(BLUETOOTH_SERIAL_COMMUNICATION_UUID);
                socket.connect();

                return socket;
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(BluetoothSocket bluetoothSocket)
        {
            ControlDeviceActivity activity = weakReference.get();

            if (activity == null || activity.isFinishing()) return;

            if (bluetoothSocket == null)
            {
                activity.showError("Could not create socket", false);
                return;
            }

            activity.socket = bluetoothSocket;
            try {
                activity.inputThread.setStream(activity.socket.getInputStream());
            } catch (IOException e) {
                activity.showError("Could not get input stream", false);
            }
            activity.inputThread.start();
            try {
                activity.outputThread.setStream(activity.socket.getOutputStream());
            } catch (IOException e) {
                activity.showError("Could not get output stream", false);
            }
            activity.outputThread.start();

            activity.connectionStatusSnackbar.dismiss();

            Snackbar snackbar = Snackbar.make(activity.findViewById(R.id.master_layout), "Connected", Snackbar.LENGTH_LONG)
                    .setAction("Action", null);
            snackbar.show();

            activity.connectionStatusSnackbar = snackbar;
        }
    }
}
