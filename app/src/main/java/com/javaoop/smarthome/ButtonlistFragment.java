package com.javaoop.smarthome;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

public class ButtonlistFragment extends Fragment {

    private LinearLayout buttonContainer;
    private DeviceViewModel deviceViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_buttonlist, container, false);
        buttonContainer = view.findViewById(R.id.buttonContainer);
        deviceViewModel = new ViewModelProvider(requireActivity()).get(DeviceViewModel.class);

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
        newButton.setText(device.getName() + " (" + device.getType() + ")");
        // Thiết lập các thuộc tính cho nút
        buttonContainer.addView(newButton);
    }
}