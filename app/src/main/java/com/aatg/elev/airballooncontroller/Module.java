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

    public Module (Parcel in){
        name = in.readString();
        type = in.readString();
        port = in.readString();
    }

    public  String getName(){return name;}

    public String getType(){return type;}

    public int getPort(){return Integer.parseInt(port);}

    public static final Creator<Module> CREATOR = new Creator<Module>() {
        @Override
        public Module createFromParcel(Parcel in) {
            return new Module(in);
        }

        @Override
        public Module[] newArray(int size) {
            return new Module[size];
        }
    };

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
