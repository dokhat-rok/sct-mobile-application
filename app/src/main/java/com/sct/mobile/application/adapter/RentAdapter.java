package com.sct.mobile.application.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sct.mobile.application.R;
import com.sct.mobile.application.activity.RentActivity;
import com.sct.mobile.application.model.view.RentView;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

public class RentAdapter extends RecyclerView.Adapter<RentAdapter.RentViewHolder> {

    private final LayoutInflater inflater;

    private final List<RentView> rents;

    @Setter
    private RentActivity rentActivity;

    public RentAdapter(Context context, List<RentView> rents){
        this.rents = rents;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public RentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_rent_item, parent, false);
        view.setOnClickListener(this::onRentClick);
        return new RentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RentViewHolder holder, int position) {
        RentView rent = rents.get(position);
        holder.date.setText(rent.getDate());
        holder.amount.setText(rent.getAmount());
        holder.transport.setText(rent.getTransport());
        holder.id.setText(String.valueOf(rent.getId()));
    }

    public void onRentClick(View view){
        CharSequence transport = ((TextView) view.findViewById(R.id.rent_id)).getText();
        RentView selected = rents.parallelStream()
                .filter(r -> r.getId() == Long.parseLong(transport.toString()))
                .findFirst()
                .orElse(null);

        assert selected != null;
        rentActivity.showRoute(selected);
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

        private final TextView id;

        public RentViewHolder(@NonNull View view) {
            super(view);
            date = view.findViewById(R.id.rent_date);
            amount = view.findViewById(R.id.rent_amount);
            transport = view.findViewById(R.id.rent_transport);
            id = view.findViewById(R.id.rent_id);
        }
    }
}
