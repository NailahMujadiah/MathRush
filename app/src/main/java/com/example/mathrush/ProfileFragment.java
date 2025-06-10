package com.example.mathrush;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the  factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    private TextView tvUsername;
    private AppDatabaseHelper dbHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        tvUsername = view.findViewById(R.id.tvUsername);
        dbHelper = new AppDatabaseHelper(getContext());

        Bundle bundle = getArguments();
        if (bundle != null) {
            int userId = bundle.getInt("userId", -1);

            if (userId != -1) {
                UserModel user = getUserById(userId);
                if (user != null) {
                    tvUsername.setText(user.getUsername());
                }
            }
        }

        return view;
    }

    private UserModel getUserById(int userId) {
        List<UserModel> userList = dbHelper.getAllUsers();
        for (UserModel user : userList) {
            if (user.getId() == userId) {
                return user;
            }
        }
        return null;
    }
}
