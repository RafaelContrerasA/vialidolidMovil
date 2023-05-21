package com.example.vialidolid

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley

class reporte_bache : AppCompatActivity() {
    var etUbicacion: EditText? = null
    var etDescripcion: EditText? = null
    var etReferencias: EditText? = null
    var gpsActivado = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reporte_bache)

        etDescripcion = findViewById(R.id.etDescripcionAP)
        etReferencias = findViewById(R.id.etReferenciasAP)
        etUbicacion = findViewById(R.id.etUbicacionAP)


        //listener obtener ubicacion
        var ubicacionListener = findViewById<EditText>(R.id.etUbicacionAP)
        ubicacionListener.setOnClickListener{
            if(ubicacionHabilitada()){
                AlertDialog.Builder(this)
                    .setTitle("¿Activar Ubicación?")
                    .setMessage("Obtener la ubicacion GPS actual para crear el reporte")
                    .setPositiveButton("Activar",
                        DialogInterface.OnClickListener{dialog, which ->
                            activarUbicacion()

                        }
                        )
                    .setNegativeButton("Rechazar",
                        DialogInterface.OnClickListener{dialog, which ->
                            gpsActivado = false
                        }
                        )
                    .setCancelable(true)
                    .show()
            }
            else{

            }
        }


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
                        finish()
                    }
                    "Maltrato animal" -> {
                        val intent = Intent(this@reporte_bache,reporte_maltrato_animal::class.java)
                        startActivity(intent)
                        finish()
                    }
                    "Reporte vial" -> {
                        val intent = Intent(this@reporte_bache, reporte_vial::class.java)
                        startActivity(intent)
                        finish()
                    }
                    "Suministro de agua" -> {
                        val intent = Intent(this@reporte_bache, reporte_suministro_agua::class.java)
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

    fun ubicacionHabilitada(): Boolean{
        var locationManager: LocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            return true
        }
        if(locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
            return true
        }

        return false
    }

    fun activarUbicacion(){
        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
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