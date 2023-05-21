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

class reporte_maltrato_animal : AppCompatActivity() {
    var etUbicacionMA: EditText? = null
    var etDescripcionMA: EditText? = null
    var etReferenciasMA: EditText? = null
    var etTipoAnimal: EditText? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reporte_maltrato_animal)

        etDescripcionMA = findViewById(R.id.etDescripcionAP)
        etReferenciasMA = findViewById(R.id.etReferenciasAP)
        etTipoAnimal = findViewById(R.id.eteTipoAnimal)

        var arrayCategoria = resources.getStringArray(R.array.categoria)
        var spinnerCategoriaMA = findViewById<Spinner>(R.id.spinMalAnim)
        var categoriaAdapter = ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,
            arrayCategoria)
        spinnerCategoriaMA.adapter = categoriaAdapter

        spinnerCategoriaMA.setSelection(3)

        spinnerCategoriaMA.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedItem = parent.getItemAtPosition(position).toString()
                when (selectedItem) {
                    "Alumbrado público" -> {
                        val intent = Intent(this@reporte_maltrato_animal,reporte_alumbrado_publico::class.java)
                        startActivity(intent)
                        finish()
                    }
                    "Bache" -> {
                        val intent = Intent(this@reporte_maltrato_animal,reporte_bache::class.java)
                        startActivity(intent)
                        finish()
                    }
                    "Reporte vial" -> {
                        val intent = Intent(this@reporte_maltrato_animal, reporte_vial::class.java)
                        startActivity(intent)
                        finish()
                    }
                    "Suministro de agua" -> {
                        val intent = Intent(this@reporte_maltrato_animal, reporte_suministro_agua::class.java)
                        startActivity(intent)
                        finish()
                    }
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>) {
                // nada por hacer aquí
            }
        }
        val btCancelar = findViewById<Button>(R.id.btCancelar)
        btCancelar.setOnClickListener(View.OnClickListener {
            finish()
        })
    }
    //---------------- Función para mandar los datos a la base de datos
    fun insertar(view: View){
        val url="http://${resources.getString(R.string.server_ip)}/rest/insertarReporteMaltratoAnimal.php"
        val queue= Volley.newRequestQueue(this)
        var resultadoPost = object : StringRequest(Request.Method.POST,url,
            Response.Listener<String> { response ->
                Toast.makeText(this@reporte_maltrato_animal,"Reporte generado", Toast.LENGTH_LONG).show()
            } , Response.ErrorListener { error ->
                Toast.makeText(this@reporte_maltrato_animal,"Error $error", Toast.LENGTH_LONG).show()
            }){
            override fun getParams(): MutableMap<String, String>? {
                val parametros=HashMap<String,String>()
                parametros.put("descripcion",etDescripcionMA?.text.toString())
                parametros.put("referencias",etReferenciasMA?.text.toString())
                parametros.put("tipo_mascota",etTipoAnimal?.text.toString())

                return parametros
            }
        }
        queue.add(resultadoPost)
        val intent = Intent(this@reporte_maltrato_animal,reporte_comp::class.java)
        startActivity(intent)

    }

    fun borrarEditText() {
        val ubi = findViewById<EditText>(R.id.etUbicacionAP)
        ubi.setText(null)
        val desc = findViewById<EditText>(R.id.etDescripcionAP)
        desc.setText(null)
        val ref = findViewById<EditText>(R.id.etReferenciasAP)
        ref.setText(null)
        val tm = findViewById<EditText>(R.id.eteTipoAnimal)
        tm.setText(null)
    }
}