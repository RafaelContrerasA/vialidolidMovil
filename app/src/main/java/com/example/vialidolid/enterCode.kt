package com.example.vialidolid

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import java.util.concurrent.TimeUnit

// this stores the phone number of the user

class enterCode : AppCompatActivity() {
    var number : String =""

    // create instance of firebase auth
    lateinit var auth: FirebaseAuth

    // we will use this to match the sent otp from firebase
    lateinit var storedVerificationId:String
    lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    private lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_enter_code)
        //Colocar el telefono del usuario en pantalla
        var tvPrompt = findViewById<TextView>(R.id.tvPrompt)
        tvPrompt.text = "Se envió un código de confirmación a su teléfono (${intent.getStringExtra("telefono")})"

        auth=FirebaseAuth.getInstance()

        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            // This method is called when the verification is completed
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                //startActivity(Intent(applicationContext, MainActivity::class.java))
                //finish()
                Log.d("GFG" , "onVerificationCompleted Success")
                println("onVerificationCompleted Success")
            }

            // Called when verification is failed add log statement to see the exception
            override fun onVerificationFailed(e: FirebaseException) {
                Log.d("GFG" , "onVerificationFailed  $e")
                println("onVerificationFailed")
            }

            // On code is sent by the firebase this method is called
            // in here we start a new activity where user can enter the OTP
            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                Log.d("GFG","onCodeSent: $verificationId")
                println("Code sent")
                storedVerificationId = verificationId
                resendToken = token

                // Start a new activity using intent
                // also send the storedVerificationId using intent
                // we will use this id to send the otp back to firebase
                /*
                val intent = Intent(applicationContext,OtpActivity::class.java)
                intent.putExtra("storedVerificationId",storedVerificationId)
                startActivity(intent)
                finish()*/
            }
        }

        //Enviar codigo de autenticacion al iniciar la actividad
        login()

        //listener de boton confirmar
        // fill otp and call the on click on button
        findViewById<Button>(R.id.btConfirmar).setOnClickListener {
            val otp = findViewById<EditText>(R.id.etCode).text.trim().toString()
            if(otp.isNotEmpty()){
                val credential : PhoneAuthCredential = PhoneAuthProvider.getCredential(
                    storedVerificationId.toString(), otp)
                signInWithPhoneAuthCredential(credential)
            }else{
                Toast.makeText(this,"Ingrese el código", Toast.LENGTH_SHORT).show()
            }
        }
    }



    private fun login() {
        number = intent.getStringExtra("telefono").toString()

        // get the phone number from edit text and append the country cde with it
        if (number.isNotEmpty()){
            number = "+52$number"
            sendVerificationCode(number)
        }else{
            Toast.makeText(this,"Enter mobile number", Toast.LENGTH_SHORT).show()
        }
    }

    // this method sends the verification code
    // and starts the callback of verification
    // which is implemented above in onCreate
    private fun sendVerificationCode(number: String) {
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(number) // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this) // Activity (for callback binding)
            .setCallbacks(callbacks) // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
        Log.d("GFG" , "Auth started")
        println("Inicio autenticacion")
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    println("Verificacion dentro de funcion")
                    insertCiudadano()

                } else {
                    // Sign in failed, display a message and update the UI
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                        Toast.makeText(this,"Codigo incorrecto", Toast.LENGTH_SHORT).show()
                    }
                }
            }
    }

    fun insertCiudadano(){
        //url de archivo para insertar en PHP
        val url = "http://${resources.getString(R.string.server_ip)}/rest/insertCiudadano.php"
        val queue = Volley.newRequestQueue(this)
        var resultadoPost = object : StringRequest(Request.Method.POST, url,
            Response.Listener<String>{ response ->
                Toast.makeText(this,"Usuario Insertado",Toast.LENGTH_LONG).show()
                println("Ciudadano ingresado")
                val intent = Intent(this , accountCreated::class.java)
                startActivity(intent)
                finish()

            }, Response.ErrorListener{ error ->
                Toast.makeText(this,"Error de conexión, reintentelo mas tarde",Toast.LENGTH_LONG).show()
                println("Insercion fallida")
                finish()
            }){
            override fun getParams(): MutableMap<String, String>? {
                val parametros = HashMap<String,String>()
                parametros["nombre"] = intent.getStringExtra("nombre").toString()
                println(intent.getStringExtra("nombre").toString())
                parametros["apellido_paterno"] = intent.getStringExtra("apellido_paterno").toString()
                println(intent.getStringExtra("apellido_paterno").toString())
                parametros["apellido_materno"] = intent.getStringExtra("apellido_materno").toString()
                println(intent.getStringExtra("apellido_materno").toString())
                parametros["correo"] = intent.getStringExtra("correo").toString()
                println(intent.getStringExtra("correo").toString())
                parametros["contraseña"] = intent.getStringExtra("contraseña").toString()
                println(intent.getStringExtra("contraseña").toString())
                parametros["telefono"] = intent.getStringExtra("telefono").toString()
                println(intent.getStringExtra("contraseña").toString())
                parametros["estado"] = intent.getStringExtra("estado").toString()
                println(intent.getStringExtra("estado").toString())
                parametros["ciudad"] = intent.getStringExtra("ciudad").toString()
                println(intent.getStringExtra("ciudad").toString())
                parametros["n_penalizaciones"] = "0"
                return parametros
            }
        }
        queue.add(resultadoPost)
    }

}


