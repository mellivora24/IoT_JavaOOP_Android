package com.javaoop.smarthome;

public class Device {
    private String name;
    private String type;
    private String port;

    public Device(String name, String type, String port) {
        this.name = name;
        this.type = type;
        this.port = port;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getPort() {
        return port;
    }
}