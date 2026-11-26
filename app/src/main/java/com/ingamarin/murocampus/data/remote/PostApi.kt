package com.ingamarin.murocampus.data.remote

import com.ingamarin.murocampus.model.Comentario
import com.ingamarin.murocampus.model.NuevoPost
import com.ingamarin.murocampus.model.Post
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Las llamadas del servicio JSONPlaceholder descritas con anotaciones de Retrofit.
 * Es solo una interface: Retrofit genera el codigo que hace las peticiones HTTP.
 * Las rutas NO empiezan con "/" (la barra reemplazaria el camino de la URL base).
 */
interface PostApi {

    /** GET posts -> las 100 publicaciones */
    @GET("posts")
    suspend fun listar(): List<Post>

    /** GET posts/1 -> @Path reemplaza {id} dentro de la ruta */
    @GET("posts/{id}")
    suspend fun detalle(@Path("id") id: Int): Post

    /** GET posts/1/comments -> los comentarios de esa publicacion */
    @GET("posts/{id}/comments")
    suspend fun comentarios(@Path("id") id: Int): List<Comentario>

    /** GET posts?userId=2 -> @Query agrega el parametro despues del ? */
    @GET("posts")
    suspend fun porUsuario(@Query("userId") userId: Int): List<Post>

    /** POST posts -> @Body envia el objeto como JSON en el cuerpo de la peticion */
    @POST("posts")
    suspend fun crear(@Body nuevo: NuevoPost): Post

    /** [Fase 2] DELETE posts/{id} -> respuesta vacia; Response permite revisar el codigo (200) */
    @DELETE("posts/{id}")
    suspend fun borrar(@Path("id") id: Int): Response<Unit>
}
