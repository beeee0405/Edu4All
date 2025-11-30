package com.example.myapplication.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.myapplication.Database.DBHelper;
import com.example.myapplication.Model.Reading;
import com.example.myapplication.R;

import java.util.List;

public class UnitDetailActivity extends AppCompatActivity {

    private static final String TAG = "UnitDetailActivity";

    TextView tvReading, tvVocabulary, tvListen, tvGrammar;
    Button btnPracticeReading, btnPracticeVocabulary, btnPlayAudio, btnPracticeGrammar;
    ImageView imgBanner;

    DBHelper db;
    int unitId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unit_detail);

        unitId = getIntent().getIntExtra("UNIT_ID", 1);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        String unitName = getUnitTitle(unitId);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Unit " + unitId + " – " + unitName);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        initializeViews();
        setBannerImage(unitId);

        try {
            db = new DBHelper(this);
        } catch (Exception e) {
            Log.e(TAG, "DB error: " + e.getMessage());
            Toast.makeText(this, "Lỗi cơ sở dữ liệu!", Toast.LENGTH_LONG).show();
            return;
        }

        loadContent();
        setupListeners();
    }

    private void initializeViews() {
        tvReading = findViewById(R.id.tvReading);
        tvVocabulary = findViewById(R.id.tvVocabulary);
        tvListen = findViewById(R.id.tvListen);
        tvGrammar = findViewById(R.id.tvGrammar);

        btnPracticeReading = findViewById(R.id.btnPracticeReading);
        btnPracticeVocabulary = findViewById(R.id.btnPracticeVocabulary);
        btnPlayAudio = findViewById(R.id.btnPlayAudio);
        btnPracticeGrammar = findViewById(R.id.btnPracticeGrammar);

        imgBanner = findViewById(R.id.imgUnitBanner);
    }

    private void setupListeners() {
        btnPracticeReading.setOnClickListener(v -> {
            Intent intent = new Intent(UnitDetailActivity.this, ReadingQuizActivity.class);
            intent.putExtra("unit_id", unitId);
            startActivity(intent);
        });

        btnPracticeVocabulary.setOnClickListener(v -> {
            Intent intent = new Intent(UnitDetailActivity.this, FlashcardActivity.class);
            intent.putExtra("unit_id", unitId);
            startActivity(intent);
        });

        btnPlayAudio.setOnClickListener(v -> {
             Intent intent = new Intent(UnitDetailActivity.this, ListeningActivity.class);
             intent.putExtra("unit_id", unitId);
             startActivity(intent);
        });
        
        btnPracticeGrammar.setOnClickListener(v -> {
            Toast.makeText(this, "Tính năng đang được phát triển!", Toast.LENGTH_SHORT).show();
        });
    }

    private void loadContent() {
        loadReading();
        loadVocabulary();
        loadListening();
    }

    private void setBannerImage(int unitId) {
        String bannerName = "unit_" + unitId + "_banner";
        int resId = getResources().getIdentifier(bannerName, "drawable", getPackageName());
        if (resId != 0) {
            imgBanner.setImageResource(resId);
        } else {
            // You should have a default banner image
            // imgBanner.setImageResource(R.drawable.default_banner);
        }
    }

    // CORRECTED: Re-added the full implementation for loading content
    private void loadReading() {
        try {
            Reading reading = db.getReading(unitId);
            if (reading != null) tvReading.setText(reading.content);
            else tvReading.setText("Không có bài đọc cho unit này.");
        } catch (Exception e) {
            tvReading.setText("Lỗi tải bài đọc!");
            Log.e(TAG, "Reading error", e);
        }
    }

    private void loadVocabulary() {
        try {
            List<String> list = db.getVocabularyList(unitId);
            if (list.isEmpty()) {
                tvVocabulary.setText("Không có từ vựng cho unit này.");
                return;
            }
            StringBuilder sb = new StringBuilder();
            for (String s : list) sb.append("• ").append(s).append("\n\n");
            tvVocabulary.setText(sb.toString());
        } catch (Exception e) {
            tvVocabulary.setText("Lỗi tải từ vựng!");
            Log.e(TAG, "Vocabulary error", e);
        }
    }

    private void loadListening() {
        try {
            String script = db.getListeningTranscript(unitId);
            if (script == null || script.trim().isEmpty()) tvListen.setText("Không có script cho unit này.");
            else tvListen.setText(script);
        } catch (Exception e) {
            tvListen.setText("Lỗi tải script!");
            Log.e(TAG, "Listening error", e);
        }
    }

    private String getUnitTitle(int unitId) {
        switch (unitId) {
            case 1: return "Life Stories";
            case 2: return "Urbanisation";
            case 3: return "The Green Movement";
            case 4: return "Education System";
            case 5: return "Cultural Identity";
            default: return "Unit " + unitId;
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
