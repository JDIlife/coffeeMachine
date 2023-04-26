package com.example.mid_201710379;

import android.annotation.SuppressLint;
import android.util.Log;
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
    private Review review;

    // 리뷰를 삭제하는 스레드
    class DeleteReviewThread implements Runnable{
        @Override
        public void run(){
            ReviewActivity.mReviewDao.deleteReview(review);
        }
    }

    // 뷰홀더
    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView rating;
        private TextView reviewText;
        public ViewHolder(@NonNull View itemView){
            super(itemView);
            rating = itemView.findViewById(R.id.item_rating);
            reviewText = itemView.findViewById(R.id.item_review_text);

            // RecyclerView 의 item 을 길게 터치했을 때 이벤트 설정
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    int pos = getAdapterPosition();
                    review = localDataSet.get(pos);

                    if(pos != RecyclerView.NO_POSITION){
                        PopupMenu popupMenu = new PopupMenu(v.getContext(), v);
                        popupMenu.getMenuInflater().inflate(R.menu.review_menu, popupMenu.getMenu());

                        // 팝업 메뉴의 메뉴 아이템을 클릭했을 때의 이벤트를 설정한다
                        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            if(item.getItemId() == R.id.delete_review){ // 리뷰 삭제 버튼을 누르면 해당 리뷰를 삭제한다
                                // 리뷰를 삭제하는 스레드 실행
                                DeleteReviewThread deleteReviewThread = new DeleteReviewThread();
                                Thread t = new Thread(deleteReviewThread);
                                t.start();

                                localDataSet.remove(pos);
                                notifyItemRemoved(pos);
                                }

                            return false;
                            }
                        });
                        popupMenu.show();
                    }
                    return false;
                }
            });
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
        review = localDataSet.get(position);

        holder.rating.setText(String.valueOf(review.getRating()));
        holder.reviewText.setText(review.getReview());
    }

    @Override
    public int getItemCount() {
        return localDataSet.size();
    }
}
