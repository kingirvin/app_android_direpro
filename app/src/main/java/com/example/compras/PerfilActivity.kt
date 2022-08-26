package com.example.compras

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.compras.model.Mytoolbar
import org.json.JSONException
import com.example.compras.UserVipAplication.Companion.prefs
import com.example.compras.adapter.DepartamentoAdapter
import com.example.compras.databinding.ActivityPerfilBinding
import com.example.compras.model.Departamento
import org.json.JSONObject

class PerfilActivity : AppCompatActivity() {
    var dialog: ProgressDialog? = null
    private lateinit var binding: ActivityPerfilBinding
    private var departamento_id:String="0"
    private var provincia_id:String="0"
    private var distrito_id:String="0"
    private var nombreDepartamento:String=""
    private var  nombreProvincia:String=""
    private var  nombreDistrito:String=""

    private var departamentoInit_id:String="0"
    private var provinciaInit_id:String="0"
    private var distritoInit_id:String="0"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityPerfilBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Mytoolbar().show(this,"Comprale a la Madre de dios", true)
        init()
        llenarDepartamentos()
        llenarProvincia("1")
        llenarDistrito("1")
        binding.btnDepartamento.setOnClickListener(){
            binding.spDepartamentoPerfil.visibility=View.VISIBLE
            binding.spProvinciaPerfil.visibility=View.VISIBLE
            binding.spDistritoPerfil.visibility=View.VISIBLE
            binding.tvDepartamentoPerfil.visibility=View.GONE
            binding.tvProvinciaPerfil.visibility=View.GONE
            binding.tvDistritoPerfil.visibility=View.GONE
            binding.btnDepartamento.visibility=View.GONE
            binding.btnCancelar.visibility=View.VISIBLE

        }
        binding.btnCancelar.setOnClickListener(){
            binding.spDepartamentoPerfil.visibility=View.GONE
            binding.spProvinciaPerfil.visibility=View.GONE
            binding.spDistritoPerfil.visibility=View.GONE
            binding.btnCancelar.visibility=View.GONE


            binding.tvDepartamentoPerfil.text=nombreDepartamento
            binding.tvProvinciaPerfil.text=nombreProvincia
            binding.tvDistritoPerfil.text=nombreDistrito

            provincia_id=provinciaInit_id
            departamento_id=departamentoInit_id
            distrito_id=distritoInit_id

            binding.tvDepartamentoPerfil.visibility=View.VISIBLE
            binding.tvProvinciaPerfil.visibility=View.VISIBLE
            binding.tvDistritoPerfil.visibility=View.VISIBLE
            binding.btnDepartamento.visibility=View.VISIBLE

        }
        binding.btnGuardar.setOnClickListener {
            actualizarPerfil()
        }
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
    private fun init(){

        val url="http://compra.regionmadrededios.gob.pe/app/${prefs.getId()}/usuario"
        val jsonRequest = object : JsonObjectRequest(
            Request.Method.GET, url, null,
            Response.Listener { response ->
                try {
                    val responseObj = response
                    val usuario=responseObj.getJSONObject("user")
                    binding.etNombrePerfil.setText(usuario.getString("nombre"))
                    binding.etaMaterno.setText(usuario.getString("amaterno"))
                    binding.etaPaterno.setText(usuario.getString("apaterno"))
                    if (usuario.getString("dni") != "null")
                        binding.etDni.setText(usuario.getString("dni"))
                    if (usuario.getString("telefono") != "null")
                        binding.etTelefonoPerfil.setText(usuario.getString("telefono"))
                    if (usuario.getString("direccion") != "null")
                        binding.etDireccionPerfil.setText(usuario.getString("direccion"))
                    if (usuario.getString("departamento_id") != "null"){
                        val departamento=usuario.getJSONObject("departamento")
                        nombreDepartamento=departamento.getString("nombre")
                        departamentoInit_id=departamento.getString("id")
                        binding.tvDepartamentoPerfil.setText(departamento.getString("nombre"))
                    }
                    if (usuario.getString("provincia_id") != "null"){
                        val provincia=usuario.getJSONObject("provincia")
                        nombreProvincia=provincia.getString("nombre")
                        provinciaInit_id=provincia.getString("id")
                        binding.tvProvinciaPerfil.setText(provincia.getString("nombre"))
                    }
                    if (usuario.getString("distrito_id") != "null"){
                        val distrito=usuario.getJSONObject("distrito")
                        //mensaje(distrito.getString("nombre"))
                        nombreDistrito=distrito.getString("nombre")
                        distritoInit_id=distrito.getString("id")
                        binding.tvDistritoPerfil.setText(distrito.getString("nombre"))
                    }

                } catch (e: JSONException) {
                    e.printStackTrace()
                    mensajeToas("error")
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
    private fun actualizarPerfil(){
        val itemsObject = JSONObject()
        itemsObject.put("id", prefs.getId())
        itemsObject.put("nombre", binding.etNombrePerfil.text)
        itemsObject.put("apaterno",binding.etaPaterno.text)
        itemsObject.put("amaterno",binding.etaMaterno.text)
        itemsObject.put("dni",binding.etDni.text)
        itemsObject.put("telefono",binding.etTelefonoPerfil.text)
        itemsObject.put("departamento",departamento_id)
        itemsObject.put("provincia",provincia_id)
        itemsObject.put("distrito",distrito_id)
        itemsObject.put("direccion",binding.etDireccionPerfil.text)
        val  url="http://compra.regionmadrededios.gob.pe/app/actualizarPerfil"
        mensaje("Procesando pedido")
        val jsonRequest = object : JsonObjectRequest(
            Request.Method.POST, url, itemsObject,
            Response.Listener { response ->
                try {
                    //mensaje(response.toString())
                    val message=response.getString("message")
                    mensajeToas(message)

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
    }
    private fun llenarDepartamentos(){
        val url="http://compra.regionmadrededios.gob.pe/app/listaDepartamento"
        val list: MutableList<Departamento> = ArrayList()
        mensaje("Cargando informacion")
        val jsonRequest = object : JsonObjectRequest(
            Request.Method.GET, url, null,
            Response.Listener { response ->
                try {
                    val responseObj = response
                    val departamentos=responseObj.getJSONArray("departamentos")
                    for (i in 0.. departamentos.length()-1){
                        val item=departamentos.getJSONObject(i)
                        val depa= Departamento(item.getString("id"),item.getString("nombre"))
                        list.add(depa)
                    }
                    var adapter= DepartamentoAdapter(this,list)
                    binding.spDepartamentoPerfil.adapter=adapter
                    binding.spDepartamentoPerfil.onItemSelectedListener=object :
                        AdapterView.OnItemSelectedListener{
                        override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                            departamento_id=list[position].id
                            llenarProvincia(list[position].id)
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
                        val provin= Departamento(item.getString("id"),item.getString("nombre"))
                        list.add(provin)
                    }
                    var adapter= DepartamentoAdapter(this,list)
                    binding.spProvinciaPerfil.adapter=adapter

                    binding.spProvinciaPerfil.onItemSelectedListener=object :
                        AdapterView.OnItemSelectedListener{
                        override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                            provincia_id=list[position].id
                            llenarDistrito(list[position].id)
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
                        val dist= Departamento(item.getString("id"),item.getString("nombre"))
                        list.add(dist)
                    }
                    var adapter= DepartamentoAdapter(this,list)
                    binding.spDistritoPerfil.adapter=adapter
                    binding.spDistritoPerfil.onItemSelectedListener=object :
                        AdapterView.OnItemSelectedListener{
                        override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                            distrito_id=list[position].id
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