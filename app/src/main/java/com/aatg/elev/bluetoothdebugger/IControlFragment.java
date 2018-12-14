package com.aatg.elev.bluetoothdebugger;

import android.support.v4.app.Fragment;

public interface IControlFragment {

    public Integer getPacketId();

    public void handlePacket(BluetoothPacket packet);

    public Fragment getFragment();
}
