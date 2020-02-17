package com.example.appmainframe.Utils;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import org.androidannotations.annotations.EBean;

import java.util.List;

public class MyFragmentPagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> fList;

        public MyFragmentPagerAdapter(@NonNull FragmentManager fm,List<Fragment> list) {
        super(fm);
        this.fList = list;
    }

    @Override
    public Fragment getItem(int position) {
        return this.fList == null? null : this.fList.get(position);
    }

    @Override
    public int getCount() {
        return this.fList == null? null : this.fList.size();
    }
}
