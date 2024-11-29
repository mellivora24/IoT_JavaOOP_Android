package com.javaoop.smarthome;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity {
    EditText newemail;
    EditText password;
    EditText comfirmpassword;
    Button registerButton;
    FirebaseAuth mAuth;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent = new Intent(getApplicationContext(), MenuscreenActivity.class);
            startActivity(intent);
            finish();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();
        newemail = findViewById(R.id.new_email_input);
        password = findViewById(R.id.new_pass_input);
        comfirmpassword = findViewById(R.id.confirm_pass);
        registerButton = findViewById(R.id.register_btn);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email,pass;
                email = String.valueOf(newemail.getText());
                pass = String.valueOf(password.getText());
                if(newemail.getText().toString().isEmpty() || password.getText().toString().isEmpty() || comfirmpassword.getText().toString().isEmpty()){
                    Toast.makeText(RegisterActivity.this, "Vui lòng nhập đầy đủ thông tin.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (password.length() < 8) {
                    Toast.makeText(RegisterActivity.this, "Mật khẩu tối thiểu 8 ký tự.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!comfirmpassword.getText().toString().equals(password.getText().toString())) {
                    Toast.makeText(RegisterActivity.this, "Mật khẩu chưa trùng khớp.", Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth.createUserWithEmailAndPassword(email, pass)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    createUser(email, RegisterActivity.this);
                                    //Toast.makeText(RegisterActivity.this, "Đăng ký thành công.",Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                    startActivity(intent);

                                } else {
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(RegisterActivity.this, "Đăng ký thất bại.",
                                            Toast.LENGTH_SHORT).show();

                                }
                            }
                        });

            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.register_screen), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void switchToLoginPage(View view) {
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    private void createUser(String email, Context context) {
        String url = "http://192.168.0.103:8080/users"; // Thay đổi URL theo API của bạn

        // Tạo JSON object cho dữ liệu người dùng
        JSONObject userData = new JSONObject();
        try {
            userData.put("phone", "");
            userData.put("name", "");
            userData.put("email", email);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Gọi API
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST, url, userData,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Xử lý phản hồi từ API
                        Toast.makeText(context, "Tạo người dùng thành công!", Toast.LENGTH_SHORT).show();
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