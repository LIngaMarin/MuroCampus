package com.ingamarin.murocampus.viewmodel

/**
 * [E] Los tres estados de una pantalla que pide datos a internet.
 * La pantalla solo dibuja el estado con un when.
 */
sealed interface UiState<out T> {
    data object Cargando : UiState<Nothing>
    data class Exito<T>(val datos: T) : UiState<T>
    data class Error(val mensaje: String) : UiState<Nothing>
}

// La funcion mensajeDe(e) usa HttpException (Retrofit) y SerializationException (kotlinx),
// por eso se agrega en el Paso 1, junto con esas dependencias.
