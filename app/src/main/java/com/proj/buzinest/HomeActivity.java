package com.proj.buzinest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HomeActivity extends AppCompatActivity{
    private Button btnLogoutHome, btnRegister, btnExploreHome, btnCartHome, btnChatHome;
    private FirebaseUser user;
    private DatabaseReference reference;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        btnRegister = findViewById(R.id.btnRegisterHome);
        btnExploreHome = findViewById(R.id.btnExploreHome);
        btnCartHome = findViewById(R.id.btnCartHome);
        btnChatHome = findViewById(R.id.btnChatHome);



        reference = FirebaseDatabase.getInstance().getReference("Users");
        user = FirebaseAuth.getInstance().getCurrentUser();
        userID = user.getUid();



        btnRegister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, RegisterBusiness.class));
            }
        });
        btnExploreHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              startActivity(new Intent(HomeActivity.this, AllUsers.class));
            }
        });

        btnChatHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, ChatPage.class));
            }
        });

        btnCartHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, AccountSettings.class));
             //   startActivity();
            }
        });
        btnLogoutHome = findViewById(R.id.btnlogoutHome);
        btnLogoutHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(HomeActivity.this, LoginActivity.class));
            }
        });


        final TextView greeting = (TextView) findViewById(R.id.greeting);
        final TextView notGreeting = (TextView) findViewById(R.id.notGreeting);
        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);
                if (userProfile != null){
                    String username = userProfile.Username;
                    greeting.setText("Welcome, " + username+"!");
                    notGreeting.setText(username+"?");

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(HomeActivity.this,"Some error occurred", Toast.LENGTH_LONG).show();

            }
        });

    }
}