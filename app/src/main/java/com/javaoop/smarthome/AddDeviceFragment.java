package com.javaoop.smarthome;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

public class AddDeviceFragment extends DialogFragment {

    private DeviceViewModel deviceViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_device, container, false);

        EditText deviceNameInput = view.findViewById(R.id.deviceNameInput);
        Spinner deviceTypeSpinner = view.findViewById(R.id.deviceTypeSpinner);
        EditText portInput = view.findViewById(R.id.portInput);
        Button addDeviceButton = view.findViewById(R.id.addDeviceButton);
        Button cancelButton = view.findViewById(R.id.cancelButton);

        deviceViewModel = new ViewModelProvider(requireActivity()).get(DeviceViewModel.class);

        addDeviceButton.setOnClickListener(v -> {
            String deviceName = deviceNameInput.getText().toString().trim();
            String deviceType = deviceTypeSpinner.getSelectedItem().toString();
            String port = portInput.getText().toString().trim();

            if (!deviceName.isEmpty() && !port.isEmpty()) {
                Device newDevice = new Device(deviceName, deviceType, port);
                deviceViewModel.addDevice(newDevice);
                Toast.makeText(getActivity(), "Đã thêm thiết bị: " + deviceName, Toast.LENGTH_SHORT).show();
                dismiss();
            } else {
                Toast.makeText(getActivity(), "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            }
        });

        cancelButton.setOnClickListener(v -> dismiss());

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() != null) {
            getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }
}