package com.example.vialidolid

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast

class reporte_comp : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reporte_comp)
        val btInicio = findViewById<Button>(R.id.btPInicio)
        btInicio.setOnClickListener(View.OnClickListener {
            val intent = Intent(this@reporte_comp,MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        })

        val btMReportes = findViewById<Button>(R.id.btMReportes)
        btMReportes.setOnClickListener(View.OnClickListener {
            val intent = Intent(this@reporte_comp,MisReportes::class.java)
            startActivity(intent)
            finish()
        })

    }

    override fun onBackPressed() {
        val intent = Intent(this@reporte_comp,MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }
}