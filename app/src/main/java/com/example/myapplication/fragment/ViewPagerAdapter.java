package com.example.myapplication.fragment;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class ViewPagerAdapter extends FragmentStatePagerAdapter{
    public ViewPagerAdapter(@NonNull FragmentManager fm, int a) {
        super(fm, a);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new MovieFragment();
            case 1:
                return new TheaterFragment();
            case 2:
                return new UserFragment();
            default:
                return new MovieFragment();
        }
    }

    @Override
    public int getCount() {
        return 3;
    }
}
