package com.example.vialidolid

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner

class newAccount : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_account)


        //Obtiene la lista de estados que se encuentra dentro de strings.xml
        var arrayEstado = resources.getStringArray(R.array.estados)
        var spinnerEstado = findViewById<Spinner>(R.id.spEstado)
        var estadoAdapter = ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,
            arrayEstado)
        spinnerEstado.adapter = estadoAdapter
        spinnerEstado.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                showCiudades(position, arrayEstado)
            }
        }

    }

    fun showCiudades(position: Int, arrayEstado: Array<String>){
        //Generar nombre del array de ciudades tienen estructura cd+Estado ej.cdMichoacan
        var arrayCall = "cd"+ arrayEstado[position]
        var spinnerCiudad = findViewById<Spinner>(R.id.spCiudad)
        var ciudadAdapter = ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,
            resources.getStringArray(resources.getIdentifier(arrayCall, "array", getPackageName())))
        spinnerCiudad.adapter = ciudadAdapter
    }




}


