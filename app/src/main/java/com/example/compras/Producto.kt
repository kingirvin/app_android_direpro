package com.example.compras

import org.json.JSONArray
import org.json.JSONObject

data class Producto (
    val id: String,
    val nombre:String,
    val precio: String,
    val stock:String,
    val producto_categoria_id: String,
    val producto_unidad_id: String,
//
    val descripcion: String,
    val empresa_id: String,
    val sku: String,
    val dimensiones: String,
    val peso: String,
    val estado: String,
    val user_id: String,
    val destacado: String,
    val certifica: String,
    val nro_certificado: String,
    val vigencia_certificado: String,
    val pedido_min: String,
    val pedido_max: String,
    val exporta: String,
    // realcione con otros tablas

    val imagen: JSONArray,
    val unidad: JSONObject,
    val categoria: JSONObject,
    val empresa: JSONObject,
    val producto_categoria : JSONObject,
    val producto_caracteristica: JSONObject
    )