package com.proj.buzinest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.annotations.NotNull;
import com.ybs.passwordstrengthmeter.PasswordStrength;

import java.util.regex.Pattern;

public class SigninActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher{

    private EditText edtUsernameSignIn, edtPasswordSignIn, edtEmailSignIn;
    private Button btnSignSignIn, btnLogInSignIn;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar, passStrength;
    private TextView strengthView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_signin);
        mAuth = FirebaseAuth.getInstance();
        edtUsernameSignIn = findViewById(R.id.edtUsernameSignIn);
        edtPasswordSignIn = findViewById(R.id.edtPasswordSignIn);
        edtPasswordSignIn.addTextChangedListener(this);
        edtEmailSignIn = findViewById(R.id.edtEmailSignIn);
        btnSignSignIn = findViewById(R.id.btnSignSignIn);
        btnSignSignIn.setOnClickListener(this);
        btnLogInSignIn = findViewById(R.id.btnLogInSignIn);
        btnLogInSignIn.setOnClickListener(this);
        progressBar = findViewById(R.id.progressBar);
        passStrength = findViewById(R.id.passStrength);
        strengthView = findViewById(R.id.password_strength);
        edtUsernameSignIn.animate().alpha(1).setDuration(1000);
        edtEmailSignIn.animate().alpha(1).setDuration(1000);
        edtPasswordSignIn.animate().alpha(1).setDuration(1000);
        passStrength.animate().alpha(1).setDuration(1200);
        strengthView.animate().alpha(1).setDuration(1000);
        btnLogInSignIn.animate().alpha(1).setDuration(1700);
        edtUsernameSignIn.animate().translationY(-70).setDuration(700);
        edtEmailSignIn.animate().translationY(-70).setDuration(700);
        edtPasswordSignIn.animate().translationY(-70).setDuration(700);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnLogInSignIn:
                startActivity(new Intent(this, LoginActivity.class));
                break;
            case R.id.btnSignSignIn:
                registerUser();
                break;
        }
    }

    private void registerUser() {
        final String email = edtEmailSignIn.getText().toString().trim();
        final String username = edtUsernameSignIn.getText().toString().trim();
        final String password = edtPasswordSignIn.getText().toString().trim();

        if(email.isEmpty()){
            edtEmailSignIn.setError("Email is required!");
            edtEmailSignIn.requestFocus();
            return;
        }
        if(username.isEmpty()){
            edtUsernameSignIn.setError("A username is required!");
            edtUsernameSignIn.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            edtEmailSignIn.setError("Enter a valid email");
            edtEmailSignIn.requestFocus();
            return;
        }
        if(password.isEmpty()) {
                edtPasswordSignIn.setError("Password is required!");
                edtPasswordSignIn.requestFocus();
                return;
        }
        if(password.length() < 6){
            edtPasswordSignIn.setError("Password should be at least 6 characters long");
            edtPasswordSignIn.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    User user= new User(username, email, password);
                    FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if(task.isSuccessful()){
                                Toast.makeText(SigninActivity.this,username+" has been registered successfully", Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                                startActivity(new Intent(SigninActivity.this, HomeActivity.class));
                            } else {
                                Toast.makeText(SigninActivity.this,"Failed to register. Try again!", Toast.LENGTH_LONG).show();
                                progressBar.setVisibility(View.GONE);
                            }
                        }
                    });
                }else{
                    Toast.makeText(SigninActivity.this,"Failed to register. Try again!", Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                }
            }
        });

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        updatePasswordStrengthView(s.toString());
    }

    private void updatePasswordStrengthView(String edtPasswordSignIn) {
        if (TextView.VISIBLE != strengthView.getVisibility())
            return;

        if (edtPasswordSignIn.isEmpty()) {
            strengthView.setText("");
            passStrength.setProgress(0);
            return;
        }
        PasswordStrength str = PasswordStrength.calculateStrength(edtPasswordSignIn);
        strengthView.setText(str.getText(this));
        strengthView.setTextColor(str.getColor());

        passStrength.getProgressDrawable().setColorFilter(str.getColor(), android.graphics.PorterDuff.Mode.SRC_IN);
        if (str.getText(this).equals("Weak")) {
            passStrength.setProgress(25);
        } else if (str.getText(this).equals("Medium")) {
            passStrength.setProgress(50);
        } else if (str.getText(this).equals("Strong")) {
            passStrength.setProgress(75);
        } else {
            passStrength.setProgress(100);
        }


    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}