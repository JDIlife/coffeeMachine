package com.example.mid_201710379;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Review.class}, version = 1, exportSchema = false)
abstract public class ReviewDatabase extends RoomDatabase {

    public abstract ReviewDao reviewDao();

    private static ReviewDatabase INSTANCE;
    private static final Object lock = new Object();
    public static ReviewDatabase getInstance(Context context){
        synchronized (lock){
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), ReviewDatabase.class, "Review.db")
                    .build();
        }
        return INSTANCE;
    }
}
