package com.example.whenyoucomemerona.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.whenyoucomemerona.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomnavigation.BottomNavigationView.OnNavigationItemSelectedListener;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setItemIconTintList(null);
        // 하단 네비게이션 바가 선택될때,
        bottomNavigation.setOnNavigationItemSelectedListener(bottomNavigationSelectedListener);
    }

    // 하단 네비게이션 바가 선택될때,
    
    private BottomNavigationView.OnNavigationItemSelectedListener bottomNavigationSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.page_1:
                    Toast.makeText(MainActivity.this, "Home", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.page_2:
                    Toast.makeText(MainActivity.this, "Map", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.page_3:
                    Toast.makeText(MainActivity.this, "Add", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.page_4:
                    Toast.makeText(MainActivity.this, "Nofication", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.page_5:
                    Toast.makeText(MainActivity.this, "MyPage", Toast.LENGTH_SHORT).show();
                    break;

            }
            return true;
        }
    };
}