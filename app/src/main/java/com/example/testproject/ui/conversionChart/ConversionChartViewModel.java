package com.example.testproject.ui.conversionChart;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ConversionChartViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public ConversionChartViewModel() {
        mText = new MutableLiveData<>();
//        mText.setValue("This is dashboard fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}