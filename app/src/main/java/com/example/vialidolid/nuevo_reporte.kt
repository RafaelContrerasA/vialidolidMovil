package com.example.vialidolid

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Spinner

class nuevo_reporte : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nuevo_reporte2)

        val imagen=findViewById<ImageView>(R.id.ivImagenUsuario)
        imagen.setOnClickListener{
            val intent = Intent(this@nuevo_reporte, misReportes::class.java)
            startActivity(intent)
        }

        val arrayCategoria = resources.getStringArray(R.array.categoria)
        val spinnerCategoria = findViewById<Spinner>(R.id.spCategoria)
        val categoriaAdapter = ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item,
            arrayCategoria)
        spinnerCategoria.adapter = categoriaAdapter
        spinnerCategoria.setSelection(0)

        spinnerCategoria.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedItem = parent.getItemAtPosition(position).toString()
                when (selectedItem) {
                    "Alumbrado público" -> {

                        val intent = Intent(this@nuevo_reporte, reporte_alumbrado_publico::class.java)
                        startActivity(intent)
                        finish()
                    }
                    "Bache" -> {
                        val intent = Intent(this@nuevo_reporte,reporte_bache::class.java)
                        startActivity(intent)
                        finish()
                    }
                    "Maltrato animal" -> {
                        val intent = Intent(this@nuevo_reporte,reporte_maltrato_animal::class.java)
                        startActivity(intent)
                        finish()
                    }
                    "Reporte vial" -> {
                        val intent = Intent(this@nuevo_reporte,reporte_vial::class.java)
                        startActivity(intent)
                        finish()
                    }

                    "Suministro de agua" -> {
                        val intent = Intent(this@nuevo_reporte, reporte_suministro_agua::class.java)
                        startActivity(intent)
                        finish()
                    }

                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }
    }
}