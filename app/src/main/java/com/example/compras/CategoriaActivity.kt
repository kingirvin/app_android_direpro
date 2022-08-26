package com.example.compras

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.compras.adapter.CategoriaAdapter
import com.example.compras.adapter.ProductoAdapter
import com.example.compras.databinding.ActivityCategoriaBinding
import com.example.compras.model.Categoria
import com.example.compras.model.Mytoolbar
import org.json.JSONException
import org.json.JSONObject

class CategoriaActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCategoriaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityCategoriaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val bundle=intent.extras
        val id=bundle?.getString("id")
        Mytoolbar().show(this,"Comprale a la Madre de dios", true)

        initRecyclerView(id)
        //setContentView(R.layout.activity_categoria)
    }
    //Muestra el listado de Productos segun categoria
    var dialog: ProgressDialog? = null
    private fun initRecyclerView(id:String?){
        dialog = ProgressDialog.show(this, "", "Ingresando", true);
        val itemsObject = JSONObject()
        itemsObject.put("id", id)
        val url="http://compra.regionmadrededios.gob.pe/app/"+id+"/listaProductosCategoria"
        val jsonRequest = object : JsonObjectRequest(
            Request.Method.GET, url, null,
            Response.Listener { response ->
                try {
                    val responseObj = response
                    val data=responseObj.getJSONObject("data")
                    val arrayProductos=data.getJSONArray("productos")
                    val itemsProductos:MutableList<Producto> = ArrayList()
                    for (i in 0 until arrayProductos.length()){
                        val arrayProducto=arrayProductos.getJSONObject(i)
                        val producto=Producto(arrayProducto.getString("id"),arrayProducto.getString("nombre"),
                            arrayProducto.getString("precio"),arrayProducto.getString("stock"),arrayProducto.getString("producto_categoria_id"),
                            arrayProducto.getString("producto_unidad_id"),
                            "","","","","","","","","","","","","","",
                            arrayProducto.getJSONArray("imagenes"),arrayProducto.getJSONObject("unidad"),arrayProducto.getJSONObject("categoria"),
                            arrayProducto.getJSONObject("categoria"),arrayProducto.getJSONObject("categoria"),arrayProducto.getJSONObject("categoria")
                        )
                        itemsProductos.add(producto)
                    }
                    val manager= GridLayoutManager(this,2)
                    binding.recycleProductoCategoria.layoutManager=manager
                    binding.recycleProductoCategoria.adapter= ProductoAdapter(itemsProductos,{ producto ->
                        onItemSelectedProducto(producto)
                    })
                    dialog?.dismiss()
                } catch (e: JSONException) {
                    dialog?.dismiss()
                    Toast.makeText(this, "error", Toast.LENGTH_SHORT).show()
                    e.printStackTrace()
                }
            },
            Response.ErrorListener { error ->
                //dialog?.dismiss()
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
    fun onItemSelectedProducto(producto: Producto){
        val intento = Intent(this, DetallesActivity::class.java)
        intento.putExtra("id",producto.id)
        startActivity(intento)
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
}