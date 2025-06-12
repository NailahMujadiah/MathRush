package com.example.mathrush;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
    private static final String ARG_USER_ID = "userId";
    private static final String ARG_TOPIC = "topic";
    private static final String ARG_LEVEL = "level";

    private ArrayList<QuestionsItem> questionList;
    private int currentQuestionIndex = 0;
    private AppDatabaseHelper dbHelper;
    private int userId;
    private String topic;
    private String level;
    private Button btnRefresh;


    private TextView tvQuestion;
    private RecyclerView rvChoices;
    private ChoicesAdapter choicesAdapter;

    public static QuestionFragment newInstance(ArrayList<QuestionsItem> questions, int userId, String topic, String level) {
        QuestionFragment fragment = new QuestionFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_QUESTIONS, questions);
        args.putInt(ARG_USER_ID, userId);
        args.putString(ARG_TOPIC, topic);
        args.putString(ARG_LEVEL, level);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            questionList = getArguments().getParcelableArrayList(ARG_QUESTIONS);
            userId = getArguments().getInt(ARG_USER_ID, userId);
            topic = getArguments().getString(ARG_TOPIC, "");
            level = getArguments().getString(ARG_LEVEL, "");
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
        btnRefresh = view.findViewById(R.id.btnRefresh);
        btnRefresh.setOnClickListener(v -> {
            btnRefresh.setVisibility(View.GONE);
            loadQuestion();
        });

        tvQuestion = view.findViewById(R.id.tvQuestion);
        rvChoices = view.findViewById(R.id.rvChoices);
        rvChoices.setLayoutManager(new GridLayoutManager(getContext(), 2));

        loadQuestion();
    }

    private void loadQuestion() {
        if (currentQuestionIndex >= questionList.size()) {
            showSafeToast("Semua soal selesai!");
            new Handler().postDelayed(() -> {
                if (isAdded()) {
                    requireActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, new HomeFragment())
                            .commit();
                }
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
                        dbHelper = new AppDatabaseHelper(getContext());
                        choicesAdapter = new ChoicesAdapter(requireContext(), allChoices, selected -> {
                            if (selected.equals(correctAnswer)) {
                                Activity activity = getActivity();
                                if (activity != null && !activity.isFinishing()) {
                                    activity.runOnUiThread(() ->
                                            Toast.makeText(activity.getApplicationContext(), "Jawaban benar!", Toast.LENGTH_SHORT).show()

                                    );
                                }
                                dbHelper.printAllProgressDebug();

                                AppDatabaseHelper dbHelper = new AppDatabaseHelper(requireContext());
                                boolean isLast = (currentQuestionIndex == questionList.size() - 1);
                                dbHelper.addOrUpdateQuizProgress(userId, topic, level, 10, isLast);

                                new Handler().postDelayed(() -> {
                                    currentQuestionIndex++;
                                    loadQuestion();
                                }, 1000);
                            } else {
                                showSafeToast("Salah!");
                            }
                        });

                        rvChoices.setAdapter(choicesAdapter);
                    } catch (IOException e) {
                        e.printStackTrace();
                        showSafeToast("Gagal memproses jawaban");
                    }
                } else {
                    showSafeToast("Gagal ambil jawaban");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("QuestionFragment", "API call gagal", t);
                showSafeToast("Terjadi kesalahan jaringan");


                if (btnRefresh != null) {
                    btnRefresh.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void showSafeToast(String message) {
        Activity activity = getActivity();
        if (activity != null && isAdded()) {
            Toast.makeText(activity.getApplicationContext(), message, Toast.LENGTH_SHORT).show();
        }
    }
}
