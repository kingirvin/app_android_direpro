package com.example.compras

import android.app.Application

class UserVipAplication : Application(){
    companion object{
        lateinit var prefs:Prefs
    }
    override fun onCreate() {
        super.onCreate()
        prefs=Prefs(applicationContext)
    }
}

