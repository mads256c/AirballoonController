package com.aatg.elev.bluetoothdebugger;

import com.aatg.elev.bluetoothdebugger.bluetooth.BluetoothPacket;

public interface IPacketHandler
{
    public void handlePacket(final BluetoothPacket packet);
}
