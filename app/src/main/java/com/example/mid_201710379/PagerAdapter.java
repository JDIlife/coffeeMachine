package com.example.mid_201710379;

import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class PagerAdapter extends FragmentStateAdapter {

    public PagerAdapter(FragmentActivity fa){
        super(fa);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if(position == 0){
            return new FragmentOne();
        } else {
            return new FragmentTwo();
        }
    }

    // 총 Fragment 개수 반환
    @Override
    public int getItemCount() {
        return 2;
    }
}
