package com.Motawer.kalemah.utils;

import android.content.Context;

import androidx.recyclerview.widget.LinearLayoutManager;

public class CustomGridLayoutManager extends LinearLayoutManager {
    private boolean isScrollEnabled = true;

    public CustomGridLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }


    public void setScrollEnabled(boolean flag) {
        this.isScrollEnabled = flag;
    }
    @Override
    public boolean canScrollHorizontally() {
        return super.canScrollHorizontally();
    }

}
