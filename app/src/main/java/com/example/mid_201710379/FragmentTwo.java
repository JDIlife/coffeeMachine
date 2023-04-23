package com.example.mid_201710379;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.util.List;

public class FragmentTwo extends Fragment {

    private StockDao mStockDao;
    private StockDatabase mStockDatabase;
    private static List<Stock> stockList;
    private Stock stock;

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

    // 재고를 채우는 스레드
    class DBFillStockThread implements Runnable{

        @Override
        public void run() {
            mStockDao.fillStock(1000, 1000, 30, 30, 0);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.tab_fragment_two, container, false);

        // UI 요소 지정
        TextView stockCoffeeBeans = (TextView) view.findViewById(R.id.coffee_beans_textview);
        TextView stockSteamMilk = (TextView) view.findViewById(R.id.steam_milk_textview);
        TextView stockStraw = (TextView) view.findViewById(R.id.straw_textview);
        TextView stockCup = (TextView) view.findViewById(R.id.cup_textview);
        Button fillStockBtn = (Button) view.findViewById(R.id.fill_stock_btn);

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

        // DB 에서 가져온 값에 맞게 해당 TextView 값 변경
        Double coffeeBeans = stock.getCoffeeBeans() / 1000.0;
        Double steamMilk = stock.getSteamMilk() / 1000.0;

        stockCoffeeBeans.setText(coffeeBeans + "/1kg");
        stockSteamMilk.setText(steamMilk + "/1L");
        stockStraw.setText(stock.getStraw() + "/30");
        stockCup.setText(stock.getCup() + "/30");

        // 재고 채우기 버튼 이벤트 설정
        fillStockBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 재고를 채워주는 스레드를 실행한다
                DBFillStockThread dbFillStockThread = new DBFillStockThread();
                Thread t = new Thread(dbFillStockThread);
                t.start();

                // 채워진 재고에 맞게 텍스트를 변견한다
                stockCoffeeBeans.setText("1kg/1kg");
                stockSteamMilk.setText("1L/1L");
                stockStraw.setText("30/30");
                stockCup.setText("30/30");
            }
        });


        return view;
    }
}
