package com.aatg.elev.bluetoothdebugger.dummy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DeviceItem {
    public final String name;
    public final String mac;
    public final String details;

    public DeviceItem(String name, String mac, String details) {
        this.name = name;
        this.mac = mac;
        this.details = details;
    }

    @Override
    public String toString() {
        return mac;
    }
}
