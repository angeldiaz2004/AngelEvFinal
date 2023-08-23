package com.ang.ec4angel.views.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.ang.ec4angel.data.adapter.AnimeFirebaseAdapter
import com.ang.ec4angel.databinding.FragmentFirebaseBinding
import com.ang.ec4angel.models.Anime
import com.ang.ec4angel.views.AgregarAnimeActivity
import com.google.firebase.firestore.FirebaseFirestore

class FirebaseFragment : Fragment() {

    private var _binding: FragmentFirebaseBinding? = null
    private val binding get() = _binding!!

    private lateinit var db: FirebaseFirestore
    private lateinit var adapter: AnimeFirebaseAdapter
    private val ADD_ANIME_REQUEST_CODE = 123

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFirebaseBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        db = FirebaseFirestore.getInstance()
        adapter = AnimeFirebaseAdapter()

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter

        binding.btnAddAnime.setOnClickListener {
            val intent = Intent(requireContext(), AgregarAnimeActivity::class.java)
            startActivityForResult(intent, ADD_ANIME_REQUEST_CODE)
        }

        listarAnimes()
    }

    private fun listarAnimes(){
        db.collection("animes").get()
            .addOnSuccessListener { result ->
                val animeList = ArrayList<Anime>()
                for (document in result) {
                    val anime = document.toObject(Anime::class.java)
                    animeList.add(anime)
                }
                adapter.setData(animeList)
            }
            .addOnFailureListener { exception ->
                Log.e("FirebaseFragment", "Error al obtener los animes", exception)
                Toast.makeText(requireContext(), "Ocurrió un error.", Toast.LENGTH_SHORT).show()
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == ADD_ANIME_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            listarAnimes()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
