package com.aatg.elev.bluetoothdebugger.bluetooth;


import android.os.Looper;

import com.aatg.elev.bluetoothdebugger.IPacketHandler;

import java.io.IOException;
import java.io.InputStream;

public final class InputThread extends Thread {

    private volatile boolean keepRunning = true;

    private InputStream stream;
    private IPacketHandler packetHandler;

    public InputThread(IPacketHandler packetHandler) {
        if (packetHandler == null) throw new IllegalArgumentException("packetHandler cannot be null");

        this.packetHandler = packetHandler;
    }

    @Override
    public void run(){
        if (stream == null) throw new IllegalStateException("setStream should be called before attempting to start the thread");

        Looper.prepare();

        while (keepRunning)
        {
            try {
                if(stream.available() >= BluetoothPacket.packetLength){
                    final BluetoothPacket packet = BluetoothPacket.readPacket(stream);

                    if (packet == null) continue;

                    packetHandler.handlePacket(packet);

                }
                else {
                    sleep(1);
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }

        close();
    }

    public void setStream(InputStream stream)
    {
        if (stream == null) throw new IllegalArgumentException("stream cannot be null");
        if (this.stream != null) throw new IllegalStateException("Stream was already set");

        this.stream = stream;
    }

    public void stopThread() {
        if (!keepRunning) throw new IllegalStateException("Thread is already stopping or has stopped");

        keepRunning = false;
    }

    private void close(){
        try {
            stream.close();
        } catch (IOException ignored) { }

        packetHandler = null;
    }
}
