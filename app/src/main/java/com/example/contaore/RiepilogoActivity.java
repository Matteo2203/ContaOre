package com.example.contaore;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.contaore.data.AppDatabase;
import com.example.contaore.data.RiepilogoMese;

import java.util.List;

public class RiepilogoActivity extends AppCompatActivity {

    RecyclerView recyclerRiepilogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_riepilogo);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        recyclerRiepilogo = findViewById(R.id.recyclerRiepilogo);
        recyclerRiepilogo.setLayoutManager(new LinearLayoutManager(this));

        new Thread(() -> {
            AppDatabase db = AppDatabase.getInstance(getApplicationContext());
            List<RiepilogoMese> riepilogo = db.weekEntryDao().getRiepilogoMensile();

            runOnUiThread(() -> recyclerRiepilogo.setAdapter(new RiepilogoAdapter(riepilogo)));
        }).start();
    }
}