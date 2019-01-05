package com.aatg.elev.airballooncontroller;

import com.aatg.elev.airballooncontroller.bluetooth.BluetoothPacket;

public interface IBluetoothController {

    public boolean isFake();
    public String getDeviceName();
    public String getDeviceAddress();

    public void sendPacket(BluetoothPacket packet);

    public void setPacketHook(IPacketHookListener listener);
    public void removePacketHook(IPacketHookListener listener);
}
