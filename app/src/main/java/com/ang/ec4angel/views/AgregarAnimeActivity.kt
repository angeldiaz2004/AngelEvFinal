package com.ang.ec4angel.views

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.ang.ec4angel.databinding.ActivityAgregarAnimeBinding
import com.ang.ec4angel.models.Anime
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class AgregarAnimeActivity : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    private lateinit var binding: ActivityAgregarAnimeBinding
    private val PICK_IMAGE_REQUEST = 1
    private var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAgregarAnimeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = FirebaseFirestore.getInstance()

        binding.btnAgregar.setOnClickListener {
            agregarDatos()
            // Notificar al fragmento que se agregó un nuevo anime
            val data = Intent()
            setResult(RESULT_OK, data)
            finish()
        }

        binding.btnSeleccionarFoto.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                abrirImagenes()
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    PICK_IMAGE_REQUEST
                )
            }
        }
    }

    private fun agregarDatos(){
        val titulo = binding.editTitulo.text.toString()
        val genero = binding.editGenero.text.toString()
        val episodios = binding.edtiEpisodios.text.toString().toIntOrNull() ?: 0
        val anio = binding.editAnio.text.toString().toIntOrNull() ?: 0
        val rating = binding.editRating.text.toString().toDoubleOrNull() ?: 0.0

        val animeId = db.collection("animes").document().id
        val anime = Anime(animeId, titulo, genero, episodios, anio, rating, "")

        // Agregar a Firestore
        db.collection("animes").document(animeId).set(anime)
            .addOnSuccessListener {
                if (imageUri != null) {
                    cargarImagenDeStorage(animeId)
                } else {
                    Toast.makeText(this, "Se ha registrado con éxito", Toast.LENGTH_SHORT).show()
                    setResult(Activity.RESULT_OK)
                    finish()
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Error al registrar", Toast.LENGTH_SHORT).show()

            }
    }

    private fun cargarImagenDeStorage(animeId: String) {
        val storageReference = FirebaseStorage.getInstance().reference.child("animes/$animeId.jpg")

        storageReference.putFile(imageUri!!)
            .addOnSuccessListener {
                storageReference.downloadUrl.addOnSuccessListener { uri ->
                    val imageUrl = uri.toString()
                    guardarUrlEnFirestore(animeId, imageUrl)
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Error al subir la imagen", Toast.LENGTH_SHORT).show()
                setResult(Activity.RESULT_OK)
                finish()
            }
    }

    private fun guardarUrlEnFirestore(animeId: String, imageUrl: String) {
        val animeRef = db.collection("animes").document(animeId)
        val updates = hashMapOf<String, Any>("foto" to imageUrl)

        animeRef.update(updates)
            .addOnSuccessListener {
                Toast.makeText(this, "Imagen subida y URL guardada con éxito", Toast.LENGTH_SHORT).show()
                setResult(Activity.RESULT_OK)
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Error al guardar la URL de la imagen en Firestore", Toast.LENGTH_SHORT).show()
                setResult(Activity.RESULT_OK)
                finish()
            }
    }

    private fun abrirImagenes() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            imageUri = data.data
            binding.imageView.setImageURI(imageUri) // Muestra la imagen seleccionada en un ImageView
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PICK_IMAGE_REQUEST) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                abrirImagenes()
            } else {
                Toast.makeText(this, "Permiso denegado. No se puede acceder a la Galería.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}