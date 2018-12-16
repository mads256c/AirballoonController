package com.aatg.elev.bluetoothdebugger.selectpaireddevice;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.aatg.elev.bluetoothdebugger.ControlDeviceActivity;
import com.aatg.elev.bluetoothdebugger.R;

import java.util.ArrayList;
import java.util.Set;

public final class SelectPairedDeviceActivity extends AppCompatActivity implements PairedDeviceItemListFragment.OnPairedDeviceItemClickedListener {

    //To send the bluetooth device to ControlDeviceActivity we need extra intent data. We basically tie the data to this string and send it.
    public static final String INTENT_MESSAGE_DEVICE = "com.aatg.elev.bluetoothdebugger.INTENT_MESSAGE_DEVICE";

    //onCreate gets called every time a layout change happens (mostly).
    //We don't want to get all our bluetooth devices every time. So we save them in a bundle.
    //This string is used to save and get the bluetooth devices from the bundle.
    private static final String SAVEDSTATE_ITEMS = "SAVEDSTATE_ITEMS";

    //Is responsible for creating the list of bluetooth devices.
    private PairedDeviceItemListFragment pairedDeviceItemListFragment;

    //Holds our bluetooth devices.
    private ArrayList<PairedDeviceItem> items;


    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();

            // It means the user has changed his bluetooth state.
            if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {

                refreshBluetoothDevices();
                pairedDeviceItemListFragment.updateView(items);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_paired_device);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Get our PairedDeviceItemListFragment from the XML.
        pairedDeviceItemListFragment = (PairedDeviceItemListFragment) getSupportFragmentManager().findFragmentById(R.id.fragment);



        //Get refresh devices fab from XML and set its onClickListener.
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Refreshing bluetooth devices", Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();

                refreshBluetoothDevices();
                pairedDeviceItemListFragment.updateView(items);
            }
        });

        //If savedInstanceState is null we are creating a fresh Activity,
        //and we need to get the bluetooth devices.
        if (savedInstanceState == null)
            refreshBluetoothDevices();
        //Else get items from the bundle.
        else
            items = savedInstanceState.getParcelableArrayList(SAVEDSTATE_ITEMS);

        //Update the PairedDeviceItemListFragment using the data.
        pairedDeviceItemListFragment.updateView(items);

        this.registerReceiver(broadcastReceiver, new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED));
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        // Save UI state changes to the savedInstanceState.
        // This bundle will be passed to onCreate if the process is
        // killed and restarted.

        //Save our bluetooth devices.
        savedInstanceState.putParcelableArrayList(SAVEDSTATE_ITEMS, items);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // Restore UI state from the savedInstanceState.
        // This bundle has also been passed to onCreate.

        //Restore our bluetooth devices.
        items = savedInstanceState.getParcelableArrayList(SAVEDSTATE_ITEMS);
    }


    //Refreshes the bluetooth devices.
    private void refreshBluetoothDevices(){

        items = new ArrayList<>();

        //Get the devices bluetooth adapter
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        //If the bluetooth adapter is null the device does not have bluetooth eg. the emulator.
        if (mBluetoothAdapter != null)
        {
            //Check if the bluetooth is enabled
            if (mBluetoothAdapter.isEnabled())
            {
                //Get all paired bluetooth devices
                Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();

                //Add bluetooth devices to items.
                for(BluetoothDevice bt : pairedDevices)
                {
                    items.add(new PairedDeviceItem(bt.getName(), bt.getAddress(), bt));
                }
            }
            //If the bluetooth is not enabled create fake devices.
            else
            {
                Toast.makeText(this, "Enable Bluetooth", Toast.LENGTH_LONG).show();

                items.add(new PairedDeviceItem("You", "AA:BB:CC:DD:EE:FF", null));
                items.add(new PairedDeviceItem("Don't", "AA:BB:CC:DD:EE:FF", null));
                items.add(new PairedDeviceItem("Have", "AA:BB:CC:DD:EE:FF", null));
                items.add(new PairedDeviceItem("Bluetooth", "AA:BB:CC:DD:EE:FF", null));
                items.add(new PairedDeviceItem("Enabled", "AA:BB:CC:DD:EE:FF", null));
            }
        }
        //If the bluetooth does not exist create fake devices.
        else
        {
            Toast.makeText(this, "You don't have a bluetooth module", Toast.LENGTH_LONG).show();

            items.add(new PairedDeviceItem("You", "AA:BB:CC:DD:EE:FF", null));
            items.add(new PairedDeviceItem("Don't", "AA:BB:CC:DD:EE:FF", null));
            items.add(new PairedDeviceItem("Have", "AA:BB:CC:DD:EE:FF", null));
            items.add(new PairedDeviceItem("Bluetooth", "AA:BB:CC:DD:EE:FF", null));
        }
    }

    //TODO: Implement settings menu or remove this
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_select_device, menu);
        return true;
    }

    //TODO: Implement settings menu or remove this
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //Called when a bluetooth device is pressed
    @Override
    public void onPairedDeviceItemClicked(PairedDeviceItem item) {

        if (item.device == null)
        {
            Toast.makeText(this, "This is a null device", Toast.LENGTH_SHORT).show();
        }

        //Start ControlDeviceActivity
        Intent intent = new Intent(this, ControlDeviceActivity.class);
        intent.putExtra(INTENT_MESSAGE_DEVICE, item.device); //Put the device in the intent extra data

        startActivity(intent);
    }

}
