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
}
