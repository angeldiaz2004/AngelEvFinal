package com.ang.ec4angel.data.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ang.ec4angel.data.repository.AnimeRepository
import com.ang.ec4angel.models.AnimeChan
import kotlinx.coroutines.launch

class AnimeViewModel : ViewModel() {
    private val repository = AnimeRepository()

    private val _quotes = MutableLiveData<List<AnimeChan>>()
    val quotes: LiveData<List<AnimeChan>> = _quotes

    fun getQuotesByAnime(title: String, page: Int) {
        viewModelScope.launch {
            _quotes.value = repository.getQuotesByAnime(title, page)
        }
    }

}