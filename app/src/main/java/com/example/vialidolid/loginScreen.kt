package com.example.vialidolid

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.sql.DriverManager
import java.sql.SQLException

class loginScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_screen)

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



        }
/*Funcion para jdbc, no se utilizo
fun buscar(){
    GlobalScope.launch{
        var connection = ConnectionHelper()
        connection.connect()

}


    }
*/
}