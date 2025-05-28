package com.makita.InventarioDirigido

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

data class VerListadoResponse
    (
    val Clasif1 : String,
    val NumeroReconteo : String,
    val Item : String,
    val Ubicacion : String
)

data class VerListadoResponse2
    (
    var Clasif1 : String,
    var NumeroReconteo : String,
    var Item : String,
    var Ubicacion : String,
    var Cantidad : String
)


data class VerListadoResponse3
    (
    var Clasif1 : String,
    var NumeroReconteo : String,
    var Item : String,
    var Ubicacion : String,
    var Cantidad : String,
    var FechaInventario : String,
)


data class ReconteoResponse2(
    val exito: Boolean,
    val mensaje: String
)


data class ReconteoResponse(
    val clasif1: String,
    val Item: String,
    val ubicacion: String
)

data class Item(
    val Item: String,
    var Ubicacion: String,  // debe poder ser modificable si vas a actualizarla
    val Clasif1: String = ""

)


data class GrupoBodegaResponse(
    val GrupoBodega: String,
    val NombreGrupoBodega: String

)

data class CategoriaResponse(
    val Codigo: String,
    val Descripcion: String
)



data class UltimaResponse(
    var ubicacion: String
)


data class ListaDatos(
    var id: String,
    var item: String,
    var cantidad: String,
    var ubicacion: String,
    var FechaInventario: String
)



data class InventariadoResponse(
    var respuesta: String
)

data class RespuestaPreconteo(
    val resultado: Int,
    val mensaje: String
)

data class ItemConCantidad(
    val tipoitem: String,
    val numeroreconteo: String,
    val ubicacion: String,
    val item: String,
    var cantidad: String = ""  // Cantidad ingresada por el usuario (inicialmente vacía)
)

data class ItemConCantidadHE(
    val tipoitem: String,
    val numeroreconteo: String,
    val ubicacion: String,
    val item: String,
    var cantidad: String,
    var fechainventario :String
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



     @GET("api/insertar-inventario-categoria/{empresa}")
    //@GET("http://172.16.1.213:3001/api/insertar-inventario-categoria/{empresa}")
    suspend fun obtenerCategoria(
        @Path("empresa") Empresa: String
    ):  List<CategoriaResponse>

    // cambiar a servidor
    @GET("api/insertar-inventario-categoria/{empresa}/{agno}/{mes}/{fechainventario}/{tipoinventario}/{numerolocal}/{tipoitem}/{usuario}/{grupobodega}")
    suspend fun obtenerReconteo99(
        @Path("empresa") Empresa: String,
        @Path("agno") Agno: String,
        @Path("mes") Mes: String,
        @Path("fechainventario") fechainventario: String,
        @Path("tipoinventario") TipoInventario: String,
        @Path("numerolocal") NumeroLocal: String,
        @Path("tipoitem") TipoItem: String,
        @Path("usuario") Usuario: String,
        @Path("grupobodega") GrupoBodega: String,
    ):  List<ItemsReconteoResponse>


    @POST("api/insertar-inventario-categoria")
    suspend fun updateReconteo99(@Body request: RegistraReconteoRequest): ReconteoResponse2

    @GET("api/insertar-inventario-vercapturaH/{tipoinventario}/{tipoitem}/{usuario}/{fechainventario}/{local}")
    suspend fun VerLoCapturado(
        @Path("tipoinventario") tipoinventario: String,
        @Path("tipoitem") tipoitem: String,
        @Path("usuario") usuario: String,
        @Path("fechainventario") fechainventario: String,
        @Path("local") bodega: String
    ): List<VerListadoResponse3>

    @GET("api/insertar-inventario-vercaptura/{tipoinventario}/{tipoitem}/{usuario}/{fechainventario}/{local}")
    suspend fun VerLoCapturadoReconteo(
        @Path("tipoinventario") tipoinventario: String,
        @Path("tipoitem") tipoitem: String,
        @Path("usuario") usuario: String,
        @Path("fechainventario") fechainventario: String,
        @Path("local") local: String
    ): List<VerListadoResponse2>




}