package com.example.vialidolid

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var btn = findViewById<Button>(R.id.btn)
        btn.setOnClickListener(View.OnClickListener { reporte() })
    }
    fun reporte(){
        val intent = Intent(this@MainActivity,nuevo_reporte::class.java)
        startActivity(intent)

    }
}