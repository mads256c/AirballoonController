package com.aatg.elev.airballooncontroller.dataconverters;

public class UltraSonicDistanceConverter extends BaseDataConverter {

    public enum DistanceUnit {
        Meters,
        Centimeters,
        Inches
    }
    private static final double M = 2800.0;
    private static final double CM = 28.0;
    private static final double INC = 71.0;

    private DistanceUnit distanceUnit;

    public UltraSonicDistanceConverter(DistanceUnit unit)
    {
        if (unit == null) throw new IllegalArgumentException("unit cannot be null");

        distanceUnit = unit;
    }

    @Override
    public Number getData(long data) {
        switch (distanceUnit)
        {

            case Meters:
                return (double)data / M / 2.0;
            case Centimeters:
                return (double)data / CM / 2.0;
            case Inches:
                return (double)data / INC / 2.0;

            default:
                throw new IllegalStateException("distanceUnit is not valid");
        }
    }
}
