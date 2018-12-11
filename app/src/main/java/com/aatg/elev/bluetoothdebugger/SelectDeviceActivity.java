package com.aatg.elev.bluetoothdebugger;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.aatg.elev.bluetoothdebugger.dummy.DeviceContent;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class SelectDeviceActivity extends AppCompatActivity implements ItemFragment.OnListFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_device);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Refreshing bluetooth devices", Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();

                refreshBluetoothDevices();
            }
        });

        refreshBluetoothDevices();

    }

    private void refreshBluetoothDevices(){
        DeviceContent.ITEMS.clear();
        DeviceContent.ITEM_MAP.clear();

        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (mBluetoothAdapter != null)
        {
            if (mBluetoothAdapter.isEnabled())
            {
                Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();

                for(BluetoothDevice bt : pairedDevices)
                {
                    DeviceContent.addItem(new DeviceContent.DeviceItem(bt.getName(), bt.getAddress(), "My name jeff"));
                }
            }
            else
            {
                Toast toast = Toast.makeText(this, "Enable Bluetooth and launch the app again", Toast.LENGTH_LONG);
                toast.show();
            }
        }
        else
        {
            Toast toast = Toast.makeText(this, "You don't have a bluetooth module", Toast.LENGTH_LONG);
            toast.show();

            DeviceContent.addItem(new DeviceContent.DeviceItem("You", "AA:BB:CC:DD:EE", "You don't have a bluetooth module"));
            DeviceContent.addItem(new DeviceContent.DeviceItem("Don't", "AA:BB:CC:DD:EE", "You don't have a bluetooth module"));
            DeviceContent.addItem(new DeviceContent.DeviceItem("Have", "AA:BB:CC:DD:EE", "You don't have a bluetooth module"));
            DeviceContent.addItem(new DeviceContent.DeviceItem("Bluetooth", "AA:BB:CC:DD:EE", "You don't have a bluetooth module"));
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
    public void onListFragmentInteraction(DeviceContent.DeviceItem item) {
        Toast toast = Toast.makeText(this, item.details, Toast.LENGTH_SHORT);
        toast.show();
    }
}
