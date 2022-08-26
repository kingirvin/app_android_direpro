package com.example.compras

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.compras.adapter.CaracteristicaAdapter
import com.example.compras.databinding.ActivityListaMainBinding
import com.example.compras.model.Caracteristica
import com.example.compras.UserVipAplication.Companion.prefs

import org.json.JSONException

class ListaMainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityListaMainBinding
    var dialog: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityListaMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
    private fun initRecyclerView(id:String?){

        val url="http://compra.regionmadrededios.gob.pe/app/${prefs.getId()}/listaPedidos"
        dialog = ProgressDialog.show(this, "Operacion","Cargando", true);
        val jsonRequest = object : JsonObjectRequest(
            Request.Method.GET, url, null,
            Response.Listener { response ->
                try {
                    val responseObj = response
                    val producto=responseObj.getJSONObject("productos")
                    val empresa=producto.getJSONObject("empresa")
                    val itemsCaracteristicas:MutableList<Caracteristica> = ArrayList()
                    var caracteristicas: Caracteristica
                    caracteristicas= Caracteristica("Certidica : ",producto.getString("certifica"),producto,this,this)
                    itemsCaracteristicas.add(caracteristicas)
                    binding.rvPedidos.layoutManager= LinearLayoutManager(this)
                    binding.rvPedidos.adapter= CaracteristicaAdapter(itemsCaracteristicas,{ caracteristicas ->
                    })
                    dialog?.dismiss()

                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            Response.ErrorListener { error ->
                Toast.makeText(this,"Error al ingresar", Toast.LENGTH_LONG).show()
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
        val queue = Volley.newRequestQueue(this)
        queue.add(jsonRequest)
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.salir->{
                UserVipAplication.prefs.remove()
                val intento1 = Intent(this, login::class.java)
                startActivity(intento1)
            }
            R.id.perfil->{
                val intento1 = Intent(this, PerfilActivity::class.java)
                startActivity(intento1)
            }
            R.id.pedidos->{
                val intento1 = Intent(this, ListaMainActivity::class.java)
                startActivity(intento1)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun mensaje(mensaje:String){
        dialog = ProgressDialog.show(this, "Mensaje", mensaje, true);
    }
    private fun mensajeToas(mensaje:String){
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
    }
    private fun finMensaje(){
        dialog?.dismiss()
    }
}