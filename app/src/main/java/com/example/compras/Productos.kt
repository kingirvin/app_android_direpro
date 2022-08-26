package com.example.compras

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.compras.UserVipAplication.Companion.prefs
import com.example.compras.adapter.ProductoAdapter
import com.example.compras.databinding.ActivityProductosBinding
import org.json.JSONException
import com.example.compras.adapter.CategoriaAdapter
import com.example.compras.model.Categoria
import com.example.compras.model.Mytoolbar
import org.imaginativeworld.whynotimagecarousel.model.CarouselItem
import org.imaginativeworld.whynotimagecarousel.model.CarouselType


class Productos : AppCompatActivity() {

    val list= mutableListOf<CarouselItem>()
    var dialog: ProgressDialog? = null

    private lateinit var binding: ActivityProductosBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_productos)
        binding= ActivityProductosBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Mytoolbar().show(this,"Comprale a la Madre de dios", false)

        dialog = ProgressDialog.show(this, "", "Ingresando", true);
        //crea el slider de eventos

        //ejecutar()

        // Muestra el lsitado de Productos
        initRecyclerView()

    }
    //Muestra el listado de Productos

    private fun initRecyclerView(){
        val url="http://compra.regionmadrededios.gob.pe/app/listaProductos"
        val jsonRequest = object : JsonObjectRequest(
            Request.Method.GET, url, null,
            Response.Listener { response ->
                try {
                    val responseObj = response
                    val data=responseObj.getJSONObject("data")
                    val arrayProductos=data.getJSONArray("productos")
                    val arrayCategorias=data.getJSONArray("categorias")
                    val arrayEventos=data.getJSONArray("eventos")

                    val itemsProductos:MutableList<Producto> = ArrayList()
                    val itemsCategorias:MutableList<Categoria> = ArrayList()
                    //val clave=responseObj.names()

                    for (i in 0 until arrayProductos.length()){

                        val arrayProducto=arrayProductos.getJSONObject(i)
                        //Toast.makeText(this,arrayProducto.toString(),Toast.LENGTH_SHORT).show()

                        val producto=Producto(arrayProducto.getString("id"),arrayProducto.getString("nombre"),
                            arrayProducto.getString("precio"),arrayProducto.getString("stock"),arrayProducto.getString("producto_categoria_id"),
                            arrayProducto.getString("producto_unidad_id"),
                            "","","","","","","","","","","","","","",
                            arrayProducto.getJSONArray("imagenes"),arrayProducto.getJSONObject("unidad"),arrayProducto.getJSONObject("categoria"),
                            arrayProducto.getJSONObject("categoria"),arrayProducto.getJSONObject("categoria"),arrayProducto.getJSONObject("categoria")
                        )
                        itemsProductos.add(producto)
                    }
                    val manager=GridLayoutManager(this,2)
                    binding.recycleProductos.layoutManager=manager
                    binding.recycleProductos.adapter=ProductoAdapter(itemsProductos,{producto ->
                        onItemSelectedProducto(producto)
                    })

                    for (i in 0 until arrayCategorias.length()){
                        val arrayCategoria=arrayCategorias.getJSONObject(i)
                        val categoria=Categoria(arrayCategoria.getString("id"),arrayCategoria.getString("nombre"),arrayCategoria.getString("imagen")
                        )
                        itemsCategorias.add(categoria)
                    }
                    //manager=GridLayoutManager(this,2,GridLayoutManager.HORIZONTAL)
                    binding.recyleCategorias.layoutManager=LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
                    binding.recyleCategorias.adapter=CategoriaAdapter(itemsCategorias,{ categoria ->
                        onItemSelectedCategoria(categoria)
                    })
                    // eventos
                    for (i in 0 until arrayEventos.length()){
                        val eventos=arrayEventos.getJSONObject(i)
                        list.add(CarouselItem("http://compra.regionmadrededios.gob.pe/"+eventos.getString("imagen")))
                    }
                    binding.carousel.addData(list)
                    binding.carousel.infiniteCarousel = true
                    binding.carousel.autoWidthFixing = true
                    binding.carousel.autoPlay = true
                    binding.carousel.autoPlayDelay = 3000 // Milliseconds
                    binding.carousel.carouselType = CarouselType.BLOCK
                    binding.carousel.scaleOnScroll = false
                    binding.carousel.scalingFactor = .15f


                    dialog?.dismiss()
                } catch (e: JSONException) {
                    //dialog?.dismiss()
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
    //Selecionale itemm
    fun onItemSelectedProducto(producto: Producto){
        val intento = Intent(this, DetallesActivity::class.java)
        intento.putExtra("id",producto.id)
        startActivity(intento)
    }
    fun onItemSelectedCategoria(categoria: Categoria){
        val intento = Intent(this, CategoriaActivity::class.java)
        intento.putExtra("id",categoria.id)
        startActivity(intento)
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.salir->{
                prefs.remove()
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
    //crear un la lista de
}