package ru.viise.lightsearch.fragment;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.widget.Spinner;

public class SpinnerWithCallback extends Spinner {

    private AfterSetAdapterCallback afterSetAdapterCallback;

    public SpinnerWithCallback(Context context) {
        super(context);
    }

    public SpinnerWithCallback(Context context, int mode) {
        super(context, mode);
    }

    public SpinnerWithCallback(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SpinnerWithCallback(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public SpinnerWithCallback(Context context, AttributeSet attrs, int defStyleAttr, int mode) {
        super(context, attrs, defStyleAttr, mode);
    }

    public SpinnerWithCallback(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes, int mode) {
        super(context, attrs, defStyleAttr, defStyleRes, mode);
    }

    public SpinnerWithCallback(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes, int mode, Resources.Theme popupTheme) {
        super(context, attrs, defStyleAttr, defStyleRes, mode, popupTheme);
    }

    public void afterSetAdapterCallback(AfterSetAdapterCallback afterSetAdapterCallback) {
        this.afterSetAdapterCallback = afterSetAdapterCallback;
    }

    public void call(String[] data) {
        afterSetAdapterCallback.newData(data);
    }
}
