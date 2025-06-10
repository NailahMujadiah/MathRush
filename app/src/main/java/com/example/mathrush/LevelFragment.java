package com.example.mathrush;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mathrush.questions.LevelsItem;
import com.example.mathrush.questions.QuestionsItem;

import java.util.ArrayList;
import java.util.List;

public class LevelFragment extends Fragment {

    private static final String ARG_SELECTED_TOPIC = "selected_topic";

    private QuestionsItem selectedTopic;
    private RecyclerView rvLevels;

    public static LevelFragment newInstance(QuestionsItem topic) {
        LevelFragment fragment = new LevelFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_SELECTED_TOPIC, topic); // make sure QuestionsItem Parcelable
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            selectedTopic = getArguments().getParcelable(ARG_SELECTED_TOPIC);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_level, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvLevels = view.findViewById(R.id.rvLevels);
        rvLevels.setLayoutManager(new LinearLayoutManager(getContext()));

        if (selectedTopic == null) {
            Log.e("LevelFragment", "selectedTopic is null");
            Toast.makeText(getContext(), "Topic data not found!", Toast.LENGTH_SHORT).show();
            return;
        }

        List<LevelsItem> levels = selectedTopic.getLevels();

        if (levels == null || levels.isEmpty()) {
            Log.e("LevelFragment", "Levels list empty");
            Toast.makeText(getContext(), "No levels available", Toast.LENGTH_SHORT).show();
            return;
        }

        LevelAdapter adapter = new LevelAdapter(levels, level -> {
            List<QuestionsItem> questions = level.getQuestions();

            if (questions != null && !questions.isEmpty()) {
                // Convert ke ArrayList karena Bundle butuh ParcelableArrayList
                ArrayList<QuestionsItem> questionList = new ArrayList<>(questions);

                // Buat fragment baru dan kirim data
                QuestionFragment fragment = QuestionFragment.newInstance(questionList);
                requireActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, fragment)
                        .addToBackStack(null)
                        .commit();
            } else {
                Toast.makeText(getContext(), "Soal belum tersedia untuk level ini", Toast.LENGTH_SHORT).show();
            }
        });


        rvLevels.setAdapter(adapter);
    }
}
