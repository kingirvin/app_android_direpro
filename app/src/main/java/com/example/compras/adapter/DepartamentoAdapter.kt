package com.example.compras.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.compras.R
import com.example.compras.model.Departamento

class DepartamentoAdapter(context: Context,list: List<Departamento>):ArrayAdapter<Departamento>(context,0,list) {

    private var layoutInflater= LayoutInflater.from(context)
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view:View=layoutInflater.inflate(R.layout.item_sniper,null,true)
        return myView(position,view,parent)
        //return super.getView(position, convertView, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        var cv=convertView
        if (cv==null)
            cv=layoutInflater.inflate(R.layout.item_sniper,null,false)
        return myView(position,cv,parent)
        return super.getDropDownView(position, convertView, parent)
    }
    private fun myView(position: Int, convertView: View?, parent: ViewGroup): View {
        val list=getItem(position)?:return convertView!!
        val txt=convertView!!.findViewById<TextView>(R.id.tvDepartamento)
        txt.text=list.nombre
        return convertView
    }
}