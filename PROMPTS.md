# PROMPTS.md · Fase 2 (rama `mejora-ia`)

Mejoras de MuroCampus hechas con ayuda de IA, sin romper MVVM + Repository.

> **Nota honesta:** por falta de tiempo no usé Gemini en Android Studio. El asistente de IA que usé fue **Claude** (Anthropic), en un chat. Las filas de abajo son los pedidos reales que le hice. Revisé, probé y ajusté todo lo que generó.

| Prompt que escribí | Qué respondió la IA | Qué cambié o descarté | Por qué |
|---|---|---|---|
| «Agrega un Interceptor de OkHttp que ponga el encabezado `Accept: application/json` a todas las peticiones de MuroCampus.» | Un `Interceptor { chain -> chain.request().newBuilder().header(...) }` agregado al `OkHttpClient` de `RetrofitClient`. | Lo dejé **antes** del `HttpLoggingInterceptor` y lo comprobé en Logcat buscando `Accept`. | Si va después del de registro, el log no muestra el encabezado. Solo cambia `RetrofitClient` y no toca el ViewModel ni la UI. |
| «Agrega un botón Eliminar en el detalle con `@DELETE("posts/{id}")` que revise el código de respuesta con `Response<Unit>`, respetando MVVM + Repository.» | `borrar()` en `PostApi`, `borrar()` en el Repository que devuelve `Result<Int>` (el código HTTP), un `StateFlow` `borrado` en el ViewModel y el botón en `PantallaDetalle`. | Verifiqué que si `isSuccessful` es falso se lanza `HttpException(respuesta)` **dentro** de `llamar { }`, para que termine en `Result.failure` y no escape. Limpio el mensaje al abrir otro detalle. | Con `Response<Unit>` un 404 no lanza excepción solo. Hay que revisarlo a mano, si no se mostraría "eliminada" aunque falle. El ViewModel sigue sin conocer `Response` ni `HttpException`. |
| «Agrega un campo de búsqueda que filtre por título las publicaciones ya descargadas, sin volver a llamar a la API.» | `_busqueda` en el ViewModel y `listaVisible` con `combine(_lista, _busqueda)` + `stateIn`. En la pantalla, un `OutlinedTextField` sobre los chips. | La pantalla ahora observa `listaVisible` en vez de `lista`. Ignoro mayúsculas y espacios (`ignoreCase`, `trim`). Si no hay coincidencias se reutiliza el mensaje "Sin publicaciones". | El filtro es en memoria (más rápido y sin consumo de red), y el estado Cargando/Error se sigue mostrando igual porque solo se filtra cuando es `Exito`. |

## Revisión del código generado

- No hay `catch` vacío. Todo fallo termina en `Result.failure` y se relanza `CancellationException`.
- No se llama a la API desde un Composable. Las pantallas solo llaman funciones del ViewModel.
- No hay `GlobalScope` ni `runBlocking`. Todo usa `viewModelScope`.
- La URL base es `https://`.
- `PostsViewModel` no importa Retrofit, `PostApi`, `Response` ni `HttpException`.
