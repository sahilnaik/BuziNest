package com.proj.buzinest;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.net.URI;

import de.hdodenhof.circleimageview.CircleImageView;

public class AccountSettings extends AppCompatActivity {
    private DatabaseReference reference;
    private FirebaseUser user;
    private CircleImageView profilepicSetting;
    private TextView usernameSetting, mstatus;
    private Button btnUpdateImage, btnUpdateStatus;
    private static final int GALLERY_PICK =1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_settings);

        profilepicSetting = findViewById(R.id.profilepicSetting);
        usernameSetting = findViewById(R.id.usernameSetting);
        mstatus = findViewById(R.id.status);
        btnUpdateImage = findViewById(R.id.btnUpdateImage);
        btnUpdateStatus = findViewById(R.id.btnUpdateStatus);

        user= FirebaseAuth.getInstance().getCurrentUser();
        String userId = user.getUid();
        reference = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name = snapshot.child("Username").getValue().toString();
                String image = snapshot.child("image").getValue().toString();
                String status = snapshot.child("Status").getValue().toString();
                String thumb_image = snapshot.child("thumb_image").getValue().toString();

                usernameSetting.setText(name);
                mstatus.setText(status);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btnUpdateStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username_value = usernameSetting.getText().toString();
                String status_value = mstatus.getText().toString();
                Intent status_intent =new Intent(AccountSettings.this, StatusActivity.class);
                status_intent.putExtra("status_value",status_value);
                status_intent.putExtra("username_value", username_value);
                startActivity(status_intent);

            }
        });
        btnUpdateImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent();
                galleryIntent.setType("image/*");
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(galleryIntent,"Select Image"), GALLERY_PICK);

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GALLERY_PICK && resultCode == RESULT_OK){
            Uri imageUri = data.getData();
            CropImage.activity(imageUri).setAspectRatio(1,1).start(AccountSettings.this);
        }
    }
}