package ru.viise.lightsearch.fragment.pager;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.viewpager.widget.ViewPager;

import ru.viise.lightsearch.R;

public class SwipeableViewPager extends ViewPager {

    private boolean isSwipe;

    public SwipeableViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SwipeableViewPager);
        try {
            isSwipe = a.getBoolean(R.styleable.SwipeableViewPager_isSwipe, true);
        } finally {
            a.recycle();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (this.isSwipe) {
            return super.onTouchEvent(event);
        }

        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (this.isSwipe) {
            return super.onInterceptTouchEvent(event);
        }

        return false;
    }
}
