package com.example.whenyoucomemerona.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import androidx.fragment.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.whenyoucomemerona.R;
import com.example.whenyoucomemerona.ui.add.AddFragment;
import com.example.whenyoucomemerona.ui.home.HomeFragment;
import com.example.whenyoucomemerona.ui.map.MapFragment;
import com.example.whenyoucomemerona.ui.mypage.MyPageFragment;
import com.example.whenyoucomemerona.ui.notification.NotificationFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomnavigation.BottomNavigationView.OnNavigationItemSelectedListener;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigation;

    AddFragment addFragment;
    HomeFragment homeFragment;
    MapFragment mapFragment;
    NotificationFragment notificationFragment;
    MyPageFragment myPageFragment;

    FragmentManager fm;
    FragmentTransaction ft;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // fragment setup
        addFragment = new AddFragment();
        homeFragment = new HomeFragment();
        mapFragment = new MapFragment();
        notificationFragment = new NotificationFragment();
        myPageFragment = new MyPageFragment();

        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();

        getSupportFragmentManager().beginTransaction().add(R.id.body_rl, homeFragment).commit();

        // bottom navigation bar 선택
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
                    onFragmentSelected(1, null);
                    break;
                case R.id.page_2:
                    onFragmentSelected(2, null);
                    break;
                case R.id.page_3:
                    onFragmentSelected(3, null);
                    break;
                case R.id.page_4:
                    onFragmentSelected(4, null);
                    break;
                case R.id.page_5:
                    onFragmentSelected(5, null);
                    break;


            }
            return true;
        }
    };

    public void onFragmentSelected(int position, Bundle bundle) {
        Fragment currentFragment = null;

        switch (position) {
            case 1:
                currentFragment = homeFragment;
                break;
            case 2:
                currentFragment = mapFragment;
                break;
            case 3:
                currentFragment = addFragment;
                break;
            case 4:
                currentFragment = notificationFragment;
                break;
            case 5:
                currentFragment = myPageFragment;
                break;

        }

        getSupportFragmentManager().beginTransaction().replace(R.id.body_rl, currentFragment).commit();
    }

}