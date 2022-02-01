package com.laplace.dove.batcharchiver.ui.stepper.step;

import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.laplace.dove.batcharchiver.R;

import java.util.HashMap;
import java.util.Map;

import ernestoyaquello.com.verticalstepperform.Step;

public class ArchiveCompressLevelStep extends Step<String> {
    private RadioGroup compressLevelRadioGroup;

    private String compressLevel;
    private HashMap<String, RadioButton> levelRadioMap = new HashMap<>();

    public ArchiveCompressLevelStep(String title) { super(title); }

    @Override
    public String getStepData() {
        return compressLevel;
    }

    @Override
    public String getStepDataAsHumanReadableString() {
        return null;
    }

    @Override
    public void restoreStepData(String data) {
        if (data != null && !data.isEmpty()){
            compressLevelRadioGroup.check(levelRadioMap.get(data).getId());
        }
    }

    @Override
    protected IsDataValid isStepDataValid(String stepData) {
        return null;
    }


    @Override
    protected View createStepContentLayout() {
        compressLevelRadioGroup = new RadioGroup(getContext());
        compressLevelRadioGroup.setOrientation(RadioGroup.HORIZONTAL);
        RadioButton fastestButton = new RadioButton(getContext());
        fastestButton.setText(R.string.radio_compress_fastest);
        RadioButton normalButton = new RadioButton(getContext());
        normalButton.setText(R.string.radio_compress_normal);
        RadioButton storageButton = new RadioButton(getContext());
        storageButton.setText(R.string.radio_compress_storage);
        compressLevelRadioGroup.addView(fastestButton);
        compressLevelRadioGroup.addView(normalButton);
        compressLevelRadioGroup.addView(storageButton);

        levelRadioMap.put("FASTEST", fastestButton);
        levelRadioMap.put("NORMAL", normalButton);
        levelRadioMap.put("ULTRA", storageButton);
        compressLevelRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                for (Map.Entry<String, RadioButton> entry : levelRadioMap.entrySet()) {
                    String level =entry.getKey();
                    RadioButton button = entry.getValue();

                    if (i == button.getId()){
                        compressLevel = level;
                    }
                }
            }
        });
        compressLevelRadioGroup.check(fastestButton.getId());

        return compressLevelRadioGroup;
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
