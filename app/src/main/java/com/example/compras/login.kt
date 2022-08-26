package com.example.compras

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.compras.UserVipAplication.Companion.prefs
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.lang.Exception

class login : AppCompatActivity() {
    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        initUI()
        val btnRegister: Button = findViewById(R.id.btnRegistrar)
        btnRegister.setOnClickListener { view ->
            val intento1 = Intent(this, Registro::class.java)
            startActivity(intento1)
        }
        val btnIngresar: Button = findViewById(R.id.btnIngresar)
        // Register the onClick listener with the implementation above
        btnIngresar.setOnClickListener { view ->
            jsonArrayRequestPost()
        }
    }
    var dialog: ProgressDialog? = null
    fun jsonArrayRequestPost() {
        val txtUsuario=findViewById<TextView>(R.id.txtUsuario)
        val txtPassword=findViewById<TextView>(R.id.txtPassword)
        val txtmensaje=findViewById<TextView>(R.id.lblMensaje)
        val  url="http://compra.regionmadrededios.gob.pe/app/login_app"
        val itemsObject = JSONObject()
        itemsObject.put("email", txtUsuario.text)
        itemsObject.put("password",txtPassword.text)
        dialog = ProgressDialog.show(this, "", "Ingresando", true);
        val jsonRequest = object : JsonObjectRequest(
            Request.Method.POST, url, itemsObject,

            Response.Listener { response ->
                try {
                    dialog?.dismiss()
                    val responseObj = response
                    val data=responseObj.getJSONObject("data")
                    val estado=data.getString("estado")
                    if (estado.toString() == "1"){
                        val user=data.getJSONObject("user")
                        prefs.saveName(user.getString("nombre"))
                        prefs.saveId(user.getString("id"))
                        val intento1 = Intent(this, Productos::class.java)
                        startActivity(intento1)
                    }
                    else{
                        Toast.makeText(this,"datos incorrectos",Toast.LENGTH_LONG).show()
                    }
                    //if (response)
                } catch (e: JSONException) {
                    dialog?.dismiss()

                    e.printStackTrace()
                }
            },
            Response.ErrorListener { error ->

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

        val queue = Volley.newRequestQueue(this@login)
        queue.add(jsonRequest)
    }
    fun initUI(){
        if(prefs.getName().isNotEmpty()){
            val intento1 = Intent(this, Productos::class.java)
            startActivity(intento1)
        }
    }
    fun accessToDetail(){
       //if ()

    }
}