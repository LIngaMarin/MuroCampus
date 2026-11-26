package com.ingamarin.murocampus.model

import kotlinx.serialization.Serializable

// Cada data class representa el JSON del servicio.
// Los nombres de las propiedades son IGUALES a los del JSON (si no, SerializationException).

/** GET posts y GET posts/{id} */
@Serializable
data class Post(
    val userId: Int,
    val id: Int,
    val title: String,
    val body: String
)

/** GET posts/{id}/comments */
@Serializable
data class Comentario(
    val postId: Int,
    val id: Int,
    val name: String,
    val email: String,
    val body: String
)

/** Lo que enviamos en el POST: no lleva id (el servidor lo asigna). */
@Serializable
data class NuevoPost(
    val userId: Int,
    val title: String,
    val body: String
)

/** [E] Ejemplo resuelto del proyecto base: GET users */
@Serializable
data class Usuario(
    val id: Int,
    val name: String,
    val email: String
)
