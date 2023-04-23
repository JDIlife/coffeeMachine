package com.example.mid_201710379;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SaleDetailsAdapter extends RecyclerView.Adapter<SaleDetailsAdapter.ViewHolder> {

    private List<String> localDataSet;

    // 뷰홀더
    public class ViewHolder  extends RecyclerView.ViewHolder{
        private TextView salesMenu, salesSize, salesAmount, salesTotalPrice;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            salesMenu = itemView.findViewById(R.id.sales_menu_textview);
            salesSize = itemView.findViewById(R.id.sales_size_textview);
            salesAmount = itemView.findViewById(R.id.sales_amount_textview);
            salesTotalPrice = itemView.findViewById(R.id.sales_total_price_textview);
        }
    }

    // 데이터를 전달받는 생성자
    public SaleDetailsAdapter(List<String> dataSet){
        localDataSet = dataSet;
    }

    // 뷰홀더 객체 생성 후 리턴
    @NonNull
    @Override
    public SaleDetailsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.sales_recycler_item, parent, false);
        SaleDetailsAdapter.ViewHolder viewHolder = new SaleDetailsAdapter.ViewHolder(view);

        return viewHolder;
    }

    // 뷰홀더에 데이터 연동
    @Override
    public void onBindViewHolder(@NonNull SaleDetailsAdapter.ViewHolder holder, int position) {
        String text = localDataSet.get(position);

        // 정규식 : 메뉴(가격)사이즈(+가격)양_총가격
        String regex = "(\\w+)\\((\\w+)\\)(\\w+)\\((\\+\\w+)\\)(\\w+)(_\\w+)";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);

        // 정규식으로 나눈 그룹으로 가져온 salesDetails 에서 필요한 부분을 골라 해당하는 부분에 값으로 넣어준다
        if (matcher.matches()){
            String menu = matcher.group(1);
            String size = matcher.group(3);
            String amount = matcher.group(5);
            String totalPrice = matcher.group(6).substring(1); // 언더바(_) 이후의 문자들로 추린다

            holder.salesMenu.setText(menu);
            holder.salesSize.setText(size);
            holder.salesAmount.setText(amount);
            holder.salesTotalPrice.setText(totalPrice);
        }

    }

    @Override
    public int getItemCount() {
        return localDataSet.size();
    }

}
