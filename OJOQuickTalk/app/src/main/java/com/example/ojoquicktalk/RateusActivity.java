package com.example.ojoquicktalk;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class RateusActivity extends AppCompatActivity{
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    TextView ev,mail,mail2;
    LottieAnimationView lottie;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rateus);

        drawerLayout=findViewById(R.id.drawer_layout);
        toolbar=findViewById(R.id.toolbar);

        FirebaseAuth firebaseAuth= FirebaseAuth.getInstance();
        FirebaseUser firebaseUser= firebaseAuth.getCurrentUser();
        String email= firebaseUser.getEmail();
        ev= findViewById(R.id.subTitleTv);
        mail= findViewById(R.id.email);
        mail2=findViewById(R.id.email2);
        ev.setText(email);
        lottie= findViewById(R.id.lottie);

        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sellerEmail= mail.getText().toString();
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                emailIntent.setData(Uri.parse("mailto:" + sellerEmail)); // Set the email address
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Regarding the app"); // Set the email subject
                emailIntent.putExtra(Intent.EXTRA_TEXT, "Hello, I am interested in your app."); // Set the email body
                try {
                    startActivity(Intent.createChooser(emailIntent, "Send Email"));
                } catch (android.content.ActivityNotFoundException ex) {
                    // Handle the case where no email client is installed on the device
                    Toast.makeText(RateusActivity.this, "No email client installed.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        mail2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sellerEmail= mail2.getText().toString();
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                emailIntent.setData(Uri.parse("mailto:" + sellerEmail)); // Set the email address
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Regarding the app"); // Set the email subject
                emailIntent.putExtra(Intent.EXTRA_TEXT, "Hello, I am interested in your app."); // Set the email body

                try {
                    startActivity(Intent.createChooser(emailIntent, "Send Email"));
                } catch (android.content.ActivityNotFoundException ex) {
                    // Handle the case where no email client is installed on the device
                    Toast.makeText(RateusActivity.this, "No email client installed.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

}
