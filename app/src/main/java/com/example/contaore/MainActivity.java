package com.example.contaore;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.contaore.data.AppDatabase;
import com.example.contaore.data.WeekEntry;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        AppDatabase db = AppDatabase.getInstance(getApplicationContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        new Thread(() -> {
            List<WeekEntry> lista = db.weekEntryDao().getAllEntries();

            runOnUiThread(() -> {
                WeekAdapter adapter = new WeekAdapter(lista);
                recyclerView.setAdapter(adapter);
            });
        }).start();

        Button btnAggiungi = findViewById(R.id.btnAggiungi);
        btnAggiungi.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddEntryActivity.class);
            startActivity(intent);
        });
    }
}