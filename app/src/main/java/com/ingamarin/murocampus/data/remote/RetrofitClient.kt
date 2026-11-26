package com.ingamarin.murocampus.data.remote

import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

/**
 * Cliente Retrofit unico para toda la app (object = una sola instancia).
 * OkHttp hace la conexion y kotlinx.serialization convierte el JSON en data class.
 */
object RetrofitClient {

    private const val URL_BASE = "https://jsonplaceholder.typicode.com/" // termina con /

    // ignoreUnknownKeys: si el servidor agrega un campo nuevo, la app no falla
    private val json = Json { ignoreUnknownKeys = true }

    // [Fase 2] Interceptor que agrega el encabezado Accept: application/json a TODAS las peticiones
    private val encabezadoJson = Interceptor { chain ->
        val peticion = chain.request().newBuilder()
            .header("Accept", "application/json")
            .build()
        chain.proceed(peticion)
    }

    // Registro del trafico HTTP en Logcat (filtro "okhttp"). Solo para desarrollo.
    // El de encabezado va primero para que el log muestre la peticion ya con el Accept.
    private val cliente = OkHttpClient.Builder()
        .addInterceptor(encabezadoJson)
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .build()

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(URL_BASE)
            .client(cliente)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
    }

    /** Se crea una sola vez, la primera vez que se usa (by lazy). */
    val api: PostApi by lazy { retrofit.create(PostApi::class.java) }

    /** [E] Ejemplo resuelto del proyecto base. */
    val usuarioApi: UsuarioApi by lazy { retrofit.create(UsuarioApi::class.java) }
}
