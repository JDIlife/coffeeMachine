package com.example.mid_201710379;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Review {
    @PrimaryKey(autoGenerate = true)
    private  int id = 0;

    private double rating;

    private String review;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

}
