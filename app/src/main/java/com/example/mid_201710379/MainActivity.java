package com.example.mid_201710379;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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

public class MainActivity extends AppCompatActivity {

    int price = 0;
    int amount = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

                int menu  = menuRadioGroup.getCheckedRadioButtonId();
                int size = sizeRadioGroup.getCheckedRadioButtonId();

                if(menu == R.id.americano_radio_btn){
                    price += 1000;
                } else if(menu == R.id.coffee_latte_radio_btn){
                    price += 1500;
                } else {
                    price += 2000;
                }

                if(size == R.id.medium_size_radio_btn){
                    price += 500;
                } else if(size == R.id.large_size_radio_btn){
                    price += 1000;
                }

                int totalPrice = price * amount;
                totalPriceTextView.setText(String.valueOf(totalPrice));
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
                // 다이얼로그를 띄운다
                dialog.show();
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
}