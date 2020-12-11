package com.proj.buzinest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {
    private TextView profile_displayName, profileStatus;
    private ImageView profileImage;
    private Button profileRequest;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        String userId = getIntent().getStringExtra("userId");
        reference = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);
        profile_displayName= findViewById(R.id.profile_displayName);
        profileImage = findViewById(R.id.profileImage);
        profileRequest = findViewById(R.id.profileRequest);
        profileStatus = findViewById(R.id.profileStatus);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name = snapshot.child("Username").getValue().toString();
                String status = snapshot.child("Status").getValue().toString();
                String Image = snapshot.child("image").getValue().toString();
                profile_displayName.setText(name);
                profileStatus.setText(status);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}