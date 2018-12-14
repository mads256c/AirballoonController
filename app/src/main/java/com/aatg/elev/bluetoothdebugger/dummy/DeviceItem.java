package com.aatg.elev.bluetoothdebugger.dummy;

import android.bluetooth.BluetoothDevice;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DeviceItem implements Parcelable {
    public final String name;
    public final String mac;
    public final BluetoothDevice device;

    public DeviceItem(String name, String mac, BluetoothDevice device) {
        this.name = name;
        this.mac = mac;
        this.device = device;
    }

    protected DeviceItem(Parcel in) {
        name = in.readString();
        mac = in.readString();
        device = in.readParcelable(BluetoothDevice.class.getClassLoader());
    }

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
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(mac);
        dest.writeParcelable(device, flags);
    }
}
