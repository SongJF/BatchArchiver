package com.laplace.dove.batcharchiver.ui.stepper.step;

import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.widget.LinearLayoutCompat;

import com.laplace.dove.batcharchiver.R;
import com.laplace.dove.batcharchiver.utils.archive.ArchiveParam;

import java.nio.CharBuffer;
import java.util.HashMap;
import java.util.Map;

import ernestoyaquello.com.verticalstepperform.Step;

public class ArchivePasswordStep extends Step<ArchiveParam>{
    private EditText passWordText;
    private RadioGroup encryptionTypeRiadio;
    private HashMap<String, RadioButton> nameRiadioMap = new HashMap<>();
    private ArchiveParam archiveParam = new ArchiveParam();

    public ArchivePasswordStep(String title) { super(title); }

    @Override
    public ArchiveParam getStepData() {
        return archiveParam;
    }

    @Override
    public String getStepDataAsHumanReadableString() {
        return null;
    }

    @Override
    public void restoreStepData(ArchiveParam data) {
        if (data.canEncrypt()){
            passWordText.setText(CharBuffer.wrap(data.getPassword()));
            encryptionTypeRiadio.check(nameRiadioMap.get(data.getEncryptionType()).getId());
        }
    }

    @Override
    protected IsDataValid isStepDataValid(ArchiveParam stepData) {
        return null;
    }

    @Override
    protected View createStepContentLayout() {
        LinearLayoutCompat linearLayout = new LinearLayoutCompat(getContext());
        linearLayout.setOrientation(LinearLayoutCompat.VERTICAL);

        View encryptionView = createEncryptionView();
        encryptionView.setVisibility(View.GONE);

        passWordText = new EditText(getContext());
        passWordText.setHint(R.string.step_input_to_set_pass_word);
        passWordText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        passWordText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                archiveParam.setPassword(charSequence.toString().toCharArray());

                if (charSequence.length() > 0){
                    encryptionView.setVisibility(View.VISIBLE);
                }else {
                    encryptionView.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });

        linearLayout.addView(passWordText);
        linearLayout.addView(encryptionView);
        return linearLayout;
    }

    private View createEncryptionView() {
        LinearLayoutCompat view = new LinearLayoutCompat(getContext());
        view.setOrientation(LinearLayoutCompat.HORIZONTAL);

        TextView label = new TextView(getContext());
        label.setText(R.string.textView_encryption_type);
        label.setLayoutParams(new LinearLayoutCompat.LayoutParams(
                LinearLayoutCompat.LayoutParams.WRAP_CONTENT,
                LinearLayoutCompat.LayoutParams.MATCH_PARENT
        ));
        label.setTextSize(16);
        label.setGravity(Gravity.CENTER);

        encryptionTypeRiadio = new RadioGroup(getContext());
        RadioButton standardBtn = new RadioButton(getContext());
        standardBtn.setText(R.string.radio_standard);
        RadioButton aesButton = new RadioButton(getContext());
        aesButton.setText(R.string.radio_aes_256);
        encryptionTypeRiadio.setOrientation(RadioGroup.HORIZONTAL);
        encryptionTypeRiadio.addView(standardBtn);
        encryptionTypeRiadio.addView(aesButton);

        nameRiadioMap.put("ZIP_STANDARD", standardBtn);
        nameRiadioMap.put("AES", aesButton);
        encryptionTypeRiadio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                for (Map.Entry<String, RadioButton> entry : nameRiadioMap.entrySet()) {
                    String type =entry.getKey();
                    RadioButton button = entry.getValue();

                    if (i == button.getId()){
                        archiveParam.setEncryptionType(type);
                    }
                }
            }
        });

        encryptionTypeRiadio.check(standardBtn.getId());
        view.addView(label);
        view.addView(encryptionTypeRiadio);
        return view;
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
