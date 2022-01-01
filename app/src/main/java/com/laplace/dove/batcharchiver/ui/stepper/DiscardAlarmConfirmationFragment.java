package com.laplace.dove.batcharchiver.ui.stepper;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.laplace.dove.batcharchiver.R;

public class DiscardAlarmConfirmationFragment extends DialogFragment  {
    private final DialogInterface.OnClickListener onDialogButtonClicked;

    public DiscardAlarmConfirmationFragment(DialogInterface.OnClickListener onDialogButtonClicked) {
        this.onDialogButtonClicked = onDialogButtonClicked;
    }

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Activity activity = getActivity();
        if (activity == null) {
            throw new IllegalStateException("Fragment " + this + " not attached to an activity.");
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(R.string.form_discard_question)
                .setMessage(R.string.form_info_will_be_lost)
                .setPositiveButton(R.string.form_discard, onDialogButtonClicked)
                .setNegativeButton(R.string.form_discard_cancel, onDialogButtonClicked)
                .setCancelable(false);
        Dialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);

        return dialog;
    }
}
