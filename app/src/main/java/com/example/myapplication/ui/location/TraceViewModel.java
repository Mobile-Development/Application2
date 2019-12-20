package com.example.myapplication.ui.location;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class TraceViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public TraceViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is trace fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}