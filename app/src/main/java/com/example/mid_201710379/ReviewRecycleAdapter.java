package com.example.mid_201710379;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ReviewRecycleAdapter extends RecyclerView.Adapter<ReviewRecycleAdapter.ViewHolder> {

    private List<Review> localDataSet = new ArrayList<>();

    // 뷰홀더
    public static class ViewHolder extends RecyclerView.ViewHolder{
        private TextView rating;
        private TextView reviewText;
        public ViewHolder(@NonNull View itemView){
            super(itemView);
            rating = itemView.findViewById(R.id.item_rating);
            reviewText = itemView.findViewById(R.id.item_review_text);
        }
    }

    // 데이터를 전달받는 생성자
    public ReviewRecycleAdapter(List<Review> dataSet){
        localDataSet = dataSet;
    }

    // ViewHolder 객체를 생성해서 리턴한다
    public ReviewRecycleAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.review_recycler_item, parent, false);
        ReviewRecycleAdapter.ViewHolder viewHolder = new ReviewRecycleAdapter.ViewHolder(view);

        return viewHolder;
    }

    // ViewHolder 에 데이터를 연동한다
    @Override

    public void onBindViewHolder(@NonNull ReviewRecycleAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Review review = localDataSet.get(position);
        holder.rating.setText(String.valueOf(review.getRating()));
        holder.reviewText.setText(review.getReview());

        // 리뷰를 롱터치했을 때 이벤트 설정
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) { // 롱터치하면 팝업 메뉴를 띄운다
                PopupMenu popupMenu = new PopupMenu(v.getContext(), v);
                popupMenu.getMenuInflater().inflate(R.menu.review_menu, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if(item.getItemId() == R.id.delete_review){ // 리뷰 삭제 버튼을 누르면 해당 리뷰를 삭제한다
                            // ReviewActivity 의 review 를 리뷰 삭제가 클릭된 review 로 대입해서 DeleteReviewThread 가 실행될 때 정상적으로 해당 review 가 삭제되도록 한다
                            ReviewActivity.review = localDataSet.get(position);

                            localDataSet.remove(position);
                            notifyItemRemoved(position);
                        }

                        return false;
                    }
                });
                popupMenu.show();

                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return localDataSet.size();
    }
}
