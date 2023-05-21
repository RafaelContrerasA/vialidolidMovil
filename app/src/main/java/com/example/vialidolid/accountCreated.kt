package com.example.vialidolid

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView

class accountCreated : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account_created)

        //Listener temporal para abrir ventana hacer reporte, a elimirse
        val back =findViewById<Button>(R.id.btLogin)
        back.setOnClickListener {
            val i = Intent(this@accountCreated, loginScreen::class.java)
            startActivity(i)
            finish()
        }
    }
}