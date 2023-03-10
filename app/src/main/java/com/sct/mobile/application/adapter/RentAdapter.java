package com.sct.mobile.application.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sct.mobile.application.R;
import com.sct.mobile.application.model.view.RentView;

import java.util.List;

import lombok.Getter;

public class RentAdapter extends RecyclerView.Adapter<RentAdapter.RentViewHolder> {

    private final LayoutInflater inflater;

    private final List<RentView> rents;

    private Toast toast;

    public RentAdapter(Context context, List<RentView> rents){
        this.rents = rents;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public RentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_rent_item, parent, false);
        toast = new Toast(parent.getContext());
        view.setOnClickListener(this::onRentClick);
        return new RentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RentViewHolder holder, int position) {
        RentView rent = rents.get(position);
        holder.date.setText(rent.getDate());
        holder.amount.setText(rent.getAmount());
        holder.transport.setText(rent.getTransport());
        holder.distance.setText(rent.getDistance());
        holder.time.setText(rent.getTime());
    }

    public void onRentClick(View view){
        toast.setText(((TextView)view.findViewById(R.id.rent_transport)).getText());
        toast.show();
    }

    @Override
    public int getItemCount() {
        return rents.size();
    }

    @Getter
    public static class RentViewHolder extends RecyclerView.ViewHolder {

        private final TextView date;

        private final TextView amount;

        private final TextView transport;

        private final TextView distance;

        private final TextView time;

        public RentViewHolder(@NonNull View view) {
            super(view);
            date = view.findViewById(R.id.rent_date);
            amount = view.findViewById(R.id.rent_amount);
            transport = view.findViewById(R.id.rent_transport);
            distance = view.findViewById(R.id.rent_distance);
            time = view.findViewById(R.id.rent_time);
        }
    }
}
