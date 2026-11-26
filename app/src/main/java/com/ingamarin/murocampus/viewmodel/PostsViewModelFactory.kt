package com.ingamarin.murocampus.viewmodel

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.ingamarin.murocampus.data.PostRepositoryRemote
import com.ingamarin.murocampus.data.remote.RetrofitClient

/** [E] Crea el PostsViewModel con su Repository (el unico lugar que junta Retrofit con el ViewModel). */
object PostsViewModelFactory {
    val Factory: ViewModelProvider.Factory = viewModelFactory {
        initializer { PostsViewModel(PostRepositoryRemote(RetrofitClient.api)) }
    }
}
