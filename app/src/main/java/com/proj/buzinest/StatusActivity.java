package com.proj.buzinest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class StatusActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextInputEditText status_input;
    private Button btnSaveStatus;
    private DatabaseReference reference;
    private String userId;
    private ProgressDialog progressDialog;
    private EditText edtUsernameUpdate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);

        toolbar = findViewById(R.id.status_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("About");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String status_value = getIntent().getStringExtra("status_value");
        String username_value = getIntent().getStringExtra("username_value");

        userId= FirebaseAuth.getInstance().getCurrentUser().getUid();
        reference = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);
        status_input =(TextInputEditText) findViewById(R.id.status_input);
        edtUsernameUpdate = findViewById(R.id.edtUsernameUpdate);
        btnSaveStatus = findViewById(R.id.btnSaveStatus);
        status_input.setText(status_value);
        edtUsernameUpdate.setText(username_value);
        btnSaveStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog = new ProgressDialog(StatusActivity.this);
                progressDialog.setTitle("Saving changes");
                progressDialog.setMessage("Please wait");
                progressDialog.show();
                String status = status_input.getText().toString();
                String username = edtUsernameUpdate.getText().toString();
                reference.child("Status").setValue(status).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            progressDialog.dismiss();
                        }
                        else {
                            Toast.makeText(StatusActivity.this,"Error", Toast.LENGTH_LONG).show();
                        }
                    }
                });
                reference.child("Username").setValue(username).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            progressDialog.dismiss();
                        }
                        else {
                            Toast.makeText(StatusActivity.this,"Error", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
    }


}