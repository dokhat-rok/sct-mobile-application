package com.sct.mobile.application.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.sct.mobile.application.R;

import java.util.Objects;

public class EndTripDialog extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_end_dialog, null);
        builder.setView(view);
        view.findViewById(R.id.end_dialog_close_button).setOnClickListener(this::onClick);
        return builder.create();
    }

    private void onClick(View view) {
        Objects.requireNonNull(this.getDialog()).cancel();
    }
}
