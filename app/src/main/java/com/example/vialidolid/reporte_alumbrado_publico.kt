package com.example.vialidolid

import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.util.*
import kotlin.collections.HashMap

class reporte_alumbrado_publico : AppCompatActivity() {
    var etUbicacionAP: EditText? = null
    var etDescripcionAP: EditText? = null
    var etReferenciasAP: EditText? = null

    //Obtener las coordenadas actuales y colocarlas dentro del editText
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var geocoder: Geocoder
    private val LOCATION_PERMISSION_REQUEST_CODE = 1


    //Ubicacion por defecto en caso de no poder obtener la ubicacion del usuario.
    private var etUbicacion: EditText? = null
    private var lat: Double = 19.7040
    private var lng: Double = -101.1908
    private var calle : String? = "Guillermo Prieto 314,"
    private var colonia : String? = "Centro histórico de Morelia"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reporte_alumbrado_publico)

        etUbicacion = findViewById(R.id.etUbicacionAP)
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
                        finish()
                    }
                    "Maltrato animal" -> {
                        val intent = Intent(this@reporte_alumbrado_publico,reporte_maltrato_animal::class.java)
                        startActivity(intent)
                        finish()

                    }
                    "Reporte vial" -> {
                        val intent = Intent(this@reporte_alumbrado_publico, reporte_vial::class.java)
                        startActivity(intent)
                        finish()

                    }
                    "Suministro de agua" -> {
                        val intent = Intent(this@reporte_alumbrado_publico, reporte_suministro_agua::class.java)
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
            val intent = Intent(this@reporte_alumbrado_publico,nuevo_reporte::class.java)
            finish()
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

        //Obtener la ubicacion del usuario, primero en coordenada, posteriormente
        //se transforman a texto y se muestran en el campo de ubicacion.
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        geocoder = Geocoder(this, Locale.getDefault())
        getLocation()
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

    private fun getLocation() {
        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    if (location != null) {
                        val latitude = location.latitude
                        val longitude = location.longitude
                        val locationText = "Lat: $latitude, Long: $longitude"
                        var fullAddress = geocoder.getFromLocation(latitude, longitude, 1)
                        showToast(locationText)
                        if(fullAddress != null && fullAddress.isNotEmpty()){
                            //TODO borrar
                            println("Locale: ${fullAddress?.get(0)?.locale}")
                            println("Locality: ${fullAddress?.get(0)?.locality}")
                            println("Address: ${fullAddress?.get(0)?.getAddressLine(0)}")
                            println("SubLocality: ${fullAddress?.get(0)?.subLocality}")
                            println("SubAdminArea: ${fullAddress?.get(0)?.subAdminArea}")
                            println("Premises: ${fullAddress?.get(0)?.premises}")
                            println("SubThroughfare: ${fullAddress?.get(0)?.subThoroughfare}")
                            println("Throughfare: ${fullAddress?.get(0)?.thoroughfare}")
                            //Guardar la calle y colonia obtenidos y mostrarlos dentro de la app
                            calle = fullAddress?.get(0)?.thoroughfare + " "+ fullAddress?.get(0)?.subThoroughfare
                            colonia = fullAddress?.get(0)?.subLocality
                            etUbicacion?.setText(calle+", "+colonia)
                            etUbicacion?.isEnabled = false
                        }
                        else{
                            showToast("Problemas al obtener la ubicacion")
                        }

                    }
                    else {
                        showToast("No se pudo obtener la ubicación")
                    }
                }
                .addOnFailureListener { e ->
                    showToast("Error al obtener la ubicación: ${e.message}")
                }
        } else {
            // Permission not granted, request the permission
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Location permission granted, proceed to get location
                getLocation()
            } else {
                // Location permission denied, handle the scenario accordingly
                showToast("Location permission denied")
            }
        }
    }


}


private fun EditText.addTextChangedListener(function: () -> Unit) {

}


