package com.example.ac2tem5;

import androidx.recyclerview.widget.RecyclerView;

public abstract class AdaptadorAnimales extends RecyclerView.Adapter<AdaptadorAnimales.AnimalesViewHolder>
{
    private Animales [] listaAnimales;
    public AdaptadorAnimales(Animales[] listaAnimales)
    {
        this.listaAnimales = listaAnimales;
    }
}
