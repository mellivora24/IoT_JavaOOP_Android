package com.javaoop.smarthome;

public class Device {
    private String id;
    private String port;
    private String deviceId;
    private String deviceData; // true/false hoặc số
    private String deviceType; // Digital hoặc Analog
    private String deviceName;

    public Device(String id, String port, String deviceId, String deviceData, String deviceType, String deviceName) {
        this.id = id;
        this.port = port;
        this.deviceId = deviceId;
        this.deviceData = deviceData;
        this.deviceType = deviceType;
        this.deviceName = deviceName;
    }

    public String getId() {
        return id;
    }

    public String getPort() {
        return port;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public String getDeviceData() {
        return deviceData;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }
}