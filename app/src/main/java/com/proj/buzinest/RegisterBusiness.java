package com.proj.buzinest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class RegisterBusiness extends AppCompatActivity {

    private EditText edtBuzName, edtContact;
    private Button btnRegOne;
    private FirebaseUser user;
    private DatabaseReference reference;
    private String userID;
    private ProgressBar progressBar4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_business);
        edtBuzName = findViewById(R.id.edtBuzName);
        edtContact = findViewById(R.id.edtContact);
        btnRegOne = findViewById(R.id.btnRegOne);
        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        userID = user.getUid();
        progressBar4 = findViewById(R.id.progressBar4);
        btnRegOne.setOnClickListener(new View.OnClickListener() {
            class BusinessAddress {
                public String BizName, contact;
                public BusinessAddress(){

                }
                public BusinessAddress(String BizName, String contact){
                    this.BizName = BizName;
                    this.contact = contact;
                }
            }

            @Override
            public void onClick(View v) {
                registerBusiness();
            }

            private void registerBusiness() {
                final String BizName = edtBuzName.getText().toString().trim();
                String contact = edtContact.getText().toString().trim();

                if(BizName.isEmpty()){
                    edtBuzName.setError("Business Name is required");
                    edtBuzName.requestFocus();
                    return;
                }
                if(contact.isEmpty()){
                    edtContact.setError("A contact is required");
                    edtContact.requestFocus();
                    return;
                }
                BusinessAddress details = new BusinessAddress(BizName, contact);
                progressBar4.setVisibility(View.VISIBLE);
                reference.child(userID).child("Details").setValue(details).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(RegisterBusiness.this,BizName+" has been registered successfully", Toast.LENGTH_SHORT).show();
                            progressBar4.setVisibility(View.GONE);
                            startActivity(new Intent(RegisterBusiness.this, RegisterBusinessAddress.class));
                        } else {
                            Toast.makeText(RegisterBusiness.this,"Failed to register. Try again!", Toast.LENGTH_LONG).show();
                            progressBar4.setVisibility(View.GONE);
                        }
                    }
                });



            }
        });

    }

}