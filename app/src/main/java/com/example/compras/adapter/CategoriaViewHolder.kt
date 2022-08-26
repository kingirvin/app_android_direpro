package com.example.compras.adapter

import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.compras.databinding.ItemCategoriaBinding
import com.example.compras.model.Categoria

class CategoriaViewHolder (view: View): RecyclerView.ViewHolder(view){

    val binding= ItemCategoriaBinding.bind(view)

    fun render(categoriaModel: Categoria, onClickListener: (Categoria)->Unit){
        binding.tvNombreCategoria.text=categoriaModel.nombre
        Glide.with(binding.ivcategoria.context).load("http://compra.regionmadrededios.gob.pe"+categoriaModel.imagen).into(binding.ivcategoria)

        //binding.tvNombreCategoria.setOnClickListener { Toast.makeText(binding.tvNombreCategoria.context,categoriaModel.imagen , Toast.LENGTH_SHORT).show() }
        itemView.setOnClickListener { onClickListener(categoriaModel) }

    }
}