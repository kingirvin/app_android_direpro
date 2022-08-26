package com.example.compras.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.compras.R
import com.example.compras.model.Caracteristica

class CaracteristicaAdapter (private val caracteristicaLista:List<Caracteristica>, private val onClickListener: (Caracteristica)->Unit ) : RecyclerView.Adapter<CaracteristicaViewHolder> (){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CaracteristicaViewHolder {
        val layoutInflater= LayoutInflater.from(parent.context)
        return CaracteristicaViewHolder(layoutInflater.inflate(R.layout.item_caracteristicas,parent,false))
    }
    override fun getItemCount(): Int =caracteristicaLista.size
    override fun onBindViewHolder(holder: CaracteristicaViewHolder, position: Int) {
        val item=caracteristicaLista[position]
        holder.render(item,onClickListener)
    }
}