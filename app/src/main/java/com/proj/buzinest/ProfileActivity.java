package com.proj.buzinest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ProfileActivity extends AppCompatActivity {
    private TextView profile_displayName, profileStatus;
    private ImageView profileImage;
    private Button profileRequest, btnViewPostings;
    private DatabaseReference reference;
    private TextView addressOne, addressTwo, addressThree, addressFour, contact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        final String userId = getIntent().getStringExtra("userId");
        reference = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);
        addressOne = findViewById(R.id.addressOne);
        addressTwo = findViewById(R.id.addressTwo);
        addressThree = findViewById(R.id.addressThree);
        addressFour = findViewById(R.id.addressFour);
        contact = findViewById(R.id.contact);
        profile_displayName= findViewById(R.id.profile_displayName);
        profileImage = findViewById(R.id.profileImage);
        profileRequest = findViewById(R.id.profileRequest);
        profileStatus = findViewById(R.id.profileStatus);
        btnViewPostings = findViewById(R.id.btnViewPostings);
        btnViewPostings.setVisibility(View.GONE);
        reference.child("Address").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    btnViewPostings.setVisibility(View.VISIBLE);
                    btnViewPostings.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent profileIntent = new Intent(ProfileActivity.this, UserPostsBusiness.class);
                            profileIntent.putExtra("userId", userId);
                            startActivity(profileIntent);
                        }
                    });
                    addressOne.setVisibility(View.VISIBLE);
                    addressTwo.setVisibility(View.VISIBLE);
                    addressThree.setVisibility(View.VISIBLE);
                    addressFour.setVisibility(View.VISIBLE);
                    contact.setVisibility(View.VISIBLE);
                    String AddressOne = snapshot.child("AddressOne").getValue().toString();
                    addressOne.setText(AddressOne);
                    String AddressTwo = snapshot.child("AddressTwo").getValue().toString();
                    addressTwo.setText(AddressTwo);
                    String AddressThree = snapshot.child("Landmark").getValue().toString();
                    addressThree.setText(AddressThree);
                    String AddressFour = snapshot.child("City").getValue().toString();
                    addressFour.setText(AddressFour);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        reference.child("Details").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    contact.setVisibility(View.VISIBLE);
                    String Contact = snapshot.child("contact").getValue().toString();
                    contact.setText(Contact);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name = snapshot.child("Username").getValue().toString();
                String status = snapshot.child("Status").getValue().toString();
                String Image = snapshot.child("image").getValue().toString();
                profile_displayName.setText(name);
                profileStatus.setText(status);
                Picasso.get().load(Image).placeholder(R.drawable.hqdefault).into(profileImage);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }

}