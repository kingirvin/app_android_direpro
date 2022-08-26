package com.example.compras

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import androidx.recyclerview.widget.GridLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.compras.adapter.CaracteristicaAdapter
import com.example.compras.adapter.CaracteristicaAdapter2
import com.example.compras.adapter.ProductoAdapter
import com.example.compras.adapter.TabPageAdapter
import com.example.compras.databinding.ActivityDetalleBinding
import com.example.compras.model.Caracteristica
import com.example.compras.model.Mytoolbar
import com.google.android.material.tabs.TabLayout
import org.imaginativeworld.whynotimagecarousel.model.CarouselItem
import org.imaginativeworld.whynotimagecarousel.model.CarouselType
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class DetalleActivity : AppCompatActivity() {
    val list= mutableListOf<CarouselItem>()

    private lateinit var binding: ActivityDetalleBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityDetalleBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val bundle=intent.extras
        val id=bundle?.getString("id")
        Mytoolbar().show(this,"Comprale a la Madre de dios", true)

        initRecyclerView(id)
        setUpTabBar("nombre")
    }
    var dialog: ProgressDialog? = null

    private fun initRecyclerView(id:String?){

        val url="http://compra.regionmadrededios.gob.pe/app/"+id+"/producto"
        //Toast.makeText(this, url, Toast.LENGTH_SHORT).show()
        dialog = ProgressDialog.show(this, "", url, true);
        val jsonRequest = object : JsonObjectRequest(
            Request.Method.GET, url, null,
            Response.Listener { response ->
                try {
                    val responseObj = response
                    val producto=responseObj.getJSONObject("productos")
                    val arrayImagenes=producto.getJSONArray("imagenes")
                    val arrayCaracteristicas=producto.getJSONArray("caracteristicas")
                    //recuperamos las caracteristicas y las imagenes
                    mostrarImagenes(arrayImagenes)
                    mostrarCaracteristocas(arrayCaracteristicas,producto)
                    mostrarProducto(producto)
                    dialog?.dismiss()

                } catch (e: JSONException) {
                    dialog?.dismiss()
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
    fun mostrarCaracteristocas(arrayCaracteristicas: JSONArray,producto: JSONObject){


        val itemsCaracteristicas:MutableList<Caracteristica> = ArrayList()
        var caracteristica:Caracteristica
        if (producto.getString("certifica").isNotEmpty()){
            caracteristica= Caracteristica("Certidica : ",producto.getString("certifica"),producto,this,this)
            itemsCaracteristicas.add(caracteristica)
        }
        if (producto.getString("dimensiones").isNotEmpty()){
            caracteristica= Caracteristica("Dimensiones : ",producto.getString("dimensiones"),producto,this,this)
            itemsCaracteristicas.add(caracteristica)
        }
        if (producto.getString("peso").isNotEmpty()){
            caracteristica= Caracteristica("Peso : ",producto.getString("peso"),producto,this,this)
            itemsCaracteristicas.add(caracteristica)
        }
        for (i in 0 until  arrayCaracteristicas.length()){
            val tem=arrayCaracteristicas.getJSONObject(i)
            if(tem.getString("descripcion").isNotEmpty()) {
                caracteristica= Caracteristica(tem.getString("nombre_caracteristica"),tem.getString("descripcion"),producto,this,this)
                itemsCaracteristicas.add(caracteristica)
            }
        }
        val manager= GridLayoutManager(this,2)
        binding.recyclarCaracteristicas.layoutManager=manager
        binding.recyclarCaracteristicas.adapter= CaracteristicaAdapter2(itemsCaracteristicas,{ caracteristicas ->
        })
    }
    fun mostrarImagenes(arrayImagenes: JSONArray){

        for (i in 0 until arrayImagenes.length()){
            val imagenes=arrayImagenes.getJSONObject(i)
            list.add(CarouselItem("http://compra.regionmadrededios.gob.pe/"+imagenes.getString("ruta")))
        }
        binding.carouselDetalle.addData(list)
        //propiedades del slider de imagenes
        binding.carouselDetalle.addData(list)
        binding.carouselDetalle.infiniteCarousel = true
        binding.carouselDetalle.autoWidthFixing = true
        binding.carouselDetalle.autoPlay = true
        binding.carouselDetalle.autoPlayDelay = 6000 // Milliseconds
        binding.carouselDetalle.carouselType = CarouselType.BLOCK
        binding.carouselDetalle.scaleOnScroll = false
        binding.carouselDetalle.scalingFactor = .15f
    }
    fun mostrarProducto(producto: JSONObject){

        binding.tvNombreProducto.text=producto.getString("nombre")
        var empresa=producto.getJSONObject("empresa")
        binding.tvEmpresa.text="Empresa: "+ empresa.getString("nombre")
        var descripcion=producto.getString("descripcion")
        if(producto.getString("descripcion").length > 100)
            descripcion=producto.getString("descripcion").substring(0,100)

        binding.tvEmpresa.text="Empresa: "+ empresa.getString("nombre")
        binding.tvDescripcion.text=descripcion
        binding.tvRuc.text="RUC: "+ empresa.getString("ruc")
        binding.tvPrecioProducto.text="Precio: S/. "+ producto.getString("precio")
        binding.tvStockProducto.text="Stock: "+ producto.getString("stock")
        binding.tvDescripcionCompleta.text=producto.getString("descripcion")
    }
    fun setUpTabBar(producto: String){
        val bundle=Bundle()
        bundle.putString("nombre","nombre")
        val adapter=TabPageAdapter(this,binding.tabLayout.tabCount,bundle)
        binding.viewPager.adapter=adapter
        binding.viewPager.registerOnPageChangeCallback(object:ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                binding.tabLayout.selectTab(binding.tabLayout.getTabAt(position))
            }
        })
        binding.tabLayout.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab) {
                binding.viewPager.currentItem=tab.position
            }
            override fun onTabReselected(tab: TabLayout.Tab) {
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }
        })
    }
}