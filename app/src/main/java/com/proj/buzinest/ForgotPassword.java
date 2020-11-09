package com.proj.buzinest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity {
    private EditText edtEmailForgot;
    private Button btnResetPass, btnLogInForgot;
    private ProgressBar progressBar3;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        edtEmailForgot = findViewById(R.id.edtEmailForgot);
        btnResetPass = findViewById(R.id.btnResetPass);
        btnLogInForgot = findViewById(R.id.btnLogInForgot);
        progressBar3 = findViewById(R.id.progressBar3);
        auth = FirebaseAuth.getInstance();

        btnResetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPassword();
            }
        });
        btnLogInForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ForgotPassword.this, LoginActivity.class));
            }
        });
    }

    private void resetPassword() {
        String email = edtEmailForgot.getText().toString().trim();
        if (email.isEmpty()) {
            edtEmailForgot.setError("Email is required");
            edtEmailForgot.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            edtEmailForgot.setError("Provide a valid email");
            edtEmailForgot.requestFocus();
            return;
        }
        progressBar3.setVisibility(View.VISIBLE);
        auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(ForgotPassword.this, "Check your email to reset password!", Toast.LENGTH_LONG).show();
                    progressBar3.setVisibility(View.GONE);
                }
                else {
                    Toast.makeText(ForgotPassword.this,"Try again!", Toast.LENGTH_LONG).show();
                    progressBar3.setVisibility(View.GONE);
                }
            }
        });
    }
}