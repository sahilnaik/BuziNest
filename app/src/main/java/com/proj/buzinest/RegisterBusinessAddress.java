package com.proj.buzinest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterBusinessAddress extends AppCompatActivity {

    private EditText edtAddressOne, edtAddressTwo, edtLandmark, edtCity, edtCountry, edtState;
    private Button btnRegTwo;
    private FirebaseUser user;
    private DatabaseReference reference;
    private String userID;
    private ProgressBar progressBar5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_business_address);
        edtAddressOne = findViewById(R.id.edtAddressOne);
        edtAddressTwo = findViewById(R.id.edtAddressTwo);
        edtLandmark = findViewById(R.id.edtLandmark);
        edtCity = findViewById(R.id.edtCity);
        edtCountry= findViewById(R.id.edtCountry);
        edtState= findViewById(R.id.edtState);
        btnRegTwo= findViewById(R.id.btnRegTwo);
        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        userID = user.getUid();
        progressBar5 = findViewById(R.id.progressBar5);
        btnRegTwo.setOnClickListener(new View.OnClickListener() {
            class RegAddress {
                public String AddressOne, AddressTwo, State, City, Country, Landmark;
                public RegAddress(){

                }
                public RegAddress(String AddressOne, String AddressTwo, String State, String City, String Country, String Landmark){
                    this.AddressOne = AddressOne;
                    this.AddressTwo = AddressTwo;
                    this.City = City;
                    this.Country = Country;
                    this.Landmark = Landmark;
                    this.State = State;
                }
            }

            @Override
            public void onClick(View v) {
                registerAddress();
            }

            private void registerAddress() {
                String AddressOne = edtAddressOne.getText().toString().trim();
                String AddressTwo = edtAddressTwo.getText().toString().trim();
                String Landmark = edtLandmark.getText().toString().trim();
                String City = edtCity.getText().toString().trim();
                String Country = edtCountry.getText().toString().trim();
                String State = edtState.getText().toString().trim();

                if(AddressOne.isEmpty()){
                    edtAddressOne.setError("Address Line 1 required");
                    edtAddressOne.requestFocus();
                }
                if(AddressTwo.isEmpty()){
                    edtAddressTwo.setError("Address Line 1 required");
                    edtAddressTwo.requestFocus();
                }
                if(Landmark.isEmpty()){
                    edtLandmark.setError("Address Line 1 required");
                    edtLandmark.requestFocus();
                }
                if(City.isEmpty()){
                    edtCity.setError("Address Line 1 required");
                    edtCity.requestFocus();
                }
                if(Country.isEmpty()){
                    edtCountry.setError("Address Line 1 required");
                    edtCountry.requestFocus();
                }
                if(State.isEmpty()){
                    edtState.setError("Address Line 1 required");
                    edtState.requestFocus();
                }
                progressBar5.setVisibility(View.VISIBLE);
                RegAddress addr = new RegAddress(AddressOne, AddressTwo, State, City, Country, Landmark);
                reference.child(userID).child("Address").setValue(addr).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(RegisterBusinessAddress.this,"Address has been registered successfully", Toast.LENGTH_SHORT).show();
                            progressBar5.setVisibility(View.GONE);
                            startActivity(new Intent(RegisterBusinessAddress.this, RegisterBusinessAddress.class));
                        } else {
                            Toast.makeText(RegisterBusinessAddress.this,"Failed to register. Try again!", Toast.LENGTH_LONG).show();
                            progressBar5.setVisibility(View.GONE);
                        }
                    }
                });

            }
        });
    }
}