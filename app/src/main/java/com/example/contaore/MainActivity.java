package com.example.contaore;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
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

    RecyclerView recyclerView;
    AppDatabase db;

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
        Button btnAggiungi = findViewById(R.id.btnAggiungi);
        btnAggiungi.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddEntryActivity.class);
            startActivity(intent);
        });
        recyclerView = findViewById(R.id.recyclerView);
        db = AppDatabase.getInstance(getApplicationContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onResume() {
        super.onResume();
        caricaLista();

    }

    void caricaLista() {
        new Thread(() -> {
            List<WeekEntry> lista = db.weekEntryDao().getAllEntries();

            runOnUiThread(() -> {
                WeekAdapter adapter = new WeekAdapter(lista, entry -> {
                    AlertDialog.Builder alert = new AlertDialog.Builder(this);
                    alert.setTitle("ALLERTAA");
                    alert.setMessage("SEI SICURO DI VOLERLO ELIMINARE??");
                    alert.setPositiveButton("ELIMINA", (dialog, which)->{
                        new Thread(() -> {
                            db.weekEntryDao().cancella(entry);
                            caricaLista();
                        }).start();
                    });
                    alert.setNegativeButton("ANNULLA",null );
                    alert.show();
                });
                recyclerView.setAdapter(adapter);
            });
        }).start();
    }


}
