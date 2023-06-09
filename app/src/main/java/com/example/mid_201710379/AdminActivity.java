package com.example.mid_201710379;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

public class AdminActivity extends AppCompatActivity {

    private ViewPager2 viewPager;
    private FragmentStateAdapter pagerAdapter;
    private TabLayout tabLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        // Toolbar 설정
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Toolbar 뒤로가기 버튼 활성화
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // TabLayout 설정
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        // viewPager 설정, PagerAdapter 설정
        viewPager = (ViewPager2) findViewById(R.id.view_pager);
        pagerAdapter = new PagerAdapter(this);
        viewPager.setAdapter(pagerAdapter);

        // 탭을 클릭하면 해당 Fragment 를 보여준다
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        // Tab 의 이름을 정해주고, 좌우로 스와이프 했을 때 해당하는 Tab 으로 이동한다
        new TabLayoutMediator(tabLayout, viewPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                if(position == 0){
                    tab.setText("매출확인");
                } else {
                    tab.setText("재고확인");
                }
            }
        }).attach();

    }

    // 툴바의 메뉴아이템을 눌렀을 때 발생하는 이벤트
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home: { // 뒤로가기 버튼을 누르면 메인 엑티비티로 돌아간다
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
