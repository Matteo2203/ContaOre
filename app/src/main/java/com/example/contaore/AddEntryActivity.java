package com.example.contaore;

import static java.lang.Integer.parseInt;
import static kotlin.reflect.KClasses.cast;

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

    String dataScelta= "";
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
        btnScegliImmagine.setOnClickListener(v -> {
            pickMedia.launch(new PickVisualMediaRequest.Builder()
                    .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                    .build());
        });

        Button btnSave = findViewById(R.id.btnSave);
        EditText etOre = findViewById(R.id.etOre);
        Button btnData = findViewById(R.id.btnData);
        TextView tvDataScelta = findViewById(R.id.tvDataScelta);
        EditText etOreDue = findViewById(R.id.etOreSecondoMese);
        btnData.setOnClickListener(v->{
            Calendar i = Calendar.getInstance();
            int year = i.get(Calendar.YEAR);
            int month = i.get(Calendar.MONTH);
            int day = i.get(Calendar.DAY_OF_MONTH);
            Calendar x = Calendar.getInstance();

            DatePickerDialog dialogData = new DatePickerDialog(AddEntryActivity.this, (p1,annoScelto,meseScelto,giornoScelto) ->{
                x.set(annoScelto,meseScelto ,giornoScelto);
                int giornoSettimana = x.get(Calendar.DAY_OF_WEEK);
                if(giornoSettimana != Calendar.MONDAY){
                    Toast.makeText(AddEntryActivity.this, "SCEGLI UN LUNEDI!",Toast.LENGTH_SHORT).show();
                    return;
                }
                dataScelta = String.format("%d-%02d-%02d", annoScelto, (meseScelto + 1), giornoScelto);
                tvDataScelta.setText(dataScelta);
                int mese = x.get(Calendar.MONTH);
                x.add(Calendar.DAY_OF_MONTH, 6);
                int meseFine = x.get(Calendar.MONTH);
                if(mese != meseFine){
                    etOreDue.setVisibility(View.VISIBLE);
                }else
                    etOreDue.setVisibility(View.GONE);


            }, (year), (month) , (day));

                dialogData.show();


        });

        btnSave.setOnClickListener(v -> {
            String testoOre = etOre.getText().toString();
            if(testoOre.trim().isEmpty()){
                AddEntryActivity contesto = AddEntryActivity.this;
                Toast.makeText(contesto, "INSERISCI LE ORE", Toast.LENGTH_SHORT).show();
                return;
            }
            if(immagineSelezionata == null){
                AddEntryActivity contesto = AddEntryActivity.this;
                Toast.makeText(contesto, "INSERISCI L'IMMAGINE", Toast.LENGTH_SHORT).show();
                return;
            }
            if(dataScelta.isEmpty()){
                AddEntryActivity contesto = AddEntryActivity.this;
                Toast.makeText(contesto, "INSERISCI LA DATA", Toast.LENGTH_SHORT).show();
                return;
            }


            int minutiTotali =convertiInMinuti(testoOre);
            if(minutiTotali < 0)
                return;

            WeekEntry i = new WeekEntry();
            i.setMinutiTotali(minutiTotali );
            i.setData(dataScelta);

            Thread t = new Thread(() ->{
                File destinazione = null;
                try {
                    InputStream in = getContentResolver().openInputStream(immagineSelezionata);
                    String name = System.currentTimeMillis() + ".jpg";
                    destinazione = new File(getFilesDir(), name);
                    OutputStream out = new FileOutputStream(destinazione);
                    byte[] buffer = new byte[1024];
                    int letti;
                    while ((letti = in.read(buffer)) != -1){
                        out.write(buffer, 0, letti);
                    }
                    in.close();
                    out.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                i.setPercorsoImmagine(destinazione.getAbsolutePath());


                AppDatabase db = AppDatabase.getInstance(getApplicationContext());
                WeekEntryDao d = db.weekEntryDao();
                d.inserisci(i);

                runOnUiThread(() -> {
                    finish();
                });

            });
            t.start();
        });

    }
    ImageView ivPreview;
    Uri immagineSelezionata;
    ActivityResultLauncher<PickVisualMediaRequest> pickMedia =
            registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
                if (uri != null) {
                    ivPreview.setImageURI(uri);
                    ivPreview.setImageURI(uri);
                    immagineSelezionata = uri;
                }
            });

    int convertiInMinuti(String testo) {
        int ore = 0;
        int minuti = 0;
        try {
            testo = testo.replace(',', '.');
            String[] pezzi = testo.split("\\.");
            if (pezzi.length > 2) {
                Toast.makeText(AddEntryActivity.this, "ORE NON VALIDE ", Toast.LENGTH_SHORT).show();
                return -1 ;
            }
            ore = Integer.parseInt(pezzi[0]);
            if (pezzi.length == 2) {
                if (pezzi[1].length() != 2) {
                    Toast.makeText(AddEntryActivity.this, "ORE NON VALIDE ", Toast.LENGTH_SHORT).show();
                    return -1;
                }
                minuti = Integer.parseInt(pezzi[1]);
                if (minuti >= 60) {
                    Toast.makeText(AddEntryActivity.this, "ORE NON VALIDE ", Toast.LENGTH_SHORT).show();
                    return - 1 ;
                }

            }
            return ore * 60 + minuti;
        } catch (NumberFormatException e) {
            AddEntryActivity contesto = AddEntryActivity.this;
            Toast.makeText(contesto, "ORE NON VALIDE", Toast.LENGTH_SHORT).show();
            return -1 ;


        }
    }

}