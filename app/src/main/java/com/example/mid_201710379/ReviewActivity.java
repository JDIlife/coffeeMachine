package com.example.mid_201710379;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ReviewActivity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        // Toolbar 설정
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Toolbar 뒤로가기 버튼 활성화
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // UI 지정
        RecyclerView reviewListView = (RecyclerView) findViewById(R.id.review_recyclerview);
        RatingBar ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        EditText reviewEditText = (EditText) findViewById(R.id.review_edittext);
        Button postBtn = (Button) findViewById(R.id.post_btn);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        reviewListView.setLayoutManager(linearLayoutManager);

        // ReviewRecycleAdapter 로 전해줄 dataSet 생성
        ArrayList<String> dataSet = new ArrayList<>();

        // post 버튼을 누르면 입력한 별점과 리뷰가 등록된다
        postBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String review = ratingBar.getRating() + reviewEditText.getText().toString();

                // dataSet 에 입력받은 값을 추가해 ReviewRecycleAdapter 에 전달해준다
                dataSet.add(review);
                ReviewRecycleAdapter reviewRecycleAdapter = new ReviewRecycleAdapter(dataSet);
                reviewListView.setAdapter(reviewRecycleAdapter);

                // 입력값을 초기화하고, 가상 키보드를 내린다
                ratingBar.setRating(3);
                reviewEditText.setText("");
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(reviewEditText.getWindowToken(), 0);
            }
        });
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
