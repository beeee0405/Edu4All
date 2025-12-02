package com.example.myapplication.Activity;

import static android.widget.Toast.LENGTH_SHORT;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.Database.AppDatabase;
import com.example.myapplication.Entity.UserEntity;
import com.example.myapplication.Manager.SharedPrefManager;
import com.example.myapplication.R;

public class MainActivity extends AppCompatActivity {

    private EditText edtUserName, edtPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        setupListeners();
    }

    private void initViews() {
        edtUserName = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
    }

    private void setupListeners() {
        findViewById(R.id.btnLogin).setOnClickListener(v -> handleLogin());
        findViewById(R.id.tvSignup).setOnClickListener(v -> handleRegister());
        findViewById(R.id.tvForgot).setOnClickListener(v -> handleForgotPassword());
        findViewById(R.id.btnGoogleLogin).setOnClickListener(v -> handleSocialLogin("Google"));
        findViewById(R.id.btnFacebookLogin).setOnClickListener(v -> handleSocialLogin("Facebook"));
    }

    private void handleLogin() {
        String username = edtUserName.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();

        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", LENGTH_SHORT).show();
            return;
        }

        try {
            UserEntity user = AppDatabase.getInstance(this).userDao().getUserByUsername(username);

            if (user != null && user.getPassword().equals(password)) {
                // Save user stats to SharedPreferences
                SharedPrefManager sharedPrefManager = new SharedPrefManager(this);
                sharedPrefManager.saveUsername(user.getUsername());

                // Show success message before navigating
                Toast.makeText(this, "Đăng nhập thành công", LENGTH_SHORT).show();
                
                // Navigate to HomeActivity
                Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("user", user);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Sai tên đăng nhập hoặc mật khẩu", LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Đã xảy ra lỗi khi đăng nhập", LENGTH_SHORT).show();
        }
    }

    private void handleRegister() {
        startActivity(new Intent(this, RegisterActivity.class));
    }

    private void handleForgotPassword() {
        startActivity(new Intent(this, ResetPasswordActivity.class));
    }

    private void handleSocialLogin(String provider) {
        Toast.makeText(this, "Tính năng đăng nhập bằng " + provider + " đang được phát triển!", LENGTH_SHORT).show();
    }
}
