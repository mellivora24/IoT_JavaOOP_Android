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

public class ForgotActivity extends AppCompatActivity {
    EditText email;
    EditText code;
    Button verifyButton;
    Button sendButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_forgot);

        email = findViewById(R.id.forgotusername_input);
        code = findViewById(R.id.code_input);



        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.forgot_screen), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void switchToLoginPage(View view) {
        Intent intent = new Intent(ForgotActivity.this, LoginActivity.class);
        startActivity(intent);
    }
}
