package com.example.vialidolid

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley

class recoverPassword : AppCompatActivity() {

    var etTelefono: EditText? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recover_password)

        etTelefono = findViewById(R.id.etTelefono)


        //Listener del boton crear cuenta
        val createAccountButton =findViewById<Button>(R.id.btSendCode)
        createAccountButton.setOnClickListener{
            if (phoneIsCorrect()) {
                var respuesta = "Pendiente"
                val queue = Volley.newRequestQueue(this)
                var telefono = etTelefono?.text.toString()
                val url = "http://192.168.1.66/rest/telefonoDisponible.php?telefono=$telefono"
                println(url)
                val jsonObjectRequest = JsonObjectRequest(
                    Request.Method.GET, url, null,
                    Response.Listener { response ->
                        //aqui se guarda la respuesta que haya dado la BD
                        respuesta = response.getString("response")
                        if(respuesta=="Telefono Disponible")

                            etTelefono?.error = "No hay ninguna cuenta con ese teléfono"

                        else {
                            var intent = Intent(this@recoverPassword,enterRecoveryCode::class.java)
                            intent.putExtra("telefono", etTelefono?.text.toString())
                            startActivity(intent)

                        }

                    },
                    Response.ErrorListener{ error ->
                        Toast.makeText(this,"Error de conexion ${error.toString()}", Toast.LENGTH_LONG).show()
                    }
                )
                queue.add(jsonObjectRequest)




            }
        }



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

    fun saveAccountDetails(spinnerEstado: Spinner, spinnerCiudad: Spinner){
        var intent = Intent(this@recoverPassword,enterCode::class.java)
        intent.putExtra("telefono", etTelefono?.text.toString())
        startActivity(intent)
    }


}