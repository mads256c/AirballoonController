package com.aatg.elev.airballooncontroller;

import com.aatg.elev.airballooncontroller.bluetooth.BluetoothPacket;

public interface IPacketHandler
{
    public void handlePacket(final BluetoothPacket packet);
}
