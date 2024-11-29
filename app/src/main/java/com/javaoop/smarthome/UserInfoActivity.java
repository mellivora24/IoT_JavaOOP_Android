package com.javaoop.smarthome;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

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

public class UserInfoActivity extends AppCompatActivity {

    private EditText userName;
    private EditText userEmail;
    private EditText userPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_info);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ImageView backbtn = findViewById(R.id.info_back_button);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Button updatebtn = findViewById(R.id.userinfo_change_btn);
        updatebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateInfo(UserInfoActivity.this);
            }
        });

        showInfo();
    }

    private void showInfo() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        String email = currentUser.getEmail();
        userName = findViewById(R.id.user_name_input);
        userEmail = findViewById(R.id.user_email_input);
        userPhone = findViewById(R.id.user_phonenumber_input);

        String url = "http://192.168.0.103:8080/users/email/" + email; // Thay đổi URL theo API của bạn

        // Gọi API
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Xử lý phản hồi từ API
                        try {
                            // Kiểm tra nếu thành công
                            boolean success = response.getBoolean("success");
                            if (success) {
                                JSONObject data = response.getJSONObject("data");
                                String uid = data.getString("uid");
                                String phone = data.getString("phone");
                                String name = data.getString("name");
                                String email = data.getString("email");

                                // Hiển thị thông tin người dùng
                                userName.setText(name);
                                userEmail.setText(email);
                                userPhone.setText(phone);
                            } else {
                                String message = response.getString("message");
                                Toast.makeText(UserInfoActivity.this, message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(UserInfoActivity.this, "Lỗi phân tích dữ liệu", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Xử lý lỗi
                        Toast.makeText(UserInfoActivity.this, "Lỗi: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        );

        // Thêm yêu cầu vào hàng đợi
        requestQueue.add(jsonObjectRequest);
    }
    private void updateInfo(Context context) {
        Users user = UserSingleton.getInstance().getUser();
        String uid = user.getUid();
        String url = "http://192.168.0.103:8080/users/" + uid; // Thay đổi URL theo API của bạn

        // Tạo JSON object cho dữ liệu người dùng
        JSONObject userData = new JSONObject();
        try {
            userData.put("phone", userPhone.getText());
            userData.put("name", userName.getText());
            userData.put("email", userEmail.getText());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Gọi API
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.PUT, url, userData,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Xử lý phản hồi từ API
                        Toast.makeText(context, "Cập nhật thông tin thành công!", Toast.LENGTH_SHORT).show();
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



}