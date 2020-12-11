package com.proj.buzinest;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;

import de.hdodenhof.circleimageview.CircleImageView;

public class BusinessProfile extends AppCompatActivity implements View.OnClickListener {
    private FirebaseUser user;
    private DatabaseReference reference, checkProfileImage;
    private String userID;
    private CircleImageView profile_image;
    private Bitmap bitmap;

    private String imageDownloadLink;
    private Button btnBusinessChat, btnBusAccountSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_profile);

        btnBusinessChat = findViewById(R.id.btnBusinessChat);
        btnBusinessChat.setOnClickListener(this);

        btnBusAccountSettings = findViewById(R.id.btnBusAccSettings);
        btnBusAccountSettings.setOnClickListener(this);



        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        userID = user.getUid();

        checkProfileImage = FirebaseDatabase.getInstance().getReference().child("Users").child(userID);
        checkProfileImage.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String image = snapshot.child("image").getValue().toString();


                Picasso.get().load(image).placeholder(R.drawable.hqdefault).into(profile_image);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        final TextView txtBuzName = (TextView) findViewById(R.id.txtBuzName);
        final TextView txtUsername = (TextView) findViewById(R.id.txtUsername);
        reference.child(userID).child("Details").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String Bizname = snapshot.child("BizName").getValue().toString();
                    txtBuzName.setText(Bizname);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(BusinessProfile.this,"Some error occurred", Toast.LENGTH_LONG).show();

            }
        });
        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String Username = snapshot.child("Username").getValue().toString();
                txtUsername.setText(Username);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        profile_image = findViewById(R.id.busprofilepicSetting);
        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
    }

    private void selectImage() {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1000);
        }
        else {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, 1000);

        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 1000 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            selectImage();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1000 && resultCode == RESULT_OK && data != null){
            Uri chosenImageData = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), chosenImageData);
                profile_image.setImageBitmap(bitmap);
                uploadImageToServer();
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private void uploadImageToServer(){
        if(bitmap!= null){
        profile_image.setDrawingCacheEnabled(true);
        profile_image.buildDrawingCache();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] data = baos.toByteArray();



        final UploadTask uploadTask = FirebaseStorage.getInstance().getReference().child(userID).child("Profile Picture").child(userID).putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                Toast.makeText(BusinessProfile.this, exception.toString(), Toast.LENGTH_LONG).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                // ...
                Toast.makeText(BusinessProfile.this, "Profile picture updated", Toast.LENGTH_SHORT).show();
            }

        });

    }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnBusinessChat:
                startActivity(new Intent(this, ChatPage.class));
                break;
            case R.id.btnBusAccSettings:
                startActivity(new Intent(this, BusinessAccountSettings.class));
                break;

        }
    }
}