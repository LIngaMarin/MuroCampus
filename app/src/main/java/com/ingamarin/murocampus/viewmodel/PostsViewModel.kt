package com.ingamarin.murocampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ingamarin.murocampus.data.PostRepository
import com.ingamarin.murocampus.model.Comentario
import com.ingamarin.murocampus.model.Post
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/** Lo que muestra la pantalla de detalle: la publicacion y sus comentarios. */
data class DetalleUi(
    val post: Post,
    val comentarios: List<Comentario>
)

/**
 * Solo conoce la interface PostRepository: nada de Retrofit, PostApi, HttpException ni Context.
 * Convierte cada Result en un UiState que la pantalla dibuja.
 */
class PostsViewModel(private val repo: PostRepository) : ViewModel() {

    // ---- Lista de publicaciones ----
    private val _lista = MutableStateFlow<UiState<List<Post>>>(UiState.Cargando)
    val lista: StateFlow<UiState<List<Post>>> = _lista.asStateFlow()

    // ---- Filtro por usuario: null significa Todos ----
    private val _usuarioFiltro = MutableStateFlow<Int?>(null)
    val usuarioFiltro: StateFlow<Int?> = _usuarioFiltro.asStateFlow()

    private var trabajoLista: Job? = null

    init { cargar() }

    /**
     * Pide la lista segun el filtro: Cargando -> Exito o Error.
     * Tambien lo usa el boton Reintentar (respeta el filtro elegido).
     */
    fun cargar() {
        trabajoLista?.cancel() // si el usuario cambia rapido de chip, solo vale la ultima peticion
        trabajoLista = viewModelScope.launch {
            _lista.value = UiState.Cargando
            val userId = _usuarioFiltro.value
            val resultado = if (userId == null) {
                repo.listar()                 // GET posts
            } else {
                repo.porUsuario(userId)       // GET posts?userId=N
            }
            resultado
                .onSuccess { _lista.value = UiState.Exito(it) }
                .onFailure { _lista.value = UiState.Error(mensajeDe(it)) }
        }
    }

    /** Guarda el filtro y recarga la lista. */
    fun filtrar(userId: Int?) {
        _usuarioFiltro.value = userId
        cargar()
    }

    // ---- Detalle de una publicacion ----
    private val _detalle = MutableStateFlow<UiState<DetalleUi>>(UiState.Cargando)
    val detalle: StateFlow<UiState<DetalleUi>> = _detalle.asStateFlow()

    private var trabajoDetalle: Job? = null

    /**
     * Pide la publicacion (GET posts/{id}) y sus comentarios (GET posts/{id}/comments) A LA VEZ
     * con async. Si cualquiera de las dos falla, todo el detalle muestra el error.
     */
    fun abrir(id: Int) {
        trabajoDetalle?.cancel() // si habia otro detalle cargando, se cancela
        trabajoDetalle = viewModelScope.launch {
            _detalle.value = UiState.Cargando
            coroutineScope {
                val postDiferido = async { repo.detalle(id) }
                val comentariosDiferido = async { repo.comentarios(id) }
                val post = postDiferido.await()
                val comentarios = comentariosDiferido.await()

                val error = post.exceptionOrNull() ?: comentarios.exceptionOrNull()
                _detalle.value = if (error == null) {
                    UiState.Exito(DetalleUi(post.getOrThrow(), comentarios.getOrThrow()))
                } else {
                    UiState.Error(mensajeDe(error))
                }
            }
        }
    }
}
