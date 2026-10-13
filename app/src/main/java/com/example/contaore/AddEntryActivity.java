package com.example.contaore;

import android.app.DatePickerDialog;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.contaore.data.AppDatabase;
import com.example.contaore.data.WeekEntry;
import com.example.contaore.data.WeekEntryDao;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;

public class AddEntryActivity extends AppCompatActivity {

    private String dataScelta = "";
    private ImageView ivPreview;
    private Uri immagineSelezionata;

    private final ActivityResultLauncher<PickVisualMediaRequest> pickMedia =
            registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
                if (uri != null) {
                    ivPreview.setImageURI(uri);
                    immagineSelezionata = uri;
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_entry);

        ivPreview = findViewById(R.id.imgView);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button btnScegliImmagine = findViewById(R.id.btnNext);
        btnScegliImmagine.setOnClickListener(v -> pickMedia.launch(
                new PickVisualMediaRequest.Builder()
                        .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                        .build()
        ));

        Button btnSave = findViewById(R.id.btnSave);
        EditText etOre = findViewById(R.id.etOre);
        Button btnData = findViewById(R.id.btnData);
        TextView tvDataScelta = findViewById(R.id.tvDataScelta);
        EditText etOreDue = findViewById(R.id.etOreSecondoMese);

        btnData.setOnClickListener(v -> {
            Calendar i = Calendar.getInstance();
            int year = i.get(Calendar.YEAR);
            int month = i.get(Calendar.MONTH);
            int day = i.get(Calendar.DAY_OF_MONTH);
            Calendar x = Calendar.getInstance();

            DatePickerDialog dialogData = new DatePickerDialog(AddEntryActivity.this, (p1, annoScelto, meseScelto, giornoScelto) -> {
                x.set(annoScelto, meseScelto, giornoScelto);
                int giornoSettimana = x.get(Calendar.DAY_OF_WEEK);

                if (giornoSettimana != Calendar.MONDAY) {
                    Toast.makeText(AddEntryActivity.this, "SCEGLI UN LUNEDÌ!", Toast.LENGTH_SHORT).show();
                    return;
                }

                dataScelta = String.format("%d-%02d-%02d", annoScelto, (meseScelto + 1), giornoScelto);
                tvDataScelta.setText(dataScelta);

                int mese = x.get(Calendar.MONTH);
                x.add(Calendar.DAY_OF_MONTH, 6);
                int meseFine = x.get(Calendar.MONTH);

                if (mese != meseFine) {
                    etOreDue.setVisibility(View.VISIBLE);
                } else {
                    etOreDue.setVisibility(View.GONE);
                }

            }, year, month, day);

            dialogData.show();
        });

        btnSave.setOnClickListener(v -> {
            String testoOre = etOre.getText().toString();
            if (testoOre.trim().isEmpty()) {
                Toast.makeText(this, "INSERISCI LE ORE", Toast.LENGTH_SHORT).show();
                return;
            }
            if (immagineSelezionata == null) {
                Toast.makeText(this, "INSERISCI L'IMMAGINE", Toast.LENGTH_SHORT).show();
                return;
            }
            if (dataScelta.isEmpty()) {
                Toast.makeText(this, "INSERISCI LA DATA", Toast.LENGTH_SHORT).show();
                return;
            }

            int minutiTotali = convertiInMinuti(testoOre);
            if (minutiTotali < 0) return;

            int minutiSecondoMese = 0;
            if (etOreDue.getVisibility() == View.VISIBLE) {
                String testoDue = etOreDue.getText().toString();
                if (testoDue.trim().isEmpty()) {
                    Toast.makeText(this, "INSERISCI LE ORE DEL MESE NUOVO", Toast.LENGTH_SHORT).show();
                    return;
                }
                minutiSecondoMese = convertiInMinuti(testoDue);
                if (minutiSecondoMese < 0) return;

                if (minutiSecondoMese > minutiTotali) {
                    Toast.makeText(this, "ORE MESE NUOVO MAGGIORI DEL TOTALE", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            WeekEntry entry = new WeekEntry();
            entry.setMinutiTotali(minutiTotali);
            entry.setMinutiSecondoMese(minutiSecondoMese);
            entry.setData(dataScelta);

            new Thread(() -> {
                File destinazione = null;
                try (InputStream in = getContentResolver().openInputStream(immagineSelezionata)) {
                    String name = System.currentTimeMillis() + ".jpg";
                    destinazione = new File(getFilesDir(), name);
                    try (OutputStream out = new FileOutputStream(destinazione)) {
                        byte[] buffer = new byte[1024];
                        int letti;
                        while ((letti = in.read(buffer)) != -1) {
                            out.write(buffer, 0, letti);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    runOnUiThread(() -> Toast.makeText(AddEntryActivity.this, "Errore salvataggio immagine", Toast.LENGTH_SHORT).show());
                    return;
                }

                entry.setPercorsoImmagine(destinazione.getAbsolutePath());

                AppDatabase db = AppDatabase.getInstance(getApplicationContext());
                WeekEntryDao d = db.weekEntryDao();
                d.inserisci(entry);

                runOnUiThread(this::finish);
            }).start();
        });
    }

    private int convertiInMinuti(String testo) {
        try {
            testo = testo.replace(',', '.').trim();
            String[] pezzi = testo.split("\\.");

            if (pezzi.length > 2) {
                Toast.makeText(this, "ORE NON VALIDE", Toast.LENGTH_SHORT).show();
                return -1;
            }

            int ore = Integer.parseInt(pezzi[0]);
            int minuti = 0;

            if (pezzi.length == 2) {
                if (pezzi[1].length() != 2) {
                    Toast.makeText(this, "INSERISCI I MINUTI CON 2 CIFRE (es. 05, 30)", Toast.LENGTH_SHORT).show();
                    return -1;
                }
                minuti = Integer.parseInt(pezzi[1]);
                if (minuti >= 60) {
                    Toast.makeText(this, "I MINUTI DEVONO ESSERE MINORI DI 60", Toast.LENGTH_SHORT).show();
                    return -1;
                }
            }

            return ore * 60 + minuti;

        } catch (NumberFormatException e) {
            Toast.makeText(this, "FORMATO ORE NON VALIDO", Toast.LENGTH_SHORT).show();
            return -1;
        }
    }
}