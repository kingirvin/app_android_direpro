package com.example.compras.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.compras.Producto
import com.example.compras.R
import com.example.compras.databinding.ItemProductoBinding

class ProductoViewHolder (view: View): RecyclerView.ViewHolder(view){

    val binding=ItemProductoBinding.bind(view)

    fun render(productoModel:Producto, onClickListener: (Producto)->Unit){
        binding.tvNombre.text=productoModel.nombre
        binding.tvPrecio.text="S/. "+ productoModel.precio
        binding.tvStock.text= productoModel.stock
        val imagenes=productoModel.imagen.getJSONObject(0)
        val ruta=imagenes.getString("ruta")
        Glide.with(binding.ivImage.context).load("http://compra.regionmadrededios.gob.pe"+ruta).into(binding.ivImage)
        //binding.tvNombre.setOnClickListener { Toast.makeText(binding.tvNombre.context,productoModel.nombre , Toast.LENGTH_SHORT).show() }
        itemView.setOnClickListener { onClickListener(productoModel) }

    }
}