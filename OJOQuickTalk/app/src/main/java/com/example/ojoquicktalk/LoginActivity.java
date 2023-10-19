package com.example.ojoquicktalk;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.core.view.WindowCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.ojoquicktalk.databinding.ActivityLoginBinding;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityLoginBinding binding;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

    }

    @Override
    protected void onStart() {
        super.onStart();
        if(currentUser!=null){
            SendUserToMainActivity();
        }
    }

    private void SendUserToMainActivity() {
        Intent loginIntent = new Intent(LoginActivity.this,MainActivity.class);
        startActivity(loginIntent);
    }
}