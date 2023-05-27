package com.example.vialidolid

import ReportAPAAdapter
import adapterVial
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mis_reportes)

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
        val idCiudadano = 1 // Obtener el valor del ID del ciudadano desde alguna variable
        val tipoReporte = 1 // Valor fijo para el parámetro tipo_reporte
        val url = "http://192.168.1.64/001%20-%20Kotlin/consultaRepAP.php?id_ciudadano=$idCiudadano&tipo_reporte=$tipoReporte"
        val stringRequest = StringRequest(
            Request.Method.GET, url,
            { response ->
                Log.d("Response", response) // Imprimir la respuesta en el Logcat

                try {
                    val jsonArray = JSONArray(response)
                    // Resto del código para procesar la respuesta JSON
                    val listaRAP = mutableListOf<RAlumbradoP>()

                    for (i in 0 until jsonArray.length()) {
                        val jsonObject = jsonArray.getJSONObject(i)
                        falla = jsonObject.getString("descripcion")
                        status = jsonObject.getString("estatus")
                        var calle = jsonObject.getString("calle")
                        var colonia = jsonObject.getString("colonia")
                        fecha = jsonObject.getString("fecha")

                        val resultado: String = "$calle, $colonia"

                        val reporte = RAlumbradoP(falla, fecha, resultado, status)
                        listaRAP.add(reporte)
                    }

                    val adapter = ReportAPAAdapter(this, listaRAP)
                    val layoutManager = LinearLayoutManager(this)
                    val recyclerView = findViewById<RecyclerView>(R.id.lista)
                    recyclerView.hasFixedSize()
                    recyclerView.layoutManager = layoutManager
                    recyclerView.adapter = adapter
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
        val idCiudadano = 1 // Obtener el valor del ID del ciudadano desde alguna variable
        val tipoReporte = 2 // Valor fijo para el parámetro tipo_reporte
        val url = "http://192.168.1.64/001%20-%20Kotlin/consultaRepBache.php?id_ciudadano=$idCiudadano&tipo_reporte=$tipoReporte"
        val stringRequest = StringRequest(
            Request.Method.GET, url,
            { response ->
                Log.d("Response", response) // Imprimir la respuesta en el Logcat

                try {
                    val jsonArray = JSONArray(response)
                    // Resto del código para procesar la respuesta JSON
                    val listaRAP = mutableListOf<RAlumbradoP>()

                    for (i in 0 until jsonArray.length()) {
                        val jsonObject = jsonArray.getJSONObject(i)
                        falla = jsonObject.getString("descripcion")
                        status = jsonObject.getString("estatus")
                        var calle = jsonObject.getString("calle")
                        var colonia = jsonObject.getString("colonia")
                        fecha = jsonObject.getString("fecha")

                        val resultado: String = "$calle, $colonia"

                        val reporte = RAlumbradoP(falla, fecha, resultado, status)
                        listaRAP.add(reporte)
                    }

                    val adapter = ReportAPAAdapter(this, listaRAP)
                    val layoutManager = LinearLayoutManager(this)
                    val recyclerView = findViewById<RecyclerView>(R.id.lista)
                    recyclerView.hasFixedSize()
                    recyclerView.layoutManager = layoutManager
                    recyclerView.adapter = adapter
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
    val idCiudadano = 1 // Obtener el valor del ID del ciudadano desde alguna variable
    val tipoReporte = 3 // Valor fijo para el parámetro tipo_reporte
    val url = "http://192.168.1.64/001%20-%20Kotlin/consultaRepMAnimal.php?id_ciudadano=$idCiudadano&tipo_reporte=$tipoReporte"
    val stringRequest = StringRequest(
        Request.Method.GET, url,
        { response ->
            Log.d("Response", response) // Imprimir la respuesta en el Logcat

            try {
                val jsonArray = JSONArray(response)
                // Resto del código para procesar la respuesta JSON
                val listaRAP = mutableListOf<RAlumbradoP>()

                for (i in 0 until jsonArray.length()) {
                    val jsonObject = jsonArray.getJSONObject(i)
                    falla = jsonObject.getString("descripcion")
                    status = jsonObject.getString("estatus")
                    var calle = jsonObject.getString("calle")
                    var colonia = jsonObject.getString("colonia")
                    fecha = jsonObject.getString("fecha")

                    val resultado: String = "$calle, $colonia"

                    val reporte = RAlumbradoP(falla, fecha, resultado, status)
                    listaRAP.add(reporte)
                }

                val adapter = ReportAPAAdapter(this, listaRAP)
                val layoutManager = LinearLayoutManager(this)
                val recyclerView = findViewById<RecyclerView>(R.id.lista)
                recyclerView.hasFixedSize()
                recyclerView.layoutManager = layoutManager
                recyclerView.adapter = adapter
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
    val idCiudadano = 1 // Obtener el valor del ID del ciudadano desde alguna variable
    val tipoReporte = 4 // Valor fijo para el parámetro tipo_reporte
    val url = "http://192.168.1.64/001%20-%20Kotlin/consultaRepOoapas.php?id_ciudadano=$idCiudadano&tipo_reporte=$tipoReporte"
    val stringRequest = StringRequest(
        Request.Method.GET, url,
        { response ->
            Log.d("Response", response) // Imprimir la respuesta en el Logcat

            try {
                val jsonArray = JSONArray(response)
                // Resto del código para procesar la respuesta JSON
                val listaRAP = mutableListOf<RAlumbradoP>()

                for (i in 0 until jsonArray.length()) {
                    val jsonObject = jsonArray.getJSONObject(i)
                    falla = jsonObject.getString("descripcion")
                    status = jsonObject.getString("estatus")
                    var calle = jsonObject.getString("calle")
                    var colonia = jsonObject.getString("colonia")
                    fecha = jsonObject.getString("fecha")

                    val resultado: String = "$calle, $colonia"

                    val reporte = RAlumbradoP(falla, fecha, resultado, status)
                    listaRAP.add(reporte)
                }

                val adapter = ReportAPAAdapter(this, listaRAP)
                val layoutManager = LinearLayoutManager(this)
                val recyclerView = findViewById<RecyclerView>(R.id.lista)
                recyclerView.hasFixedSize()
                recyclerView.layoutManager = layoutManager
                recyclerView.adapter = adapter
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
private fun consultarReportesVial() {
    val queue = Volley.newRequestQueue(this)
    val idCiudadano = 1 // Obtener el valor del ID del ciudadano desde alguna variable
    val tipoReporte = 5 // Valor fijo para el parámetro tipo_reporte
    val url = "http://192.168.1.64/001%20-%20Kotlin/consultaRepVial.php?id_ciudadano=$idCiudadano&tipo_reporte=$tipoReporte"
    val stringRequest = StringRequest(
        Request.Method.GET, url,
        { response ->
            Log.d("Response", response) // Imprimir la respuesta en el Logcat

            try {
                val jsonArray = JSONArray(response)
                // Resto del código para procesar la respuesta JSON
                val listaRAP = mutableListOf<dcReporteVial>()

                for (i in 0 until jsonArray.length()) {
                    val jsonObject = jsonArray.getJSONObject(i)
                    falla = jsonObject.getString("descripcion")
                    status = jsonObject.getString("estatus")
                    fecha = jsonObject.getString("fecha")

                    val reporte = dcReporteVial(falla, fecha, status)
                    listaRAP.add(reporte)
                }

                val adapter = adapterVial(this, listaRAP)
                val layoutManager = LinearLayoutManager(this)
                val recyclerView = findViewById<RecyclerView>(R.id.lista)
                recyclerView.hasFixedSize()
                recyclerView.layoutManager = layoutManager
                recyclerView.adapter = adapter
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