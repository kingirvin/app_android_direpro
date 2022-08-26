package com.example.compras.adapter

import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.compras.Producto
import com.example.compras.R

class ProductoAdapter(private val productoLista:List<Producto>, private val onClickListener: (Producto)->Unit ) : RecyclerView.Adapter<ProductoViewHolder> (){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductoViewHolder {
        val layoutInflater=LayoutInflater.from(parent.context)
        return ProductoViewHolder(layoutInflater.inflate(R.layout.item_producto,parent,false))
    }

    override fun onBindViewHolder(holder: ProductoViewHolder, position: Int) {
        val item=productoLista[position]
        holder.render(item,onClickListener)

    }
    override fun getItemCount(): Int =productoLista.size
}