package com.example.contaore.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
@Entity(tableName = "week_entries")
public class WeekEntry {
    @PrimaryKey (autoGenerate = true)
    int id;
    String data = "";
    int minutiTotali;
    String percorsoImmagine = "";
    int minutiSecondoMese;

    public int getMinutiSecondoMese() {
        return minutiSecondoMese;
    }

    public void setMinutiSecondoMese(int minutiSecondoMese) {
        this.minutiSecondoMese = minutiSecondoMese;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setData(String data) {
        this.data = data;
    }

    public void setMinutiTotali(int minutiTotali) {
        this.minutiTotali = minutiTotali;
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

    public int getMinutiTotali() {
        return minutiTotali;
    }

    public String getPercorsoImmagine() {
        return percorsoImmagine;
    }







}
