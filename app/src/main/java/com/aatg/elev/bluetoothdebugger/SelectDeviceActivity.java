package com.aatg.elev.bluetoothdebugger;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.aatg.elev.bluetoothdebugger.dummy.DeviceItem;

import java.util.ArrayList;
import java.util.Set;

public class SelectDeviceActivity extends AppCompatActivity implements ItemFragment.OnListFragmentInteractionListener {

    public static final String INTENT_MESSAGE_DEVICE = "com.aatg.elev.bluetoothdebugger.INTENT_MESSAGE_DEVICE";

    private static final String SAVEDSTATE_ITEMS = "SAVEDSTATE_ITEMS";

    private ItemFragment itemFragment;

    private ArrayList<DeviceItem> items;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_device);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        itemFragment = (ItemFragment) getSupportFragmentManager().findFragmentById(R.id.fragment);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Refreshing bluetooth devices", Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();

                refreshBluetoothDevices();
                itemFragment.updateView(items);
            }
        });

        if (savedInstanceState == null)
            refreshBluetoothDevices();
        else items = savedInstanceState.getParcelableArrayList(SAVEDSTATE_ITEMS);

        itemFragment.updateView(items);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        // Save UI state changes to the savedInstanceState.
        // This bundle will be passed to onCreate if the process is
        // killed and restarted.
        savedInstanceState.putParcelableArrayList(SAVEDSTATE_ITEMS, items);
        // etc.
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // Restore UI state from the savedInstanceState.
        // This bundle has also been passed to onCreate.
        items = savedInstanceState.getParcelableArrayList(SAVEDSTATE_ITEMS);
    }

    private void refreshBluetoothDevices(){

        items = new ArrayList<>();

        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (mBluetoothAdapter != null)
        {
            if (mBluetoothAdapter.isEnabled())
            {
                Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();

                for(BluetoothDevice bt : pairedDevices)
                {
                    items.add(new DeviceItem(bt.getName(), bt.getAddress(), bt));
                }
            }
            else
            {
                Toast toast = Toast.makeText(this, "Enable Bluetooth", Toast.LENGTH_LONG);
                toast.show();

                items.add(new DeviceItem("You", "AA:BB:CC:DD:EE:FF", null));
                items.add(new DeviceItem("Don't", "AA:BB:CC:DD:EE:FF", null));
                items.add(new DeviceItem("Have", "AA:BB:CC:DD:EE:FF", null));
                items.add(new DeviceItem("Bluetooth", "AA:BB:CC:DD:EE:FF", null));
                items.add(new DeviceItem("Enabled", "AA:BB:CC:DD:EE:FF", null));
            }
        }
        else
        {
            Toast toast = Toast.makeText(this, "You don't have a bluetooth module", Toast.LENGTH_LONG);
            toast.show();

            items.add(new DeviceItem("You", "AA:BB:CC:DD:EE:FF", null));
            items.add(new DeviceItem("Don't", "AA:BB:CC:DD:EE:FF", null));
            items.add(new DeviceItem("Have", "AA:BB:CC:DD:EE:FF", null));
            items.add(new DeviceItem("Bluetooth", "AA:BB:CC:DD:EE:FF", null));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_select_device, menu);
        return true;
    }

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

    @Override
    public void onListFragmentInteraction(DeviceItem item) {


        if (item.device == null)
        {
            Toast.makeText(this, "This is a null device", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(this, item.device.toString(), Toast.LENGTH_SHORT).show();
        }


        Intent intent = new Intent(this, ControlDeviceActivity.class);
        intent.putExtra(INTENT_MESSAGE_DEVICE, item.device);

        startActivity(intent);
    }

}
