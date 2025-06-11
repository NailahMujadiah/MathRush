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

    public ProgressAdapter(List<QuizProgressModel> list) {
        this.progressList = list;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTopicLevel, tvScoreStatus, tvTimestamp;

        public ViewHolder(View itemView) {
            super(itemView);
            tvTopicLevel = itemView.findViewById(R.id.tvTopicLevel);
            tvScoreStatus = itemView.findViewById(R.id.tvScoreStatus);
            tvTimestamp = itemView.findViewById(R.id.tvTimestamp);
        }
    }

    @NonNull
    @Override
    public ProgressAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_progress, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProgressAdapter.ViewHolder holder, int position) {
        QuizProgressModel item = progressList.get(position);
        holder.tvTopicLevel.setText(item.getTopic() + " - " + item.getLevel());
        holder.tvScoreStatus.setText("Skor: " + item.getScore() + " | " + item.getStatus());
        holder.tvTimestamp.setText(item.getTimestamp());
    }

    @Override
    public int getItemCount() {
        return progressList.size();
    }
}
