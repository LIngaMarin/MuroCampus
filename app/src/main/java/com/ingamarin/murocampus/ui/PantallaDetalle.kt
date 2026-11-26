package com.ingamarin.murocampus.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ingamarin.murocampus.model.Comentario
import com.ingamarin.murocampus.ui.theme.VerdeFondo
import com.ingamarin.murocampus.ui.theme.VerdeTexto
import com.ingamarin.murocampus.viewmodel.PostsViewModel
import com.ingamarin.murocampus.viewmodel.UiState

/** Detalle: la publicacion completa y debajo sus comentarios. */
@Composable
fun PantallaDetalle(id: Int, vm: PostsViewModel, modifier: Modifier = Modifier) {
    val estado by vm.detalle.collectAsStateWithLifecycle()
    val borrado by vm.borrado.collectAsStateWithLifecycle()

    when (val e = estado) {
        is UiState.Cargando -> Cargando("Cargando publicación…", modifier)
        is UiState.Error -> ErrorConReintento(
            mensaje = e.mensaje,
            onReintentar = { vm.abrir(id) },
            modifier = modifier
        )
        is UiState.Exito -> LazyColumn(
            modifier = modifier.fillMaxSize(),
            contentPadding = PaddingValues(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                // Titulo y texto COMPLETOS (sin limite de lineas)
                TarjetaPost(
                    userId = e.datos.post.userId,
                    titulo = e.datos.post.title,
                    texto = e.datos.post.body,
                    maxLineas = Int.MAX_VALUE
                )
            }
            item {
                // [Fase 2] Boton Eliminar (DELETE) y su resultado
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(
                        onClick = { vm.borrar(id) },
                        enabled = borrado !is UiState.Cargando,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.error,
                            contentColor = MaterialTheme.colorScheme.onError
                        )
                    ) { Text("Eliminar") }

                    when (val b = borrado) {
                        is UiState.Exito -> Text(
                            text = "Publicación eliminada (código ${b.datos})",
                            color = VerdeTexto,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(VerdeFondo, RoundedCornerShape(10.dp))
                                .padding(12.dp)
                        )
                        is UiState.Error -> Text(
                            text = b.mensaje,
                            color = MaterialTheme.colorScheme.onErrorContainer,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(MaterialTheme.colorScheme.errorContainer, RoundedCornerShape(10.dp))
                                .padding(12.dp)
                        )
                        else -> Unit
                    }
                }
            }
            item {
                Text(
                    text = "Comentarios (${e.datos.comentarios.size})",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
            items(e.datos.comentarios, key = { it.id }) { comentario ->
                TarjetaComentario(comentario)
            }
        }
    }
}

@Composable
private fun TarjetaComentario(comentario: Comentario) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = CardDefaults.outlinedCardBorder()
    ) {
        Column(Modifier.padding(12.dp)) {
            Text(
                text = comentario.email,
                color = MaterialTheme.colorScheme.primary, // correo en azul
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold
            )
            HorizontalDivider(Modifier.padding(vertical = 4.dp))
            Text(comentario.body, style = MaterialTheme.typography.bodySmall)
        }
    }
}
