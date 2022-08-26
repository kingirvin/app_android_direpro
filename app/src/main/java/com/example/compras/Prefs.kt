package com.example.compras

import android.content.Context

class Prefs(val contexto:Context) {
    val SHARE_NAME="Mydtb"
    val SHARE_USER_NAME="Username"
    val SHARE_ID_USER=""
    val storege=contexto.getSharedPreferences(SHARE_NAME,0)
    fun saveName(name:String){
        storege.edit().putString(SHARE_USER_NAME,name).apply()
    }
    fun saveId(name:String){
        storege.edit().putString(SHARE_ID_USER,name).apply()
    }
    fun getName():String{
        return storege.getString(SHARE_USER_NAME,"")!!
    }
    fun getId():String{
        return storege.getString(SHARE_ID_USER,"")!!
    }
    fun remove(){
        return storege.edit().clear().apply()
    }


}