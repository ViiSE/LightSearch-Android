package ru.viise.lightsearch.fragment;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Spinner;

public class SpinnerWithCallback extends Spinner {

    private AfterSetAdapterCallback afterSetAdapterCallback;

    public SpinnerWithCallback(Context context) {
        super(context);
    }

    public SpinnerWithCallback(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SpinnerWithCallback(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void afterSetAdapterCallback(AfterSetAdapterCallback afterSetAdapterCallback) {
        this.afterSetAdapterCallback = afterSetAdapterCallback;
    }

    public void call(String[] data) {
        afterSetAdapterCallback.newData(data);
    }
}
