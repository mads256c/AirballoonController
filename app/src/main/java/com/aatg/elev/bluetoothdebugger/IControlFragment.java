package com.aatg.elev.bluetoothdebugger;

public interface IControlFragment {

    public Integer getPacketId();

    public void handlePacket(BluetoothPacket packet);
}
