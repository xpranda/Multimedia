package com.example.act2tema5;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AdaptadorPerros extends RecyclerView.Adapter<AdaptadorPerros.PerrosViewHolder>
{

    private Animales[] listaPerros;

    public AdaptadorPerros(Animales[] listaPerros)
    {
        this.listaPerros = listaPerros;
    }

    public static class PerrosViewHolder extends RecyclerView.ViewHolder {

        public TextView tituloView;

        public ImageView portadaView;

        private Context context;

        public PerrosViewHolder(View view, Context context) {
            super(view);
            this.context = context;

            tituloView = view.findViewById(R.id.tituloView);
            portadaView = view.findViewById(R.id.imagenView);
        }

        public void BindPerros(Animales perros) {
            tituloView.setText(perros.getTitle());
            portadaView.setImageResource(
                    context.getResources()
                            .getIdentifier(perros.getImage(),
                                    "drawable",
                                    "com.example.act2tema5"));
        }
    }

    @NonNull
    @Override
    public PerrosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.animales_item, parent, false);
        return new PerrosViewHolder(view, parent.getContext());
    }

    @Override
    public void onBindViewHolder(@NonNull PerrosViewHolder holder, int position) {
        holder.BindPerros(this.listaPerros[position]);
    }

    @Override
    public int getItemCount() {
        return listaPerros.length;
    }
}
