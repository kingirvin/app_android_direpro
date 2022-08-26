package com.example.compras.adapter

import android.content.Context
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.compras.databinding.ItemCaracteristicasBinding
import com.example.compras.model.Caracteristica
import org.imaginativeworld.whynotimagecarousel.model.CarouselItem
import org.imaginativeworld.whynotimagecarousel.model.CarouselType
import org.json.JSONArray
import org.json.JSONObject

class CaracteristicaViewHolder (view: View): RecyclerView.ViewHolder(view){

    val binding= ItemCaracteristicasBinding.bind(view)

    fun render(caracteristicaModel: Caracteristica, onClickListener: (Caracteristica)->Unit){

        var producto=caracteristicaModel.producto
        var imagenes=producto.getJSONArray("imagenes")
        var caracteristicas=producto.getJSONArray("caracteristicas")
        imagenes(imagenes)
        mostrarProducto(producto)
        mostrarCaracteristocas(caracteristicas,producto,caracteristicaModel.context)
        itemView.setOnClickListener { onClickListener(caracteristicaModel) }

    }
    fun imagenes(arrayImagenes:JSONArray){
        val list= mutableListOf<CarouselItem>()
        for (i in 0 until arrayImagenes.length()){
            val imagenes=arrayImagenes.getJSONObject(i)
            list.add(CarouselItem("http://compra.regionmadrededios.gob.pe/"+imagenes.getString("ruta")))
        }
        binding.carouselDetalle2.addData(list)
        //propiedades del slider de imagenes
        binding.carouselDetalle2.addData(list)
        binding.carouselDetalle2.infiniteCarousel = true
        binding.carouselDetalle2.autoWidthFixing = true
        binding.carouselDetalle2.autoPlay = true
        binding.carouselDetalle2.autoPlayDelay = 6000 // Milliseconds
        binding.carouselDetalle2.carouselType = CarouselType.BLOCK
        binding.carouselDetalle2.scaleOnScroll = false
        binding.carouselDetalle2.scalingFactor = .15f
    }
    fun mostrarProducto(producto: JSONObject){

        binding.tvNombreProducto2.text=producto.getString("nombre")
        var empresa=producto.getJSONObject("empresa")
        binding.tvEmpresa2.text="Empresa: "+ empresa.getString("nombre")
        var descripcion=producto.getString("descripcion")
        if(producto.getString("descripcion").length > 100)
            descripcion=producto.getString("descripcion").substring(0,100)

        binding.tvEmpresa2.text="Empresa: "+ empresa.getString("nombre")
        binding.tvDescripcion2.text=descripcion
        binding.tvRuc2.text="RUC: "+ empresa.getString("ruc")
        binding.tvPrecioProducto2.text="Precio: S/. "+ producto.getString("precio")
        binding.tvStockProducto2.text="Stock: "+ producto.getString("stock")
        binding.tvDescripcionCompleta2.text=producto.getString("descripcion")
    }
    fun mostrarCaracteristocas(arrayCaracteristicas: JSONArray,producto: JSONObject,context: Context){

       var textView:TextView
        var caracteristica:Caracteristica
        if (producto.getString("certifica").isNotEmpty() && producto.getString("certifica") != "null"){
            textView =TextView(context)
            //textView.textSize=16f
            textView.layoutParams= ConstraintLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,)
            textView.setText("Certifica: "+producto.getString("certifica"))
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15f)
            textView.setPadding(16,0,0,0)
            binding.contenido.addView(textView)
        }
        if (producto.getString("dimensiones").isNotEmpty() && producto.getString("dimensiones") != "null"){
            textView =TextView(context)
            //textView.textSize=16f
            textView.layoutParams= ConstraintLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,)
            textView.setText("Dimensiones: "+producto.getString("dimensiones"))
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15f)
            textView.setPadding(16,0,0,0)
            binding.contenido.addView(textView)
        }
        if (producto.getString("peso").isNotEmpty() && producto.getString("peso") != "null"){
            textView =TextView(context)
            //textView.textSize=16f
            textView.layoutParams= ConstraintLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,)
            textView.setText("Peso: "+producto.getString("peso"))
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15f)
            textView.setPadding(16,0,0,0)
            binding.contenido.addView(textView)
        }
        for (i in 0 until  arrayCaracteristicas.length()){
            val tem=arrayCaracteristicas.getJSONObject(i)
            if(tem.getString("descripcion").isNotEmpty()) {
                textView =TextView(context)
                //textView.textSize=16f
                textView.layoutParams= ConstraintLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,)
                textView.setText(tem.getString("nombre_caracteristica")+": "+tem.getString("descripcion"))
                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15f)
                textView.setPadding(16,0,0,0)
                binding.contenido.addView(textView)
                //caracteristica= Caracteristica(tem.getString("nombre_caracteristica"),tem.getString("descripcion"))
                //itemsCaracteristicas.add(caracteristica)
            }
        }
    }

    /*fun setUpTabBar(fragmentActivity: FragmentActivity){
        val adapter=TabPageAdapter(fragmentActivity,binding.tabLayout.tabCount)
        binding.viewPager.adapter=adapter
        binding.viewPager.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                binding.tabLayout.selectTab(binding.tabLayout.getTabAt(position))
            }
        })
        binding.tabLayout.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab) {
                binding.viewPager.currentItem=tab.position
            }
            override fun onTabReselected(tab: TabLayout.Tab) {
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }
        })
    }*/
}