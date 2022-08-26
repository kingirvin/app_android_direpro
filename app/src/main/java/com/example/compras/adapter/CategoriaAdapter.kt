package com.example.compras.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.compras.R
import com.example.compras.model.Categoria

class CategoriaAdapter(private val categoriaLista:List<Categoria>, private val onClickListener: (Categoria)->Unit ) : RecyclerView.Adapter<CategoriaViewHolder> (){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriaViewHolder {
        val layoutInflater= LayoutInflater.from(parent.context)
        return CategoriaViewHolder(layoutInflater.inflate(R.layout.item_categoria,parent,false))
    }
    override fun getItemCount(): Int =categoriaLista.size
    override fun onBindViewHolder(holder: CategoriaViewHolder, position: Int) {
        val item=categoriaLista[position]
        holder.render(item,onClickListener)
    }
}