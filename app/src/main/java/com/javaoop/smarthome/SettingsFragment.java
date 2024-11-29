package com.javaoop.smarthome;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;


public class SettingsFragment extends Fragment {
    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        TextView btnlogout = view.findViewById(R.id.demologout);
        TextView btlchangepassword = view.findViewById(R.id.change_password_button);
        TextView btnuserinfo = view.findViewById(R.id.user_info_button);

        btlchangepassword.setOnClickListener( v -> {
            Intent intent = new Intent(getActivity(), ChangePasswordActivity.class);
            startActivity(intent);
        });

        btnuserinfo.setOnClickListener( v -> {
            Intent intent = new Intent(getActivity(), UserInfoActivity.class);
            startActivity(intent);
        });

        btnlogout.setOnClickListener( v -> {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
            FirebaseAuth.getInstance().signOut();
            Toast.makeText(getActivity().getApplicationContext(), "Đăng xuất thành công.", Toast.LENGTH_SHORT).show();
        });
        return view;
    }
}