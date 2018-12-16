package com.aatg.elev.bluetoothdebugger;

import com.aatg.elev.bluetoothdebugger.bluetooth.BluetoothPacket;

public interface IBluetoothController {

    public boolean isFake();
    public String getDeviceName();
    public String getDeviceAddress();

    public void sendPacket(BluetoothPacket packet);

    public void setPacketHook(IPacketHookListener listener);
    public void removePacketHook(IPacketHookListener listener);
}
