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

class reporte_suministro_agua : AppCompatActivity() {
    var etClavPredio: EditText? = null
    var etDescripcion: EditText? = null
    var etReferencias: EditText? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reporte_suministro_agua)

        etDescripcion = findViewById(R.id.etDescripcionAP)
        etReferencias = findViewById(R.id.etReferenciasAP)
        etClavPredio = findViewById(R.id.eteClavPredio)

        val arrayCategoria = resources.getStringArray(R.array.categoria)
        val spinnerCategoria = findViewById<Spinner>(R.id.spSA)
        val categoriaAdapter = ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item,
            arrayCategoria)
        spinnerCategoria.adapter = categoriaAdapter
        spinnerCategoria.setSelection(5)

        spinnerCategoria.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedItem = parent.getItemAtPosition(position).toString()
                when (selectedItem) {
                    "Alumbrado público" -> {

                        val intent = Intent(this@reporte_suministro_agua, reporte_alumbrado_publico::class.java)
                        startActivity(intent)
                    }
                    "Bache" -> {
                        val intent = Intent(this@reporte_suministro_agua,reporte_bache::class.java)
                        startActivity(intent)
                    }
                    "Maltrato animal" -> {
                        val intent = Intent(this@reporte_suministro_agua,reporte_maltrato_animal::class.java)
                        startActivity(intent)
                    }
                    "Reporte vial" -> {
                        val intent = Intent(this@reporte_suministro_agua,reporte_vial::class.java)
                        startActivity(intent)
                    }

                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }
        val btCancelar = findViewById<Button>(R.id.btCancelar)
        btCancelar.setOnClickListener(View.OnClickListener {
            val intent = Intent(this@reporte_suministro_agua,nuevo_reporte::class.java)
            startActivity(intent)
            borrarEditText()
        })
    }


    //---------------- Función para mandar los datos a la base de datos
    fun insertar(view: View){
        val url="http://192.168.1.64/001%20-%20Kotlin/insertarReporteOoapas.php"
        val queue= Volley.newRequestQueue(this)
        var resultadoPost = object : StringRequest(Request.Method.POST,url,
            Response.Listener<String> { response ->
                Toast.makeText(this@reporte_suministro_agua,"Reporte generado", Toast.LENGTH_LONG).show()
            } , Response.ErrorListener { error ->
                Toast.makeText(this@reporte_suministro_agua,"Error $error", Toast.LENGTH_LONG).show()
            }){
            override fun getParams(): MutableMap<String, String>? {
                val parametros=HashMap<String,String>()
                parametros.put("descripcion",etDescripcion?.text.toString())
                parametros.put("referencias",etReferencias?.text.toString())
                parametros.put("cve_predio",etClavPredio?.text.toString())

                return parametros
            }
        }
        queue.add(resultadoPost)
        val intent = Intent(this@reporte_suministro_agua,reporte_comp::class.java)
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