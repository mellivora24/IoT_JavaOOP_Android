package com.javaoop.smarthome;

import android.content.Intent;
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

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangePasswordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_change_password);

        ImageView backbtn = findViewById(R.id.back_button);

        EditText currentpasswordip = findViewById(R.id.current_password_input);
        EditText newpasswordip = findViewById(R.id.change_new_password_input);
        EditText confirmpassip = findViewById(R.id.change_confirm_pass);

        Button verifyButton = findViewById(R.id.verify_change_password_btn);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        verifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currentpass, newpass, confirmpass;
                currentpass = String.valueOf(currentpasswordip.getText());
                newpass = String.valueOf(newpasswordip.getText());
                confirmpass =String.valueOf(confirmpassip.getText());

                if (currentpass.isEmpty() || newpass.isEmpty() || confirmpass.isEmpty()){
                    Toast.makeText(ChangePasswordActivity.this, "Vui lòng nhập đầy đủ thông tin.", Toast.LENGTH_SHORT).show();
                }else if(newpass.length() < 8){
                    Toast.makeText(ChangePasswordActivity.this, "Mật khẩu mới phải có ít nhất 8 ký tự.", Toast.LENGTH_SHORT).show();
                }else if (!newpass.equals(confirmpass)) {
                    Toast.makeText(ChangePasswordActivity.this, "Mật khẩu mới chưa trùng khớp.", Toast.LENGTH_SHORT).show();
                }else {
                    FirebaseUser user = mAuth.getCurrentUser();
                    if(user != null && user.getEmail() != null){
                        AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), currentpass);

                        user.reauthenticate(credential).addOnCompleteListener(task -> {
                            if(task.isSuccessful()){
                                user.updatePassword(newpass).addOnCompleteListener(updateTask -> {
                                    if(updateTask.isSuccessful()){
                                        Toast.makeText(ChangePasswordActivity.this, "Đổi mật khẩu thành công!", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }else {
                                        Toast.makeText(ChangePasswordActivity.this, "Đổi mật khẩu thất bại: " + updateTask.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }else {
                                Toast.makeText(ChangePasswordActivity.this, "Mật khẩu hiện tại không đúng", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }


            }
        });

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



    }
}