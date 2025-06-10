package com.example.mathrush;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.mathrush.R;
import com.example.mathrush.questions.QuestionsItem;

import java.util.List;

public class TopicAdapter extends RecyclerView.Adapter<TopicAdapter.TopicViewHolder> {

    private List<QuestionsItem> topicList;
    private Context context;
    private OnTopicClickListener listener;

    public interface OnTopicClickListener {
        void onTopicClick(QuestionsItem topic);
    }

    public TopicAdapter(Context context, List<QuestionsItem> topicList, OnTopicClickListener listener) {
        this.context = context;
        this.topicList = topicList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TopicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.topic_item, parent, false);
        return new TopicViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TopicViewHolder holder, int position) {
        QuestionsItem topic = topicList.get(position);
        holder.tvTopicTitle.setText(topic.getTitle());

        // Ambil nama icon dari JSON (tanpa .png)
        int imageRes = context.getResources().getIdentifier(
                topic.getIcon(), "drawable", context.getPackageName());
        holder.imgTopicIcon.setImageResource(imageRes);

        holder.itemView.setOnClickListener(v -> listener.onTopicClick(topic));
    }

    @Override
    public int getItemCount() {
        return topicList.size();
    }

    public static class TopicViewHolder extends RecyclerView.ViewHolder {
        TextView tvTopicTitle;
        ImageView imgTopicIcon;

        public TopicViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTopicTitle = itemView.findViewById(R.id.tvTopicTitle);
            imgTopicIcon = itemView.findViewById(R.id.imgTopicIcon);
        }
    }
}
