package com.example.myapplication.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Model.Chapter;
import com.example.myapplication.R;

import java.util.List;

public class ChapterAdapter extends RecyclerView.Adapter<ChapterAdapter.ChapterViewHolder> {

    private List<Chapter> chapterList;
    private final OnChapterClickListener listener;
    private String currentTab;

    // CORRECTED: The interface now provides both the item and its position
    public interface OnChapterClickListener {
        void onChapterClick(Chapter chapter, int position);
    }

    public ChapterAdapter(List<Chapter> chapterList, String currentTab, OnChapterClickListener listener) {
        this.chapterList = chapterList;
        this.currentTab = currentTab;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ChapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_chapter, parent, false);
        return new ChapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChapterViewHolder holder, int position) {
        Chapter chapter = chapterList.get(position);
        holder.bind(chapter, currentTab, listener);
    }

    @Override
    public int getItemCount() {
        return chapterList.size();
    }

    public static class ChapterViewHolder extends RecyclerView.ViewHolder {
        TextView tvChapterTitle, tvLessonCount, btnLearnNow;
        ProgressBar progressBar;

        public ChapterViewHolder(@NonNull View itemView) {
            super(itemView);
            tvChapterTitle = itemView.findViewById(R.id.tvChapterTitle);
            tvLessonCount = itemView.findViewById(R.id.tvLessonCount);
            progressBar = itemView.findViewById(R.id.progressBar);
            btnLearnNow = itemView.findViewById(R.id.btnLearnNow);
        }

        public void bind(final Chapter chapter, String currentTab, final OnChapterClickListener listener) {
            tvChapterTitle.setText(chapter.getTitle());
            tvLessonCount.setText(chapter.getLessonCount());
            progressBar.setProgress(chapter.getProgress());

            if ("Exam".equals(currentTab) || "QuickGame".equals(currentTab)) {
                btnLearnNow.setText(R.string.do_now);
            } else {
                btnLearnNow.setText(R.string.learn_now);
            }

            // CORRECTED: The click listener now passes the adapter position as well
            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onChapterClick(chapter, position);
                    }
                }
            });
        }
    }
}
