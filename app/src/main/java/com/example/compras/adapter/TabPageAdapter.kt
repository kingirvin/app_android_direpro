package com.example.compras.adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.compras.fragmento.CalificacionFragment
import com.example.compras.fragmento.CaracteristicasFragment
import com.example.compras.fragmento.DescripcionFragment

class TabPageAdapter(activity:FragmentActivity,private val tabCount:Int,bundle: Bundle):FragmentStateAdapter(activity) {
    override fun getItemCount(): Int=tabCount

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0->DescripcionFragment()
            1->CaracteristicasFragment()
            2->CalificacionFragment()
            else->DescripcionFragment()
        }
    }
}