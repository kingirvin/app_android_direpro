package com.example.compras

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.core.widget.addTextChangedListener
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.example.compras.adapter.DepartamentoAdapter
import com.example.compras.databinding.ActivityPedidosBinding
import com.example.compras.model.Departamento
import org.json.JSONException
import com.example.compras.UserVipAplication.Companion.prefs
import com.example.compras.model.Mytoolbar
import org.json.JSONObject

class PedidosActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPedidosBinding
    var dialog: ProgressDialog? = null
    //variables
    private  var departamento:String=""
    private  var provincia:String=""
    private  var distrito:String=""
    private  var empresa_id:String=""
    private  var user_id:String=prefs.getId()
    private  var producto_id:String=""
    private  var envio:String=""
    private  var total:String="0"
    private  var precio:Int=0
    private  var cantidad:Int=0
    private  var direccion:String=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityPedidosBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val bundle=intent.extras
        var id=bundle?.getString("id")
        Mytoolbar().show(this,"Comprale a la Madre de dios", true)
//user_id= prefs.getId()
        llenarDepartamentos()
        binding.etCantidad.addTextChangedListener {
            if(binding.etCantidad.text.isNullOrEmpty())
                cantidad=0
            else
                cantidad=binding.etCantidad.text.toString().toInt()
            //mensaje("cambio")
            calcularTotal()
            //finMensaje()
        }
        binding.btnPedir.setOnClickListener {
            nuevoPedido()
        }
        pedido(id)
        llenarSpinner()
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
    private fun pedido(id:String?){
        val url="http://compra.regionmadrededios.gob.pe/app/$id/producto"
        val list: MutableList<Departamento> = ArrayList()
        mensaje("Cargadndo datos")
        val jsonRequest = object : JsonObjectRequest(
            Request.Method.GET, url, null,
            Response.Listener { response ->
                try {
                    val responseObj = response
                    val producto=responseObj.getJSONObject("productos")
                    val empresa=producto.getJSONObject("empresa")
                    val imagenes=producto.getJSONArray("imagenes")
                    binding.tvNombreProductoPedido.text=producto.getString("nombre")
                    binding.tvDireccionRecojo.text=empresa.getString("direccion")
                    binding.tvPrecioPedido.text="Precio: "+ producto.getString("precio")
                    producto_id=producto.getString("id")
                    val temPrecio=producto.getInt("precio")
                    precio=temPrecio
                    empresa_id=empresa.getString("id")
                    direccion =empresa.getString("direccion")
                    val imagen=imagenes.getJSONObject(0)
                    val ruta=imagen.getString("ruta")
                    Glide.with(binding.ivPedido).load("http://compra.regionmadrededios.gob.pe"+ruta).into(binding.ivPedido)

                    var adapter=DepartamentoAdapter(this,list)
                    binding.spDepartamento.adapter=adapter
                    binding.spDepartamento.onItemSelectedListener=object :
                        AdapterView.OnItemSelectedListener{
                        override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                            Toast.makeText(this@PedidosActivity, list[position].id, Toast.LENGTH_SHORT).show()
                        }
                        override fun onNothingSelected(p0: AdapterView<*>?) {

                        }
                    }
                    finMensaje()
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
    private fun llenarSpinner(){
        val lista= resources.getStringArray(R.array.opciones_entrega)
        val adaptador=ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item,lista)
        binding.spEntrega.adapter=adaptador
        binding.spEntrega.onItemSelectedListener=object :
            AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    if (lista[p2] == "Recojo en Tienda"){
                        envio="Recojo en Tienda"
                        val tempEnvio=findViewById<LinearLayout>(R.id.envio)
                        val recojo=findViewById<LinearLayout>(R.id.recojo)
                        recojo.visibility=View.VISIBLE
                        tempEnvio.visibility=View.GONE
                    }
                    else{
                        envio="Envio a domicilio"
                        val tempEnvio=findViewById<LinearLayout>(R.id.envio)
                        val recojo=findViewById<LinearLayout>(R.id.recojo)
                        recojo.visibility=View.GONE
                        tempEnvio.visibility=View.VISIBLE

                    }
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

        }
    }
    private fun llenarDepartamentos(){
        val url="http://compra.regionmadrededios.gob.pe/app/listaDepartamento"
        val list: MutableList<Departamento> = ArrayList()
        val jsonRequest = object : JsonObjectRequest(
            Request.Method.GET, url, null,
            Response.Listener { response ->
                try {
                    val responseObj = response
                    val departamentos=responseObj.getJSONArray("departamentos")
                    for (i in 0.. departamentos.length()-1){
                        val item=departamentos.getJSONObject(i)
                        val depa=Departamento(item.getString("id"),item.getString("nombre"))
                        list.add(depa)
                    }
                    var adapter=DepartamentoAdapter(this,list)
                    binding.spDepartamento.adapter=adapter
                    binding.spDepartamento.onItemSelectedListener=object :
                        AdapterView.OnItemSelectedListener{
                        override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                            departamento=list[position].id
                            llenarProvincia(list[position].id)
                            //Toast.makeText(this@PedidosActivity, list[position].id, Toast.LENGTH_SHORT).show()
                        }
                        override fun onNothingSelected(p0: AdapterView<*>?) {

                        }

                    }

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
    private fun llenarProvincia(id:String){
        val url="http://compra.regionmadrededios.gob.pe/app/$id/listaProvincia"
        val list: MutableList<Departamento> = ArrayList()
        val jsonRequest = object : JsonObjectRequest(
            Request.Method.GET, url, null,
            Response.Listener { response ->
                try {
                    val responseObj = response
                    val departamentos=responseObj.getJSONArray("provincias")
                    for (i in 0.. departamentos.length()-1){
                        val item=departamentos.getJSONObject(i)
                        val provin=Departamento(item.getString("id"),item.getString("nombre"))
                        list.add(provin)
                    }
                    var adapter=DepartamentoAdapter(this,list)
                    binding.spProvincia.adapter=adapter
                    binding.spProvincia.onItemSelectedListener=object :
                        AdapterView.OnItemSelectedListener{
                        override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                            provincia=list[position].id
                            llenarDistrito(list[position].id)
                            //Toast.makeText(this@PedidosActivity, list[position].id, Toast.LENGTH_SHORT).show()
                        }
                        override fun onNothingSelected(p0: AdapterView<*>?) {

                        }

                    }

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
    private fun llenarDistrito(id:String){
        val url="http://compra.regionmadrededios.gob.pe/app/$id/listaDistrito"
        val list: MutableList<Departamento> = ArrayList()
        val jsonRequest = object : JsonObjectRequest(
            Request.Method.GET, url, null,
            Response.Listener { response ->
                try {
                    val responseObj = response
                    val departamentos=responseObj.getJSONArray("distritos")
                    for (i in 0.. departamentos.length()-1){
                        val item=departamentos.getJSONObject(i)
                        val dist=Departamento(item.getString("id"),item.getString("nombre"))
                        list.add(dist)
                    }
                    var adapter=DepartamentoAdapter(this,list)
                    binding.spDistrito.adapter=adapter
                    binding.spDistrito.onItemSelectedListener=object :
                        AdapterView.OnItemSelectedListener{
                        override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                            distrito=list[position].id
                        }
                        override fun onNothingSelected(p0: AdapterView<*>?) {

                        }

                    }

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
    private fun nuevoPedido(){
        val etCantidad=findViewById<TextView>(R.id.etCantidad).text
        cantidad=etCantidad.toString().toInt()
        if(envio=="Envio a domicilio")
            direccion=findViewById<TextView>(R.id.etDireccion).text.toString()
        val telefono=findViewById<TextView>(R.id.etTelefono)
        val  url="http://compra.regionmadrededios.gob.pe/app/nuevoPedido"
        val itemsObject = JSONObject()
        itemsObject.put("envio", envio)
        itemsObject.put("cantidad",cantidad.toString())
        itemsObject.put("direccion",direccion)
        itemsObject.put("telefono",telefono.text)
        itemsObject.put("departamento",departamento)
        itemsObject.put("provincia",provincia)
        itemsObject.put("distrito",distrito)
        itemsObject.put("empresa_id",empresa_id)
        itemsObject.put("user_id",user_id)
        itemsObject.put("producto_id",producto_id)
        itemsObject.put("total",total)
        mensaje("Procesando pedido")
        val jsonRequest = object : JsonObjectRequest(
            Request.Method.POST, url, itemsObject,
            Response.Listener { response ->
                try {
                    //mensaje(response.toString())
                        mensajeToas(response.toString())
                    val responseObj = response
                    //val data=responseObj.getString("mensaje")
                    //dialog?.dismiss()

                    //if (response)
                } catch (e: JSONException) {
                    //dialog?.dismiss()
                    mensajeToas("error try")
                    e.printStackTrace()
                }
            },
            Response.ErrorListener { error ->
                mensaje("Error JSON")
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
        finMensaje()
        mensajeToas("Pedido registrado con exito")
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
    private fun calcularTotal(){
        val tempTotal=(precio*cantidad).toDouble()
        total=tempTotal.toString()
        binding.tvTotal.text="Total : "+tempTotal.toString()
    }
}