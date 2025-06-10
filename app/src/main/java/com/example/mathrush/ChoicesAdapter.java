package com.example.mathrush;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ChoicesAdapter extends RecyclerView.Adapter<ChoicesAdapter.ChoiceViewHolder> {

    public interface OnChoiceClickListener {
        void onChoiceClicked(String choice);
    }

    private Context context;
    private List<String> choices;
    private OnChoiceClickListener listener;

    public ChoicesAdapter(Context context, List<String> choices, OnChoiceClickListener listener) {
        this.context = context;
        this.choices = choices;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ChoiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_choice, parent, false);
        return new ChoiceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChoiceViewHolder holder, int position) {
        String choice = choices.get(position);
        holder.btnChoice.setText(choice);
        holder.btnChoice.setOnClickListener(v -> listener.onChoiceClicked(choice));
    }

    @Override
    public int getItemCount() {
        return choices.size();
    }

    public static class ChoiceViewHolder extends RecyclerView.ViewHolder {
        Button btnChoice;
        public ChoiceViewHolder(@NonNull View itemView) {
            super(itemView);
            btnChoice = itemView.findViewById(R.id.btnChoice);
        }
    }
}
