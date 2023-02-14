package com.sct.mobile.application.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.sct.mobile.application.R;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_profile);
        this.findViewById(R.id.profile_remove_button).setOnClickListener(this::onCLickRemove);
        this.findViewById(R.id.profile_exit_button).setOnClickListener(this::onClickExit);
        this.findViewById(R.id.profile_delete_button).setOnClickListener(this::onClickExit);
    }

    public void onClickExit(View view){
        this.startActivity(new Intent(ProfileActivity.this, AuthActivity.class));
    }

    public void onCLickRemove(View view){
        this.startActivity(new Intent(ProfileActivity.this, MapActivity.class));
    }
}