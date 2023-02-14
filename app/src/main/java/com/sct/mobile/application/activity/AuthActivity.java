package com.sct.mobile.application.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.sct.mobile.application.R;

public class AuthActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_auth);
        this.findViewById(R.id.auth_reg_button).setOnClickListener(this::click);
    }



    public void click(View view){
        this.startActivity(new Intent(AuthActivity.this, RegistrationActivity.class));
    }
}