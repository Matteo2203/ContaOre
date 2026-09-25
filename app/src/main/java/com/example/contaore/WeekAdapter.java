package com.example.contaore;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.List;

import com.example.contaore.data.WeekEntry;

public class WeekAdapter extends RecyclerView.Adapter<WeekAdapter.ViewHolder>{

    private List<WeekEntry> lista;

    //costruttore
    WeekAdapter(List<WeekEntry> lista){
        this.lista = lista;
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
        holder.tvData.setText(entry.getData());
        holder.tvOre.setText(entry.getOre() + " ore");

    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvData, tvOre;

        public ViewHolder(View itemView) {
            super(itemView);
            tvData = itemView.findViewById(R.id.tvData);
            tvOre = itemView.findViewById(R.id.tvOre);
        }
    }

}
