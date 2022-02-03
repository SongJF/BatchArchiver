package com.laplace.dove.batcharchiver.ui.stepper.step;

import android.app.Activity;
import android.os.Environment;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.github.gzuliyujiang.filepicker.FilePicker;
import com.github.gzuliyujiang.filepicker.annotation.ExplorerMode;
import com.github.gzuliyujiang.filepicker.contract.OnFilePickedListener;
import com.laplace.dove.batcharchiver.R;

import java.io.File;

import ernestoyaquello.com.verticalstepperform.Step;

public class ArchiveFolderSelectStep extends Step<String> implements OnFilePickedListener {
    private TextView textView;

    public ArchiveFolderSelectStep(String title) {
        super(title);
    }

    @Override
    public String getStepData() {
        CharSequence text = textView.getText();
        if (text != null) {
            return text.toString();
        }

        return "";
    }

    @Override
    public String getStepDataAsHumanReadableString() {
        return null;
    }

    @Override
    public void restoreStepData(String data) {
        textView.setText(data);
        markAsCompletedOrUncompleted(false);
    }

    @Override
    protected IsDataValid isStepDataValid(String stepData) {
        if (stepData != null && new File(stepData).exists()){
            return new IsDataValid(true);
        }
        return new IsDataValid(false);
    }

    @Override
    protected View createStepContentLayout() {
        textView = new TextView(getContext());
        textView.setHint(R.string.step_click_to_select_folder);
        textView.setClickable(true);
        ArchiveFolderSelectStep step = this;
        textView.setOnClickListener(view -> {
            Activity activity = (Activity) view.getContext();
            FilePicker picker = new FilePicker(activity);
            picker.setInitDir(ExplorerMode.DIRECTORY, Environment.getExternalStorageDirectory());
            picker.setOnFilePickedListener(step);
            picker.show();
        });
        return textView;
    }

    @Override
    protected void onStepOpened(boolean animated) {}

    @Override
    protected void onStepClosed(boolean animated) {}

    @Override
    protected void onStepMarkedAsCompleted(boolean animated) {}

    @Override
    protected void onStepMarkedAsUncompleted(boolean animated) {}

    @Override
    public void onFilePicked(@NonNull File file) {
        if (file.isDirectory()){
            textView.setText(file.getAbsolutePath());
            markAsCompletedOrUncompleted(true);
        }
    }
}
