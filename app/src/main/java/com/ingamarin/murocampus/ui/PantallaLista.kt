package com.ingamarin.murocampus.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ingamarin.murocampus.viewmodel.PostsViewModel
import com.ingamarin.murocampus.viewmodel.UiState

/** Opciones de la fila de chips: null = Todos, luego Usuario 1 a Usuario 10. */
private val OPCIONES_FILTRO: List<Int?> = listOf(null) + (1..10)

/** Lista de publicaciones con su fila de filtros: solo dibuja el estado que le da el ViewModel. */
@Composable
fun PantallaLista(
    vm: PostsViewModel,
    onAbrir: (Int) -> Unit,
    modifier: Modifier = Modifier,
    estadoScroll: LazyListState = rememberLazyListState()
) {
    val estado by vm.listaVisible.collectAsStateWithLifecycle() // [Fase 2] lista ya filtrada por la busqueda
    val busqueda by vm.busqueda.collectAsStateWithLifecycle()
    val filtro by vm.usuarioFiltro.collectAsStateWithLifecycle()

    Column(modifier.fillMaxSize()) {

        // [Fase 2] Campo de busqueda por titulo (no llama a la API: filtra lo descargado)
        OutlinedTextField(
            value = busqueda,
            onValueChange = { vm.buscar(it) },
            label = { Text("Buscar por título") },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 4.dp)
        )

        // Fila de chips desplazable: Todos, Usuario 1 ... Usuario 10
        LazyRow(
            contentPadding = PaddingValues(horizontal = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(OPCIONES_FILTRO) { opcion ->
                FilterChip(
                    selected = filtro == opcion, // el activo se ve relleno
                    onClick = {
                        estadoScroll.requestScrollToItem(0) // la nueva lista empieza arriba
                        vm.filtrar(opcion)
                    },
                    label = { Text(if (opcion == null) "Todos" else "Usuario $opcion") }
                )
            }
        }

        when (val e = estado) {
            is UiState.Cargando -> Cargando()
            is UiState.Error -> ErrorConReintento(
                mensaje = e.mensaje,
                onReintentar = { vm.cargar() }
            )
            is UiState.Exito -> if (e.datos.isEmpty()) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Sin publicaciones", style = MaterialTheme.typography.bodyLarge)
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    state = estadoScroll, // al volver del detalle sigue en la misma posicion
                    contentPadding = PaddingValues(12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(e.datos, key = { it.id }) { post ->
                        TarjetaPost(
                            userId = post.userId,
                            titulo = post.title,
                            texto = post.body,
                            maxLineas = 2,
                            onClick = { onAbrir(post.id) }
                        )
                    }
                }
            }
        }
    }
}
