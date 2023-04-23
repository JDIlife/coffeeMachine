package com.example.mid_201710379;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface StockDao {

    @Insert
    void insertStock(Stock stock);

    @Update
    void setStockUse(Stock stock);

    @Update
    void stockUp(Stock stock);

    @Query("SELECT * FROM Stock")
    List<Stock> getStock();

    @Query("UPDATE Stock SET profit = :zero WHERE id = :stockId")
    void withdrawProfit(int stockId, int zero);

    @Query("UPDATE Stock SET coffeeBeans = :coffeeBeans , steamMilk = :steamMilk, straw = :straw, cup = :cup WHERE id = :stockId")
    void fillStock(int coffeeBeans, int steamMilk, int straw, int cup, int stockId);

    @Query("UPDATE Stock SET salesDetails = :salesDetails WHERE id = :stockId")
    void emptySalesDetails(List<String> salesDetails, int stockId);
}
