package com.example.vialidolid

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.json.JSONArray
import org.json.JSONException
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions

class reportFragment(): BottomSheetDialogFragment() {
    private var tvCalle : TextView? = null
    private var tvDescripción : TextView? = null
    private var tvLikes : TextView? = null
    private var nApoyos=0

    private var liked = false
    private lateinit var likeButton: ImageButton

    //iniciar y obtener usuario del sharedPreference
    lateinit var sharedPreferences: SharedPreferences

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

        //iniciar y obtener usuario del sharedPreference
        sharedPreferences = requireContext().getSharedPreferences("usuario", Context.MODE_PRIVATE)

        //Obtener
        var idReporte =arguments?.getInt("idReporte")
        var tipoReporte = this.arguments?.getInt("tipoReporte")
        var idCiudadano = sharedPreferences.getString("uid", null)!!

        obtenerDatosReporte(idReporte?:0,tipoReporte?:0)





        likeButton = view.findViewById(R.id.likeButton)
        likeButton.setOnClickListener {
            if (liked == false) {
                println("Apenas daras like")
                guardarApoyo(idReporte?:0,idCiudadano)

            } else {
                println("Ya diste like")
                eliminarApoyo(idReporte?:0,idCiudadano)

            }
        }

        //BUscar si el usuario ya ha dado like al post anteriormente
        verificarApoyoPrevio(idReporte?:0,idCiudadano)

        val imageUrl = "https://ae01.alicdn.com/kf/Ha5718126b72049ee8002429a6e721d2bP/Peluche-de-ballena-asesina-para-ni-os-juguete-de-animales-de-peluche-Vida-del-oc-ano.jpg_.webp" // URL de la imagen que deseas mostrar

        val imageView = view.findViewById<ImageView>(R.id.ivReporte)

        /*val requestOptions = RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.NONE) // Opcional: desactiva la caché de Glide
            .skipMemoryCache(true) // Opcional: salta la caché de memoria de Glide
            .fitCenter()

        Glide.with(requireContext())
            .load(imageUrl)
            .apply(requestOptions)
            .into(imageView)*/
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
                        nApoyos = jsonObject.optInt("n_apoyos")
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

    fun verificarApoyoPrevio(idReporte: Int, idCiudadano: String) {
        // URL del script PHP
        val url = "http://${resources.getString(R.string.server_ip)}/rest/verificarApoyoPrevio.php?id_reporte=$idReporte&id_ciudadano=$idCiudadano"

        // Realizar la solicitud HTTP utilizando Volley
        val request = StringRequest(Request.Method.GET, url,
            Response.Listener { response ->
                // Mostrar la respuesta en un Toast
                if(response == "Hay apoyo"){
                    liked = true
                    likeButton.setBackgroundColor(getResources().getColor(R.color.light_blue))
                }
            },
            Response.ErrorListener { error ->
                // Manejar el error en caso de que ocurra
                Toast.makeText(requireContext(), "Error: " + error.message, Toast.LENGTH_SHORT).show()
            })

        // Agregar la solicitud a la cola de solicitudes de Volley
        Volley.newRequestQueue(context).add(request)
    }


    fun guardarApoyo(idReporte: Int, idCiudadano: String) {
        // URL del script PHP
        val url = "http://${resources.getString(R.string.server_ip)}/rest/nuevoApoyo.php?id_reporte=$idReporte&id_ciudadano=$idCiudadano"

        // Realizar la solicitud HTTP utilizando Volley
        val request = StringRequest(Request.Method.GET, url,
            Response.Listener { response ->
                println(response)
                // Mostrar la respuesta en un Toast
                if(response == "Apoyo Exitoso"){
                    liked = true
                    likeButton.setBackgroundColor(getResources().getColor(R.color.light_blue))
                    nApoyos+=1
                    tvLikes?.setText(nApoyos.toString())
                }
            },
            Response.ErrorListener { error ->
                // Manejar el error en caso de que ocurra
                Toast.makeText(requireContext(), "Error, intentelo mas tarde", Toast.LENGTH_SHORT).show()
            })

        // Agregar la solicitud a la cola de solicitudes de Volley
        Volley.newRequestQueue(context).add(request)
    }

    fun eliminarApoyo(idReporte: Int, idCiudadano: String) {
        // URL del script PHP
        val url = "http://${resources.getString(R.string.server_ip)}/rest/eliminarApoyo.php?id_reporte=$idReporte&id_ciudadano=$idCiudadano"

        // Realizar la solicitud HTTP utilizando Volley
        val request = StringRequest(Request.Method.GET, url,
            Response.Listener { response ->
                println(response)
                // Mostrar la respuesta en un Toast
                if(response == "Apoyo Eliminado"){
                    liked = false
                    likeButton.setBackgroundColor(getResources().getColor(R.color.dark_gray))
                    nApoyos-=1
                    tvLikes?.setText(nApoyos.toString())
                }
            },
            Response.ErrorListener { error ->
                // Manejar el error en caso de que ocurra
                Toast.makeText(requireContext(), "Error, intentelo mas tarde", Toast.LENGTH_SHORT).show()
            })

        // Agregar la solicitud a la cola de solicitudes de Volley
        Volley.newRequestQueue(context).add(request)
    }





}