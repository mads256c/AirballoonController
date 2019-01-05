package com.aatg.elev.airballooncontroller;

import com.aatg.elev.airballooncontroller.bluetooth.BluetoothPacket;

public interface IPacketHookListener {

    public void getHookedPacket(BluetoothPacket packet);
}
