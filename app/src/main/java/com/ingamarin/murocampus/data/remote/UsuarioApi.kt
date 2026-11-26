package com.ingamarin.murocampus.data.remote

import com.ingamarin.murocampus.model.Usuario
import retrofit2.http.GET

/** [E] Ejemplo resuelto del proyecto base. */
interface UsuarioApi {
    @GET("users")
    suspend fun listar(): List<Usuario>
}
