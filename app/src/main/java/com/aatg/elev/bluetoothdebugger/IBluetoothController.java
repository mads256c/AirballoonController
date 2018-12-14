package com.aatg.elev.bluetoothdebugger;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

import java.io.OutputStream;

public interface IBluetoothController {

    public boolean isFake();
    public String getDeviceName();
    public String getDeviceAdress();

    public void sendPacket(BluetoothPacket packet);

    public void setPacketHook(IPacketHookListener listener);
    public void removePacketHook(IPacketHookListener listener);
}
