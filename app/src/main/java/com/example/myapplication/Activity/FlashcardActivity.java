package com.example.myapplication.Activity;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import com.example.myapplication.Database.DBHelper;
import com.example.myapplication.Manager.SharedPrefManager;
import com.example.myapplication.Model.Word;
import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FlashcardActivity extends AppCompatActivity {

    private List<Word> wordList;
    private int currentWordIndex = 0;
    private static final int XP_FOR_ROUND_COMPLETION = 20;
    private static final int XP_TO_LEVEL_UP = 100;

    private SharedPrefManager sharedPrefManager;
    private AnimatorSet frontAnim, backAnim;
    private boolean isFront = true;

    private CardView cardFront, cardBack;
    private TextView tvFront, tvBack;
    private Button btnNextCard;
    private DBHelper db;
    private int unitId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flashcard);

        sharedPrefManager = new SharedPrefManager(this);
        db = new DBHelper(this);
        unitId = getIntent().getIntExtra("UNIT_ID", 1); // Get the Unit ID

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Flashcards - Unit " + unitId);
        }

        initializeViews();
        loadAnimations();
        prepareWordList(); // This will now load from DB

        if (wordList == null || wordList.isEmpty()) {
            Toast.makeText(this, "Không có từ vựng cho unit này!", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        displayCurrentWord();
        setupListeners();
    }

    private void initializeViews() {
        cardFront = findViewById(R.id.cardFront);
        cardBack = findViewById(R.id.cardBack);
        tvFront = findViewById(R.id.tvFront);
        tvBack = findViewById(R.id.tvBack);
        btnNextCard = findViewById(R.id.btnNextCard);
    }

    private void loadAnimations() {
        float scale = getApplicationContext().getResources().getDisplayMetrics().density;
        cardFront.setCameraDistance(8000 * scale);
        cardBack.setCameraDistance(8000 * scale);
        frontAnim = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.card_flip_out);
        backAnim = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.card_flip_in);
    }

    // CORRECTED: This method now loads words from the database based on unitId
    private void prepareWordList() {
        wordList = new ArrayList<>();
        List<String> vocabFromDb = db.getVocabularyList(unitId);

        for (String item : vocabFromDb) {
            String[] parts = item.split("–");
            if (parts.length >= 2) {
                String front = parts[0].trim();
                String back = parts[1].split("\\n")[0].trim(); // Get only the first line of the meaning
                wordList.add(new Word(front, back));
            }
        }
        Collections.shuffle(wordList);
    }

    private void displayCurrentWord() {
        if (wordList != null && !wordList.isEmpty()) {
            tvFront.setText(wordList.get(currentWordIndex).getFront());
            tvBack.setText(wordList.get(currentWordIndex).getBack());
        }
    }

    private void setupListeners() {
        View.OnClickListener flipClickListener = v -> flipCard();
        cardFront.setOnClickListener(flipClickListener);
        cardBack.setOnClickListener(flipClickListener);
        btnNextCard.setOnClickListener(v -> showNextWord());
    }

    private void flipCard() {
        if (isFront) {
            frontAnim.setTarget(cardFront);
            backAnim.setTarget(cardBack);
            frontAnim.start();
            backAnim.start();
            isFront = false;
        } else {
            frontAnim.setTarget(cardBack);
            backAnim.setTarget(cardFront);
            backAnim.start();
            frontAnim.start();
            isFront = true;
        }
    }

    private void showNextWord() {
        currentWordIndex++;
        if (currentWordIndex >= wordList.size()) {
            awardXp();
            currentWordIndex = 0; // Loop back
            Collections.shuffle(wordList);
            Toast.makeText(this, "Hoàn thành vòng! Quay lại từ đầu.", Toast.LENGTH_SHORT).show();
        }

        if (!isFront) {
            flipCard();
        }

        cardFront.postDelayed(this::displayCurrentWord, 300);
    }

    private void awardXp() {
        Toast.makeText(this, "+" + XP_FOR_ROUND_COMPLETION + " XP", Toast.LENGTH_SHORT).show();

        int currentXp = sharedPrefManager.getXp();
        int currentLevel = sharedPrefManager.getLevel();

        currentXp += XP_FOR_ROUND_COMPLETION;
        if (currentXp >= XP_TO_LEVEL_UP) {
            currentLevel++;
            currentXp -= XP_TO_LEVEL_UP;
            Toast.makeText(this, "Chúc mừng, bạn đã lên cấp!", Toast.LENGTH_LONG).show();
        }
        sharedPrefManager.saveUserStats(currentXp, currentLevel);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
