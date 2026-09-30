package com.example.contaore;

import android.app.DatePickerDialog;
import android.net.Uri;
import android.os.Bundle;
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
        btnData.setOnClickListener(v->{
            Calendar i = Calendar.getInstance();
            int year = i.get(Calendar.YEAR);
            int month = i.get(Calendar.MONTH);
            int day = i.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog dialogData = new DatePickerDialog(AddEntryActivity.this, (p1,annoScelto,meseScelto,giornoScelto) ->{
                dataScelta =  giornoScelto + "/" + (meseScelto + 1) + "/" + annoScelto;
                tvDataScelta.setText("...");
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

            double ore = 0;
            try {
                ore = Double.parseDouble(testoOre);
            } catch (NumberFormatException e) {
                AddEntryActivity contesto = AddEntryActivity.this;
                Toast.makeText(contesto, "ORE NON VALIDE", Toast.LENGTH_SHORT).show();
                return;

            }
            WeekEntry i = new WeekEntry();
            i.setOre(ore);
            i.setData("da definire");

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
}