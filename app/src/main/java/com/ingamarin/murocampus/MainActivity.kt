package com.ingamarin.murocampus

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.viewmodel.compose.viewModel
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
                    }
                ) { innerPadding ->
                    PantallaLista(vm, Modifier.padding(innerPadding))
                }
            }
        }
    }
}
