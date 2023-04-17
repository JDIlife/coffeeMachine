package com.example.mid_201710379;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ReviewRecycleAdapter extends RecyclerView.Adapter<ReviewRecycleAdapter.ViewHolder> {

    private ArrayList<String> localDataSet;

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
    public ReviewRecycleAdapter(ArrayList<String> dataSet){
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
    public void onBindViewHolder(@NonNull ReviewRecycleAdapter.ViewHolder holder, int position) {
        String text = localDataSet.get(position);
        holder.rating.setText(text.substring(0, 3));
        holder.reviewText.setText(text.substring(3));
    }

    @Override
    public int getItemCount() {
        return localDataSet.size();
    }
}
