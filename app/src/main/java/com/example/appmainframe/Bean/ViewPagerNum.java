package com.example.appmainframe.Bean;

import androidx.viewpager.widget.ViewPager;

public class ViewPagerNum {
    private ViewPager viewPager;

    public ViewPagerNum(ViewPager viewPager) {
        this.viewPager = viewPager;
    }

    public void setViewPager(ViewPager viewPager) {
        this.viewPager = viewPager;
    }

    public ViewPager getViewPager() {
        return viewPager;
    }
}
