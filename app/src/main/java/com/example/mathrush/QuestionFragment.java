// 1. QuestionFragment.java
package com.example.mathrush;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mathrush.questions.QuestionsItem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuestionFragment extends Fragment {

    private static final String ARG_QUESTIONS = "questions";
    private ArrayList<QuestionsItem> questionList;
    private int currentQuestionIndex = 0;

    private TextView tvQuestion;
    private RecyclerView rvChoices;
    private ChoicesAdapter choicesAdapter;

    public static QuestionFragment newInstance(ArrayList<QuestionsItem> questions) {
        QuestionFragment fragment = new QuestionFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_QUESTIONS, questions);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            questionList = getArguments().getParcelableArrayList(ARG_QUESTIONS);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_question, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvQuestion = view.findViewById(R.id.tvQuestion);
        rvChoices = view.findViewById(R.id.rvChoices);
        rvChoices.setLayoutManager(new GridLayoutManager(getContext(), 2));

        loadQuestion();
    }

    private void loadQuestion() {
        if (currentQuestionIndex >= questionList.size()) {
            Toast.makeText(getContext(), "Semua soal selesai!", Toast.LENGTH_LONG).show();
            new Handler().postDelayed(() -> {
                requireActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new HomeFragment())
                        .commit();
            }, 1500);
            return;
        }

        QuestionsItem question = questionList.get(currentQuestionIndex);
        tvQuestion.setText(question.getDisplayQuestion());

        question.getCorrectAnswerCall().enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        String correctAnswer = response.body().string().trim();
                        List<String> allChoices = new ArrayList<>(question.getWrongChoices());
                        allChoices.add(correctAnswer);
                        Collections.shuffle(allChoices);

                        choicesAdapter = new ChoicesAdapter(requireContext(), allChoices, selected -> {
                            if (selected.equals(correctAnswer)) {
                                Toast.makeText(getContext(), "Benar!", Toast.LENGTH_SHORT).show();
                                new Handler().postDelayed(() -> {
                                    currentQuestionIndex++;
                                    loadQuestion();
                                }, 1000);
                            } else {
                                Toast.makeText(getContext(), "Salah!", Toast.LENGTH_SHORT).show();
                            }
                        });

                        rvChoices.setAdapter(choicesAdapter);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("QuestionFragment", "API call gagal", t);
            }
        });
    }
}
