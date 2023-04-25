package com.example.mid_201710379;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class FragmentOne extends Fragment {

    private StockDao mStockDao;
    private StockDatabase mStockDatabase;
    private static List<Stock> stockList;
    private Stock stock;
    private List<String> details = new ArrayList<>();

    // 데이터베이스의 값을 가져오는 스레드
    class DBGetStockThread implements Runnable{
        Stock newStock = new Stock();
        @Override
        public void run(){
            if(mStockDao.getStock().isEmpty()){ // 기존에 재고가 설정되어있지 않으면 기본 값으로 재고를 설정한다
                mStockDao.insertStock(newStock);
            }
            // 기존에 사용하던 재고가 있으면 그것을 사용한다
            stockList = mStockDao.getStock();
            stock = stockList.get(0);
        }
    }

    // 출금해서 profit 을 0으로 업데이트하는 스레드
    class DBWithdrawThread implements Runnable{
        @Override
        public void run() {
            mStockDao.withdrawProfit(0, 0);
        }
    }

    // 출금해서 판매내역을 초기화하는 스레드
    class DBEmptySalesDetailsThread implements Runnable{
        @Override
        public void run(){
            mStockDao.emptySalesDetails(details, 0);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.tab_fragment_one, container, false);

        // UI 요소 지정
        RecyclerView salesDetailListView = view.findViewById(R.id.sales_detail_recyclerview);
        TextView salesProfit = view.findViewById(R.id.sales_revenue_textview);
        Button withdrawBtn = view.findViewById(R.id.withdraw_btn);

        // Room 데이터베이스 초기화
        mStockDatabase = StockDatabase.getInstance(getContext());
        mStockDao = mStockDatabase.stockDao();

        // 데이터베이스에서 재고를 가져오는 스레드를 별도로 실행
        DBGetStockThread dbGetStockThread = new DBGetStockThread();
        Thread t = new Thread(dbGetStockThread);
        t.start();

        // 별도로 실행한 DBGetStockThread 에서 stockList 를 초기화할 때 까지 기다린다
        try {
            t.join();
        } catch (InterruptedException e){
            e.printStackTrace();
        }

        // RecyclerView 에 LinearLayoutManager 설정
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        salesDetailListView.setLayoutManager(linearLayoutManager);

        // SaleDetailsAdapter 에 전달할 데이터를 만들고 RecyclerView 에 어댑터 설정
        List<String> dataSet = stock.getSalesDetails();
        SaleDetailsAdapter saleDetailsAdapter = new SaleDetailsAdapter(dataSet);
        salesDetailListView.setAdapter(saleDetailsAdapter);

        salesProfit.setText(String.valueOf(stock.getProfit()));

        // 출금하기 버튼 이벤트 지정
        withdrawBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "정상적으로 출금되었습니다.", Toast.LENGTH_SHORT).show();

                // profit 을 0 으로 업데이트하는 스레드 실행
                DBWithdrawThread dbWithdrawThread = new DBWithdrawThread();
                Thread t = new Thread(dbWithdrawThread);
                t.start();

                // 판매내역을 초기화하는 스레드 실행
                DBEmptySalesDetailsThread dbEmptySalesDetailsThread = new DBEmptySalesDetailsThread();
                Thread et = new Thread(dbEmptySalesDetailsThread);
                et.start();

                // 매출액 부분을 0으로 바꾼다
                salesProfit.setText("0");

                // 판매 내역 부분인 RecyclerView 를 빈 데이터를 줘서 초기화한다
                List<String> dataSet = new ArrayList<>();
                SaleDetailsAdapter saleDetailsAdapter = new SaleDetailsAdapter(dataSet);
                salesDetailListView.setAdapter(saleDetailsAdapter);

                // MainActivity 의 stock 의 profit 과 salesDetails 를 초기화한다
                MainActivity.stock.setProfit(0);
                MainActivity.stock.setSalesDetails(new ArrayList<>());
            }
        });

        return view;
    }
}
