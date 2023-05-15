package com.example.vialidolid

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley

class enterNewPassword : AppCompatActivity() {
    var etPassword: EditText? = null
    var etConfirmPassword: EditText? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_enter_new_password)

        etPassword = findViewById(R.id.etPassword)
        etConfirmPassword = findViewById(R.id.etConfirmPassword)

        //Listener del boton nueva contraseña
        val createNewPassword =findViewById<Button>(R.id.btCreateNewPassword)
        createNewPassword.setOnClickListener{
            if (validarContrasena() && compararContrasenas()) {
                actualizarContraseña()
            }
        }


    }

    fun validarContrasena(): Boolean {
        if(etPassword?.text.toString().length >= 8)
            return true
        else{
            Toast.makeText(this,"La contraseña debe tener al menos 8 caracteres", Toast.LENGTH_LONG).show()
            etPassword?.error = "La contraseña debe tener al menos 8 caracteres"
            return false
        }
    }

    fun compararContrasenas(): Boolean {
        if (etPassword?.text.toString()==etConfirmPassword?.text.toString()){
            return true
        }
        else{
            Toast.makeText(this,"Las contraseñas no coinciden", Toast.LENGTH_LONG).show()
            etConfirmPassword?.error = "Las contraseñas no coinciden"
            return false
        }
    }
    fun actualizarContraseña(){
        //url de archivo para insertar en PHP
        val url = "http://192.168.1.66/rest/updateContraseña.php"
        val queue = Volley.newRequestQueue(this)
        var resultadoPost = object : StringRequest(Request.Method.POST, url,
            Response.Listener<String>{ response ->
                Toast.makeText(this,"Contraseña actualizada correctamente",Toast.LENGTH_LONG).show()
                println("Contraseña actualizada correctamente")
                val intent = Intent(this , passwordRestored::class.java)
                startActivity(intent)
                finish()

            }, Response.ErrorListener{ error ->
                Toast.makeText(this,"Error de conexión, reintentelo mas tarde",Toast.LENGTH_LONG).show()
                println("actualizacion fallida")
                finish()
            }){
            override fun getParams(): MutableMap<String, String>? {
                val parametros = HashMap<String,String>()
                parametros["password"] = etPassword?.text.toString()
                parametros["telefono"] = intent.getStringExtra("telefono").toString().substring(3)
                println(intent.getStringExtra("telefono").toString().substring(3))
                return parametros
            }
        }
        queue.add(resultadoPost)
    }

}