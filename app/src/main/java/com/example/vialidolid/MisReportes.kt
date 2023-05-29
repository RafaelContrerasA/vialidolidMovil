package com.example.vialidolid

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.vialidolid.databinding.ActivityMisReportesBinding
import org.json.JSONArray
import org.json.JSONException

class MisReportes : AppCompatActivity() {
    lateinit var falla: String
    lateinit var status: String
    lateinit var ubicacion: String
    lateinit var fecha: String
    lateinit var spinnerCategoria: Spinner

    private lateinit var reporteAdapter: ReportAPAAdapter
    private var listaReportes: MutableList<RAlumbradoP> = mutableListOf()
    private lateinit var reporteVAdapter: adapterVial
    private var listaReportesV: MutableList<dcReporteVial> = mutableListOf()

    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mis_reportes)

        reporteAdapter = ReportAPAAdapter(this, listaReportes)
        val layoutManager = LinearLayoutManager(this)
        val recyclerView = findViewById<RecyclerView>(R.id.lista)
        recyclerView.hasFixedSize()
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = reporteAdapter

        sharedPreferences = getSharedPreferences("usuario", Context.MODE_PRIVATE)

        val arrayCategoria = resources.getStringArray(R.array.categoria)
        spinnerCategoria = findViewById(R.id.spCat)
        val categoriaAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            arrayCategoria
        )
        spinnerCategoria.adapter = categoriaAdapter
        spinnerCategoria.setSelection(0)

        spinnerCategoria.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedItem = parent.getItemAtPosition(position).toString()
                when (selectedItem) {
                    "Alumbrado público" -> {
                        consultarReportesAP()
                    }
                    "Bache" -> {
                        consultarReportesBACHE()
                    }
                    "Maltrato animal" -> {
                        consultarReportesMaltrato()
                    }
                    "Reporte vial" -> {
                        consultarReportesOoapas()
                    }
                    "Suministro de agua" -> {
                        consultarReportesVial()
                    }
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                // Implementar el comportamiento cuando no se selecciona nada en el spinner
            }
        }
    }

    // Función para realizar la solicitud de consulta de reportes de alumbrado público
    private fun consultarReportesAP() {
        val queue = Volley.newRequestQueue(this)
        val idCiudadano = sharedPreferences.getString("uid", null)!!
        val tipoReporte = 1 // Valor fijo para el parámetro tipo_reporte
        val url = "http://192.168.1.64/001%20-%20Kotlin/consultaRepAP.php?id_ciudadano=$idCiudadano&tipo_reporte=$tipoReporte"
        val stringRequest = StringRequest(
            Request.Method.GET, url,
            { response ->
                Log.d("Response", response) // Imprimir la respuesta en el Logcat

                try {
                    val jsonArray = JSONArray(response)
                    /////////////////////////////////////////////////////////////////////////////
                    listaReportes.clear() // Limpiar la lista antes de agregar nuevos reportes
                    listaReportesV.clear()

                    for (i in 0 until jsonArray.length()) {
                        val jsonObject = jsonArray.getJSONObject(i)
                        falla = jsonObject.getString("descripcion")
                        status = jsonObject.getString("estatus")
                        var calle = jsonObject.getString("calle")
                        var colonia = jsonObject.getString("colonia")
                        fecha = jsonObject.getString("fecha")

                        val resultado: String = "$calle, $colonia"

                        val reporte = RAlumbradoP(falla, fecha, resultado, status)
                        listaReportes.add(reporte)
                    }
                    /////////////////////////////////////////////////////////////////////////////
                    reporteAdapter.notifyDataSetChanged()
                    /////////////////////////////////////////////////////////////////////////////
                } catch (e: JSONException) {
                    e.printStackTrace()
                    // Manejo de la excepción en caso de que la respuesta no sea un arreglo JSON válido
                }
            },
            { error ->
                // Manejo del error de la solicitud
            })
        queue.add(stringRequest)
    }
    //-------------------------------------------------------------------------------------------------------------------
    // Función para realizar la solicitud de consulta de reportes de alumbrado público
    private fun consultarReportesBACHE() {
        val queue = Volley.newRequestQueue(this)
        val idCiudadano = sharedPreferences.getString("uid", null)!!
        val tipoReporte = 2 // Valor fijo para el parámetro tipo_reporte
        val url = "http://192.168.1.64/001%20-%20Kotlin/consultaRepBache.php?id_ciudadano=$idCiudadano&tipo_reporte=$tipoReporte"
        val stringRequest = StringRequest(
            Request.Method.GET, url,
            { response ->
                Log.d("Response", response) // Imprimir la respuesta en el Logcat

                try {
                    val jsonArray = JSONArray(response)
                    // Resto del código para procesar la respuesta JSON
                    ///////////////////////////////////////////////////////////////////////////////
                    listaReportes.clear() // Limpiar la lista antes de agregar nuevos reportes
                    listaReportesV.clear()
                    for (i in 0 until jsonArray.length()) {

                        val jsonObject = jsonArray.getJSONObject(i)
                        falla = jsonObject.getString("descripcion")
                        status = jsonObject.getString("estatus")
                        var calle = jsonObject.getString("calle")
                        var colonia = jsonObject.getString("colonia")
                        fecha = jsonObject.getString("fecha")

                        val resultado: String = "$calle, $colonia"

                        val reporte = RAlumbradoP(falla, fecha, resultado, status)
                        listaReportes.add(reporte)
                    }

                    // Notificar al adaptador que los datos han cambiado
                    reporteAdapter.notifyDataSetChanged()
                    ///////////////////////////////////////////////////////////////////////////////
                } catch (e: JSONException) {
                    e.printStackTrace()
                    // Manejo de la excepción en caso de que la respuesta no sea un arreglo JSON válido
                }
            },
            { error ->
                // Manejo del error de la solicitud
            })
        queue.add(stringRequest)
    }
    //-----------------------------------------------------------------------------------------------------------------
// Función para realizar la solicitud de consulta de reportes de alumbrado público
    private fun consultarReportesMaltrato() {
        val queue = Volley.newRequestQueue(this)
        val idCiudadano = sharedPreferences.getString("uid", null)!!
        val tipoReporte = 3 // Valor fijo para el parámetro tipo_reporte
        val url = "http://192.168.1.64/001%20-%20Kotlin/consultaRepMAnimal.php?id_ciudadano=$idCiudadano&tipo_reporte=$tipoReporte"
        val stringRequest = StringRequest(
            Request.Method.GET, url,
            { response ->
                Log.d("Response", response) // Imprimir la respuesta en el Logcat

                try {
                    val jsonArray = JSONArray(response)
                    // Resto del código para procesar la respuesta JSON
                    ////////////////////////////////////////////////////////////////////////////////
                    listaReportes.clear() // Limpiar la lista antes de agregar nuevos reportes
                    listaReportesV.clear()
                    for (i in 0 until jsonArray.length()) {
                        val jsonObject = jsonArray.getJSONObject(i)
                        falla = jsonObject.getString("descripcion")
                        status = jsonObject.getString("estatus")
                        var calle = jsonObject.getString("calle")
                        var colonia = jsonObject.getString("colonia")
                        fecha = jsonObject.getString("fecha")

                        val resultado: String = "$calle, $colonia"

                        val reporte = RAlumbradoP(falla, fecha, resultado, status)
                        listaReportes.add(reporte)
                    }

                    // Notificar al adaptador que los datos han cambiado
                    reporteAdapter.notifyDataSetChanged()
                    ////////////////////////////////////////////////////////////////////////////////
                } catch (e: JSONException) {
                    e.printStackTrace()
                    // Manejo de la excepción en caso de que la respuesta no sea un arreglo JSON válido
                }
            },
            { error ->
                // Manejo del error de la solicitud
            })
        queue.add(stringRequest)
    }
    //------------------------------------------------------------------------------------------------------------------
// Función para realizar la solicitud de consulta de reportes de alumbrado público
    private fun consultarReportesOoapas() {
        val queue = Volley.newRequestQueue(this)
        val idCiudadano = sharedPreferences.getString("uid", null)!!
        val tipoReporte = 4 // Valor fijo para el parámetro tipo_reporte
        val url = "http://192.168.1.64/001%20-%20Kotlin/consultaRepOoapas.php?id_ciudadano=$idCiudadano&tipo_reporte=$tipoReporte"
        val stringRequest = StringRequest(
            Request.Method.GET, url,
            { response ->
                Log.d("Response", response) // Imprimir la respuesta en el Logcat

                try {
                    val jsonArray = JSONArray(response)
                    // Resto del código para procesar la respuesta JSON
                    ////////////////////////////////////////////////////////////////////////////////
                    listaReportes.clear() // Limpiar la lista antes de agregar nuevos reportes
                    listaReportesV.clear()

                    for (i in 0 until jsonArray.length()) {
                        val jsonObject = jsonArray.getJSONObject(i)
                        falla = jsonObject.getString("descripcion")
                        status = jsonObject.getString("estatus")
                        var calle = jsonObject.getString("calle")
                        var colonia = jsonObject.getString("colonia")
                        fecha = jsonObject.getString("fecha")

                        val resultado: String = "$calle, $colonia"

                        val reporte = RAlumbradoP(falla, fecha, resultado, status)
                        listaReportes.add(reporte)
                    }

                    // Notificar al adaptador que los datos han cambiado
                    reporteAdapter.notifyDataSetChanged()
                    ////////////////////////////////////////////////////////////////////////////////
                } catch (e: JSONException) {
                    e.printStackTrace()
                    // Manejo de la excepción en caso de que la respuesta no sea un arreglo JSON válido
                }
            },
            { error ->
                // Manejo del error de la solicitud
            })
        queue.add(stringRequest)
    }
    //----------------------------------------------------------------------------------------------------------------------
// Función para realizar la solicitud de consulta de reportes de alumbrado público
    fun consultarReportesVial() {
        val queue: RequestQueue = Volley.newRequestQueue(this)
        val idCiudadano: String = sharedPreferences.getString("uid", null)!!
        val tipoReporte = 5 // Valor fijo para el parámetro tipo_reporte
        val url = "http://192.168.1.64/001%20-%20Kotlin/consultaRepVial.php?id_ciudadano=$idCiudadano&tipo_reporte=$tipoReporte"
        val stringRequest = StringRequest(
            Request.Method.GET, url,
            { response ->
                Log.d("Response", response) // Imprimir la respuesta en el Logcat

                try {
                    val jsonArray = JSONArray(response)
                    // Resto del código para procesar la respuesta JSON
                    ////////////////////////////////////////////////////////////////////////////////
                    listaReportes.clear() // Limpiar la lista antes de agregar nuevos reportes
                    listaReportesV.clear()

                    for (i in 0 until jsonArray.length()) {
                        val jsonObject = jsonArray.getJSONObject(i)
                        falla = jsonObject.getString("descripcion")
                        status = jsonObject.getString("estatus")
                        var ubicacionV = ""
                        fecha = jsonObject.getString("fecha")

                        val reporte = RAlumbradoP(falla, fecha, ubicacionV, status)
                        listaReportes.add(reporte)
                    }

                    // Notificar al adaptador que los datos han cambiado
                    reporteAdapter.notifyDataSetChanged()
                    ////////////////////////////////////////////////////////////////////////////////
                } catch (e: JSONException) {
                    e.printStackTrace()
                    // Manejo de la excepción en caso de que la respuesta no sea un arreglo JSON válido
                }
            },
            { error ->
                // Manejo del error de la solicitud
            })
        queue.add(stringRequest)
    }


}