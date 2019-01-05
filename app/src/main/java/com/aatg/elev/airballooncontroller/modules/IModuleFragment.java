package com.aatg.elev.airballooncontroller.modules;

import android.support.v4.app.Fragment;

import com.aatg.elev.airballooncontroller.bluetooth.BluetoothPacket;

public interface IModuleFragment {

    public Integer getPacketId();

    public void handlePacket(BluetoothPacket packet);

    public Fragment getFragment();
}
