package com.example.compras.model

import org.json.JSONArray
import org.json.JSONObject

data class Pedidos (
    var fecha:String,
    var envio:String,
    var cantidad:String,
    var total:String,
    var descripcion:String,
    var estado:String,
    var direccion:String,
    var user_id:String,
    var producto_id:String,
    var departamento_id:String,
    var provincia_id:String,
    var distrito_id:String,
    var empresa_id:String,
    var telefono:String,
    var fecha_limite:String,
    // variables extras

    val imagen: JSONArray,
    val producto: JSONObject
)
