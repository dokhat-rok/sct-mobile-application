package com.sct.mobile.application.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.sct.mobile.application.R;

public class RegistrationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        this.findViewById(R.id.reg_reg_button).setOnClickListener(this::onClick);
    }

    public void onClick(View view){
        this.startActivity(new Intent(RegistrationActivity.this, AuthActivity.class));
    }
}