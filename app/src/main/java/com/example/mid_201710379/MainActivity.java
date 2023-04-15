package com.example.mid_201710379;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

        minusBtn.setEnabled(false);


        // 초기화 버튼을 누르면 아메리카노, SMALL 로 체크되고 이미지도 처음 이미지로 변경, 계산된 숫자도 0으로 만든다
        initBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                americanoBtn.setChecked(true);
                smallSizeBtn.setChecked(true);

                price = 0;

                coffeeImageView.setImageResource(R.drawable.americano);

                totalPriceTextView.setText("0");
            }
        });

        // 계산 버튼을 누르면 위치 선택에 따라서 금액이 나오도록 한다
        calculateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

    // 기본은 1로 시작하고, -,+ 버튼을 통해 숫자 증감소 (최소 숫자는 1이고, 0 이하는 나오지 않는다)
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
}