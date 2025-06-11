package com.example.mathrush;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mathrush.R;
import com.example.mathrush.TopicAdapter;
import com.example.mathrush.questions.QuestionsItem;
import com.example.mathrush.questions.Questions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.List;

public class HomeFragment extends Fragment {

    private RecyclerView rvTopics;
    private TextView tvGreeting;
    private AppDatabaseHelper dbHelper;
    private int userId;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvTopics = view.findViewById(R.id.rvTopics);
        tvGreeting = view.findViewById(R.id.tvGreeting);
        rvTopics.setLayoutManager(new LinearLayoutManager(getContext()));

        List<QuestionsItem> topicList = loadQuestionsFromJson();

        dbHelper = new AppDatabaseHelper(getContext());

        if (getActivity() instanceof MainActivity) {
            userId = ((MainActivity) getActivity()).getUserId();
            Log.d("CEK_USERID", "userId final yang dipakai di HomeFragment: " + userId);

            // ❗️Baru panggil username setelah userId valid
            UserModel user = dbHelper.getUserById(userId);
            if (user != null) {
                tvGreeting.setText("Hai, " + user.getUsername() + "!");
            } else {
                tvGreeting.setText("Hai, User!");
                Log.e("HomeFragment", "User dengan ID " + userId + " tidak ditemukan!");
            }
        }

        TopicAdapter adapter = new TopicAdapter(requireContext(), topicList, userId, (topic, userId) -> {
            LevelFragment fragment = LevelFragment.newInstance(topic, userId);
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit();
        });


        rvTopics.setAdapter(adapter);
        Log.d("CEK_USERID", "userId final yang dipakai di fragment: " + userId);

    }

    private List<QuestionsItem> loadQuestionsFromJson() {
        String json = null;
        try {
            InputStream is = requireContext().getAssets().open("data/questions.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        Type listType = new TypeToken<List<QuestionsItem>>() {}.getType();
        return new Gson().fromJson(json, listType);
    }



}
