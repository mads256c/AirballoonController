package com.aatg.elev.airballooncontroller.bluetooth;

import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.ConcurrentLinkedQueue;

public final class OutputThread extends Thread {

    private volatile boolean keepRunning = true;

    private OutputStream stream;

    private ConcurrentLinkedQueue<BluetoothPacket> bluetoothPackets;

    public OutputThread()
    {
        bluetoothPackets = new ConcurrentLinkedQueue<>();
    }

    @Override
    public void run()
    {
        if (stream == null) throw new IllegalStateException("setStream should be called before attempting to start the thread");

        while (keepRunning)
        {
            if (!bluetoothPackets.isEmpty())
            {
                BluetoothPacket packet = bluetoothPackets.poll();
                packet.sendPacket(stream);
            }
            else
            {
                try {
                    sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        close();
    }

    public void add(BluetoothPacket packet)
    {
        bluetoothPackets.add(packet);
    }

    public void setStream(OutputStream stream)
    {
        if (stream == null) throw new IllegalArgumentException("stream cannot be null");
        if (this.stream != null) throw new IllegalStateException("Stream was already set");

        this.stream = stream;
    }

    public void stopThread(){
        if (!keepRunning) throw new IllegalStateException("Thread is already stopping or has stopped");

        keepRunning = false;
    }

    private void close(){
        try {
            stream.close();
        } catch (IOException ignored) { }
    }
}
