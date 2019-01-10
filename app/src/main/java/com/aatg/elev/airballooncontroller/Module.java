package com.aatg.elev.airballooncontroller;

import android.os.Parcel;
import android.os.Parcelable;

public final class Module implements Parcelable {
    public String name;
    public String type;
    public String port;

    public Module(String name, String type, String port) {
        this.name = name;
        this.type = type;
        this.port = port;
    }

    public  String getName(){return name;}

    public String getType(){return type;}

    public int getPort(){return Integer.parseInt(port);}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(type);
        dest.writeString(port);
    }
}
