package com.example.mid_201710379;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {Stock.class}, version = 4, exportSchema = false)
@TypeConverters({StringListTypeConverter.class})
abstract public class StockDatabase extends RoomDatabase {

    public abstract StockDao stockDao();

    // 싱글톤 패턴으로 Room Database 구현
    private static StockDatabase INSTANCE;
    private static final Object sLock = new Object();
    public static StockDatabase getInstance(Context context){
        synchronized (sLock){
            if(INSTANCE==null){
                INSTANCE = Room.databaseBuilder(context.getApplicationContext(), StockDatabase.class, "Stock.db")
                        // TypeConverter 추가
                        .addTypeConverter(new StringListTypeConverter())
                        // 마이그레이션 추가
                        .addMigrations(MIGRATION_2_3)
                        .addMigrations(MIGRATION_3_4)
                        .build();
            }
            return INSTANCE;
        }
    }

    // 수동 마이그레이션
    static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE Stock ADD COLUMN salesDetail Text");
        }
    };

    static final Migration MIGRATION_3_4 = new Migration(3, 4) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE Stock ADD COLUMN profit int");
        }
    };
}
