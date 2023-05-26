package com.example.vialidolid

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley

class userProfile : AppCompatActivity() {
    //declarar shared preferences
    lateinit var sharedPreferences: SharedPreferences

    //Edit text de los datos del usuario
    var etNombre: EditText? = null
    var etApellidoPaterno: EditText? = null
    var etApellidoMaterno: EditText? = null
    var etCorreo: EditText? = null
    var etContraseña: EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)

        //iniciar y obtener usuario del sharedPreference
        sharedPreferences = getSharedPreferences("usuario", Context.MODE_PRIVATE)
        println(sharedPreferences.getString("phone", null)!!)
        println(sharedPreferences.getString("pwd", null)!!)
        println(sharedPreferences.getString("uid", null)!!)

        //campos del formulario para obtener sus datos
        etNombre = findViewById(R.id.etNombre)
        etApellidoPaterno = findViewById(R.id.etApellidoPaterno)
        etApellidoMaterno = findViewById(R.id.etApellidoMaterno)
        etCorreo = findViewById(R.id.etCorreo)
        etContraseña = findViewById(R.id.etContraseña)

        //Desactivar inicialmente los editText
        etNombre?.isEnabled = false
        etApellidoPaterno?.isEnabled = false
        etApellidoMaterno?.isEnabled = false
        etCorreo?.isEnabled = false
        etContraseña?.isEnabled = false

        println(etNombre?.text.toString())

        //Listener temporal para abrir ventana hacer reporte, a elimirse
        val temp =findViewById<ImageView>(R.id.imageView)
        temp.setOnClickListener {
            val i = Intent(this@userProfile, nuevo_reporte::class.java)
            startActivity(i)
        }

        //Listener temporal para abrir ventana hacer reporte, a elimirse
        val temp2 =findViewById<TextView>(R.id.tvTitle)
        temp2.setOnClickListener {
            val i = Intent(this@userProfile, MainActivity::class.java)
            startActivity(i)
        }

        //Listener boton editar1
        val edit1 =findViewById<ImageView>(R.id.iVe1)
        edit1.setOnClickListener {
            etNombre?.isEnabled = true
        }

        //Listener boton editar2
        val edit2 =findViewById<ImageView>(R.id.iVe2)
        edit2.setOnClickListener {
            etApellidoPaterno?.isEnabled = true
        }

        //Listener boton editar3
        val edit3 =findViewById<ImageView>(R.id.iVe3)
        edit3.setOnClickListener {
            etApellidoMaterno?.isEnabled = true
        }

        //Listener boton editar4
        val edit4 =findViewById<ImageView>(R.id.iVe4)
        edit4.setOnClickListener {
            etCorreo?.isEnabled = true
        }

        //Listener boton editar5
        val edit5 =findViewById<ImageView>(R.id.iVe5)
        edit5.setOnClickListener {
            etContraseña?.isEnabled = true
        }

        //Listener boton guardarCambios
        val botonGuardarCambios =findViewById<Button>(R.id.btSaveChanges)
        botonGuardarCambios.setOnClickListener {
            actualizarDatos()
        }

        //Listener eliminar cuenta, actualmente funciona como logout.
        val eliminarCuenta =findViewById<Button>(R.id.btDeleteAccount)
        eliminarCuenta.setOnClickListener {

            // on below line we are creating a variable for
            // editor of shared preferences and initializing it.
            val editor: SharedPreferences.Editor = sharedPreferences.edit()

            // on below line we are clearing our editor.
            editor.clear()

            // on below line we are applying changes which are cleared.
            editor.apply()

            // on below line we are opening our mainactivity by calling intent
            val i = Intent(this@userProfile, loginScreen::class.java)
            i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(i)
            finish()
        }



        //Llenar con los datos del usuario haciendo llamada a BD
        val queue = Volley.newRequestQueue(this)
        val url = "http://${resources.getString(R.string.server_ip)}/rest/perfilUsuario.php?telefono=${sharedPreferences.getString("phone", null)!!}"
        println(url)
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            Response.Listener { response ->
                //Si se obtuvo respuesta se colocan los campos en el editText
                etNombre?.setText(response.getString("nombre"))
                etApellidoPaterno?.setText(response.getString("apellido_paterno"))
                etApellidoMaterno?.setText(response.getString("apellido_materno"))
                etCorreo?.setText(response.getString("correo"))
                etContraseña?.setText(response.getString("contrasena"))
            },
            Response.ErrorListener{ error ->
                Toast.makeText(this,"Error de conexion ${error.toString()}", Toast.LENGTH_LONG).show()
            }
        )
        queue.add(jsonObjectRequest)

    }

    fun actualizarDatos(){
        //url de archivo para insertar en PHP
        val url = "http://${resources.getString(R.string.server_ip)}/rest/updatePerfil.php"
        val queue = Volley.newRequestQueue(this)
        var resultadoPost = object : StringRequest(Request.Method.POST, url,
            Response.Listener<String>{ response ->
                Toast.makeText(this,"Datos actualizados correctamente",Toast.LENGTH_LONG).show()
                println("Datos actualizados correctamente")
                //Faltaria cambiar la shared preference

            }, Response.ErrorListener{ error ->
                Toast.makeText(this,"Error de conexión, reintentelo mas tarde",Toast.LENGTH_LONG).show()
                println("actualizacion fallida")
                finish()
            }){
            override fun getParams(): MutableMap<String, String>? {
                val parametros = HashMap<String,String>()
                parametros["nombre"] = etNombre?.text.toString()
                parametros["apellido_paterno"] = etApellidoPaterno?.text.toString()
                parametros["apellido_materno"] = etApellidoMaterno?.text.toString()
                parametros["correo"] = etCorreo?.text.toString()
                parametros["contraseña"] = etContraseña?.text.toString()
                parametros["telefono"] = sharedPreferences.getString("phone", null)!!
                println(intent.getStringExtra("telefono").toString().substring(3))
                return parametros
            }
        }
        queue.add(resultadoPost)
    }

}