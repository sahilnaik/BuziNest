package com.proj.buzinest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText edtEmailLogIn, edtPasswordLogIn;
    private Button btnLogIn, btnForgotPass, btnSignLogIn;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_main);
        edtEmailLogIn = findViewById(R.id.edtEmailLogIn);
        edtPasswordLogIn = findViewById(R.id.edtPasswordLogIn);
        btnLogIn = findViewById(R.id.btnLogin);
        btnLogIn.setOnClickListener(this);
        btnForgotPass = findViewById(R.id.btnForgotPass);
        btnForgotPass.setOnClickListener(this);
        btnSignLogIn = findViewById(R.id.btnSignLogIn);
        btnSignLogIn.setOnClickListener(this);
        mAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progressBar2);
        edtEmailLogIn.animate().alpha(1).setDuration(1200);
        edtPasswordLogIn.animate().alpha(1).setDuration(1200);
        btnForgotPass.animate().alpha(1).setDuration(1500);
        btnSignLogIn.animate().alpha(1).setDuration(1700);
        edtPasswordLogIn.animate().translationY(-70).setDuration(700);
        edtEmailLogIn.animate().translationY(-70).setDuration(700);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnLogin:
                userLogin();
                break;
            case R.id.btnForgotPass:
                startActivity(new Intent(this, ForgotPassword.class));
                break;
            case R.id.btnSignLogIn:
                startActivity(new Intent(this, SigninActivity.class));
                break;
        }

    }

    private void userLogin() {
        String email = edtEmailLogIn.getText().toString();
        String password = edtPasswordLogIn.getText().toString();

        if (email.isEmpty()){
            edtEmailLogIn.setError("Enter your email");
            edtEmailLogIn.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            edtEmailLogIn.setError("Enter a valid email");
            edtEmailLogIn.requestFocus();
            return;
        }
        if(password.isEmpty()) {
            edtPasswordLogIn.setError("Password is required!");
            edtPasswordLogIn.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if(user.isEmailVerified()){
                        Toast.makeText(LoginActivity.this, "Logged in successfully", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                        startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                    }else{
                        user.sendEmailVerification();
                        Toast.makeText(LoginActivity.this,"Check your email for verification", Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                    }

                }
                else {
                    Toast.makeText(LoginActivity.this, "Failed to sign in", Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);


                }
            }
        });
    }
}