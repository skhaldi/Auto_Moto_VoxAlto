package com.voxalto.automotovoxalto;

/**
 * Created by Sabah on 5/6/2016.
 */
public class Car  {

    private int id;
    private String vin;
    private String make;
    private String model;
    private int year;
    private String engineOilType;
    private String engineCoolantType;
    private String brakeType;
    private String steeringType;

    public Car() {
    }

    public Car(int id, String vin, String make, String model, int year,
               String engineOilType, String engineCoolantType, String brakeType, String steeringType) {
        this.id    = id;
        this.vin   = vin;
        this.make  = make;
        this.model = model;
        this.year  = year;
        this.engineOilType = engineOilType;
        this.engineCoolantType = engineCoolantType;
        this.brakeType = brakeType;
        this.steeringType = steeringType;
    }

    public void setId(int id) { this.id = id; }
    public int getId() { return id; }

    public void setVin(String vin) {
        this.vin = vin;
    }
    public String getVin() {
        return vin;
    }

    public void setMake(String make) {
        this.make = make;
    }
    public String getMake() {
        return make;
    }

    public void setModel(String model) {
        this.model = model;
    }
    public String getModel() {
        return model;
    }

    public void setYear(int year) {
        this.year = year;
    }
    public int getYear() {
        return year;
    }

    public void setEngineOilType(String engineOilType) {
        this.engineOilType = engineOilType;
    }
    public String getEngineOilType() {
        return engineOilType;
    }

    public void setEngineCoolantType(String engineCoolantType) {
        this.engineCoolantType = engineCoolantType;
    }
    public String getEngineCoolantType() {
        return engineCoolantType;
    }

    public void setBrakeType(String brakeType) {
        this.brakeType = brakeType;
    }
    public String getBrakeType() {
        return brakeType;
    }

    public void setSteeringType(String steeringType) {
        this.steeringType = steeringType;
    }
    public String getSteeringType() {  return steeringType; }
}
