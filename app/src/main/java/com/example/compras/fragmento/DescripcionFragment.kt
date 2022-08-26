package com.example.compras.fragmento

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.example.compras.R


class DescripcionFragment : Fragment() {

    //lateinit var nombre:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Toast.makeText(context, "fsdfsdf", Toast.LENGTH_SHORT).show()
        if(arguments != null){
            //nombre=arguments.getString("nombre","")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_descripcion, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
       // if (arguments != null) {
        //id=bundle?.getString("id")
           // var nombreFragment = requireArguments().getString("nombre")
            Toast.makeText(context, requireArguments().toString(), Toast.LENGTH_SHORT).show()

            //  requireArguments().getString("nombre")
            val tv: TextView = requireView().findViewById(R.id.tvdescripcion_)
            //tv.text = "hola " + nombreFragment
       // }
    }
}