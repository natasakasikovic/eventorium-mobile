package com.eventorium.presentation.shared.dialogs;

import android.app.AlertDialog;
import android.content.Context;

import com.eventorium.presentation.shared.listeners.OnConfirmButtonListener;

public class ConfirmationDialog {

    private final Context context;
    private String message;
    private String positiveButtonText;
    private String negativeButtonText;
    private OnConfirmButtonListener positiveButtonListener;

    public ConfirmationDialog(Context context) {
        this.context = context;
        this.positiveButtonText = "YES";
        this.negativeButtonText = "NO";
    }

    public ConfirmationDialog setMessage(String message) {
        this.message = message;
        return this;
    }

    public ConfirmationDialog setOnConfirmButtonListener(OnConfirmButtonListener listener) {
        this.positiveButtonListener = listener;
        return this;
    }

    public void show() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message)
                .setCancelable(true)
                .setPositiveButton(positiveButtonText, (dialog, which) -> {
                    if (positiveButtonListener != null)
                        positiveButtonListener.onConfirm();
                })
                .setNegativeButton(negativeButtonText, (dialog, which) -> dialog.dismiss());

        builder.create().show();
    }
}