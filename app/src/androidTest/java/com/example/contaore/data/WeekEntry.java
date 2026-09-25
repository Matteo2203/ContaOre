package com.example.contaore.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
@Entity(tableName = "week_entries")
public class WeekEntry {
    @PrimaryKey (autoGenerate = true)
    int id;
    String data = "";
    double ore;
    String percorsoImmagine = "";

    public void setId(int id) {
        this.id = id;
    }

    public void setData(String data) {
        this.data = data;
    }

    public void setOre(double ore) {
        this.ore = ore;
    }

    public void setPercorsoImmagine(String percorsoImmagine) {
        this.percorsoImmagine = percorsoImmagine;
    }

    public int getId() {
        return id;
    }

    public String getData() {
        return data;
    }

    public double getOre() {
        return ore;
    }

    public String getPercorsoImmagine() {
        return percorsoImmagine;
    }







}
