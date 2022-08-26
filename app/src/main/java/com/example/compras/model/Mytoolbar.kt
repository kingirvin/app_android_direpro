package com.example.compras.model

import androidx.appcompat.app.AppCompatActivity
import com.example.compras.R

class Mytoolbar {
    fun show(activities:AppCompatActivity,title:String,upButton: Boolean){
        activities.setSupportActionBar(activities.findViewById(R.id.toolbar))
        activities.supportActionBar?.title=title
        activities.supportActionBar?.setDisplayHomeAsUpEnabled(upButton)
    }
}