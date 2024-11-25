package com.javaoop.smarthome;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotActivity extends AppCompatActivity {
    EditText email;
    EditText code;
    Button verifyButton;
    Button sendButton;
    ProgressBar progressBar;
    FirebaseAuth mAuth;
    String stremail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_forgot);

        email = findViewById(R.id.forgotusername_input);
        verifyButton = findViewById(R.id.verify_btn);
        progressBar = findViewById(R.id.forgot_progressbar);
        mAuth = FirebaseAuth.getInstance();
        progressBar.setVisibility(View.INVISIBLE);
        verifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String stremail = email.getText().toString();
                if (!TextUtils.isEmpty(stremail)){
                    ResetPassword();
                }else{
                    Toast.makeText(ForgotActivity.this, "Email không được để trống.", Toast.LENGTH_SHORT).show();
                }
            }
        });

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

    public void ResetPassword(){
        progressBar.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.INVISIBLE);
        String emailReset = email.getText().toString().trim();
        mAuth.sendPasswordResetEmail(emailReset)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(ForgotActivity.this, "Đã gửi yêu cầu cài lại mật khẩu đến email của bạn.", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(ForgotActivity.this, "Gửi yêu cầu thất bại: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
