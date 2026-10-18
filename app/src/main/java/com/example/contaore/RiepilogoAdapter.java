package com.example.contaore;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.contaore.data.RiepilogoMese;

import java.util.List;

public class RiepilogoAdapter extends RecyclerView.Adapter<RiepilogoAdapter.ViewHolder> {

    private final List<RiepilogoMese> lista;

    private final String[] mesi = {"GENNAIO", "FEBBRAIO", "MARZO", "APRILE", "MAGGIO", "GIUGNO",
            "LUGLIO", "AGOSTO", "SETTEMBRE", "OTTOBRE", "NOVEMBRE", "DICEMBRE"};

    RiepilogoAdapter(List<RiepilogoMese> lista) {
        this.lista = lista;
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_mese, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        RiepilogoMese r = lista.get(position);

        // "2026-10" -> "OTTOBRE 2026"
        String[] split = r.getMese().split("-");
        int mese = Integer.parseInt(split[1]);
        holder.tvMese.setText(mesi[mese - 1] + " " + split[0]);

        // 6450 minuti -> "107.30 ORE"
        int ore = r.getMinuti() / 60;
        int minuti = r.getMinuti() % 60;
        holder.tvOreMese.setText(String.format("%d.%02d ORE", ore, minuti));
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvMese, tvOreMese;

        public ViewHolder(View itemView) {
            super(itemView);
            tvMese = itemView.findViewById(R.id.tvMese);
            tvOreMese = itemView.findViewById(R.id.tvOreMese);
        }
    }
}