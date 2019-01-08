package com.aatg.elev.airballooncontroller;

import android.bluetooth.BluetoothDevice;
import android.os.Parcel;
import android.os.Parcelable;

import com.aatg.elev.airballooncontroller.selectpaireddevice.PairedDeviceItem;

public final class ModuleItem {
    public final String name;
    public final String type;
    public final int port;

    //Creates a new PairedDeviceItem
    public ModuleItem(String name, String type, int port) {
        this.name = name;
        this.type = type;
        this.port = port;
    }

    @Override
    public String toString() {
        return name;
    }

}
