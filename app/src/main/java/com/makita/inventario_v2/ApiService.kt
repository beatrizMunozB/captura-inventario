package com.makita.inventario_v2

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

data class ItemResponse(
    var ubicacion: String,
    var descripcion: String,
    var item: String,
    var tipoItem: String
)
data class ResponseUsuario(
    val status: Int,
    val data: UsuarioResponse
)

data class UsuarioResponse(
    val Empresa: String,
    val Usuario: String,
    val Capturador: String,
    val Periodo: String,
    val Mes: String,
    val Fecha: String,
    val TipoProducto: String
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

data class RegistraReconteoRequest
    (
    val Id : String,
    val Empresa: String,
    val Agno: String,
    val Mes: String,
    val FechaInventario : String,
    val TipoInventario  : String,
    val NumeroReconteo: String,
    val NumeroLocal : String,
    val GrupoBodega : String,
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

data class ItemsReconteoResponse
    (

    val Clasif1 : String,
    val NumeroReconteo : String,
    val Item : String,
    val Ubicacion : String


)

data class ReconteoResponse(
    val clasif1: String,
    val Item: String,
    val ubicacion: String
)


data class GrupoBodegaResponse(
    val GrupoBodega: String,
    val NombreGrupoBodega: String

)


data class UltimaResponse(
    var ubicacion: String
)

data class InventariadoResponse(
    var respuesta: String
)

data class ItemConCantidad(
    val tipoitem: String,
    val numeroreconteo: String,
    val ubicacion: String,
    val item: String,
    var cantidad: String = ""  // Cantidad ingresada por el usuario (inicialmente vacía)
)

interface ApiService
{

    @GET("http://172.16.1.206:3024/api/obtener-ubicacion/{item}")
    suspend fun obtenerUbicacionItem(@Path("item") item: String) : List<ItemResponse>

    @GET("http://172.16.1.206:3024/api/consultar-asignacion-filtro/{capturador}/{mes}/{periodo}")
    suspend fun obtenerUsuario(
         @Path("capturador") capturador: String
        ,@Path("mes") mes: String
        ,@Path("periodo") periodo: String
        ) : ResponseUsuario


    @POST("api/insertar-inventario")
    suspend fun insertarinventario(@Body request: RegistraInventarioRequest): Response<Unit>

    @POST("api/insertar-inventario-reconteo")
    suspend fun insertarReconteo(@Body request: RegistraReconteoRequest): Response<Unit>


    //@GET("api/insertar-inventario/{tipoinventario}/{tipoitem}")
    //suspend fun obtenerUltimaUbicacion(@Path("tipoinventario") tipoinventario: String) : List<UltimaResponse>

    @GET("api/insertar-inventario/{tipoinventario}/{tipoitem}/{usuario}/{fechainventario}/{bodega}")
    suspend fun obtenerUltimaUbicacion(
        @Path("tipoinventario") tipoinventario: String,
        @Path("tipoitem") tipoitem: String,
        @Path("usuario") usuario: String,
        @Path("fechainventario") fechainventario: String,
        @Path("bodega") bodega: String
    ): List<UltimaResponse>


    @GET("api/insertar-inventario/{fechainventario}/{item}/{ubicacion}/{usuario}")
    suspend fun validarUbicacionProducto(
        @Path("fechainventario") FechaInventario: String,
        @Path("item") Item: String,
        @Path("ubicacion") Ubicacion: String,
        @Path("usuario") Usuario: String
    ):  String

    /*
    @GET("api/insertar-inventario/{empresa}/{agno}/{mes}/{tipoinventario}/{numeroconteo}/{numerolocal}/{tipoitem}/{usuario}")
    suspend fun obtenerReconteo(
        @Path("empresa") Empresa: String,
        @Path("agno") Agno: String,
        @Path("mes") Mes: String,
        @Path("tipoinventario") TipoInventario: String,
        @Path("numeroconteo") NumeroConteo: String,
        @Path("numerolocal") NumeroLocal: String,
        @Path("tipoitem") TipoItem: String,
        @Path("usuario") Usuario: String,
        ):  List<ItemsReconteoResponse>

    */

    @GET("api/insertar-inventario/{empresa}/{agno}/{mes}/{tipoinventario}/{numerolocal}/{tipoitem}/{usuario}/{grupobodega}")
    suspend fun obtenerReconteo(
        @Path("empresa") Empresa: String,
        @Path("agno") Agno: String,
        @Path("mes") Mes: String,
        @Path("tipoinventario") TipoInventario: String,
        @Path("numerolocal") NumeroLocal: String,
        @Path("tipoitem") TipoItem: String,
        @Path("usuario") Usuario: String,
        @Path("grupobodega") GrupoBodega: String,
    ):  List<ItemsReconteoResponse>

    @GET("api/insertar-inventario-grupo/{empresa}/{numerolocal}")
    suspend fun obtenerGrupoBodega(
        @Path("empresa") Empresa: String,
        @Path("numerolocal") NumeroLocal: String,
        ):  List<GrupoBodegaResponse>




    @GET("api/insertar-inventario/{item}/{tipoitem}")
    suspend fun validarTipoItem(
        @Path("item") Item: String,
        @Path("tipoitem") tipoitem: String
    ):  String




    // @GET("api/generar-etiquetaC/{item}")
   // suspend fun obtenerHerramienta(@Path("item") item: String) : List<ItemResponse>

    //@GET("api/generar-etiquetaC/{item}")
    //suspend fun obtenerHerramienta(@Path("item") item: String) : List<ItemResponse>

   // @GET("api/obtener-ubicacion/{ubicacion}")
   // suspend fun obtenerUbicacion(@Path("ubicacion") ubicacion: String) : List<UbicacionResponse>



}