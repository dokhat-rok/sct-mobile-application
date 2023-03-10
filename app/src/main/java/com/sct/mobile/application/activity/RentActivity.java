package com.sct.mobile.application.activity;

import androidx.annotation.LayoutRes;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.sct.mobile.application.R;

public class RentActivity extends AppCompatActivity {

    private LinearLayout listRentLayout;

    private Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_rent);

        toast = new Toast(this.getApplicationContext());
    }

    public void onRemoveClick(View view){
        this.startActivity(new Intent(RentActivity.this, MapActivity.class));
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        this.findViewById(R.id.rent_remove_button).setOnClickListener(this::onRemoveClick);

//        ScrollView scrollView = this.findViewById(R.id.rent_scroll);
//        scrollView.setOnScrollChangeListener(this::onScrollChange);

        this.findViewById(R.id.rent_scroll).setOnScrollChangeListener(this::onScrollChange);

        listRentLayout = this.findViewById(R.id.rent_list_rent);
        for(int i = 0; i < 10; i++){
            RentView rentView = new RentView(this.getApplicationContext(), listRentLayout, i);
            findViewById(R.id.rent_details_button).setOnClickListener(this::onClickRent);
            listRentLayout.addView(rentView);
            listRentLayout.getChildAt(i).setOnClickListener(this::onClickRent);
        }
    }

    public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
        for(int i = 0; i < listRentLayout.getChildCount(); i++){
            listRentLayout.getChildAt(i).setOnClickListener(this::onClickRent);
        }
    }

    public void onClickRent(View view){
        RentView rentView = (RentView) view.getParent();
        toast.setText("Поездка номер " + rentView.getId());
//        toast.setText("ВАУ");
        toast.show();
    }
}