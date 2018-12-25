package com.aatg.elev.airballooncontroller.selectpaireddevice;

import android.bluetooth.BluetoothDevice;
import android.os.Parcel;
import android.os.Parcelable;

//Used to display bluetooth devices in PairedDeviceItemListFragment
public final class PairedDeviceItem implements Parcelable {
    public final String name;
    public final String mac;
    public final BluetoothDevice device;

    //Creates a new PairedDeviceItem
    public PairedDeviceItem(String name, String mac, BluetoothDevice device) {
        this.name = name;
        this.mac = mac;
        this.device = device;
    }

    //Used to create a PairedDeviceItem from parcel.
    private PairedDeviceItem(Parcel in) {
        name = in.readString();
        mac = in.readString();
        device = in.readParcelable(BluetoothDevice.class.getClassLoader());
    }

    //Creates PairedDeviceItem from parcel.
    public static final Creator<PairedDeviceItem> CREATOR = new Creator<PairedDeviceItem>() {
        @Override
        public PairedDeviceItem createFromParcel(Parcel in) {
            return new PairedDeviceItem(in);
        }

        @Override
        public PairedDeviceItem[] newArray(int size) {
            return new PairedDeviceItem[size];
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

    //Writes the PairedDeviceItem as simple data.
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(mac);
        dest.writeParcelable(device, flags);
    }
}
