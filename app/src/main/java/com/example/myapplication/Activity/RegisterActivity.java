package com.example.myapplication.Activity;

import static android.widget.Toast.LENGTH_SHORT;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myapplication.Database.AppDatabase;
import com.example.myapplication.Entity.UserEntity;
import com.example.myapplication.R;

public class RegisterActivity extends AppCompatActivity {

    private EditText editEmail, edtUsername, edtPassword, edtConfirmPassword;
    private Button btnRegister;
    private TextView tvLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);

        init();
        setupListeners();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void setupListeners() {
        btnRegister.setOnClickListener(v -> clickHandleRegister());
        
        tvLogin.setOnClickListener(v -> {
            finish();
        });
    }

    private void clickHandleRegister() {
        String email = editEmail.getText().toString().trim();
        String username = edtUsername.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();
        String confirmPassword = edtConfirmPassword.getText().toString().trim();

        if(email.isEmpty()) {
            editEmail.setError("Vui lòng nhập email!");
            return;
        }
        if(username.isEmpty()) {
            edtUsername.setError("Vui lòng nhập tên đăng nhập!");
            return;
        }
        if(password.isEmpty()) {
            edtPassword.setError("Hãy nhập mật khẩu!");
            return;
        }
        if(confirmPassword.isEmpty()) {
            edtConfirmPassword.setError("Nhập lại mật khẩu!");
            return;
        }
        if(!password.equals(confirmPassword)) {
            edtConfirmPassword.setError("Mật khẩu nhập lại không đúng!");
            return;
        }

        UserEntity user = new UserEntity(username, email, password);
        AppDatabase.getInstance(RegisterActivity.this).userDao().insertUser(user);

        Toast.makeText(this, "Đăng ký thành công", LENGTH_SHORT).show();

        UserEntity entity = AppDatabase.getInstance(RegisterActivity.this).userDao().getUserByUsername(username);
        Intent intent = new Intent(this, HomeActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("user", entity);
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }

    private void init() {
        editEmail = findViewById(R.id.edtEmail);
        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        edtConfirmPassword = findViewById(R.id.edtConfirmPassword);
        btnRegister = findViewById(R.id.btnRegister);
        tvLogin = findViewById(R.id.tvLogin);
    }
}