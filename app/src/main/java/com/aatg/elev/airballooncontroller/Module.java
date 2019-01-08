package com.aatg.elev.bluetoothdebugger;

public final class Module {
    public String name;
    public String type;
    public int port;

    public Module(String name, String type, int port) {
        this.name = name;
        this.type = type;
        this.port = port;
    }
}
