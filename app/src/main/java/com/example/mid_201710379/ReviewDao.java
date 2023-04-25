package com.example.mid_201710379;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ReviewDao {

    @Insert
    void insertReview(Review review);

    @Delete
    void deleteReview(Review review);

    @Query("SELECT * FROM Review")
    List<Review> getAllReview();

}
