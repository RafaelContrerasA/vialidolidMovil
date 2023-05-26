package com.example.vialidolid

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.sql.DriverManager
import java.sql.SQLException

class loginScreen : AppCompatActivity() {
    var etTelefono: EditText? = null
    var etContraseña: EditText? = null

    // on below line we are creating
    // a variable for shared preferences.
    lateinit var sharedPreferences: SharedPreferences

    // on below line we are creating a variable
    // for prefs key and email key and pwd key.
    var PREFS_KEY = "usuario"
    var PHONE_KEY = "phone"
    var PWD_KEY = "pwd"
    var UID_KEY = "uid"

    // on below line we are creating a
    // variable for email and password.
    var phone = ""
    var pwd = ""
    var uid = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_screen)

        // on below line we are initializing our shared preferences.
        sharedPreferences = getSharedPreferences(PREFS_KEY, Context.MODE_PRIVATE)

        // on below line we are getting data from
        // our shared prefs and setting it to email.
        phone = sharedPreferences.getString(PHONE_KEY, "").toString()

        // on below line we are getting data from
        // shared prefs and setting it to pwd.
        pwd = sharedPreferences.getString(PWD_KEY, "").toString()

        uid = sharedPreferences.getString(UID_KEY, "").toString()

        //campos de texto de telefono y contraseña
        etTelefono = findViewById(R.id.etTelefono)
        etContraseña = findViewById(R.id.etContraseña)



        //Listener botoncrear cuenta
        val newAccountButton =findViewById<TextView>(R.id.tvNewAccount)
        newAccountButton.setOnClickListener{
            val Intent = Intent(this,newAccount::class.java)
            startActivity(Intent)

        }

        //Listener para boton recuperar contraseña
        val recoverPasswordButton =findViewById<TextView>(R.id.tvForgot)
        recoverPasswordButton.setOnClickListener{
            val Intent = Intent(this,recoverPassword::class.java)
            startActivity(Intent)
        }

        //listener boton iniciar sesion
        val loginButton =findViewById<TextView>(R.id.btLogin)
        loginButton.setOnClickListener{
            validarUsuario()
        }



        }

    fun validarUsuario(){
        var respuesta = "Pendiente"
        val queue = Volley.newRequestQueue(this)
        val telefono = etTelefono?.text.toString()
        val contraseña = etContraseña?.text.toString()
        val url = "http://${resources.getString(R.string.server_ip)}/rest/userLogin.php"
        println(url)


        var resultadoPost = object : StringRequest(Request.Method.POST, url,
            Response.Listener<String>{ response ->
                println(response.toString())
                if(response.toString() == "Login Exitoso"){
                    Toast.makeText(this,"Login Exitoso",Toast.LENGTH_LONG).show()
                    //Guardar la sesion del usuario en shared preferences
                    val editor: SharedPreferences.Editor = sharedPreferences.edit()
                    editor.putString(PHONE_KEY, telefono)
                    editor.putString(PWD_KEY, contraseña)
                    // on below line we are applying
                    // changes to our shared prefs.
                    editor.apply()

                    obtenerIdCiudadano(telefono)
                    //Enviar al usuario a al menu principal
                    val i = Intent(this@loginScreen, userProfile::class.java)
                    startActivity(i)
                    finish()

                }
                else{
                    Toast.makeText(this,"Usuario y/o contraseña inconrrecta",Toast.LENGTH_LONG).show()

                }

            }, Response.ErrorListener{ error ->
                Toast.makeText(this,"Error de conexión, reintentelo mas tarde",Toast.LENGTH_LONG).show()

            }){
            override fun getParams(): MutableMap<String, String>? {
                val parametros = HashMap<String,String>()
                parametros["telefono"] = telefono
                parametros["contraseña"] = contraseña
                return parametros
            }
        }
        queue.add(resultadoPost)

    }

    fun obtenerIdCiudadano(telefono: String) {
        var respuesta = "Pendiente"
        val queue = Volley.newRequestQueue(this)
        val url = "http://${resources.getString(R.string.server_ip)}/rest/obtenerIdCiudadano.php"
        println(url)


        var resultadoPost = object : StringRequest(Request.Method.POST, url,
            Response.Listener<String>{ response ->
                println(response.toString())
                if(response.toString() != "Error"){
                    Toast.makeText(this,"Id obtenido exitosamente",Toast.LENGTH_LONG).show()
                    //Guardar la sesion del usuario en shared preferences
                    val editor: SharedPreferences.Editor = sharedPreferences.edit()
                    editor.putString(UID_KEY, response)
                    // on below line we are applying
                    // changes to our shared prefs.
                    editor.apply()

                    Toast.makeText(this,sharedPreferences.getString("uid", null)!!,Toast.LENGTH_LONG).show()


                    //Enviar al usuario a al menu principal


                }
                else{
                    Toast.makeText(this,"Error para obtener ID",Toast.LENGTH_LONG).show()

                }

            }, Response.ErrorListener{ error ->
                Toast.makeText(this,"Error de conexión, reintentelo mas tarde",Toast.LENGTH_LONG).show()

            }){
            override fun getParams(): MutableMap<String, String>? {
                val parametros = HashMap<String,String>()
                parametros["telefono"] = telefono
                return parametros
            }
        }
        queue.add(resultadoPost)
    }

    override fun onStart() {
        super.onStart()
        // in this method we are checking if email and pwd are not empty.
        if (!phone.equals("") && !pwd.equals("")) {
            // if email and pwd is not empty we
            // are opening our main 2 activity on below line.
            val i = Intent(this@loginScreen, userProfile::class.java)

            // on below line we are calling start
            // activity method to start our activity.
            startActivity(i)

            // on below line we are calling
            // finish to finish our main activity.
            finish()
        }
    }

}