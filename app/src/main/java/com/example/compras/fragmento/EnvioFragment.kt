package com.example.compras.fragmento

import android.app.Activity
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.compras.PedidosActivity
import com.example.compras.Producto
import com.example.compras.R
import com.example.compras.adapter.CategoriaAdapter
import com.example.compras.adapter.DepartamentoAdapter
import com.example.compras.adapter.ProductoAdapter
import com.example.compras.databinding.ActivityPedidosBinding
import com.example.compras.model.Categoria
import com.example.compras.model.Departamento
import org.imaginativeworld.whynotimagecarousel.model.CarouselItem
import org.imaginativeworld.whynotimagecarousel.model.CarouselType
import org.json.JSONException
import java.util.zip.Inflater

class EnvioFragment() : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_envio, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //var spinner=view!!.findViewById<Spinner>(R.id.spDepartamento)



        //Toast.makeText(context, "departamentos.length().toString()", Toast.LENGTH_SHORT).show()
        //llenarDepartamento(spinner)
    }

    private fun llenarDepartamento(sniper:Spinner){
        val url="http://compra.regionmadrededios.gob.pe/app/listaDepartamento"
        val jsonRequest = object : JsonObjectRequest(
            Request.Method.GET, url, null,
            Response.Listener { response ->
                try {
                    Toast.makeText(context, ".gfdgdfgdfgdf()", Toast.LENGTH_SHORT).show()

                    val responseObj = response
                    val departamentos=responseObj.getJSONArray("departamentos")
                    val list:MutableList<Departamento> = ArrayList()
                    Toast.makeText(context, departamentos.length().toString(), Toast.LENGTH_SHORT).show()
                    for (i in 0..departamentos.length()-1){
                        val departamento=departamentos.getJSONObject(i)
                        val nombre=departamento.getString("nombre")
                        val id=departamento.getString("id")
                        val depa=Departamento(id,nombre)
                        list.add(depa)
                    }
                    val adapter= DepartamentoAdapter(requireContext() ,list)
                    sniper.adapter=adapter
                    Toast.makeText(context, "termino", Toast.LENGTH_SHORT).show()


                } catch (e: JSONException) {
                    //dialog?.dismiss()
                    e.printStackTrace()
                    Toast.makeText(context, "depa.toString()", Toast.LENGTH_SHORT).show()
                }
            },
            Response.ErrorListener { error ->
                //dialog?.dismiss()
                Toast.makeText(context,"Error al ingresar", Toast.LENGTH_LONG).show()
            }) {
            @Throws(AuthFailureError::class)
            override fun getBodyContentType(): String {
                return "application/json"
            }
            override fun getHeaders(): Map<String, String> {
                val apiHeader = HashMap<String, String>()
                apiHeader["Authorization"] = "Bearer $"
                return apiHeader
            }
        }
        val queue = Volley.newRequestQueue(context)
        queue.add(jsonRequest)
    }

}