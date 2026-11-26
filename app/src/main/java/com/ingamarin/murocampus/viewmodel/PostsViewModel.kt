package com.ingamarin.murocampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ingamarin.murocampus.data.PostRepository
import com.ingamarin.murocampus.model.Post
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * Solo conoce la interface PostRepository: nada de Retrofit, PostApi, HttpException ni Context.
 * Convierte cada Result en un UiState que la pantalla dibuja.
 */
class PostsViewModel(private val repo: PostRepository) : ViewModel() {

    // ---- Lista de publicaciones ----
    private val _lista = MutableStateFlow<UiState<List<Post>>>(UiState.Cargando)
    val lista: StateFlow<UiState<List<Post>>> = _lista.asStateFlow()

    init { cargar() }

    /** Pide la lista: Cargando -> Exito o Error. Tambien lo usa el boton Reintentar. */
    fun cargar() {
        viewModelScope.launch {
            _lista.value = UiState.Cargando
            repo.listar()
                .onSuccess { _lista.value = UiState.Exito(it) }
                .onFailure { _lista.value = UiState.Error(mensajeDe(it)) }
        }
    }
}
