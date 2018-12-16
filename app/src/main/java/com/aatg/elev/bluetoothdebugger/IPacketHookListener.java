package com.aatg.elev.bluetoothdebugger;

import com.aatg.elev.bluetoothdebugger.bluetooth.BluetoothPacket;

public interface IPacketHookListener {

    public void getHookedPacket(BluetoothPacket packet);
}
