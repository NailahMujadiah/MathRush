package com.example.mathrush;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class ProfileFragment extends Fragment {

    private RecyclerView rvProgress;
    private TextView tvUsername;
    private AppDatabaseHelper dbHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        rvProgress = view.findViewById(R.id.rvProgress);
        rvProgress.setLayoutManager(new LinearLayoutManager(getContext()));

        tvUsername = view.findViewById(R.id.tvUsername);
        dbHelper = new AppDatabaseHelper(getContext());

        Bundle bundle = getArguments();
        if (bundle != null) {
            int userId = bundle.getInt("userId", -1);

            // 🚀 Pindahin query berat ke thread background
            new Thread(() -> {
                List<QuizProgressModel> progressList = dbHelper.getUserProgressList(userId);
                UserModel user = dbHelper.getUserById(userId); // kamu bisa bikin query langsung di DBHelper

                Log.d("ProfileFragment", "Jumlah progress: " + progressList.size());

                requireActivity().runOnUiThread(() -> {
                    // Update UI di main thread
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
        }

        return view;
    }
}
