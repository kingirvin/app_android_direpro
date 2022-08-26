package com.example.compras

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.compras.model.Mytoolbar
import org.json.JSONException
import org.json.JSONObject

class Registro : AppCompatActivity() {
    private var registro:String="0"
    private var usuario=JSONObject()
    private val key="my_key"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)
        //obtenemos los datos
        val btnRegistro:Button=findViewById(R.id.btnRegistrarUsuario)
        //obtenemos el PreferenceManager
        Mytoolbar().show(this,"Comprale a la Madre de dios", true)
        btnRegistro.setOnClickListener {
            regsitrarUsuario()
        }

    }
    var dialog: ProgressDialog? = null
    fun regsitrarUsuario() {

        val btnRegistro:Button=findViewById(R.id.btnRegistrarUsuario)
        val tvtNombre=findViewById<TextView>(R.id.tvNombreUsuaro)
        val tvaPaterno=findViewById<TextView>(R.id.tvPapellido)
        val tvaMaterno=findViewById<TextView>(R.id.tvAapellido)
        val tvemail=findViewById<TextView>(R.id.tvCorreo)
        val tvDni=findViewById<TextView>(R.id.tvDni)
        val tvPassword=findViewById<TextView>(R.id.tvPasswordUsuario)
        //creamos un objeto
        val itemsObject = JSONObject()
        itemsObject.put("email", tvemail.text)
        itemsObject.put("password",tvPassword.text)
        itemsObject.put("nombre",tvtNombre.text)
        itemsObject.put("apaterno",tvaPaterno.text)
        itemsObject.put("amaterno",tvaMaterno.text)
        itemsObject.put("dni",tvDni.text)

        val prefs=PreferenceManager.getDefaultSharedPreferences(this)
        val url:String="http://compra.regionmadrededios.gob.pe/app/store"
        dialog = ProgressDialog.show(this, "", "Ingresando", true);
        val jsonRequest = object : JsonObjectRequest(
            Request.Method.POST, url, itemsObject,
            Response.Listener { response ->
                try {
                    dialog?.dismiss()
                    Toast.makeText(this,"Registro exitoso", Toast.LENGTH_LONG).show()
                    val responseObj = response
                    val data=responseObj.getJSONObject("data")
                    val estado=data.getString("estado")
                    if (estado.toString() == "1"){
                        registro="1"
                        usuario=data.getJSONObject("user")
                        val editor=prefs.edit()
                        editor.putString(key,usuario.getString("nombre").toString())
                        editor.apply()
                        enviarEmail(usuario)
                    }
                    else{
                        Toast.makeText(this,"Verifique los datos", Toast.LENGTH_LONG).show()
                    }
                    //if (response)
                } catch (e: JSONException) {
                    dialog?.dismiss()
                    e.printStackTrace()
                }
            },
            Response.ErrorListener { error ->
                dialog?.dismiss()
                Toast.makeText(this,"Error al registrar", Toast.LENGTH_LONG).show()
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
        val queue = Volley.newRequestQueue(this@Registro)
        queue.add(jsonRequest)
    }
    fun enviarEmail(itemsObject: JSONObject, ) {
            val url: String = "http://compra.regionmadrededios.gob.pe/app/email"
            dialog = ProgressDialog.show(this, "", "Ingresando", true);
            val jsonRequest = object : JsonObjectRequest(
                Request.Method.POST, url, itemsObject,
                Response.Listener { response ->
                    try {
                        dialog?.dismiss()
                        val intento1 = Intent(this, Productos::class.java)
                        startActivity(intento1)
                    } catch (e: JSONException) {
                        dialog?.dismiss()
                        e.printStackTrace()
                    }
                },
                Response.ErrorListener { error ->
                    dialog?.dismiss()
                    Toast.makeText(this, "Error al registrar email", Toast.LENGTH_LONG).show()
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
            val queue = Volley.newRequestQueue(this@Registro)
            queue.add(jsonRequest)
    }
}