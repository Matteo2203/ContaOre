package com.example.contaore;

import androidx.recyclerview.widget.RecyclerView;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;

import com.example.contaore.data.WeekEntry;

public class WeekAdapter extends RecyclerView.Adapter<WeekAdapter.ViewHolder>{

    private List<WeekEntry> lista;
    private OnEliminaListener listener;
    private OnClickFoto click;

    private String[] mesi = {"GENNAIO","FEBBRAIO","MARZO","APRILE","MAGGIO","GIUGNO","LUGLIO","AGOSTO","SETTEMBRE","OTTOBRE","NOVEMBRE","DICEMBRE"};

    //costruttore
    WeekAdapter(List<WeekEntry> lista,OnEliminaListener listener,OnClickFoto click){
        this.lista = lista;
        this.listener = listener;
        this.click = click;
    }

    //RecyclerView

    @Override
    public int getItemCount(){

        int i = lista.size();
        return i;

    }
    //ViewHolder

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_week, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position){
        WeekEntry entry = lista.get(position);
        //holder.tvData.setText(entry.getData());
        holder.tvOre.setText(entry.getOre() + " ore");
        String percorso = entry.getPercorsoImmagine();
        Uri uri = Uri.parse(percorso);
        holder.ivFoto.setImageURI(uri);
        holder.itemView.setOnLongClickListener(v -> {
            listener.onElimina(entry);
            return true;
        });
        holder.itemView.setOnClickListener(v->{
            click.onClick(entry);
        });

        String[] split = entry.getData().split("-");
        int mese = Integer.parseInt(split[1]);
        int giorno = Integer.parseInt(split[2]);
        int nWeek = (giorno -1 )/7 + 1 ;
        String titolo = " " + nWeek+ "° settimana di " + mesi[mese -1 ];
        holder.tvData.setText(titolo);
    }

    public interface OnEliminaListener {
        void onElimina(WeekEntry entry);
    }

    public interface OnClickFoto{


        void onClick(WeekEntry entry);

    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvData, tvOre;
        ImageView ivFoto;

        public ViewHolder(View itemView) {
            super(itemView);
            tvData = itemView.findViewById(R.id.tvData);
            tvOre = itemView.findViewById(R.id.tvOre);
            ivFoto = itemView.findViewById(R.id.ivFoto);
        }
    }

}
