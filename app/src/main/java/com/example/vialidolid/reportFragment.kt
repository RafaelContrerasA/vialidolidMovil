package com.example.vialidolid

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class reportFragment(): BottomSheetDialogFragment() {
    private var tvCalle : TextView? = null
    private var tvDescripción : TextView? = null
    private var tvLikes : TextView? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_report, container, false)


        //Colocar el texto de la calle
        tvCalle=view.findViewById(R.id.tvCalle)
        var calle =arguments?.getInt("idReporte")
        tvCalle?.setText(calle.toString())
        println(calle)

        //Colocar el texto de la descripcion
        tvDescripción=view.findViewById(R.id.tvDescripcion)
        var descripcion = this.arguments?.getInt("tipoReporte")
        tvDescripción?.setText(descripcion.toString())
        println(descripcion)
        /*
        //Colocar cantidad de likes
        tvLikes=view.findViewById(R.id.tvLikes)
        var likes = this.arguments?.getString("Likes")
        tvLikes?.setText(likes)
        println(likes)*/


        return view
    }

}