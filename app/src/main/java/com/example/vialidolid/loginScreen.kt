package com.example.vialidolid

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class loginScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_screen)

        val newAccountButton =findViewById<TextView>(R.id.tvNewAccount)
        newAccountButton.setOnClickListener{
            val Intent = Intent(this,newAccount::class.java)
            startActivity(Intent)

        }
    }
}