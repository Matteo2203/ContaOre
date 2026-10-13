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

    @Query("SELECT * FROM week_entries ORDER BY data DESC")
    List<WeekEntry> getAllEntries();

    @Query("SELECT mese, SUM(minuti) AS minuti " +
            "FROM (" +
            "SELECT substr(data, 1, 7) AS mese, minutiTotali - minutiSecondoMese AS minuti FROM week_entries " +
            "UNION ALL " +
            "SELECT strftime('%Y-%m', data, '+6 days') AS mese, minutiSecondoMese AS minuti FROM week_entries" +
            ") " +
            "GROUP BY mese " +
            "ORDER BY mese DESC")
    List<RiepilogoMese> getRiepilogoMensile();


}
