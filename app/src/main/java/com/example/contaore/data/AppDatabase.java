package com.example.contaore.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {WeekEntry.class}, version = 1 )
public abstract class AppDatabase extends RoomDatabase {

    public abstract  WeekEntryDao weekEntryDao();
    static AppDatabase i;

    public static AppDatabase getInstance(Context context){
        if(i == null)
            i = Room.databaseBuilder(context, AppDatabase.class , "orari").build();

        return i;

    }



}
