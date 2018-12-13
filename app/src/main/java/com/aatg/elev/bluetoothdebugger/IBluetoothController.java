package com.aatg.elev.bluetoothdebugger;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

import java.io.OutputStream;

public interface IBluetoothController {
    public BluetoothDevice getDevice();
    public BluetoothSocket getSocket();
    public OutputStream getOutputStream();

    public void setPacketHook(IPacketHookListener listener);
    public void removePacketHook(IPacketHookListener listener);
}
