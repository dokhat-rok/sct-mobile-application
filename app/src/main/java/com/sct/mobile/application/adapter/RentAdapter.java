package com.sct.mobile.application.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sct.mobile.application.R;
import com.sct.mobile.application.model.view.RentView;

import java.util.List;

public class RentAdapter extends RecyclerView.Adapter<RentAdapter.RentViewHolder> {

    private final LayoutInflater inflater;

    private final List<RentView> rents;

    public RentAdapter(Context context, List<RentView> rents){
        this.rents = rents;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public RentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_rent_item, parent, false);
        return new RentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RentViewHolder holder, int position) {
        RentView rent = rents.get(position);
        holder
    }

    public static class RentViewHolder extends RecyclerView.ViewHolder {

        private final EditText date;

        private final EditText amount;

        private final EditText transport;

        private final EditText distance;

        private final EditText time;

        public RentViewHolder(@NonNull View view) {
            super(view);
            date = view.findViewById(R.id.rent_date);
            amount = view.findViewById(R.id.rent_amount);
            transport.

        }
    }
}
