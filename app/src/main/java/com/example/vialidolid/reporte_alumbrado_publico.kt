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

class reporte_alumbrado_publico : AppCompatActivity() {
    var etUbicacionAP: EditText? = null
    var etDescripcionAP: EditText? = null
    var etReferenciasAP: EditText? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reporte_alumbrado_publico)

        etUbicacionAP = findViewById(R.id.etUbicacionAP)
        etDescripcionAP = findViewById(R.id.etDescripcionAP)
        etReferenciasAP = findViewById(R.id.etReferenciasAP)

        val arrayCategoria = resources.getStringArray(R.array.categoria)
        val spinnerCategoriaAP = findViewById<Spinner>(R.id.spSA)
        val categoriaAdapter = ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,
            arrayCategoria)
        spinnerCategoriaAP.adapter = categoriaAdapter

        spinnerCategoriaAP.setSelection(1)

        spinnerCategoriaAP.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedItem = parent.getItemAtPosition(position).toString()
                when (selectedItem) {
                    "Bache" -> {
                        val intent = Intent(this@reporte_alumbrado_publico,reporte_bache::class.java)
                        startActivity(intent)
                        borrarEditText()
                    }
                    "Maltrato animal" -> {
                        borrarEditText()
                        val intent = Intent(this@reporte_alumbrado_publico,reporte_maltrato_animal::class.java)
                        startActivity(intent)

                    }
                    "Reporte vial" -> {
                        borrarEditText()
                        val intent = Intent(this@reporte_alumbrado_publico, reporte_vial::class.java)
                        startActivity(intent)

                    }
                    "Suministro de agua" -> {
                        borrarEditText()
                        val intent = Intent(this@reporte_alumbrado_publico, reporte_suministro_agua::class.java)
                        startActivity(intent)
                    }
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>) {
                // nada por hacer aquí
            }
        }
        val btCancelar = findViewById<Button>(R.id.btCancelar)
        btCancelar.setOnClickListener(View.OnClickListener {
            val intent = Intent(this@reporte_alumbrado_publico,nuevo_reporte::class.java)
            startActivity(intent)
            borrarEditText()
        })

        var et1 = findViewById<EditText>(R.id.etUbicacionAP)
        et1.addTextChangedListener{
            if (et1.text.length == 0) et1.setError("Campo vacio")
        }
        var et2 = findViewById<EditText>(R.id.etDescripcionAP)
        et2.addTextChangedListener{
            if  (et2.text.length == 0) et2.setError("Campo vacio")
        }
        var et3 = findViewById<EditText>(R.id.etReferenciasAP)
        et3.addTextChangedListener{
            if  (et3.text.length == 0) et3.setError("Campo vacio")
        }

    }

    //---------------- Función para mandar los datos a la base de datos
    fun insertar(view: View){
        val url="http://${resources.getString(R.string.server_ip)}/rest/insertarReporteAlumbrado.php"
        val queue= Volley.newRequestQueue(this)
        var resultadoPost1 = object : StringRequest(Request.Method.POST,url,
            Response.Listener<String> { response ->
                Toast.makeText(this@reporte_alumbrado_publico,"Reporte generado", Toast.LENGTH_LONG).show()
            } , Response.ErrorListener { error ->
                Toast.makeText(this@reporte_alumbrado_publico,"Error $error", Toast.LENGTH_LONG).show()
            }){
            override fun getParams(): MutableMap<String, String>? {
                val parametros1=HashMap<String,String>()
                parametros1.put("descripcion",etDescripcionAP?.text.toString())
                parametros1.put("referencias",etReferenciasAP?.text.toString())
                return parametros1
            }
        }
        queue.add(resultadoPost1)
        val intent = Intent(this@reporte_alumbrado_publico,reporte_comp::class.java)
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

private fun EditText.addTextChangedListener(function: () -> Unit) {

}


