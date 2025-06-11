package com.example.mathrush;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ProgressAdapter extends RecyclerView.Adapter<ProgressAdapter.ViewHolder> {
    private List<QuizProgressModel> progressList;

    public ProgressAdapter(List<QuizProgressModel> progressList) {
        this.progressList = progressList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_progress, parent, false); // layout item-nya nanti kita bikin
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        QuizProgressModel model = progressList.get(position);
        holder.tvTopic.setText("Topik: " + model.getTopic());
        holder.tvScore.setText("Skor: " + model.getScore());
    }

    @Override
    public int getItemCount() {
        return progressList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTopic, tvScore;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTopic = itemView.findViewById(R.id.tvTopic);
            tvScore = itemView.findViewById(R.id.tvScore);
        }
    }
}

