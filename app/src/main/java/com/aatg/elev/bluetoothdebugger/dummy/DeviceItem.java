package com.aatg.elev.bluetoothdebugger.dummy;

import android.bluetooth.BluetoothDevice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DeviceItem {
    public final String name;
    public final String mac;
    public final BluetoothDevice device;

    public DeviceItem(String name, String mac, BluetoothDevice device) {
        this.name = name;
        this.mac = mac;
        this.device = device;
    }

    @Override
    public String toString() {
        return mac;
    }
}
