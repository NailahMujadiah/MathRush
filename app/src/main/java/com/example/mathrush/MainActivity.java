package com.example.mathrush;

import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNav;
    private int userId; // tambahkan di atas


    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        userId = getIntent().getIntExtra("userId", -1);
        bottomNav = findViewById(R.id.bottom_nav);// Ambil dari LoginActivity

//        ProfileFragment profileFragment = new ProfileFragment();
//        Bundle bundle = new Bundle();
//        bundle.putInt("userId", userId);
//        profileFragment.setArguments(bundle);
        // Fragment default (HomeFragment)
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new com.example.mathrush.HomeFragment())
                .commit();

        bottomNav.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            int itemId = item.getItemId();

            if (itemId == R.id.nav_home) {
                HomeFragment homeFragment = new HomeFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("userId", userId); // ✅ kirim userId
                homeFragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, homeFragment)
                        .commit();
            } else if (itemId == R.id.nav_leaderboard) {
                selectedFragment = new LeaderBoardFragment();
            } else if (itemId == R.id.nav_profile) {
                selectedFragment = new ProfileFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("userId", userId); // kirim userId ke ProfileFragment
                selectedFragment.setArguments(bundle);
            }

            if (selectedFragment != null) {
                // Hanya ganti fragment jika fragment yang dipilih berbeda
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, selectedFragment)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .commit();
            }

            return true;
        });
    }
    public int getUserId() {
        return userId;
    }


}
