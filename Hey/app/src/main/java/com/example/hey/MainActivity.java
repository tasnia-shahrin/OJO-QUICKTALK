package com.example.hey;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hey.Adapter.FragmentsAdapter;
import com.example.hey.databinding.ActivityMainBinding;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    FirebaseAuth mAuth;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mAuth = FirebaseAuth.getInstance();
        binding.viewPager.setAdapter(new FragmentsAdapter(getSupportFragmentManager()));
        binding.tabLayout.setupWithViewPager(binding.viewPager);
        ImageView righticon = findViewById(R.id.right_icon);
        TextView title = findViewById(R.id.toolbar_title);
        righticon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMenu(v);
            }
        });
        title.setText("OJO Quickchat");
    }

    private void showMenu(View v) {
        PopupMenu popupMenu = new PopupMenu(MainActivity.this, v);
        popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.settings) {
                    Intent intent2=new Intent(MainActivity.this, SettingsActivity.class);
                    startActivity(intent2);
                }
                if (menuItem.getItemId() == R.id.groupchat) {
                    Intent intent1=new Intent(MainActivity.this, GroupChatActivity.class);
                    startActivity(intent1);
                }
                if (menuItem.getItemId() == R.id.logout) {
                    mAuth.signOut();
                    Intent intent=new Intent(MainActivity.this, SignInActivity.class);
                    startActivity(intent);
                }
                return false;
            }
        });
        popupMenu.show();
    }
}

//    public boolean onCreateOptionMenu(Menu menu){
//        MenuInflater inflater=getMenuInflater();
//        inflater.inflate(R.menu.menu,menu);
//        return super.onCreateOptionsMenu(menu);
//    }
//}
      //  drawerLayout=(DrawerLayout) findViewById(R.id.dlayout);
      //  navigationView=(NavigationView) findViewById(R.id.nview);
       // navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                UserMenuselector(item);
//                return false;
//            }
//        });
//    }
//
//    private void UserMenuselector(MenuItem item) {
//        int id=item.getItemId();
//        if(id == R.id.settings){
//            Toast.makeText(this, "Setting is clicked", Toast.LENGTH_SHORT).show();
//
//        }
//        if(id == R.id.groupChat){
//            Toast.makeText(this, "group chat is started", Toast.LENGTH_SHORT).show();
//
//        }
//        if(id == R.id.logout){
//           mAuth.signOut();
//            Intent intent=new Intent(MainActivity.this,SignInActivity.class);
//            startActivity(intent);
//
//        }
//    }



//}