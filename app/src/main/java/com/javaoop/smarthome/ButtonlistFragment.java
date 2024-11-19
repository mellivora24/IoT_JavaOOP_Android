package com.javaoop.smarthome;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

public class ButtonlistFragment extends Fragment {


    private LinearLayout buttonContainer;
    private int buttonCount = 0;

    Button createButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_buttonlist, container, false);

        buttonContainer = view.findViewById(R.id.buttonContainer);
        createButton = view.findViewById(R.id.createButton);

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewButton();
            }
        });

        return view;
    }

    private void createNewButton() {
        buttonCount++;
        Button newButton = new Button(getActivity());
        newButton.setText("Nút " + buttonCount);
        newButton.setBackgroundResource(R.drawable.button_background);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        params.setMargins(0,20,0,0);
        newButton.setLayoutParams(params);

        newButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Nút mới đã được nhấn!", Toast.LENGTH_SHORT).show();
            }
        });

        buttonContainer.addView(newButton);

        buttonContainer.removeView(createButton);

        buttonContainer.addView(createButton);
    }
}