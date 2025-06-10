package com.example.mathrush;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mathrush.questions.LevelsItem;

import java.util.List;

public class LevelAdapter extends RecyclerView.Adapter<LevelAdapter.LevelViewHolder> {

    public interface OnLevelClickListener {
        void onLevelClick(LevelsItem level);
    }

    private List<LevelsItem> levelList;
    private OnLevelClickListener listener;

    public LevelAdapter(List<LevelsItem> levelList, OnLevelClickListener listener) {
        this.levelList = levelList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public LevelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.level_item, parent, false);
        return new LevelViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LevelViewHolder holder, int position) {
        LevelsItem level = levelList.get(position);
        holder.tvDifficulty.setText(level.getDifficulty());
        holder.itemView.setOnClickListener(v -> listener.onLevelClick(level));
    }

    @Override
    public int getItemCount() {
        return levelList.size();
    }

    public static class LevelViewHolder extends RecyclerView.ViewHolder {
        TextView tvDifficulty;

        public LevelViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDifficulty = itemView.findViewById(R.id.tvDifficulty);
        }
    }
}
