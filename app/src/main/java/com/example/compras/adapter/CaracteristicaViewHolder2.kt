package com.example.compras.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.compras.databinding.ItemCaracteristica2Binding
import com.example.compras.model.Caracteristica

class CaracteristicaViewHolder2 (view: View): RecyclerView.ViewHolder(view){

    val binding= ItemCaracteristica2Binding.bind(view)

    fun render(caracteristicaModel: Caracteristica, onClickListener: (Caracteristica)->Unit){

        var producto=caracteristicaModel.producto
        var imagenes=producto.getJSONArray("imagenes")
        var caracteristicas=producto.getJSONArray("caracteristicas")
        //setUpTabBar(caracteristicaModel.fragmentActivity)
        binding.tvcaracteristica3.text=caracteristicaModel.caracteristica
        itemView.setOnClickListener { onClickListener(caracteristicaModel) }

    }
}