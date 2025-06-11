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
        userId = getIntent().getIntExtra("user_id", -1);
        getSharedPreferences("USER_PREF", MODE_PRIVATE)
                .edit()
                .putInt("user_id", userId)
                .apply();
        bottomNav = findViewById(R.id.bottom_nav);// Ambil dari LoginActivity

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new com.example.mathrush.HomeFragment())
                .commit();

        bottomNav.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            int itemId = item.getItemId();

            if (itemId == R.id.nav_home) {
                HomeFragment homeFragment = new HomeFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("user_id", userId); // ✅ kirim userId
                homeFragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, homeFragment)
                        .commit();
            } else if (itemId == R.id.nav_leaderboard) {
                selectedFragment = new LeaderBoardFragment();
            } else if (itemId == R.id.nav_profile) {
                selectedFragment = new ProfileFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("user_id", userId); // kirim userId ke ProfileFragment
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
