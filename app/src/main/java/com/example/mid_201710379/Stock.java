package com.example.mid_201710379;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ProvidedTypeConverter;
import androidx.room.TypeConverter;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Entity
public class Stock implements Parcelable {

    @PrimaryKey
    private int id = 0;

    private int coffeeBeans = 1000;
    private int steamMilk = 1000;
    private int straw = 30;
    private int cup = 30;
    private int profit = 0;

    // Room 데이터베이는 List 형식을 지원하지 않기 때문에 Gson 을 사용한다
    @ColumnInfo(name = "salesDetails")
    List<String> salesDetails;


    // getter , setter
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCoffeeBeans() {
        return coffeeBeans;
    }

    public void setCoffeeBeans(int coffeeBeans) {
        this.coffeeBeans = coffeeBeans;
    }

    public int getSteamMilk() {
        return steamMilk;
    }

    public void setSteamMilk(int steamMilk) {
        this.steamMilk = steamMilk;
    }

    public int getStraw() {
        return straw;
    }

    public void setStraw(int straw) {
        this.straw = straw;
    }

    public int getCup() {
        return cup;
    }

    public void setCup(int cup) {
        this.cup = cup;
    }

    public int getProfit() {
        return profit;
    }

    public void setProfit(int profit) {
        this.profit = profit;
    }

    public List<String> getSalesDetails(){
        return salesDetails;
    }

    public void setSalesDetails(List<String> salesDetails){
        this.salesDetails = salesDetails;
    }

    // Parcelable 오버라이드
    @Override
    public int describeContents() {
        return 0;
    }

    // 직렬화용 메서드
    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(coffeeBeans);
        dest.writeInt(steamMilk);
        dest.writeInt(straw);
        dest.writeInt(cup);
        dest.writeInt(profit);
        dest.writeStringList(salesDetails);
    }

    // 역직렬화용 생성자
    protected Stock(Parcel in){
        id = in.readInt();
        coffeeBeans = in.readInt();
        steamMilk = in.readInt();
        straw = in.readInt();
        cup = in.readInt();
        profit = in.readInt();

        // salesDetails 를 ArrayList<>()로 초기화한다
        salesDetails = new ArrayList<>();
        // 초기화한 salesDetails 에 in.readStringList(salesDetails) 를 이용해서 parcel 에 보내준 값을 추가한다
        in.readStringList(salesDetails);
    }

    // 역직렬화용 메서드
    public static final Creator<Stock> CREATOR = new Creator<Stock>() {
        @Override
        public Stock createFromParcel(Parcel in) {
            return new Stock(in);
        }

        @Override
        public Stock[] newArray(int size) {
            return new Stock[size];
        }
    };

    // 기본 생성자
    public Stock(){
        this.coffeeBeans = 1000;
        this.steamMilk = 1000;
        this.straw = 30;
        this.cup = 30;
        this.profit = 0;
        this.salesDetails = new ArrayList<>();
    }
}

// Gson 을 사용해서 List<String> 을 Json 으로 변환해서 저장, Json 을 다시 List<String> 으로 변환할 수 있도록 TypeConverter 작성
@ProvidedTypeConverter
class StringListTypeConverter {
    private Gson gson = new Gson();

    @TypeConverter
    public String listToJson(List<String> salesDetails){
        return gson.toJson(salesDetails);
    }

    @TypeConverter
    public List<String> jsonToList(String str){
        return Arrays.asList(gson.fromJson(str, String[].class));
    }
}