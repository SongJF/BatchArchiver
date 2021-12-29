package com.laplace.dove.batcharchiver.ui.reflow;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ReflowViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public ReflowViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is reflow fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}