package com.ingamarin.murocampus

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ingamarin.murocampus.ui.PantallaCrear
import com.ingamarin.murocampus.ui.PantallaDetalle
import com.ingamarin.murocampus.ui.PantallaLista
import com.ingamarin.murocampus.ui.theme.MuroCampusTheme
import com.ingamarin.murocampus.viewmodel.PostsViewModel
import com.ingamarin.murocampus.viewmodel.PostsViewModelFactory

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MuroCampusTheme {
                val vm: PostsViewModel = viewModel(factory = PostsViewModelFactory.Factory)

                // Navegacion simple: null = lista; un numero = detalle de esa publicacion
                var idSeleccionado by rememberSaveable { mutableStateOf<Int?>(null) }
                var creando by rememberSaveable { mutableStateOf(false) } // true = formulario de crear
                val estadoScroll = rememberLazyListState()

                // Boton Atras del sistema: desde el detalle o el formulario vuelve a la lista (sin recargarla)
                BackHandler(enabled = idSeleccionado != null || creando) {
                    idSeleccionado = null
                    creando = false
                }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        TopAppBar(
                            title = { Text("MuroCampus", fontWeight = FontWeight.Bold) },
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                titleContentColor = MaterialTheme.colorScheme.onPrimary
                            )
                        )
                    },
                    floatingActionButton = {
                        // Boton + solo en la lista: abre el formulario de crear
                        if (idSeleccionado == null && !creando) {
                            FloatingActionButton(onClick = {
                                vm.reiniciarPublicacion()
                                creando = true
                            }) {
                                Text("+", style = MaterialTheme.typography.headlineMedium)
                            }
                        }
                    }
                ) { innerPadding ->
                    val id = idSeleccionado
                    if (creando) {
                        PantallaCrear(vm, Modifier.padding(innerPadding))
                    } else if (id == null) {
                        PantallaLista(
                            vm = vm,
                            onAbrir = { nuevoId ->
                                idSeleccionado = nuevoId
                                vm.abrir(nuevoId)
                            },
                            modifier = Modifier.padding(innerPadding),
                            estadoScroll = estadoScroll
                        )
                    } else {
                        PantallaDetalle(id, vm, Modifier.padding(innerPadding))
                    }
                }
            }
        }
    }
}
