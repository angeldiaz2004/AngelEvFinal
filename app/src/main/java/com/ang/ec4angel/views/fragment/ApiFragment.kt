package com.ang.ec4angel.views.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.ang.ec4angel.R
import com.ang.ec4angel.data.adapter.AnimeAdapter
import com.ang.ec4angel.data.viewmodel.AnimeViewModel
import com.ang.ec4angel.databinding.FragmentApiBinding


class ApiFragment : Fragment() {
    private val viewModel by viewModels<AnimeViewModel>()
    private lateinit var binding: FragmentApiBinding
    private val adapter = AnimeAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentApiBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter

        viewModel.quotes.observe(viewLifecycleOwner) { quotes ->
            adapter.setQuotes(quotes)
        }

        viewModel.getQuotesByAnime("naruto", 2)
    }
}