package com.example.compras

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.TypedValue
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.example.compras.adapter.CaracteristicaAdapter
import com.example.compras.databinding.ActivityDetallesBinding
import com.example.compras.databinding.ActivityEmpresaBinding
import com.example.compras.model.Caracteristica
import com.example.compras.model.Mytoolbar
import org.json.JSONException

class EmpresaActivity : AppCompatActivity() {
    var dialog: ProgressDialog? = null
    private lateinit var binding: ActivityEmpresaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityEmpresaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val bundle=intent.extras
        val id=bundle?.getString("id")
        Mytoolbar().show(this,"Comprale a la Madre de dios", true)
        initRecyclerView(id)
    }
    private fun initRecyclerView(id:String?){
        mensaje("Cargando datos")
        val url="http://compra.regionmadrededios.gob.pe/app/"+id+"/empresa"
        val jsonRequest = object : JsonObjectRequest(
            Request.Method.GET, url, null,
            Response.Listener { response ->
                try {
                    val responseObj = response
                    val empresa=responseObj.getJSONObject("empresa")
                    binding.tvNombreEmpresa.text=empresa.getString("nombre")
                    binding.tvRucEmresa.text=empresa.getString("ruc")
                    binding.tvRubro.text=empresa.getString("rubro")
                    if (!empresa.getString("descripcion").isNullOrEmpty())
                        binding.tvdescripcionEmpresa.text=empresa.getString("descripcion")
                    var textView:TextView

                    if (!empresa.getString("telefono").isNullOrEmpty() && empresa.getString("foto") != "null"){
                        textView = TextView(this)
                        //textView.textSize=16f
                        textView.layoutParams= ConstraintLayout.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT,)
                        textView.setText("Telefono: "+empresa.getString("telefono"))
                        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15f)
                        textView.setPadding(16,16,16,0)
                        binding.lyContenedorEmpresa.addView(textView)
                    }
                    if (!empresa.getString("correo").isNullOrEmpty() && empresa.getString("correo") != "null"){
                        textView = TextView(this)
                        //textView.textSize=16f
                        textView.layoutParams= ConstraintLayout.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT,)
                        textView.setText("Correo: "+empresa.getString("correo"))
                        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15f)
                        textView.setPadding(16,16,16,0)
                        binding.lyContenedorEmpresa.addView(textView)
                    }
                    if (!empresa.getString("web" ).isNullOrEmpty() && empresa.getString("web") != "null"){
                        textView = TextView(this)
                        //textView.textSize=16f
                        textView.layoutParams= ConstraintLayout.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT,)
                        textView.setText("Web: "+empresa.getString("web"))
                        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15f)
                        textView.setPadding(16,16,16,0)
                        binding.lyContenedorEmpresa.addView(textView)
                    }
                    if (!empresa.getString("facebook").isNullOrEmpty() && empresa.getString("facebook") != "null"){
                        textView = TextView(this)
                        //textView.textSize=16f
                        textView.layoutParams= ConstraintLayout.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT,)
                        textView.setText("Facebook: "+empresa.getString("facebook"))
                        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15f)
                        textView.setPadding(16,16,16,0)
                        binding.lyContenedorEmpresa.addView(textView)
                    }
                    if (!empresa.getString("whatsapp").isNullOrEmpty() && empresa.getString("whatsapp") != "null"){
                        textView = TextView(this)
                        //textView.textSize=16f
                        textView.layoutParams= ConstraintLayout.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT,)
                        textView.setText("Shatsapp: "+empresa.getString("whatsapp"))
                        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15f)
                        textView.setPadding(16,16,16,0)
                        binding.lyContenedorEmpresa.addView(textView)
                    }
                    if (!empresa.getString("foto").isNullOrEmpty() && empresa.getString("foto") != "null"){
                        val ruta =empresa.getString("foto")
                        Glide.with(binding.ivEmpresa).load("http://compra.regionmadrededios.gob.pe/"+ruta).into(binding.ivEmpresa)
                    }
                    else{
                        Glide.with(binding.ivEmpresa).load("http://compra.regionmadrededios.gob.pe/img/no_picture.png").into(binding.ivEmpresa)
                    }

                    finMensaje()

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
    private fun mensaje(mensaje:String){
        dialog = ProgressDialog.show(this, "Mensaje", mensaje, true);
    }
    private fun mensajeToas(mensaje:String){
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
    }
    private fun finMensaje(){
        dialog?.dismiss()
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