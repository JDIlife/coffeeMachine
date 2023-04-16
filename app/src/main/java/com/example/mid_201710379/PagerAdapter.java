package com.example.mid_201710379;

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
            return new Fragment(R.layout.tab_fragment_one);
        } else {
            return new Fragment(R.layout.tab_fragment_two);
        }
    }

    // 총 Fragment 개수 반환
    @Override
    public int getItemCount() {
        return 2;
    }
}
