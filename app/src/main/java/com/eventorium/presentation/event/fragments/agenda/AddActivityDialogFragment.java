package com.eventorium.presentation.event.fragments.agenda;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.eventorium.data.event.models.Activity;
import com.eventorium.data.shared.constants.ErrorMessages;
import com.eventorium.databinding.FragmentAddActivityDialogBinding;
import com.eventorium.presentation.event.listeners.OnActivityCreatedListener;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddActivityDialogFragment extends DialogFragment {
    private FragmentAddActivityDialogBinding binding;
    private OnActivityCreatedListener listener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        binding = FragmentAddActivityDialogBinding.inflate(LayoutInflater.from(getContext()));

        AlertDialog dialog = createDialog();
        dialog.setOnShowListener(dialogInterface -> setupPositiveButton(dialog));

        setupTimePickers();

        return dialog;
    }

    private AlertDialog createDialog() {
        return new AlertDialog.Builder(requireContext())
                .setView(binding.getRoot())
                .setNegativeButton("Cancel", (dialogInterface, which) -> dialogInterface.dismiss())
                .setPositiveButton("Add", null)
                .create();
    }

    private void setupPositiveButton(AlertDialog dialog) {
        Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);

        positiveButton.setOnClickListener(v -> {
            if (areFieldsEmpty()) {
                Toast.makeText(requireContext(), ErrorMessages.VALIDATION_ERROR, Toast.LENGTH_SHORT).show();
            } else {
                notifyListenerAndDismiss(dialog);
            }
        });
    }

    private void notifyListenerAndDismiss(AlertDialog dialog) {
        if (listener != null) {
            listener.onActivityCreated(loadForm());
        }
        dialog.dismiss();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (getParentFragment() instanceof OnActivityCreatedListener) {
            listener = (OnActivityCreatedListener) getParentFragment();
        }
    }

    public Activity loadForm() {
        return Activity.builder()
                .name(binding.nameEditText.getText().toString())
                .description(binding.descriptionEditText.getText().toString())
                .location(binding.locationEditText.getText().toString())
                .startTime(binding.startTime.getText().toString().toUpperCase())
                .endTime(binding.endTime.getText().toString().toUpperCase())
                .build();
    }

    private boolean areFieldsEmpty() {
        return TextUtils.isEmpty(binding.nameEditText.getText()) ||
                TextUtils.isEmpty(binding.descriptionEditText.getText()) ||
                TextUtils.isEmpty(binding.locationEditText.getText()) ||
                TextUtils.isEmpty(binding.startTime.getText()) ||
                TextUtils.isEmpty(binding.endTime.getText());
    }

    private void setupTimePickers() {
        TextInputEditText startTime = binding.startTime;
        TextInputEditText endTime = binding.endTime;

        startTime.setOnClickListener(v -> showTimePicker(startTime));
        endTime.setOnClickListener(v -> showTimePicker(endTime));

    }

    private void showTimePicker(TextInputEditText editText) {
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                requireContext(),
                (view, hourOfDay, minuteOfHour) -> {
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    calendar.set(Calendar.MINUTE, minuteOfHour);

                    String time = new SimpleDateFormat("h:mm a", Locale.getDefault()).format(calendar.getTime());

                    editText.setText(time);
                }, 0, 0, false
        );

        timePickerDialog.show();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}