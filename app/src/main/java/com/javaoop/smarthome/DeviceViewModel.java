package com.javaoop.smarthome;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class DeviceViewModel extends ViewModel {
    private final MutableLiveData<List<Device>> devices = new MutableLiveData<>(new ArrayList<>());

    public LiveData<List<Device>> getDevices() {
        return devices;
    }

    public void addDevice(Device device) {
        List<Device> currentDevices = devices.getValue();
        currentDevices.add(device);
        devices.setValue(currentDevices);
    }
}
