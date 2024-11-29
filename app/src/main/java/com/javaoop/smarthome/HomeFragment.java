package com.javaoop.smarthome;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONException;
import org.json.JSONObject;


public class HomeFragment extends Fragment {

    private TextView myTextView;

    private void loadButtonCreatorFragment() {
        ButtonlistFragment buttonlistFragment = new ButtonlistFragment();
        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.button_container, buttonlistFragment);
        fragmentTransaction.commit();
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getInfo(getContext());
        loadButtonCreatorFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        myTextView = view.findViewById(R.id.welcome_message);
        Users user = UserSingleton.getInstance().getUser();
        String name = user.getName();
        if(name != null)myTextView.setText("Xin Chào\n" + name);
        return view;
    }

    private void sayhello(){
        Users user = UserSingleton.getInstance().getUser();
        String name = user.getName();
        myTextView.setText("Xin Chào\n" + name);
    }


    public void getInfo(Context context) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser == null) {
            Toast.makeText(context, "Người dùng chưa đăng nhập", Toast.LENGTH_SHORT).show();
            return;
        }

        String email = currentUser.getEmail();

        String url = "http://192.168.0.103:8080/users/email/" + email; // Thay đổi URL theo API của bạn

        // Gọi API
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Xử lý phản hồi từ API
                        try {
                            // Kiểm tra nếu thành công
                            boolean success = response.getBoolean("success");
                            Log.d("API Response", response.toString()); // Log phản hồi API
                            if (success) {
                                JSONObject data = response.getJSONObject("data");
                                String uid = data.getString("uid");
                                String phone = data.getString("phone");
                                String name = data.getString("name");
                                String email = data.getString("email");


                                Users users = UserSingleton.getInstance().getUser();
                                users.setUid(uid);
                                users.setName(name);
                                users.setPhonenumber(phone);
                                users.setEmail(email);

                                Log.d("User Info", "UID: " + uid + ", Name: " + users.getUid() + ", Phone: " + phone + ", Email: " + email);

                            } else {
                                String message = response.getString("message");
                                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(context, "Lỗi phân tích dữ liệu", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Xử lý lỗi
                        Toast.makeText(context, "Lỗi: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        );

        // Thêm yêu cầu vào hàng đợi
        requestQueue.add(jsonObjectRequest);
    }

    public void reloadFragment() {
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        // Sử dụng replace để reload Fragment
        fragmentTransaction.replace(R.id.frame_layout, new HomeFragment());
        fragmentTransaction.commit();
    }

}