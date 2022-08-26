package com.example.compras.model

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import org.json.JSONObject

data class Caracteristica(
    var caracteristica: String,
    var descripcion:String,
    var producto:JSONObject,
    var context: Context,
    var fragmentActivity: FragmentActivity,
    //var bundle: Bundle
)