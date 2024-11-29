package com.javaoop.smarthome;

import static com.javaoop.smarthome.R.drawable.top_bar_background;

import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.List;

public class ButtonlistFragment extends Fragment {

    private LinearLayout buttonContainer;
    private DeviceViewModel deviceViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_buttonlist, container, false);
        buttonContainer = view.findViewById(R.id.buttonContainer);
        deviceViewModel = new ViewModelProvider(requireActivity()).get(DeviceViewModel.class);
        deviceViewModel.removeAllDevices();
        deviceViewModel.fetchDataFromAPI(getContext());
        deviceViewModel.getDevices().observe(getViewLifecycleOwner(), devices -> {
            buttonContainer.removeAllViews();
            for (Device device : devices) {
                createButton(device);
            }
        });

        Button createButton = view.findViewById(R.id.createButton);
        createButton.setOnClickListener(v -> {
            AddDeviceFragment addDeviceFragment = new AddDeviceFragment();
            addDeviceFragment.show(getParentFragmentManager(), "AddDeviceFragment");
        });

        return view;
    }

    private void createButton(Device device) {
        Button newButton = new Button(getActivity());
        newButton.setTextColor(Color.BLACK);
        newButton.setPadding(20, 20, 20, 20);
        newButton.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );

        params.setMargins(16, 16, 16, 16);

        newButton.setLayoutParams(params);


        String displayText;

        if ("digitalDevice".equals(device.getDeviceType())) {
            displayText = device.getDeviceName();
            newButton.setText(displayText);
            newButton.setTextColor(Color.BLACK);
            newButton.setTextSize(20);
            if(device.getDeviceData().equals("on")){
                newButton.setBackgroundResource(R.drawable.button_background_green);
            }else {
                newButton.setBackgroundResource(R.drawable.button_background_red);
            }
        } else if ("analogDevice".equals(device.getDeviceType())) {
            displayText = device.getDeviceName() + ": " + device.getDeviceData() + "%";
            newButton.setText(displayText);
            newButton.setTextColor(Color.BLACK);
            newButton.setBackgroundResource(R.drawable.button_background);
            newButton.setTextSize(20);
        }

        newButton.setOnClickListener(v -> showDeviceInfoDialog(device));

        buttonContainer.addView(newButton);
    }

    private void showDeviceInfoDialog(Device device) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_device_info, null);
        builder.setView(dialogView);

        TextView deviceNameText = dialogView.findViewById(R.id.deviceNameText);
        TextView portText = dialogView.findViewById(R.id.portText);
        TextView typeText = dialogView.findViewById(R.id.typeText);
        TextView dataText = dialogView.findViewById(R.id.dataText);
        EditText editNameInput = dialogView.findViewById(R.id.editNameInput);
        Button saveButton = dialogView.findViewById(R.id.saveButton);
        Button disconnectButton = dialogView.findViewById(R.id.disconnectButton);

        deviceNameText.setText(device.getDeviceName());
        portText.setText("Port: " + device.getPort());
        typeText.setText("Loại: " + device.getDeviceType());
        dataText.setText("Dữ liệu: " + device.getDeviceData());

        editNameInput.setText(device.getDeviceName());

        AlertDialog dialog = builder.create();
        dialog.show();

        saveButton.setOnClickListener(v -> {
            String newName = editNameInput.getText().toString().trim();
            if (!newName.isEmpty()) {
                device.setDeviceName(newName);
                deviceViewModel.updateDeviceInfo(device,getContext());
                List<Device> updatedDevices = new ArrayList<>(deviceViewModel.getDevices().getValue());
                deviceViewModel.getMutableDevices().setValue(updatedDevices);

                //Toast.makeText(getActivity(), "Cập nhật tên thiết bị thành công", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            } else {
                Toast.makeText(getActivity(), "Tên thiết bị không được để trống", Toast.LENGTH_SHORT).show();
            }
        });

        disconnectButton.setOnClickListener(v -> {
            deviceViewModel.deleteDevice(device, getContext());
            List<Device> updatedDevices = new ArrayList<>(deviceViewModel.getDevices().getValue());
            updatedDevices.remove(device);
            deviceViewModel.getMutableDevices().setValue(updatedDevices);

            //Toast.makeText(getActivity(), "Ngắt kết nối thiết bị: " + device.getDeviceName(), Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });

    }
}
