package com.example.ac2tem5;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class AnimalesViewHolder extends RecyclerView.ViewHolder {
    public TextView tituloView;
    public ImageView portadaView;
    private Context context;

    public AnimalesViewHolder(View view, Context context) {
        super(view);
        this.context = context
    }
}
