package com.example.mid_201710379;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    int price = 0;
    int amount = 1;

    private static List<Stock> stockList;
    private Stock stock;
    private StockDao mStockDao;
    private StockDatabase mStockDatabase;

    // 데이터베이스의 값을 가져오는 스레드
    class DBGetStockThread implements Runnable{
        Stock stock = new Stock();
        @Override
        public void run(){
            if(mStockDao.getStock().isEmpty()){ // 기존에 재고가 설정되어있지 않으면 기본 값으로 재고를 설정한다
                mStockDao.insertStock(stock);
            }
            // 기존에 사용하던 재고가 있으면 그것을 사용한다
            stockList = mStockDao.getStock();
        }
    }

    // 사용한 재고와 판매 내역을 업데이트하는 스레드
    class DBStockUseThread implements Runnable{
        @Override
        public void run(){
            mStockDao.setStockUse(stock);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Room 데이터베이스 초기화
        mStockDatabase = StockDatabase.getInstance(this);
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

        if(stockList != null && !stockList.isEmpty()){
            stock = stockList.get(0);
        }


        // Toolbar 설정
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // UI 요소 지정
        Button initBtn = (Button) findViewById(R.id.init_btn);

        RadioGroup menuRadioGroup = (RadioGroup) findViewById(R.id.coffee_menu_radioGroup);
        RadioButton americanoBtn = (RadioButton) findViewById(R.id.americano_radio_btn);
        RadioButton coffeeLatteBtn = (RadioButton) findViewById(R.id.coffee_latte_radio_btn);
        RadioButton cappuccinoBtn = (RadioButton) findViewById(R.id.cappuccino_radio_btn);

        RadioGroup sizeRadioGroup = (RadioGroup) findViewById(R.id.coffee_size_radioGroup2);
        RadioButton smallSizeBtn = (RadioButton) findViewById(R.id.small_size_radio_btn);
        RadioButton mediumSizeBtn = (RadioButton) findViewById(R.id.medium_size_radio_btn);
        RadioButton largeSizeBtn = (RadioButton) findViewById(R.id.large_size_radio_btn);

        Button minusBtn = (Button) findViewById(R.id.minus_btn);
        TextView amountTextView = (TextView) findViewById(R.id.amount_textview);
        Button plusBtn = (Button) findViewById(R.id.plus_btn);

        ImageView coffeeImageView = (ImageView) findViewById(R.id.coffee_imageview);

        Button calculateBtn = (Button) findViewById(R.id.calculate_btn);
        TextView totalPriceTextView = (TextView) findViewById(R.id.total_price_textview);

        // - 버튼을 기본적으로 비활성하한다
        minusBtn.setEnabled(false);


        // 초기화 버튼을 누르면 아메리카노, SMALL 로 체크되고 이미지도 처음 이미지로 변경, 계산된 숫자도 0으로 만든다
        initBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                americanoBtn.setChecked(true);
                smallSizeBtn.setChecked(true);

                // 사진을 초기 이미지로 변경한다
                coffeeImageView.setImageResource(R.drawable.americano);

                // 값을 0으로 바꾼다
                price = 0;
                totalPriceTextView.setText("0");
                // 갯수 설정도 1로 바꿔준다
                amount = 1;
                amountTextView.setText("1");
                minusBtn.setEnabled(false);
            }
        });

        // 계산 버튼을 누르면 위치 선택에 따라서 금액이 나오도록 한다
        calculateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 이전에 계산했던 금액들을 지운다 (0으로 다시 초기화한다)
                price = 0;

                // 기본적으로 소모되는 재고
                int coffeeBeans = 14;
                int steamMilk = 0;
                int straw = 1;
                int cup = 1;

                int menu  = menuRadioGroup.getCheckedRadioButtonId();
                int size = sizeRadioGroup.getCheckedRadioButtonId();

                if(menu == R.id.americano_radio_btn){
                    price += 1000;
                } else if(menu == R.id.coffee_latte_radio_btn){
                    price += 1500;
                    steamMilk = 150;
                } else {
                    price += 2000;
                    steamMilk = 130;
                }

                if(size == R.id.medium_size_radio_btn){ // 보통 사이즈에 대한 가격 증가, 한 잔에 사용하는 재고 증가
                    price += 500;
                    coffeeBeans += 7;
                    if(steamMilk != 0){ // 메뉴가 아메리카노가 아니라서 steamMilk 가 0이 아니라 다른 값으로 들어있다면 스팀밀크도 증가시킨다
                        steamMilk += 20;
                    }
                } else if(size == R.id.large_size_radio_btn){ // 라지 사이즈에 대한 가격 증가, 한 잔에 사용하는 재고 증가
                    coffeeBeans += 3;
                    price += 1000;
                    if(steamMilk != 0){
                        steamMilk += 40;
                    }
                }

                // 총 가격을 계산해서 보여주는 부분
                int totalPrice = price * amount;
                totalPriceTextView.setText(String.valueOf(totalPrice));

                // 총 소요될 재고의 양
                int usedCoffeeBeans = coffeeBeans * amount;
                int usedSteamMilk = steamMilk * amount;
                int usedStraw = straw * amount;
                int usedCup = cup * amount;

                // 판매 내역을 기록한다
                String salesDetails =  String.valueOf(totalPrice);
                List<String> salesList = new ArrayList<>(stock.getSalesDetails());
                salesList.add(salesDetails);

                stock.setCoffeeBeans(stock.getCoffeeBeans() - usedCoffeeBeans);
                stock.setSteamMilk(stock.getSteamMilk() - usedSteamMilk);
                stock.setStraw(stock.getStraw() - usedStraw);
                stock.setCup(stock.getCup() - usedCup);
                stock.setProfit(stock.getProfit() + totalPrice);
                stock.setSalesDetails(salesList);

            }
        });
    }

    // 커피 종류를 선택하면 Toast 메시지가 출력되고, 해당 종류에 맞게 커피 이미지가 변경된다
    public void onCoffeeMenuClicked(View view){
        boolean checked = ((RadioButton) view).isChecked();

        ImageView coffeeImageView = (ImageView) findViewById(R.id.coffee_imageview);
        switch(view.getId()){
            case R.id.americano_radio_btn:{
                if(checked){
                    coffeeImageView.setImageResource(R.drawable.americano);
                    Toast.makeText(this, "아메리카노 선택", Toast.LENGTH_SHORT).show();
                }
                break;
            }
            case R.id.coffee_latte_radio_btn:{
                if(checked){
                    coffeeImageView.setImageResource(R.drawable.coffee_latte);
                    Toast.makeText(this, "카페라떼 선택", Toast.LENGTH_SHORT).show();
                }
                break;
            }
            case R.id.cappuccino_radio_btn:{
                if(checked){
                    coffeeImageView.setImageResource(R.drawable.cappuccino);
                    Toast.makeText(this, "카푸치노 선택", Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }

    // 커피 사이즈를 선택하면 Toast 메시지가 출력된다
    public void onCoffeeSizeClicked(View view){
        boolean checked = ((RadioButton) view).isChecked();

        switch(view.getId()){
            case R.id.small_size_radio_btn:{
                if(checked){
                    Toast.makeText(this, "SMALL 선택", Toast.LENGTH_SHORT).show();
                }
                break;
            }
            case R.id.medium_size_radio_btn:{
                if(checked){
                    Toast.makeText(this, "MEDIUM 선택", Toast.LENGTH_SHORT).show();
                }
                break;
            }
            case R.id.large_size_radio_btn:{
                if(checked){
                    Toast.makeText(this, "LARGE 선택", Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }

    // 메뉴 숫자 설정: 기본은 1로 시작하고, -,+ 버튼을 통해 숫자 증감소 (최소 숫자는 1이고, 0 이하는 나오지 않는다)
    public void onAmountClicked(View view){
        TextView amountTextView = (TextView) findViewById(R.id.amount_textview);
        Button minusBtn = (Button) findViewById(R.id.minus_btn);
        switch (view.getId()){
            case R.id.minus_btn:{
                if(amount > 2){
                    amount -= 1;
                } else if(amount == 2){
                    amount -= 1;
                    minusBtn.setEnabled(false);
                }
                amountTextView.setText(String.valueOf(amount));
                break;
            }
            case R.id.plus_btn:{
                amount += 1;
                minusBtn.setEnabled(true);
                amountTextView.setText(String.valueOf(amount));
                break;
            }
        }
    }

    // Toolbar 의 메뉴 설정
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        return true;
    }

    // Toolbar 의 메뉴 아이템 클릭 이벤트 설정
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.admin_page:{ // 관리자 페이지 버튼을 누르면 비밀번호를 입력하는 다이얼로그가 나타난다
                EditText pwInput = new EditText(this);

                String pw = "1234";

                // 다이얼로그를 기본적으로 셋팅한다
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("관리자 비밀번호를 입력하세요")
                        .setView(pwInput)
                        .setPositiveButton("ok", null)
                        .setNegativeButton("cancel", null);

                AlertDialog dialog = builder.create();
                dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialog) {
                        // OK 버튼 지정
                        Button positiveBtn = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                        // 비밀번호 입력창이 비어있거나 빈칸인 상태라면 OK 버튼을 비활성화한다
                        positiveBtn.setEnabled(!pwInput.getText().toString().isBlank());

                        // TextWatcher 를 이용해서 EditText 의 입력값 변경에 따라 작업을 수행한다
                        pwInput.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                            }

                            // pwInput 에 값이 입력되어있으면 ok 버튼을 활성화한다
                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {
                                ((AlertDialog)dialog).getButton(android.app.AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                            }

                            // 텍스트 변경이 끝났을 때 아무것도 입력되어있지 않으면 (+ 스페이스바만 입력된 경우도) ok 버튼을 다시 비활성화한다
                            @Override
                            public void afterTextChanged(Editable s) {
                                ((AlertDialog) dialog).getButton(android.app.AlertDialog.BUTTON_POSITIVE).setEnabled(!pwInput.getText().toString().isBlank());
                            }
                        });

                        // OK 버튼이 눌렸을 때의 이벤트 처리
                        positiveBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // 입력된 값이 비밀번호와 일치한다면 관리자페이지로 넘어가고 다이얼로그를 닫는다
                                if(pwInput.getText().toString().equals(pw)){
                                    Intent intent = new Intent(getApplicationContext(), AdminActivity.class);
                                    startActivity(intent);
                                    dialog.dismiss();
                                } else { // 입력된 값이 비밀번호와 일치하지 않는다면 입력된 값들을 지우고 힌트로 비밀번호를 잘못 입력했다고 알려준다
                                    pwInput.setText("");
                                    pwInput.setHint("잘못된 비밀번호입니다.");
                                }
                            }
                        });
                    }
                });
                // 다이얼로그를 띄우고, 자동으로 pwInput 에 포커스를 준다
                dialog.show();
                pwInput.requestFocus();
                break;
            }
            case R.id.add_review:{ // 별점 남기기를 클릭하면 리뷰페이지로 이동한다
                Intent intent = new Intent(getApplicationContext(), ReviewActivity.class);
                startActivity(intent);
                break;
            }
        }

        return true;
    }

    // 결제하기 버튼 클릭 이벤트
    public void onPayBtnClicked(View view){
        if(price != 0){ // 계산 버튼을 누른 이후에만 결제하기가 수행된다
            Toast.makeText(this, "결제가 완료되었습니다.", Toast.LENGTH_SHORT).show();

            // 재고를 업데이트 한다
            DBStockUseThread dbStockUseThread = new DBStockUseThread();
            Thread t = new Thread(dbStockUseThread);
            t.start();
        }
    }
}