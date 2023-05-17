package com.example.vialidolid

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.*
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley

class newAccount : AppCompatActivity() {

    //Objetos del formulario
    var etNombre: EditText? = null
    var etApellidoPaterno: EditText? = null
    var etApellidoMaterno: EditText? = null
    var etCorreo: EditText? = null
    var etTelefono: EditText? = null
    var etContraseña: EditText? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_account)

        //campos del formulario para obtener sus datos
        etNombre = findViewById(R.id.etNombre)
        etApellidoPaterno = findViewById(R.id.etApellidoPaterno)
        etApellidoMaterno = findViewById(R.id.etApellidoMaterno)
        etCorreo = findViewById(R.id.etCorreo)
        etTelefono = findViewById(R.id.etTelefono)
        etContraseña = findViewById(R.id.etPassword)

        //Obtiene la lista de estados que se encuentra dentro de strings.xml
        var arrayEstado = resources.getStringArray(R.array.estados)
        var spinnerEstado = findViewById<Spinner>(R.id.spEstado)
        var spinnerCiudad = findViewById<Spinner>(R.id.spCiudad)
        var estadoAdapter = ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item, arrayEstado)
        spinnerEstado.adapter = estadoAdapter
        spinnerEstado.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                //cambia la lista de ciudades dependiendo del estado que se escoja
                showCiudades(position, arrayEstado)
            }
        }

        //Listener del boton crear cuenta
        val createAccountButton =findViewById<Button>(R.id.btCreateAccount)
        createAccountButton.setOnClickListener{
            if (notEmpty() && phoneIsCorrect()) {
                var respuesta = "Pendiente"
                val queue = Volley.newRequestQueue(this)
                var telefono = etTelefono?.text.toString()
                val url = "http://${resources.getString(R.string.server_ip)}/rest/telefonoDisponible.php?telefono=$telefono"
                println(url)
                val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null,
                    Response.Listener {response ->
                        //aqui se guarda la respuesta que haya dado la BD
                        respuesta = response.getString("response")
                        if(respuesta=="Telefono Disponible")
                            saveAccountDetails(spinnerEstado, spinnerCiudad)

                            /*
                            insertCiudadano(spinnerEstado, spinnerCiudad)

                            */
                        else {
                            etTelefono?.error = "Telefono ya se encuentra registrado en la app"
                        }

                                      },
                    Response.ErrorListener{ error ->
                        Toast.makeText(this,"Error de conexion ${error.toString()}",Toast.LENGTH_LONG).show()
                    }
                )
                queue.add(jsonObjectRequest)




            } else {
                Toast.makeText(this,"Datos incorrectos",Toast.LENGTH_LONG).show()
            }
        }

    }

   /* fun insertCiudadano(spinnerEstado:Spinner, spinnerCiudad:Spinner){
        //url de archivo para insertar en PHP
        val url = "http://${resources.getString(R.string.server_ip)}/rest/insertCiudadano.php"
        val queue = Volley.newRequestQueue(this)
        var resultadoPost = object : StringRequest(Request.Method.POST, url,
            Response.Listener<String>{response ->
                Toast.makeText(this,"Usuario Insertado",Toast.LENGTH_LONG).show()

        },Response.ErrorListener{error ->
                Toast.makeText(this,"Error $error",Toast.LENGTH_LONG).show()

        }){
            override fun getParams(): MutableMap<String, String>? {
                val parametros = HashMap<String,String>()
                parametros["nombre"] = etNombre?.text.toString()
                println(etNombre?.text.toString())
                parametros["apellido_paterno"] = etApellidoPaterno?.text.toString()
                println(etApellidoPaterno?.text.toString())
                parametros["apellido_materno"] = etApellidoMaterno?.text.toString()
                println(etApellidoMaterno?.text.toString())
                parametros["correo"] = etCorreo?.text.toString()
                println(etCorreo?.text.toString())
                parametros["contraseña"] = etContraseña?.text.toString()
                println("12345678")
                parametros["telefono"] = etTelefono?.text.toString()
                println(etTelefono?.text.toString())
                parametros["estado"] = spinnerEstado.selectedItem.toString()
                println(spinnerEstado.selectedItem.toString())
                parametros["ciudad"] = spinnerCiudad.selectedItem.toString()
                println(spinnerCiudad.selectedItem.toString())
                parametros["n_penalizaciones"] = "0"
                return parametros
            }
        }
        queue.add(resultadoPost)
    }*/

    fun showCiudades(position: Int, arrayEstado: Array<String>){
        //Generar nombre del array de ciudades tienen estructura cd+Estado ej.cdMichoacan
        var arrayCall = "cd"+ arrayEstado[position]
        var spinnerCiudad = findViewById<Spinner>(R.id.spCiudad)
        var ciudadAdapter = ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,
            resources.getStringArray(resources.getIdentifier(arrayCall, "array", getPackageName())))
        spinnerCiudad.adapter = ciudadAdapter

    }

    //Validar que no haya ningun campo vacio
    fun notEmpty(): Boolean {
        if (TextUtils.isEmpty(etNombre?.text)) {
            etNombre?.error = "Falta llenar este campo"
            return false
        }
        if (TextUtils.isEmpty(etApellidoPaterno?.text)) {
            etApellidoPaterno?.error = "Falta llenar este campo"
            return false
        }
        if (TextUtils.isEmpty(etApellidoMaterno?.text)) {
            etApellidoMaterno?.error = "Falta llenar este campo"
            return false
        }
        if (TextUtils.isEmpty(etCorreo?.text)) {
            etCorreo?.error = "Falta llenar este campo"
            return false
        }
        if (TextUtils.isEmpty(etTelefono?.text)) {
            etTelefono?.error = "Falta llenar este campo"
            return false
        }
        if (TextUtils.isEmpty(etContraseña?.text)) {
            etContraseña?.error = "Falta llenar este campo"
            return false
        }
        return true
    }

    //si toodo es correcto iniciar la nueva actividad  mandandole los datos del usuario
    fun saveAccountDetails(spinnerEstado:Spinner, spinnerCiudad:Spinner){
        var intent = Intent(this@newAccount,enterCode::class.java)
        intent.putExtra("nombre", etNombre?.text.toString())
        intent.putExtra("apellido_paterno", etApellidoPaterno?.text.toString())
        intent.putExtra("apellido_materno", etApellidoMaterno?.text.toString())
        intent.putExtra("correo", etCorreo?.text.toString())
        intent.putExtra("contraseña", etContraseña?.text.toString())
        intent.putExtra("telefono", etTelefono?.text.toString())
        intent.putExtra("estado", spinnerEstado.selectedItem.toString())
        intent.putExtra("ciudad", spinnerCiudad.selectedItem.toString())
        intent.putExtra("n_penalizaciones", "0")
        startActivity(intent)
    }

    fun phoneIsCorrect(): Boolean{
        // Validar que el número de teléfono contenga exactamente 10 dígitos
        val telefonoRegex = Regex("\\d{10}")
        if (!telefonoRegex.matches(etTelefono?.text.toString())) {
            etTelefono?.error = "Telefono es incorrecto"
            return false
        }

        return true
    }



}


