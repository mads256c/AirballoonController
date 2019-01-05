package com.aatg.elev.airballooncontroller.bluetooth;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

//Holds all the bluetooth packet stuff.
public final class BluetoothPacket {
    public static int packetLength = 9;

    public byte id;
    public long data;

    public BluetoothPacket(byte id, long data)
    {
        this.id = id;
        this.data = data;
    }

    public BluetoothPacket(int id, long data)
    {
        this.id = (byte)id;
        this.data = data;
    }

    public BluetoothPacket(int[] bytes)
    {
        if (bytes.length != packetLength) throw new IllegalArgumentException("bytes.length != " + packetLength);

        this.id = (byte)bytes[0];
        this.data = 0;
        for (byte i = 8; i >= 1; i--)
        {
            this.data <<= 8;
            this.data |= (long)bytes[i];
        }
    }

    public BluetoothPacket(byte id, int[] bytes){
        if (bytes.length != packetLength - 1) throw new IllegalArgumentException("bytes.length != " + (packetLength - 1));

        this.id = id;
        this.data = 0;
        for (byte i = 7; i >= 0; i--)
        {
            this.data <<= 8;
            this.data |= (long)bytes[i];
        }
    }
    //Gets data as a byte array
    public byte[] getDataBytes(){

        byte[] bytes = new byte[8];

        bytes[0] = (byte) (data & 0xFF);
        bytes[1] = (byte) ((data >> 8) & 0xFF);
        bytes[2] = (byte) ((data >> 16) & 0xFF);
        bytes[3] = (byte) ((data >> 24) & 0xFF);
        bytes[4] = (byte) ((data >> 32) & 0xFF);
        bytes[5] = (byte) ((data >> 40) & 0xFF);
        bytes[6] = (byte) ((data >> 48) & 0xFF);
        bytes[7] = (byte) ((data >> 56) & 0xFF);

        return bytes;
    }

    //Gets the whole packet as byte array
    public byte[] getPacketBytes(){
        byte[] bytes = new byte[9];

        bytes[0] = id;

        bytes[1] = (byte) (data & 0xFF);
        bytes[2] = (byte) ((data >> 8) & 0xFF);
        bytes[3] = (byte) ((data >> 16) & 0xFF);
        bytes[4] = (byte) ((data >> 24) & 0xFF);
        bytes[5] = (byte) ((data >> 32) & 0xFF);
        bytes[6] = (byte) ((data >> 40) & 0xFF);
        bytes[7] = (byte) ((data >> 48) & 0xFF);
        bytes[8] = (byte) ((data >> 56) & 0xFF);

        return bytes;
    }

    //Sends the packet using an OutputStream.
    //Returns false if it failed.
    public boolean sendPacket(OutputStream stream)
    {
        try {
            stream.write(0xFF); //Start of packet marker.
            stream.write(getPacketBytes());

            return true;
        }
        catch(IOException e)
        {
            return false;
        }
    }

    @Override
    public String toString() {
        return "ID: " + id + " Data: " + data;
    }

    //Creates a BluetoothPacket from an InputStream
    public static BluetoothPacket readPacket(InputStream stream)
    {
        try {
            while(stream.read() != 0xFF) {} //Wait for the start of a packet.


            byte id = (byte)stream.read();

            int[] bytes = new int[8];

            for (byte i = 0; i < 8; i++)
            {
                bytes[i] = stream.read();
            }

            return new BluetoothPacket(id, bytes);
        }
        catch(IOException e)
        {
            return null;
        }
    }

    //Converts an error packet to a string.
    public static String ErrorIdToString(long data)
    {
        switch ((int)data)
        {
            case 0:
                return "General failure";

            case 1:
                return "Heartbeat timeout";

            case 2:
                return "Unknown packet";

            case 3:
                return "Invalid request";

            default:
                return "Unknown error";
        }
    }
}
