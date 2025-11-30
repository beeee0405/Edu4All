package com.example.myapplication.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Model.ReadingQuestion;
import com.example.myapplication.R;

import java.util.List;

public class ReadingQuestionAdapter extends RecyclerView.Adapter<ReadingQuestionAdapter.QuestionViewHolder> {

    private List<ReadingQuestion> questionList;

    public ReadingQuestionAdapter(List<ReadingQuestion> questionList) {
        this.questionList = questionList;
    }

    @NonNull
    @Override
    public QuestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_reading_question, parent, false);
        return new QuestionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionViewHolder holder, int position) {
        ReadingQuestion question = questionList.get(position);
        holder.bind(question, position);
    }

    @Override
    public int getItemCount() {
        return questionList.size();
    }

    public static class QuestionViewHolder extends RecyclerView.ViewHolder {
        TextView tvQuestionNumber, tvQuestionText;
        RadioGroup rgOptions;

        public QuestionViewHolder(@NonNull View itemView) {
            super(itemView);
            tvQuestionNumber = itemView.findViewById(R.id.tvQuestionNumber);
            tvQuestionText = itemView.findViewById(R.id.tvQuestionText);
            rgOptions = itemView.findViewById(R.id.rgOptions);
        }

        public void bind(final ReadingQuestion question, int position) {
            tvQuestionNumber.setText("Câu " + (position + 1) + ":");
            tvQuestionText.setText(question.getQuestionText());

            rgOptions.removeAllViews();
            for (int i = 0; i < question.getOptions().size(); i++) {
                RadioButton radioButton = new RadioButton(itemView.getContext());
                radioButton.setText(question.getOptions().get(i));
                radioButton.setId(i);
                rgOptions.addView(radioButton);
            }

            rgOptions.setOnCheckedChangeListener((group, checkedId) -> {
                question.setUserAnswerIndex(checkedId);
            });

            // Restore user's previous answer if it exists
            if (question.getUserAnswerIndex() != -1) {
                rgOptions.check(question.getUserAnswerIndex());
            }
        }
    }
}
