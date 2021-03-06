package com.example.whenyoucomemerona.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;

import com.example.whenyoucomemerona.R;
import com.example.whenyoucomemerona.controller.BaseActivity;
import com.example.whenyoucomemerona.main.AddFragment;
import com.example.whenyoucomemerona.main.HomeFragment;
import com.example.whenyoucomemerona.main.MapFragment;
import com.example.whenyoucomemerona.main.MyPageFragment;
import com.example.whenyoucomemerona.main.NotificationFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends BaseActivity {
    BottomNavigationView bottomNavigation;

    AddFragment addFragment;
    HomeFragment homeFragment;
    MapFragment mapFragment;
    NotificationFragment notificationFragment;
    MyPageFragment myPageFragment;

    Fragment currentFragment;


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
        currentFragment = null;


        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.body_rl, homeFragment)
                .commit();

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
        hideKeyboard(this);
        switch (position) {
            case 1:
                homeFragment = new HomeFragment();
                currentFragment = homeFragment;
                break;
            case 2:
                mapFragment = new MapFragment();
                currentFragment = mapFragment;
                break;
            case 3:
                addFragment = new AddFragment();
                currentFragment = addFragment;
                break;
            case 4:
                notificationFragment = new NotificationFragment();
                currentFragment = notificationFragment;
                break;
            case 5:
                myPageFragment = new MyPageFragment();
                currentFragment = myPageFragment;
                break;

        }

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.body_rl, currentFragment)
                .commit();


    }

}