package com.ingamarin.murocampus.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.ingamarin.murocampus.ui.theme.EtiquetaAzul
import com.ingamarin.murocampus.ui.theme.EtiquetaNaranja

/**
 * [E] Tarjeta de una publicacion: etiqueta "Usuario N", titulo en negrita y el texto.
 * En la lista se usa con maxLineas = 2; en el detalle no se limita.
 */
@Composable
fun TarjetaPost(
    userId: Int,
    titulo: String,
    texto: String,
    modifier: Modifier = Modifier,
    maxLineas: Int = 2,
    onClick: (() -> Unit)? = null
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .then(if (onClick != null) Modifier.clickable { onClick() } else Modifier),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(Modifier.padding(12.dp)) {
            EtiquetaUsuario(userId)
            Spacer(Modifier.height(6.dp))
            Text(
                text = titulo,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                maxLines = maxLineas,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = texto,
                style = MaterialTheme.typography.bodySmall,
                maxLines = maxLineas,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

/** [E] Etiqueta de color con "Usuario N" (los usuarios pares e impares alternan color). */
@Composable
fun EtiquetaUsuario(userId: Int) {
    val fondo = if (userId % 2 == 0) EtiquetaNaranja else EtiquetaAzul
    Text(
        text = "Usuario $userId",
        color = Color.White,
        style = MaterialTheme.typography.labelSmall,
        modifier = Modifier
            .background(fondo, RoundedCornerShape(6.dp))
            .padding(horizontal = 8.dp, vertical = 2.dp)
    )
}

/** [E] Indicador de carga centrado con su texto. */
@Composable
fun Cargando(
    texto: String = "Cargando publicaciones…",
    modifier: Modifier = Modifier
) {
    Box(modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            CircularProgressIndicator()
            Spacer(Modifier.height(12.dp))
            Text(texto, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

/** [E] Recuadro de error con el mensaje y el boton Reintentar. */
@Composable
fun ErrorConReintento(
    mensaje: String,
    onReintentar: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier.fillMaxSize().padding(24.dp), contentAlignment = Alignment.Center) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = mensaje,
                color = MaterialTheme.colorScheme.onErrorContainer,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.errorContainer, RoundedCornerShape(10.dp))
                    .padding(16.dp)
            )
            Button(onClick = onReintentar) { Text("Reintentar") }
        }
    }
}
