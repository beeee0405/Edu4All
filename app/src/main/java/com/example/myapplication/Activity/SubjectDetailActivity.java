package com.example.myapplication.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Adapter.ChapterAdapter;
import com.example.myapplication.Database.AppDatabase;
import com.example.myapplication.Entity.ExamEntity;
import com.example.myapplication.Entity.UnitEntity;
import com.example.myapplication.Model.Chapter;
import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.List;

public class SubjectDetailActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView rvChapters;
    private ChapterAdapter chapterAdapter;
    private List<Chapter> chapterList;
    private String currentSubject;
    private String currentTab = "Theory";

    private TextView tabTheory, tabDocument, tabExam, tabQuickGame;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_detail);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        rvChapters = findViewById(R.id.rvChapters);
        rvChapters.setLayoutManager(new LinearLayoutManager(this));

        tabTheory = findViewById(R.id.tabTheory);
        tabDocument = findViewById(R.id.tabDocument);
        tabExam = findViewById(R.id.tabExam);
        tabQuickGame = findViewById(R.id.tabQuickGame);

        currentSubject = getIntent().getStringExtra("SUBJECT_NAME");

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            if (currentSubject != null) {
                getSupportActionBar().setTitle("Môn " + currentSubject);
            }
        }

        setupTabListeners();
        loadChapterList(currentTab);
    }

    private void setupTabListeners() {
        tabTheory.setOnClickListener(v -> loadChapterList("Theory"));
        tabDocument.setOnClickListener(v -> loadChapterList("Document"));
        tabExam.setOnClickListener(v -> loadChapterList("Exam"));
        tabQuickGame.setOnClickListener(v -> loadChapterList("QuickGame"));
    }

    private void loadChapterList(String tab) {
        currentTab = tab;
        updateTabSelection(tab);
        chapterList = new ArrayList<>();
        AppDatabase db = AppDatabase.getInstance(this);

        if ("Anh".equals(currentSubject)) {
            switch (tab) {
                case "Theory":
                    List<UnitEntity> units = db.unitDao().getAllUnits();
                    for (UnitEntity unit : units) {
                        chapterList.add(new Chapter(unit.getId(), unit.getTitle(), unit.getDescription(), unit.getProgress()));
                    }
                    break;
                case "Exam":
                    List<ExamEntity> exams = db.examDao().getAllExams();
                    for (ExamEntity exam : exams) {
                        chapterList.add(new Chapter(exam.getId(), exam.getTitle(), "50 câu", 0));
                    }
                    break;
                case "QuickGame":
                    chapterList.add(new Chapter(0, "Flashcards", "Học từ vựng", 0));
                    chapterList.add(new Chapter(0, "Word Matching", "Nối từ", 0));
                    chapterList.add(new Chapter(0, "Grammar Quiz", "Trắc nghiệm", 0));
                    break;
            }
        }

        chapterAdapter = new ChapterAdapter(chapterList, currentTab, (chapter, position) -> {
            if ("Exam".equals(currentTab)) {
                Intent intent = new Intent(this, ExamActivity.class);
                intent.putExtra("EXAM_ID", chapter.getId());
                intent.putExtra("EXAM_TITLE", chapter.getTitle());
                startActivity(intent);
            } else if ("Theory".equals(currentTab) && "Anh".equals(currentSubject)) {
                // CORRECTED: Passing the real Unit ID from the chapter object
                Intent intent = new Intent(SubjectDetailActivity.this, UnitDetailActivity.class);
                intent.putExtra("UNIT_ID", chapter.getId());
                startActivity(intent);
            } else if ("QuickGame".equals(currentTab)) {
                if ("Flashcards".equals(chapter.getTitle())) {
                    startActivity(new Intent(this, FlashcardActivity.class));
                } else if ("Word Matching".equals(chapter.getTitle())) {
                    startActivity(new Intent(this, WordMatchingActivity.class));
                } else if ("Grammar Quiz".equals(chapter.getTitle())) {
                    startActivity(new Intent(this, GrammarQuizActivity.class));
                }
            }
        });
        rvChapters.setAdapter(chapterAdapter);
    }

    private void updateTabSelection(String selectedTab) {
        updateTabTextStyle(tabTheory, "Theory".equals(selectedTab));
        updateTabTextStyle(tabDocument, "Document".equals(selectedTab));
        updateTabTextStyle(tabExam, "Exam".equals(selectedTab));
        updateTabTextStyle(tabQuickGame, "QuickGame".equals(selectedTab));
    }

    private void updateTabTextStyle(TextView tab, boolean isSelected) {
        if (isSelected) {
            tab.setTypeface(null, Typeface.BOLD);
            tab.setTextColor(Color.BLUE);
            tab.setBackgroundColor(Color.parseColor("#E0E0E0"));
        } else {
            tab.setTypeface(null, Typeface.NORMAL);
            tab.setTextColor(Color.BLACK);
            tab.setBackgroundColor(Color.TRANSPARENT);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
