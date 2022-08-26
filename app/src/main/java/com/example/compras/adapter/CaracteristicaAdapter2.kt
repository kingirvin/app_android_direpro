package com.example.compras.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.compras.R
import com.example.compras.model.Caracteristica

class CaracteristicaAdapter2(private val caracteristicaLista:List<Caracteristica>, private val onClickListener: (Caracteristica)->Unit ) : RecyclerView.Adapter<CaracteristicaViewHolder2> (){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CaracteristicaViewHolder2 {
        val layoutInflater= LayoutInflater.from(parent.context)
        return CaracteristicaViewHolder2(layoutInflater.inflate(R.layout.item_caracteristica2,parent,false))
    }
    override fun getItemCount(): Int =caracteristicaLista.size
    override fun onBindViewHolder(holder: CaracteristicaViewHolder2, position: Int) {
        val item=caracteristicaLista[position]
        holder.render(item,onClickListener)
    }
}