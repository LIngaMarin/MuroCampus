package com.ingamarin.murocampus.data

import com.ingamarin.murocampus.data.remote.PostApi
import com.ingamarin.murocampus.model.Comentario
import com.ingamarin.murocampus.model.NuevoPost
import com.ingamarin.murocampus.model.Post
import kotlin.coroutines.cancellation.CancellationException

/**
 * Contrato que usa el ViewModel. No sabe nada de Retrofit:
 * cada funcion devuelve Result (exito o fallo), nunca lanza la excepcion.
 */
interface PostRepository {
    suspend fun listar(): Result<List<Post>>
    suspend fun detalle(id: Int): Result<Post>
    suspend fun comentarios(id: Int): Result<List<Comentario>>
    suspend fun porUsuario(userId: Int): Result<List<Post>>
    suspend fun crear(nuevo: NuevoPost): Result<Post>
}

/** Implementacion que pide los datos al servicio web con Retrofit. */
class PostRepositoryRemote(private val api: PostApi) : PostRepository {

    override suspend fun listar() = llamar { api.listar() }

    override suspend fun detalle(id: Int) = llamar { api.detalle(id) }

    override suspend fun comentarios(id: Int) = llamar { api.comentarios(id) }

    override suspend fun porUsuario(userId: Int) = llamar { api.porUsuario(userId) }

    override suspend fun crear(nuevo: NuevoPost) = llamar { api.crear(nuevo) }

    /**
     * Un solo try/catch para las cinco funciones.
     * IOException (sin internet), HttpException (404, 500...) o SerializationException
     * se convierten en Result.failure: ninguna excepcion llega al ViewModel.
     */
    private suspend fun <T> llamar(bloque: suspend () -> T): Result<T> =
        try {
            Result.success(bloque())
        } catch (e: CancellationException) {
            throw e // nunca se traga la cancelacion de la corrutina
        } catch (e: Exception) {
            Result.failure(e)
        }
}
