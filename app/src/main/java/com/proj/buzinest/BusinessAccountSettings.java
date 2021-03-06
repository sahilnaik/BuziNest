package com.proj.buzinest;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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
import com.theartofdev.edmodo.cropper.CropImage;

import de.hdodenhof.circleimageview.CircleImageView;

public class BusinessAccountSettings extends AppCompatActivity {
    private DatabaseReference reference;
    private FirebaseUser user;
    private CircleImageView profilepicSetting;
    private TextView usernameSetting, mstatus;
    private Button btnUpdateImage, btnUpdateStatus;
    private static final int GALLERY_PICK =1;
    private StorageReference storageReference;
    private String userId;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_settings);

        profilepicSetting = findViewById(R.id.busprofilepicSetting);
        usernameSetting = findViewById(R.id.bususernameSetting);
        mstatus = findViewById(R.id.busstatus);
        btnUpdateImage = findViewById(R.id.busbtnUpdateImage);
        btnUpdateStatus = findViewById(R.id.busbtnUpdateStatus);

        user= FirebaseAuth.getInstance().getCurrentUser();
        userId = user.getUid();
        storageReference = FirebaseStorage.getInstance().getReference().child(userId);
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
                Picasso.get().load(image).placeholder(R.drawable.hqdefault).into(profilepicSetting);
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
                Intent status_intent =new Intent(BusinessAccountSettings.this, StatusActivity.class);
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
            CropImage.activity(imageUri).setAspectRatio(1,1).start(BusinessAccountSettings.this);
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                progressDialog = new ProgressDialog(BusinessAccountSettings.this);
                progressDialog.setTitle("Uploading Image");
                progressDialog.setMessage("Please wait");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();
                Uri resultUri = result.getUri();
                final StorageReference filepath = storageReference.child("Profile Picture").child(userId);
                filepath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if(task.isSuccessful()){
                            progressDialog.dismiss();
                            filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String download_url = uri.toString();
                                    reference.child("image").setValue(download_url);
                                }
                            });
                        }else {
                            Toast.makeText(BusinessAccountSettings.this,"Error...",Toast.LENGTH_LONG).show();
                            progressDialog.dismiss();
                        }
                    }
                });
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }
}






