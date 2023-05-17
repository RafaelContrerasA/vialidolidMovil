package com.example.vialidolid

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley

class reporte_bache : AppCompatActivity() {
    var etUbicacion: EditText? = null
    var etDescripcion: EditText? = null
    var etReferencias: EditText? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reporte_bache)

        etDescripcion = findViewById(R.id.etDescripcionAP)
        etReferencias = findViewById(R.id.etReferenciasAP)
        etUbicacion = findViewById(R.id.etUbicacionAP)

        //------------------------------------- SPINNER
        var arrayCategoria = resources.getStringArray(R.array.categoria)
        var spinnerCategoriaB = findViewById<Spinner>(R.id.spBache)
        var categoriaAdapter = ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,
            arrayCategoria)
        spinnerCategoriaB.adapter = categoriaAdapter
        spinnerCategoriaB.setSelection(2)

        spinnerCategoriaB.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedItem = parent.getItemAtPosition(position).toString()
                when (selectedItem) {
                    "Alumbrado público" -> {
                        val intent = Intent(this@reporte_bache,reporte_alumbrado_publico::class.java)
                        startActivity(intent)
                        borrarEditText()
                    }
                    "Maltrato animal" -> {
                        val intent = Intent(this@reporte_bache,reporte_maltrato_animal::class.java)
                        startActivity(intent)
                        borrarEditText()
                    }
                    "Reporte vial" -> {
                        val intent = Intent(this@reporte_bache, reporte_vial::class.java)
                        startActivity(intent)
                        borrarEditText()
                    }
                    "Suministro de agua" -> {
                        val intent = Intent(this@reporte_bache, reporte_suministro_agua::class.java)
                        startActivity(intent)
                        borrarEditText()
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // nada por hacer aquí
            }
        }
        val btCancelar = findViewById<Button>(R.id.btCancelar)
        btCancelar.setOnClickListener(View.OnClickListener {
            val intent = Intent(this@reporte_bache,nuevo_reporte::class.java)
            startActivity(intent)
            borrarEditText()
        })
    }

    //---------------- Función para mandar los datos a la base de datos
    fun insertar(view: View){
        val url="http://${resources.getString(R.string.server_ip)}/rest/insertarReportebache.php"
        val queue= Volley.newRequestQueue(this)
        var resultadoPost = object : StringRequest(Request.Method.POST,url,
            Response.Listener<String> { response ->
                Toast.makeText(this@reporte_bache,"Reporte generado", Toast.LENGTH_LONG).show()
            } , Response.ErrorListener { error ->
                Toast.makeText(this@reporte_bache,"Error $error", Toast.LENGTH_LONG).show()
            }){
            override fun getParams(): MutableMap<String, String>? {
                val parametros=HashMap<String,String>()
                parametros.put("descripcion",etDescripcion?.text.toString())
                parametros.put("coordenadas",etUbicacion?.text.toString())
                parametros.put("referencias",etReferencias?.text.toString())

                return parametros
            }
        }
        queue.add(resultadoPost)
        val intent = Intent(this@reporte_bache,reporte_comp::class.java)
        startActivity(intent)


    }

    fun borrarEditText() {
        val ubi = findViewById<EditText>(R.id.etUbicacionAP)
        ubi.setText(null)
        val desc = findViewById<EditText>(R.id.etDescripcionAP)
        desc.setText(null)
        val ref = findViewById<EditText>(R.id.etReferenciasAP)
        ref.setText(null)
    }
}