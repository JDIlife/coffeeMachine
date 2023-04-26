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
import java.util.List;

public class ReviewActivity extends AppCompatActivity {

    public static ReviewDao mReviewDao;
    private ReviewDatabase mReviewDatabase;
    private List<Review> reviewList = new ArrayList<>();
    public  Review review = new Review();

    // 리뷰 데이터를 가져오는 스레드
    class GetReviewThread implements Runnable{
        @Override
        public void run() {
            reviewList = mReviewDao.getAllReview();
        }
    }

    // 리뷰를 추가하는 스레드
    class InsertReviewThread implements Runnable{
        @Override
        public void run(){
            long reviewId = mReviewDao.insertReview(review);
            review.setId(reviewId);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        // Toolbar 설정
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Toolbar 뒤로가기 버튼 활성화
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Room 데이터베이스 초기화
        mReviewDatabase = ReviewDatabase.getInstance(getApplicationContext());
        mReviewDao = mReviewDatabase.reviewDao();

        // 데이터베이스에서 리뷰를 가져오는 스레드를 별도로 실행
        GetReviewThread getReviewThread = new GetReviewThread();
        Thread t = new Thread(getReviewThread);
        t.start();

        // DB 에서 리뷰 데이터를 가져오는 것이 완료될 때 까지 기다린다
        try{
            t.join();
        } catch (Exception e){}


        // UI 지정
        RecyclerView reviewListView = (RecyclerView) findViewById(R.id.review_recyclerview);
        RatingBar ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        EditText reviewEditText = (EditText) findViewById(R.id.review_edittext);
        Button postBtn = (Button) findViewById(R.id.post_btn);

        // RecyclerView 에 LinearLayoutManager 지정
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        reviewListView.setLayoutManager(linearLayoutManager);
        // RecyclerView 를
        ReviewRecycleAdapter reviewRecycleAdapter = new ReviewRecycleAdapter(reviewList);
        reviewListView.setAdapter(reviewRecycleAdapter);

        // post 버튼을 누르면 입력한 별점과 리뷰가 등록된다
        postBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                review = new Review(); // review 를 삭제한 이후에 다시 review 를 post 할 때, 삭제 했던 review 의 id 를 다시 쓰지 않도록 새롭게 초기화한다
                review.setRating(ratingBar.getRating());
                review.setReview(reviewEditText.getText().toString());

                // dataSet 에 입력받은 값을 추가해 ReviewRecycleAdapter 에 전달해준다
                reviewList.add(review);

                // review 를 추가하는 스레드 실행
                InsertReviewThread insertReviewThread = new InsertReviewThread();
                Thread irt = new Thread(insertReviewThread);
                irt.start();

                ReviewRecycleAdapter reviewRecycleAdapter = new ReviewRecycleAdapter(reviewList);
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
        if(item.getItemId() == android.R.id.home){ // 뒤로가기 버튼을 누르면 메인 엑티비티로 돌아간다
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
