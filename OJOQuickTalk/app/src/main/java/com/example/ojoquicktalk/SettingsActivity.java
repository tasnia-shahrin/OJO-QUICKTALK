package com.example.ojoquicktalk;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.provider.DocumentsContract;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.core.view.WindowCompat;
import androidx.navigation.ActivityNavigatorDestinationBuilderKt;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.ojoquicktalk.databinding.ActivitySettingsBinding;
import com.google.firebase.auth.FirebaseAuth;
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
import com.theartofdev.edmodo.cropper.CropImageView;

import org.checkerframework.common.initializedfields.qual.InitializedFields;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsActivity extends AppCompatActivity {
    private Button UpdateAccountSettings;
    private EditText userName,userStatus;
    private CircleImageView userProfileImage;
    private String currentUserID;
    private String photoUrl;
    private FirebaseAuth mAuth;
    private ProgressDialog loadingbar;
    private DatabaseReference RootRef;
    private static final int GalleryPick=1;
    private StorageReference UserProfileImagesRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        mAuth=FirebaseAuth.getInstance();
        currentUserID=mAuth.getCurrentUser().getUid();
        RootRef= FirebaseDatabase.getInstance().getReference();
        UserProfileImagesRef= FirebaseStorage.getInstance().getReference().child("Profile Images");
        InitializeFields();

        //userName.setVisibility(View.INVISIBLE);
        UpdateAccountSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdateSettings();
            }
        });
        RetrieveUserInfo();
        userProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent,GalleryPick);
            }
        });
    }

    private void InitializeFields(){
        UpdateAccountSettings=(Button) findViewById(R.id.update_settings_button);
        userName=(EditText) findViewById(R.id.set_user_name);
        userStatus=(EditText) findViewById(R.id.set_profile_status);
        userProfileImage=(CircleImageView) findViewById(R.id.set_profile_image);
        loadingbar=new ProgressDialog(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==GalleryPick && resultCode==RESULT_OK && data!=null){
            Uri imageUri=data.getData();
            CropImage.activity(imageUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1,1)
                    .start(this);
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if(resultCode==RESULT_OK){
                loadingbar.setTitle("Set Profile Image");
                loadingbar.setMessage("Please wait,your profile image is updating...");
                loadingbar.setCanceledOnTouchOutside(false);
                loadingbar.show();
                Uri resultUri=result.getUri();
                StorageReference filePath=UserProfileImagesRef.child(currentUserID+".jpg");
                filePath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(SettingsActivity.this, "Profile Image Uploaded Successfully", Toast.LENGTH_SHORT).show();
                            filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    final String downloadUrl=uri.toString();
                                    photoUrl=downloadUrl;
                                    HashMap<String,Object>profileMap=new HashMap<>();
                                    profileMap.put("uid",currentUserID);
                                    profileMap.put("name", userName);
                                    profileMap.put("status",userStatus);
                                    if(!TextUtils.isEmpty(photoUrl)){
                                        profileMap.put("image",photoUrl);
                                    }
                                    RootRef.child("Users").child(currentUserID).child("image")
                                            .setValue(downloadUrl)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if(task.isSuccessful()){
                                                        Toast.makeText(SettingsActivity.this, "Image saved in Database Successfully", Toast.LENGTH_SHORT).show();
                                                        loadingbar.dismiss();
                                                    }
                                                    else{
                                                        String message=task.getException().toString();
                                                        Toast.makeText(SettingsActivity.this, "Error: "+message, Toast.LENGTH_SHORT).show();
                                                        loadingbar.dismiss();
                                                    }
                                                }
                                            });
                                }
                            });
                        }
                        else{
                            String message=task.getException().toString();
                            Toast.makeText(SettingsActivity.this, "Error: "+ message, Toast.LENGTH_SHORT).show();
                            loadingbar.dismiss();
                        }
                    }
                });
            }

        }
    }

    private void UpdateSettings(){
        String setUserName=userName.getText().toString();
        String setStatus=userStatus.getText().toString();
        if(TextUtils.isEmpty(setUserName)){
            Toast.makeText(this, "Please write your name first...", Toast.LENGTH_SHORT).show();
        }
        if(TextUtils.isEmpty(setStatus)){
            Toast.makeText(this, "Please write your status..", Toast.LENGTH_SHORT).show();
        }
        else{
            HashMap<String,String>profileMap=new HashMap<>();
            profileMap.put("uid",currentUserID);
            profileMap.put("name",setUserName);
            profileMap.put("status",setStatus);
            profileMap.put("image",photoUrl);
            RootRef.child("Users").child(currentUserID).setValue(profileMap)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                SendUserToMainActivity();
                                Toast.makeText(SettingsActivity.this, "Profile Updated Successfully...", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                String message=task.getException().toString();
                                Toast.makeText(SettingsActivity.this, "Error: "+message, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }
    private void SendUserToMainActivity() {
        Intent mainIntent = new Intent(SettingsActivity.this,MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();
    }
    private void RetrieveUserInfo() {
    RootRef.child("Users").child(currentUserID)
            .addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                    if((datasnapshot.exists()) && (datasnapshot.hasChild("name") && (datasnapshot.hasChild("image")))){
                        String retrieveUserName=datasnapshot.child("name").getValue().toString();
                        String retrieveUserStatus=datasnapshot.child("status").getValue().toString();
                        String retrieveProfileImage=datasnapshot.child("image").getValue().toString();
                        userName.setText(retrieveUserName);
                        userStatus.setText(retrieveUserStatus);
                        photoUrl=retrieveProfileImage;
                        Picasso.get().load(retrieveProfileImage).into(userProfileImage);
                    }
                    else if((datasnapshot.exists()) && (datasnapshot.hasChild("name"))){
                        String retrieveUserName=datasnapshot.child("name").getValue().toString();
                        String retrieveUserStatus=datasnapshot.child("status").getValue().toString();
                        userName.setText(retrieveUserName);
                        userStatus.setText(retrieveUserStatus);
                    }
                    else{
                        userName.setVisibility(View.VISIBLE);
                        Toast.makeText(SettingsActivity.this, "Please set & update your profile information", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
  }

}