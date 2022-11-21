package com.dream.dreamtheather.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.dream.dreamtheather.R;

public class ForgottenPassword extends AppCompatActivity {

    Button btnBackToLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotten_password);

        btnBackToLogin = findViewById(R.id.btnBackToLogin);
        btnBackToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ForgottenPassword.this, LoginActivity.class));
                finish();

            }
        });

    }
}