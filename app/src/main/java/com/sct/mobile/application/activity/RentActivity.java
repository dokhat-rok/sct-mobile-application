package com.sct.mobile.application.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.sct.mobile.application.R;

public class RentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rent);
        this.findViewById(R.id.rent_remove_button).setOnClickListener(this::onRemoveClick);
    }

    public void onRemoveClick(View view){
        this.startActivity(new Intent(RentActivity.this, MapActivity.class));
    }
}