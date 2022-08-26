package com.example.compras

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.compras.adapter.CaracteristicaAdapter
import com.example.compras.adapter.TabPageAdapter
import com.example.compras.databinding.ActivityDetalleBinding
import com.example.compras.databinding.ActivityDetallesBinding
import com.example.compras.model.Caracteristica
import com.example.compras.model.Mytoolbar
import com.google.android.material.tabs.TabLayout
import org.imaginativeworld.whynotimagecarousel.model.CarouselItem
import org.imaginativeworld.whynotimagecarousel.model.CarouselType
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class DetallesActivity : AppCompatActivity() {
    val list= mutableListOf<CarouselItem>()
    private lateinit var binding: ActivityDetallesBinding
    private var empresa_id:String=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityDetallesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val bundle=intent.extras
        val id=bundle?.getString("id")
        Mytoolbar().show(this,"Comprale a la Madre de dios", true)

        initRecyclerView(id)
        botones(id)
    }
    var dialog: ProgressDialog? = null

    private fun initRecyclerView(id:String?){

        val url="http://compra.regionmadrededios.gob.pe/app/"+id+"/producto"
        dialog = ProgressDialog.show(this, "Operacion","Cargando", true);
        val jsonRequest = object : JsonObjectRequest(
            Request.Method.GET, url, null,
            Response.Listener { response ->
                try {
                    val responseObj = response
                    val producto=responseObj.getJSONObject("productos")
                    val empresa=producto.getJSONObject("empresa")
                    empresa_id=empresa.getString("id").toString()
                    val itemsCaracteristicas:MutableList<Caracteristica> = ArrayList()
                    var caracteristicas:Caracteristica
                    caracteristicas= Caracteristica("Certidica : ",producto.getString("certifica"),producto,this,this)
                    itemsCaracteristicas.add(caracteristicas)
                    binding.detalles.layoutManager=LinearLayoutManager(this)
                    binding.detalles.adapter= CaracteristicaAdapter(itemsCaracteristicas,{ caracteristicas ->
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

    private fun botones(id:String?){
        binding.btnPedido.setOnClickListener {
            val intento = Intent(this, PedidosActivity::class.java)
            intento.putExtra("id",id)
            startActivity(intento)
        }
        binding.btnVerEmpresa.setOnClickListener {
            val intento = Intent(this, EmpresaActivity::class.java)
            intento.putExtra("id",empresa_id)
            startActivity(intento)
        }
    }
}