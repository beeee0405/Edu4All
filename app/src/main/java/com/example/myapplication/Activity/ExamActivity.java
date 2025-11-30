package com.example.myapplication.Activity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.myapplication.Database.AppDatabase;
import com.example.myapplication.Entity.QuestionEntity;
import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ExamActivity extends AppCompatActivity {

    private List<QuestionEntity> questionList;
    private int currentQuestionIndex = 0;
    private int[] userAnswers; // Array to store user's answers

    private TextView tvTimer, tvQuestionNumber, tvQuestionText;
    private RadioGroup radioGroupAnswers;
    private Button btnPrev, btnNext, btnSubmitExam;

    private CountDownTimer countDownTimer;
    private long timeLeftInMillis;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        initializeViews();
        prepareExam();

        if (questionList == null || questionList.isEmpty()) {
            Toast.makeText(this, "Không có câu hỏi cho đề thi này!", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        displayCurrentQuestion();
        setupListeners();
        startTimer();
    }

    private void initializeViews() {
        tvTimer = findViewById(R.id.tvTimer);
        tvQuestionNumber = findViewById(R.id.tvQuestionNumber);
        tvQuestionText = findViewById(R.id.tvQuestionText);
        radioGroupAnswers = findViewById(R.id.radioGroupAnswers);
        btnPrev = findViewById(R.id.btnPrev);
        btnNext = findViewById(R.id.btnNext);
        btnSubmitExam = findViewById(R.id.btnSubmitExam);
    }

    private void prepareExam() {
        String examTitle = getIntent().getStringExtra("EXAM_TITLE");
        int examId = getIntent().getIntExtra("EXAM_ID", -1);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(examTitle);
        }

        if (examId != -1) {
            questionList = AppDatabase.getInstance(this).questionDao().getQuestionsByExamId(examId);
            userAnswers = new int[questionList.size()];
            for (int i = 0; i < userAnswers.length; i++) {
                userAnswers[i] = -1; // -1 means unanswered
            }
        }

        timeLeftInMillis = 45 * 60 * 1000;
    }

    private void displayCurrentQuestion() {
        radioGroupAnswers.removeAllViews();
        QuestionEntity question = questionList.get(currentQuestionIndex);

        tvQuestionNumber.setText("Câu " + (currentQuestionIndex + 1) + ":");
        tvQuestionText.setText(question.getQuestionText());

        List<String> options = new ArrayList<>();
        options.add(question.getOptionA());
        options.add(question.getOptionB());
        options.add(question.getOptionC());
        options.add(question.getOptionD());

        for (int i = 0; i < options.size(); i++) {
            RadioButton radioButton = new RadioButton(this);
            radioButton.setText(options.get(i));
            radioButton.setId(i);
            radioGroupAnswers.addView(radioButton);
            if (i == userAnswers[currentQuestionIndex]) {
                radioButton.setChecked(true);
            }
        }

        btnPrev.setEnabled(currentQuestionIndex > 0);
        btnNext.setEnabled(currentQuestionIndex < questionList.size() - 1);
        btnSubmitExam.setVisibility(currentQuestionIndex == questionList.size() - 1 ? View.VISIBLE : View.GONE);
    }

    // CORRECTED: Re-added the missing method
    private void setupListeners() {
        btnNext.setOnClickListener(v -> {
            saveUserAnswer();
            if (currentQuestionIndex < questionList.size() - 1) {
                currentQuestionIndex++;
                displayCurrentQuestion();
            }
        });

        btnPrev.setOnClickListener(v -> {
            saveUserAnswer();
            if (currentQuestionIndex > 0) {
                currentQuestionIndex--;
                displayCurrentQuestion();
            }
        });

        btnSubmitExam.setOnClickListener(v -> showSubmitConfirmation());
    }

    private void saveUserAnswer() {
        int selectedId = radioGroupAnswers.getCheckedRadioButtonId();
        userAnswers[currentQuestionIndex] = selectedId;
    }

    private void startTimer() {
        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateTimerText();
            }

            @Override
            public void onFinish() {
                Toast.makeText(ExamActivity.this, "Hết giờ! Tự động nộp bài.", Toast.LENGTH_LONG).show();
                submitExam();
            }
        }.start();
    }

    private void updateTimerText() {
        int minutes = (int) (timeLeftInMillis / 1000) / 60;
        int seconds = (int) (timeLeftInMillis / 1000) % 60;
        String timeFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        tvTimer.setText(timeFormatted);
    }

    private void showSubmitConfirmation() {
        saveUserAnswer(); // Save answer of the last question before submitting
        new AlertDialog.Builder(this)
            .setTitle("Nộp bài")
            .setMessage("Bạn có chắc chắn muốn nộp bài không?")
            .setPositiveButton("Nộp bài", (dialog, which) -> submitExam())
            .setNegativeButton("Hủy", null)
            .show();
    }

    private void submitExam() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        
        int score = 0;
        for (int i = 0; i < questionList.size(); i++) {
            if (userAnswers[i] == questionList.get(i).getCorrectOptionIndex()) {
                score++;
            }
        }

        new AlertDialog.Builder(this)
            .setTitle("Kết quả")
            .setMessage("Bạn đã trả lời đúng " + score + "/" + questionList.size() + " câu.")
            .setPositiveButton("OK", (dialog, which) -> finish())
            .setCancelable(false)
            .show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }
    
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
