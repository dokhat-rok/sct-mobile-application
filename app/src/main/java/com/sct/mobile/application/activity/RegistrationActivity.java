package com.sct.mobile.application.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.sct.mobile.application.R;

public class RegistrationActivity extends AppCompatActivity {

    private Toast toastReg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        this.findViewById(R.id.reg_reg_button).setOnClickListener(this::onClick);
        toastReg = new Toast(this.getBaseContext());
        toastReg.setText("Регистрация выполнена успешно");
    }

    public void onClick(View view){
        this.startActivity(new Intent(RegistrationActivity.this, AuthActivity.class));
        toastReg.show();
    }
}