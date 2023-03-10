package com.sct.mobile.application.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.sct.mobile.application.R;
import com.sct.mobile.application.adapter.RentAdapter;
import com.sct.mobile.application.model.view.RentView;

import java.util.ArrayList;
import java.util.List;

public class RentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_rent);
        this.findViewById(R.id.rent_remove_button).setOnClickListener(this::onRemoveClick);

        RecyclerView recyclerView = this.findViewById(R.id.rent_list);
        recyclerView.setAdapter(new RentAdapter(this, this.fillData()));
    }

    public void onRemoveClick(View view) {
        this.startActivity(new Intent(RentActivity.this, MapActivity.class));
    }

    private List<RentView> fillData() {
        List<RentView> rents = new ArrayList<>();
        for (int i = 1; i < 11; i++) {
            rents.add(RentView.builder()
                    .date("10 Марта 2023, 10:24")
                    .transport("Самокат ЭСМ-" + i)
                    .amount("270 ₽")
                    .distance("3.5 км")
                    .time("00:38")
                    .build());
        }
        return rents;
    }
}