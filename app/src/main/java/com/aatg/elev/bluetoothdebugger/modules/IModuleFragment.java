package com.aatg.elev.bluetoothdebugger.modules;

import android.support.v4.app.Fragment;

import com.aatg.elev.bluetoothdebugger.bluetooth.BluetoothPacket;

public interface IModuleFragment {

    public Integer getPacketId();

    public void handlePacket(BluetoothPacket packet);

    public Fragment getFragment();
}
