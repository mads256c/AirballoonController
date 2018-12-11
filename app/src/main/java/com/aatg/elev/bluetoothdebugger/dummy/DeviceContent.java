package com.aatg.elev.bluetoothdebugger.dummy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class DeviceContent {

    /**
     * An array of sample (dummy) items.
     */
    public static final List<DeviceItem> ITEMS = new ArrayList<DeviceItem>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, DeviceItem> ITEM_MAP = new HashMap<String, DeviceItem>();

    public static void addItem(DeviceItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.name, item);
    }


    /**
     * A dummy item representing a piece of content.
     */
    public static class DeviceItem {
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
}
