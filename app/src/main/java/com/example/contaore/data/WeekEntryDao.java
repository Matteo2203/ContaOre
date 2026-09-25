package com.example.contaore.data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface WeekEntryDao {

    @Insert
    void inserisci(WeekEntry entry);


    @Delete
    void cancella(WeekEntry delete);

    @Query("SELECT * FROM week_entries")
    List<WeekEntry> getAllEntries();



}
