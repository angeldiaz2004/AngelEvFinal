package com.ang.ec4angel.views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.ang.ec4angel.databinding.ActivityLoginBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var fAuth: FirebaseAuth
    private lateinit var gLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fAuth = Firebase.auth

        gLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
            if (result.resultCode === RESULT_OK) {
                val tarea = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                try {
                    val cuenta = tarea.getResult(ApiException::class.java)
                    authenticateWithFireabse(cuenta.idToken)
                } catch (e: Exception) {

                }
            }
        }

        binding.btnIngresar.setOnClickListener {
            val password = binding.edtClave.text.toString()
            val email = binding.edtCorreo.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Por favor ingresa correo y contraseña", Toast.LENGTH_SHORT).show()
            } else {
                login(email, password)
            }
        }

        binding.btnSignUp.setOnClickListener {
            val password = binding.edtClave.text.toString()
            val email = binding.edtCorreo.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Por favor ingresa correo y contraseña", Toast.LENGTH_SHORT).show()
            } else {
                signUp(email, password)
            }
        }

        binding.btnGoogle.setOnClickListener {
            loguearConGoogle()
        }
    }

    private fun login(email: String, password: String) {
        fAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this){ task ->
                if (task.isSuccessful){
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this, "El usuario no está registrado", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun signUp(email: String, password: String) {
        fAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = fAuth.currentUser
                    Toast.makeText(this, "Usuario registrado correctamente", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Hubo un error...", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun authenticateWithFireabse(idToken: String?) {
        val authCredential = GoogleAuthProvider.getCredential(idToken, null)
        fAuth.signInWithCredential(authCredential)
            .addOnCompleteListener(this) { task ->
                if(task.isSuccessful) {
                    val user = fAuth.currentUser
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
    }

    private fun loguearConGoogle(){
        val googleSign = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken()
            .requestEmail()
            .build()

        val googleClient = GoogleSignIn.getClient(this, googleSign)
        val intent = googleClient.signInIntent
        gLauncher.launch(intent)
    }
}
