package com.example.myapplication.Activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Adapter.ReadingQuestionAdapter;
import com.example.myapplication.Database.DBHelper;
import com.example.myapplication.Model.Question2;
import com.example.myapplication.Model.ReadingQuestion;
import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GrammarQuizActivity extends AppCompatActivity {

    private RecyclerView rvGrammarQuestions;
    private Button btnSubmitGrammarQuiz;
    private ReadingQuestionAdapter adapter;

    private DBHelper db;
    private List<ReadingQuestion> questionList;
    private int unitId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grammar_quiz);

        unitId = getIntent().getIntExtra("UNIT_ID", 1);
        db = new DBHelper(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Ngữ pháp Unit " + unitId);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        initViews();
        loadContent();
        setupRecyclerView();

        btnSubmitGrammarQuiz.setOnClickListener(v -> checkScore());
    }

    private void initViews() {
        rvGrammarQuestions = findViewById(R.id.rvGrammarQuestions);
        btnSubmitGrammarQuiz = findViewById(R.id.btnSubmitGrammarQuiz);
    }

    private void loadContent() {
        // For now, let's reuse the same questions as the reading section for demonstration
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
            Toast.makeText(this, "Không có câu hỏi cho unit này!", Toast.LENGTH_LONG).show();
            return;
        }
        adapter = new ReadingQuestionAdapter(questionList);
        rvGrammarQuestions.setLayoutManager(new LinearLayoutManager(this));
        rvGrammarQuestions.setAdapter(adapter);
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
