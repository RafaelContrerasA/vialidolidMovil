package com.example.vialidolid

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.vialidolid.databinding.ActivityMisReportesBinding
import org.json.JSONArray
import org.json.JSONObject
import reportAPAAdapter


class misReportes : AppCompatActivity() {

    lateinit var falla: String
    lateinit var status: String
    lateinit var ubicacion: String
    lateinit var fecha: String
    val binding = ActivityMisReportesBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)

        val arrayCategoria = resources.getStringArray(R.array.categoria)
        val spinnerCategoria = binding.spCat
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
                    " " -> {

                    }
                    "Alumbrado público" -> {
                        consultarReportesAlumbradoPublico()
                    }
                    "Bache" -> {
                        // Lógica específica para la opción "Bache"
                    }
                    "Maltrato animal" -> {
                        // Lógica específica para la opción "Maltrato animal"
                    }
                    "Reporte vial" -> {
                        // Lógica específica para la opción "Reporte vial"
                    }
                    "Suministro de agua" -> {
                        // Lógica específica para la opción "Suministro de agua"
                    }
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                // Implementar el comportamiento cuando no se selecciona nada en el spinner
            }
        }
    }

    // Función para realizar la solicitud de consulta de reportes de alumbrado público
    private fun consultarReportesAlumbradoPublico() {
        val queue = Volley.newRequestQueue(this@misReportes)
        val url = "http://192.168.1.64/001%20-%20Kotlin/consultaRepAP.php?id_ciudadano=1&tipo_reporte=1"
        val stringRequest = StringRequest(Request.Method.GET, url,
            { response ->
                val jsonArray = JSONArray(response)

                val listaRAP = mutableListOf<rAlumbradoP>()

                for (i in 0 until jsonArray.length()) {
                    val jsonObject = jsonArray.getJSONObject(i)
                    falla = jsonObject.getString("descripcion")
                    status = jsonObject.getString("estatus")
                    ubicacion = jsonObject.getString("latitud")
                    fecha = jsonObject.getString("fecha")

                    val reporte = rAlumbradoP(falla, fecha, ubicacion, status)
                    listaRAP.add(reporte)
                }

                val adapter = reportAPAAdapter(this@misReportes, listaRAP)
                val layoutManager = LinearLayoutManager(this@misReportes)
                binding.lista.layoutManager = layoutManager
                binding.lista.adapter = adapter
            },
            { error ->

                if (error.networkResponse != null) {
                    val statusCode = error.networkResponse.statusCode
                    val errorMessage = String(error.networkResponse.data)


                    Log.e("Error", "Código de estado: $statusCode, Mensaje: $errorMessage")
                } else {

                    Log.e("Error", "Error de red")
                }
            })
        queue.add(stringRequest)
    }
}