package com.ingamarin.murocampus.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ingamarin.murocampus.viewmodel.PostsViewModel
import com.ingamarin.murocampus.viewmodel.UiState

/** Lista de publicaciones: solo dibuja el estado que le da el ViewModel. */
@Composable
fun PantallaLista(vm: PostsViewModel, modifier: Modifier = Modifier) {
    val estado by vm.lista.collectAsStateWithLifecycle()

    when (val e = estado) {
        is UiState.Cargando -> Cargando(modifier = modifier)
        is UiState.Error -> ErrorConReintento(
            mensaje = e.mensaje,
            onReintentar = { vm.cargar() },
            modifier = modifier
        )
        is UiState.Exito -> LazyColumn(
            modifier = modifier.fillMaxSize(),
            contentPadding = PaddingValues(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(e.datos, key = { it.id }) { post ->
                TarjetaPost(
                    userId = post.userId,
                    titulo = post.title,
                    texto = post.body,
                    maxLineas = 2
                )
            }
        }
    }
}
