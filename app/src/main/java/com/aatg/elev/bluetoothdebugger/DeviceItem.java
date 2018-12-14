package com.aatg.elev.bluetoothdebugger;

import android.bluetooth.BluetoothDevice;
import android.os.Parcel;
import android.os.Parcelable;

//Used to display bluetooth devices in ItemFragment
public final class DeviceItem implements Parcelable {
    public final String name;
    public final String mac;
    public final BluetoothDevice device;

    //Creates a new DeviceItem
    public DeviceItem(String name, String mac, BluetoothDevice device) {
        this.name = name;
        this.mac = mac;
        this.device = device;
    }

    //Used to create a DeviceItem from parcel.
    private DeviceItem(Parcel in) {
        name = in.readString();
        mac = in.readString();
        device = in.readParcelable(BluetoothDevice.class.getClassLoader());
    }

    //Creates DeviceItem from parcel.
    public static final Creator<DeviceItem> CREATOR = new Creator<DeviceItem>() {
        @Override
        public DeviceItem createFromParcel(Parcel in) {
            return new DeviceItem(in);
        }

        @Override
        public DeviceItem[] newArray(int size) {
            return new DeviceItem[size];
        }
    };

    @Override
    public String toString() {
        return mac;
    }

    @Override
    public int describeContents() {
        return 0; //Not used.
    }

    //Writes the DeviceItem as simple data.
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(mac);
        dest.writeParcelable(device, flags);
    }
}
