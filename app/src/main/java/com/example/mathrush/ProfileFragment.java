package com.example.mathrush;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends Fragment {

    private RecyclerView rvProgress;
    private TextView tvUsername;
    private Button btnLogout;
    private int userId;
    private AppDatabaseHelper dbHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        rvProgress = view.findViewById(R.id.rvProgress);
        rvProgress.setLayoutManager(new LinearLayoutManager(getContext()));

        btnLogout = view.findViewById(R.id.btnLogout);

        tvUsername = view.findViewById(R.id.tvUsername);
        dbHelper = new AppDatabaseHelper(getContext());

        // Ambil userId langsung dari MainActivity
        if (getActivity() instanceof MainActivity) {
            userId = ((MainActivity) getActivity()).getUserId();
            Log.d("CEK_USERID", "userId final yang dipakai di ProfileFragment: " + userId);
        }

        btnLogout.setOnClickListener(v -> {
            // Optional: bisa tambahin clear session kalau simpan session di SharedPreferences
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // biar nggak bisa balik ke MainActivity
            startActivity(intent);
        });

        // Pastikan userId valid (> 0) baru lanjut
        if (userId > 0) {
            int total = dbHelper.getTotalScoreByUserId(userId);
            Log.d("TOTAL_SKOR", "Total skor user: " + total);

            ProgressAdapter emptyAdapter = new ProgressAdapter(new ArrayList<>());
            rvProgress.setAdapter(emptyAdapter);

            new Thread(() -> {
                List<QuizProgressModel> progressList = dbHelper.getUserProgressList(userId);
                Log.d("DEBUG_PROGRESS", "Total progress ditemukan: " + progressList.size());
                UserModel user = dbHelper.getUserById(userId);

                Log.d("ProfileFragment", "Jumlah progress: " + progressList.size());

                requireActivity().runOnUiThread(() -> {
                    ProgressAdapter adapter = new ProgressAdapter(progressList);
                    rvProgress.setAdapter(adapter);

                    if (user != null) {
                        tvUsername.setText(user.getUsername());
                        Log.d("ProfileFragment", "User ditemukan: " + user.getUsername());
                    } else {
                        Log.e("ProfileFragment", "User dengan ID " + userId + " tidak ditemukan!");
                    }
                });
            }).start();
        } else {
            Log.e("ProfileFragment", "UserId tidak valid: " + userId);
        }

        return view;
    }

}
