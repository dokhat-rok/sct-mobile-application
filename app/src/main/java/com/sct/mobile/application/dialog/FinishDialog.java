package com.sct.mobile.application.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.sct.mobile.application.R;
import com.sct.mobile.application.mapper.RentMapper;
import com.sct.mobile.application.model.dto.RentDto;
import com.sct.mobile.application.model.view.RentView;

import java.util.Objects;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FinishDialog extends DialogFragment {

    private final RentDto rent;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_finish_dialog, null);
        builder.setView(view);
        view.findViewById(R.id.finish_dialog_close_button).setOnClickListener(this::onClick);
        this.fillData(view);
        return builder.create();
    }

    private void onClick(View view) {
        Objects.requireNonNull(this.getDialog()).cancel();
    }

    private void fillData(View view) {
        RentView data = RentMapper.dtoToView(this.rent);

        TextView date = view.findViewById(R.id.finish_date);
        TextView transport = view.findViewById(R.id.finish_transport);
        TextView amount = view.findViewById(R.id.finish_amount);
        TextView distance = view.findViewById(R.id.finish_distance);
        TextView time = view.findViewById(R.id.finish_time);

        date.setText(data.getDate());
        transport.setText(data.getTransport());
        amount.setText(data.getAmount());
        distance.setText(data.getDistance());
        time.setText(data.getTime());
    }
}
