package com.laplace.dove.batcharchiver.ui.stepper.step;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;

import androidx.lifecycle.ViewModelProvider;

import com.laplace.dove.batcharchiver.MainActivity;
import com.laplace.dove.batcharchiver.MainActivityViewModel;
import com.laplace.dove.batcharchiver.R;

import ernestoyaquello.com.verticalstepperform.Step;

public class ArchiveSqlSetStep extends Step<String> {
    private InstantAutoCompleteTextView textView;

    public ArchiveSqlSetStep(String title) {
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
        if (stepData != null && !stepData.isEmpty()){
            return new IsDataValid(true);
        }
        return new IsDataValid(false);
    }

    @Override
    protected View createStepContentLayout() {
        textView = new InstantAutoCompleteTextView(getContext());
        textView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                markAsCompletedOrUncompleted(true);
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });
        textView.setHint(R.string.dialog_input_sql_hint);
        textView.setThreshold(0);
        MainActivityViewModel mainActivityViewModel = new ViewModelProvider(
                (MainActivity)getContext()).get(MainActivityViewModel.class);
        textView.setAdapter(new ArrayAdapter<>(
                getContext(),
                android.R.layout.simple_expandable_list_item_1,
                mainActivityViewModel.getStoragedSql().getValue()));

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
}
