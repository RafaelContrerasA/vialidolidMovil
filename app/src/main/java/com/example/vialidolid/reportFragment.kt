package com.example.vialidolid

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.json.JSONArray
import org.json.JSONException

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


        //find a los campos que se mostraran del reporte
        tvCalle=view.findViewById(R.id.tvCalle)
        tvDescripción=view.findViewById(R.id.tvDescripcion)
        tvLikes=view.findViewById(R.id.tvLikes)

        //Obtener
        var idReporte =arguments?.getInt("idReporte")
        var tipoReporte = this.arguments?.getInt("tipoReporte")

        obtenerDatosReporte(idReporte?:0,tipoReporte?:0)

        return view
    }


    private fun obtenerDatosReporte(idReporte: Int, tipoReporte: Int) {
        val url = "http://${resources.getString(R.string.server_ip)}/rest/obtenerDatosReportesMapa.php?id_reporte=$idReporte&tipo_reporte=$tipoReporte"

        val request = JsonArrayRequest(
            Request.Method.GET, url, null,
            Response.Listener<JSONArray> { response ->
                try {
                    // Iterar sobre los elementos de la respuesta JSON
                    for (i in 0 until response.length()) {
                        val jsonObject = response.getJSONObject(i)

                        // Obtener los datos del reporte con manejo de valores nulos
                        val idReporte = jsonObject.optInt("id_reporte")
                        val fecha = jsonObject.optString("fecha")
                        val descripcion = jsonObject.optString("descripcion")
                        val latitud = jsonObject.optDouble("latitud")
                        val longitud = jsonObject.optDouble("longitud")
                        val nApoyos = jsonObject.optInt("n_apoyos")
                        val estatus = jsonObject.optString("estatus")
                        val nDenuncias = jsonObject.optInt("n_denuncias")
                        val referencias = jsonObject.optString("referencias")
                        val idCiudadano = jsonObject.optString("id_ciudadano")
                        val tipoReporte = jsonObject.optString("tipo_reporte")
                        val idDependencia = jsonObject.optString("id_dependencia")
                        val colonia = jsonObject.optString("colonia")
                        val calle = jsonObject.optString("calle")

                        //Colocar los datos del reporte
                        if(colonia.toString()!="")
                            tvCalle?.setText(calle.toString()+", "+colonia.toString())
                        else
                            tvCalle?.setText(calle.toString())
                        tvDescripción?.setText(descripcion.toString())
                        tvLikes?.setText(nApoyos.toString())

                        /* Imprimir los datos del reporte
                        println("Reporte - ID: $idReporte")
                        println("Reporte - Fecha: $fecha")
                        println("Reporte - Descripción: $descripcion")
                        println("Reporte - Latitud: $latitud")
                        println("Reporte - Longitud: $longitud")
                        println("Reporte - Número de Apoyos: $nApoyos")
                        println("Reporte - Estatus: $estatus")
                        println("Reporte - Número de Denuncias: $nDenuncias")
                        println("Reporte - Referencias: $referencias")
                        println("Reporte - ID Ciudadano: $idCiudadano")
                        println("Reporte - Tipo de Reporte: $tipoReporte")
                        println("Reporte - ID Dependencia: $idDependencia")
                        println("Reporte - Colonia: $colonia")
                        println("Reporte - Calle: $calle")*/
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            Response.ErrorListener { error ->
                error.printStackTrace()
            })

        // Agregar la solicitud a la cola de Volley
        Volley.newRequestQueue(requireContext()).add(request)
    }

}