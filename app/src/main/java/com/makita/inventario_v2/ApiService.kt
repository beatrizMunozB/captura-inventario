package com.makita.inventario_v2

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

data class ItemResponse(
    var ubicacion: String,
    var descripcion: String,
    var item: String,
    var tipoItem: String
)

data class InventarioData(
    val id: Int,
    val nombre: String,
    val cantidad: Int
)

data class RegistraInventarioRequest
   (
    val Id : String,
    val Empresa: String,
    val FechaInventario : String,
    val TipoInventario  : String,
    val Bodega: String,
    val Clasif1 : String,
    val Ubicacion : String,
    val Item : String,
    val Cantidad : String,
    val Estado : String,
    val Usuario : String,
    val NombreDispositivo : String
 )


data class RegistraUbicacion2Request
    (

    val FechaInventario : String,
    val Item : String,
    val Ubicacion : String,
    val Usuario : String

)


data class UltimaResponse(
    var ubicacion: String
)

data class InventariadoResponse(
    var respuesta: String
)

data class ResponseUsuario(
    val status: Int,
    val data: UsuarioResponse
)

data class UsuarioResponse(
    val Empresa: String,
    val Usuario : String,
    val Capturador :  String,
    val Periodo : String,
    val Mes : String,
    val Fecha : String,
    val tipoProducto : String
)

interface ApiService
{

    @GET("http://172.16.1.206:3024/api/obtener-ubicacion/{item}")
    suspend fun obtenerUbicacionItem(@Path("item") item: String) : List<ItemResponse>

    @POST("api/insertar-inventario")
    suspend fun insertarinventario(@Body request: RegistraInventarioRequest): Response<Unit>


    //@GET("api/insertar-inventario/{tipoinventario}/{tipoitem}")
    //suspend fun obtenerUltimaUbicacion(@Path("tipoinventario") tipoinventario: String) : List<UltimaResponse>

    @GET("api/insertar-inventario/{tipoinventario}/{tipoitem}/{usuario}/{fechainventario}/{bodega}")
    suspend fun obtenerUltimaUbicacion(
        @Path("tipoinventario") tipoinventario: String,
        @Path("tipoitem") tipoitem: String,
        @Path("usuario") usuario: String,
        @Path("fechainventario") fechainventario: String,
        @Path("bodega") bodega: String,
    ): List<UltimaResponse>



    @GET("api/insertar-inventario/{fechainventario}/{item}/{ubicacion}/{usuario}")
    suspend fun validarUbicacionProducto(
        @Path("fechainventario") FechaInventario: String,
        @Path("item") Item: String,
        @Path("ubicacion") Ubicacion: String,
        @Path("usuario") Usuario: String
    ):  String


    @GET("api/insertar-inventario/{item}/{tipoitem}")
    suspend fun validarTipoItem(
        @Path("item") Item: String,
        @Path("tipoitem") tipoitem: String
    ):  String


    @GET("api/consultar-asignacion-filtro/{capturador}/{mes}/{periodo}")
    suspend fun obtenerUsuario(
        @Path("capturador") capturador: String
        ,@Path("mes") mes: String
        ,@Path("periodo") periodo: String
    ) : ResponseUsuario

}