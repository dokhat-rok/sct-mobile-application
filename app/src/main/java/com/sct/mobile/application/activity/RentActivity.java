package com.sct.mobile.application.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.sct.mobile.application.R;
import com.sct.mobile.application.adapter.RentAdapter;
import com.sct.mobile.application.component.observed.impl.RentObservedImpl;
import com.sct.mobile.application.component.subscriber.RentSubscriber;
import com.sct.mobile.application.mapper.RentMapper;
import com.sct.mobile.application.model.dto.RentDto;
import com.sct.mobile.application.model.enums.RentStatus;
import com.sct.mobile.application.model.view.RentView;

import java.util.List;
import java.util.stream.Collectors;

public class RentActivity extends AppCompatActivity implements RentSubscriber {

    private final RentObservedImpl rentObserved = new RentObservedImpl();

    private Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_rent);
        this.findViewById(R.id.rent_remove_button).setOnClickListener(this::onRemoveClick);
        toast = new Toast(this);

        rentObserved.subscribe(this);
        rentObserved.getAllRent(RentStatus.CLOSE);
    }

    public void onRemoveClick(View view) {
        this.startActivity(new Intent(RentActivity.this, MapActivity.class));
        this.finish();
    }

    @Override
    public void acceptGetAllRent(List<RentDto> rentList) {
        RecyclerView recyclerView = this.findViewById(R.id.rent_list);
        recyclerView.setAdapter(new RentAdapter(this, this.fillData(rentList)));
    }

    @Override
    public void errorGetAllRent(String error) {
        toast.setText(error);
        toast.show();
        new Thread(() -> {
            try {
                Thread.sleep(5000);
                rentObserved.getAllRent(RentStatus.OPEN);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private List<RentView> fillData(List<RentDto> rentList) {
        return rentList.parallelStream()
                .map(RentMapper::dtoToView)
                .collect(Collectors.toList());
    }
}