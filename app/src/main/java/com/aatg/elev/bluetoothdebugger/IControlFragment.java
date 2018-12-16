package com.aatg.elev.bluetoothdebugger;

import android.support.v4.app.Fragment;

import com.aatg.elev.bluetoothdebugger.bluetooth.BluetoothPacket;

public interface IControlFragment {

    public Integer getPacketId();

    public void handlePacket(BluetoothPacket packet);

    public Fragment getFragment();
}
