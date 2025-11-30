package com.example.myapplication.Activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Adapter.ReadingQuestionAdapter;
import com.example.myapplication.Database.DBHelper;
import com.example.myapplication.Model.Question2; // We'll convert from this
import com.example.myapplication.Model.Reading;
import com.example.myapplication.Model.ReadingQuestion; // To this
import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ReadingQuizActivity extends AppCompatActivity {

    private TextView tvReadingPassage;
    private RecyclerView rvReadingQuestions;
    private Button btnSubmitReadingQuiz;
    private ReadingQuestionAdapter adapter;

    private DBHelper db;
    private List<ReadingQuestion> questionList;
    private int unitId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reading_quiz);

        unitId = getIntent().getIntExtra("UNIT_ID", 1);
        db = new DBHelper(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Bài đọc Unit " + unitId);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        initViews();
        loadContent();
        setupRecyclerView();

        btnSubmitReadingQuiz.setOnClickListener(v -> checkScore());
    }

    private void initViews() {
        tvReadingPassage = findViewById(R.id.tvReadingPassage);
        rvReadingQuestions = findViewById(R.id.rvReadingQuestions);
        btnSubmitReadingQuiz = findViewById(R.id.btnSubmitReadingQuiz);
    }

    private void loadContent() {
        Reading reading = db.getReading(unitId);
        if (reading != null) {
            tvReadingPassage.setText(reading.content);
        }

        // Convert old Question2 model to new ReadingQuestion model
        List<Question2> oldQuestions = db.getQuestions(unitId);
        questionList = new ArrayList<>();
        for (Question2 oldQ : oldQuestions) {
            List<String> options = Arrays.asList(oldQ.optionA, oldQ.optionB, oldQ.optionC, oldQ.optionD);
            int correctIndex = -1;
            if ("A".equals(oldQ.correct)) correctIndex = 0;
            else if ("B".equals(oldQ.correct)) correctIndex = 1;
            else if ("C".equals(oldQ.correct)) correctIndex = 2;
            else if ("D".equals(oldQ.correct)) correctIndex = 3;

            questionList.add(new ReadingQuestion(oldQ.getQuestionText2(), options, correctIndex));
        }
    }

    private void setupRecyclerView() {
        if (questionList.isEmpty()) {
            Toast.makeText(this, "Không có câu hỏi cho bài đọc này!", Toast.LENGTH_LONG).show();
            return;
        }
        adapter = new ReadingQuestionAdapter(questionList);
        rvReadingQuestions.setLayoutManager(new LinearLayoutManager(this));
        rvReadingQuestions.setAdapter(adapter);
    }

    private void checkScore() {
        int score = 0;
        for (ReadingQuestion question : questionList) {
            if (question.getUserAnswerIndex() == question.getCorrectOptionIndex()) {
                score++;
            }
        }
        
        new AlertDialog.Builder(this)
            .setTitle("Kết quả")
            .setMessage("Bạn đã trả lời đúng " + score + "/" + questionList.size() + " câu.")
            .setPositiveButton("Làm lại", (dialog, which) -> {
                // Reset and reload
                loadContent(); 
                setupRecyclerView();
            })
            .setNegativeButton("Thoát", (dialog, which) -> finish())
            .setCancelable(false)
            .show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
