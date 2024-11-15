package com.javaoop.smarthome;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class RegisterActivity extends AppCompatActivity {
    EditText newemail;
    EditText password;
    EditText comfirmpassword;
    Button registerButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);

        newemail = findViewById(R.id.new_email_input);
        password = findViewById(R.id.new_pass_input);
        comfirmpassword = findViewById(R.id.confirm_pass);
        registerButton = findViewById(R.id.register_btn);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(newemail.getText().toString().isEmpty() || password.getText().toString().isEmpty() || comfirmpassword.getText().toString().isEmpty()){
                    Toast.makeText(RegisterActivity.this, "Vui lòng nhập đầy đủ thông tin.", Toast.LENGTH_SHORT).show();
                } else if (!comfirmpassword.getText().toString().equals(password.getText().toString())) {
                    Toast.makeText(RegisterActivity.this, "Mật khẩu chưa trùng khớp.", Toast.LENGTH_SHORT).show();
                } else if (password.length() < 8) {
                    Toast.makeText(RegisterActivity.this, "Mật khẩu tối thiểu 8 ký tự.", Toast.LENGTH_SHORT).show();
                }
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
}