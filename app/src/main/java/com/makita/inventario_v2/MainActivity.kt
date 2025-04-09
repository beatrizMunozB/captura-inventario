package com.makita.inventario_v2

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.DatePicker
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView

import androidx.core.view.WindowCompat
import com.makita.inventario_v2.ui.theme.Inventario_V2Theme
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextField
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment

import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext

import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import java.util.Calendar
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import java.io.File
import android.provider.Settings
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import java.io.IOException
import java.time.LocalDate
import java.time.format.DateTimeFormatter


class MainActivity : ComponentActivity()
{
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Inventario_V2Theme {
                AppNavigation()
            }
        }
    }
}

@Composable
fun CambiarColorBarraEstado(color: Color, darkIcons: Boolean = true) {
    val view = LocalView.current
    val composeColor = androidx.compose.ui.graphics.Color(0xFF00909E)
    val argbColor = android.graphics.Color.argb(
        (composeColor.alpha * 255).toInt(),
        (composeColor.red * 255).toInt(),
        (composeColor.green * 255).toInt(),
        (composeColor.blue * 255).toInt()
    )

    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = argbColor // Convierte a ARGB
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkIcons
        }
    }
}

@SuppressLint("SuspiciousIndentation")
@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    var selectedTipo by remember { mutableStateOf("") }
    var selectedOption by remember { mutableStateOf("") }
    val apiService = RetrofitClient.apiService
    var selectedDate by remember { mutableStateOf("") }


    NavHost(navController = navController, startDestination = "main_screen")
    {
        composable("main_screen") {
            MainScreen(navController)
        }

        composable(
            route = "second_screen/{param}/{param2}/{usuarioAsignado}",
            arguments = listOf(navArgument("param")  { type = NavType.StringType },
                               navArgument("param2") { type = NavType.StringType },
                navArgument("usuarioAsignado") { type = NavType.StringType }
                        )
        ) { backStackEntry ->


            //val param  = backStackEntry.arguments?.getString("param")
            //val param2 = backStackEntry.arguments?.getString("param2") ?: ""
            val param  = backStackEntry.arguments?.getString("param") ?: "DefaultParam"
            val param2 = backStackEntry.arguments?.getString("param2") ?: "DefaultParam2"
            val usuarioAsignado = backStackEntry.arguments?.getString("usuarioAsignado")

            if (usuarioAsignado != null) {
                SecondScreen(navController = navController, param = param, param2 = param2 , usuarioAsignado = usuarioAsignado)
            }


        }

        composable(
            route = "third_screen/{param}/{param2}/{usuarioAsignado}",
            arguments = listOf(navArgument("param")  { type = NavType.StringType },
                navArgument("param2") { type = NavType.StringType }
            )
        ) { backStackEntry ->


            val usuarioAsignado = backStackEntry.arguments?.getString("usuarioAsignado")
            val param  = backStackEntry.arguments?.getString("param") ?: "DefaultParam"
            val param2 = backStackEntry.arguments?.getString("param2") ?: "DefaultParam2"


            if (usuarioAsignado != null) {
                TerceraScreen(navController = navController, param = param, param2 = param2 , usuarioAsignado= usuarioAsignado)
            }




        }

    }



}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavController) {

    var selectedDate by remember { mutableStateOf("") }
    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    val context = LocalContext.current
    var selectedOption by remember { mutableStateOf("") } // Estado global
    var selectedTipo   by remember { mutableStateOf("") } // Estado global
    var selectedLocal  by remember { mutableStateOf("") } // Estado global
    var showError by remember { mutableStateOf(false) }
    val activity = context as? Activity
    var usuarioValido by remember { mutableStateOf(false) }

    var usuarioAsignado by remember { mutableStateOf("") }

    // Configuración del DatePickerDialog
    val datePickerDialog = DatePickerDialog(
        context,
        { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
            selectedDate = "$dayOfMonth/${month + 1}/$year"
        },
        year, month, day
    )

    CambiarColorBarraEstado(color = Color(0xFF00909E), darkIcons = true)

    Surface(
      shape = RoundedCornerShape(8.dp),
      modifier = Modifier.padding(16.dp) // Espaciado alrededor
    )

    {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top

        ) {

            Image(
                painter = painterResource(id = R.drawable.makitafondoblanco),
                contentDescription = "Logo",
                modifier = Modifier.size(120.dp),
                contentScale = ContentScale.Fit
            )

            Text(
                text = "APP Inventario, Registre parametros: ",
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            Spacer(modifier = Modifier.height(14.dp))

            UsuarioAsignadoScreen(
                context = LocalContext.current,
                onUsuarioAsignadoChange = {
                    nuevoUsuario ->
                    Log.d("*MAKITA*", "Nuevo usuario asignado: $nuevoUsuario")
                    usuarioAsignado = nuevoUsuario
                    usuarioValido = nuevoUsuario.isNotEmpty()
                }
            )

            Spacer(modifier = Modifier.height(14.dp))
            DatePickerWithTextField()

            Spacer(modifier = Modifier.height(16.dp))
            ComboBoxWithTextField(selectedOption = selectedOption,
                onOptionSelected = { selectedOption = it }

            )

            Spacer(modifier = Modifier.height(16.dp))
            ComboBoxTipoProducto(
                selectedOption = selectedTipo,
                onOptionSelected = { selectedTipo = it }
            )


            Spacer(modifier = Modifier.height(16.dp))
            ComboBoxLocal(
                selectedOption = selectedLocal,
                onOptionSelected = { selectedLocal = it }
            )

            Spacer(modifier = Modifier.height(18.dp))

            Log.d("*MAKITA*", "Opcion seleccionada $selectedOption")

            Button(onClick =
            {
                if (selectedOption.isEmpty()) {
                    showError = true // Muestra el error si no hay selección
                    Toast.makeText(context, "Campo Obligatorio, debe ingresar Tipo de Inventario", Toast.LENGTH_LONG).show()
                } else {
                    showError = false
                    // Acción adicional si la selección es válida
                    println("Opción seleccionada: $selectedOption")
                }

                if (selectedTipo == "ACCESORIOS" || selectedTipo == "REPUESTOS")
                {

                    Log.d("MAKITA" , "variables enviadas tercera $selectedTipo - $selectedLocal - $usuarioAsignado")
                    navController.navigate("third_screen/$selectedTipo/$selectedLocal/$usuarioAsignado")
                } else {
                    Log.d("MAKITA" , "variables enviadas segunda $selectedTipo - $selectedLocal - $usuarioAsignado")
                    navController.navigate("second_screen/$selectedTipo/$selectedLocal/$usuarioAsignado")
                }

            },
                enabled = selectedOption.isNotEmpty() && selectedTipo.isNotEmpty() && selectedLocal.isNotEmpty() && usuarioValido,
                colors = ButtonDefaults.buttonColors(
                    containerColor =  Color(0xFF00909E),
                    contentColor = Color.White),
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .height(60.dp)
            )
            {

                Text(text = " Inventario $selectedTipo",
                    color = Color.White,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(top = 8.dp))
            }

            Button(
                onClick = { activity?.finish() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF00909E),
                    contentColor = Color.White
                ),
                modifier = Modifier
                    .padding(horizontal = 8.dp, vertical = 4.dp)
                    .width(90.dp)
                    .height(45.dp)
                ,shape = RoundedCornerShape(8.dp),
            ) {
                Text(
                    text = "Salir",
                    color = Color.White,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(top = 8.dp)
                )

            }

            // Mensaje de error si no se selecciona una opción
            if (showError && selectedOption.isEmpty() && selectedTipo.isEmpty() && selectedLocal.isEmpty())
            {
                Text(
                    text = "Este campo es obligatorio",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                )
                mostrarDialogo(context, "Error", "Seleccione los campos obligatorios Tipo,TipoItem,Local")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UsuarioAsignadoScreen(
    context: Context,
    onUsuarioAsignadoChange: (String) -> Unit
) {

    val apiService = RetrofitClient.apiService
    var usuarioAsignado  by remember { mutableStateOf("") } // Estado global
    val gnombreDispositivo = remember { obtenerNombreDelDispositivo(context) }
    val context = LocalContext.current
    val activity = context as? Activity

    LaunchedEffect(Unit) {
                try {
                    val fechaActual = LocalDate.now()
                    val mes = fechaActual.format(DateTimeFormatter.ofPattern("MM")) // Obtiene el mes actual con dos dígitos
                    val periodo = fechaActual.year.toString() // Obtiene el año actual

                    Log.d("*MAKITA*", "Iniciando petición a la API...")

                    val respuesta01 = withContext(Dispatchers.IO) {
                        Log.d("*MAKITA*", "Llamando a API con: gnombreDispositivo=$gnombreDispositivo, mes=$mes, periodo=$periodo")


                        apiService.obtenerUsuario(gnombreDispositivo, mes, periodo)

                    }
                    Log.d("*MAKITA" , "respuesta01 : ${respuesta01}")
                    usuarioAsignado = respuesta01.data.Usuario
                    onUsuarioAsignadoChange(usuarioAsignado)

                    Log.d("*MAKITA*", "Usuario obtenido exitosamente: $usuarioAsignado ")

                } catch (e: IOException) {
                    Log.e("*MAKITA*", "Error de red: No hay conexión a Internet", e)
                    mostrarDialogo(context, "Error", "Error de red: No hay conexión a Internet")

                } catch (e: Exception) {
                    Log.e("*MAKITA*", "Error al obtener el usuario", e)
                    mostrarDialogo(context, "Usuario no asignado", "Debe asignar un usuario")
                  //  onUsuarioAsignadoChange("")
                    Toast.makeText(context, "Cerrando aplicación: usuario no asignado", Toast.LENGTH_LONG).show()

                    delay(3000)
                    //activity?.finish() // Cierra la aplicación
                }
            }



    TextField(
        value = usuarioAsignado ,
        onValueChange = { /* No se permite la edición */ },
        label = { Text("Usuario Asignado a Capturador") },
        readOnly = true, // Este campo es solo de lectura
        modifier = Modifier
            .width(320.dp) // Definir ancho
            .height(60.dp),
        textStyle = TextStyle(
            fontSize = 18.sp, // Tamaño del texto
            color = Color.Red, // Color del texto
            fontFamily = FontFamily.Serif, // Familia de fuentes
            fontWeight = FontWeight.Bold, // Peso de la fuente
            textAlign = TextAlign.Center
        ),
        enabled = false,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            containerColor = Color.White,
            disabledTextColor = Color.Black,  // Texto negro cuando está deshabilitado
            disabledLabelColor = Color.Gray,   // Etiqueta gris cuando está deshabilitado
            disabledBorderColor = Color.Black  // Borde negro cuando está deshabilitado
        )

    )

}

fun formatoFechaSS(timestamp: Long): String {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    return dateFormat.format(Date(timestamp))
}
fun formatoFechaSinSS(timestamp: Long): String {
    val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
    return dateFormat.format(Date(timestamp))
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerWithTextField() {

    var showDatePickerDialog by remember { mutableStateOf(false) }

    // Obtener la fecha actual
    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)
    var selectedDate by remember { mutableStateOf("$day/${month + 1}/$year") }

    val context = LocalContext.current

    selectedDate = formatoFechaSinSS(System.currentTimeMillis())

    // UI del TextField
    OutlinedTextField(
        value = selectedDate,
        onValueChange = {},
        modifier = Modifier
            .width(320.dp) // Ancho fijo
            .height(60.dp) // Alto fijo
            .clickable {
                // Muestra el DatePickerDialog al hacer clic
                DatePickerDialog(
                    context,
                    { _, selectedYear, selectedMonth, selectedDayOfMonth ->
                        // Actualiza la fecha seleccionada
                        selectedDate = "$selectedDayOfMonth/${selectedMonth + 1}/$selectedYear"
                    },
                    year, month, day // Fecha inicial en el DatePicker
                ).show()
            },
        readOnly = true,
        label = { Text("Ingrese Fecha Inventario") }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ComboBoxWithTextField(
    selectedOption: String,
    onOptionSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) } // Controla si el menú está abierto
    var showError by remember { mutableStateOf(false) }
    // val options = listOf("INVENTARIO", "RECONTEO")
    val options = listOf("INVENTARIO")

    Box {
        OutlinedTextField(
            value = selectedOption,
            onValueChange = {}, // Campo de solo lectura
            modifier = Modifier
                .width(320.dp)
                .height(60.dp)
                .clickable {
                    expanded = true
                    showError = false // Ocultamos el mensaje de error
                },
            readOnly = true,
            label = { Text("Seleccione Tipo de Inventario") },
            trailingIcon = {
                Icon(
                    imageVector = if (expanded) Icons.Filled.ArrowUpward else Icons.Filled.ArrowDownward,
                    contentDescription = if (expanded) "Cerrar menú" else "Abrir menú",
                    modifier = Modifier.clickable { expanded = !expanded }
                )
            },
            isError = showError
        )

        // Menú desplegable
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
                // Muestra el error si el menú se cierra sin seleccionar
                if (selectedOption.isEmpty()) showError = true
            },
            modifier = Modifier.width(280.dp) // Asegura que el menú tenga el mismo ancho
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onOptionSelected(option)
                        expanded = false
                        showError = false
                    }
                )
            }
        }
    }

    // Mensaje de error si no se selecciona ninguna opción
    if (showError && selectedOption.isEmpty()) {
        Text(
            text = "Este campo es obligatorio",
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(start = 16.dp, top = 4.dp)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ComboBoxTipoProducto(
    selectedOption: String,
    onOptionSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val options = listOf("REPUESTOS", "ACCESORIOS", "HERRAMIENTAS") // Opciones del segundo ComboBox

    Box {
        OutlinedTextField(
            value = selectedOption,
            onValueChange = {},
            modifier = Modifier
                .width(320.dp)
                .height(60.dp)
                .clickable { expanded = true },
            readOnly = true,
            label = { Text("Seleccione Tipo de Producto") },
            trailingIcon = {
                Icon(
                    imageVector = if (expanded) Icons.Filled.ArrowUpward else Icons.Filled.ArrowDownward,
                    contentDescription = null,
                    modifier = Modifier.clickable { expanded = !expanded }
                )
            }
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onOptionSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable

fun ComboBoxLocal(
    selectedOption: String,
    onOptionSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val options = listOf("01-ENEA","03-TEMUCO","04-ANTOFAGASTA","05-COPIAPO") // Opciones del segundo ComboBox

    Box {
        OutlinedTextField(
            value = selectedOption,
            onValueChange = {},
            modifier = Modifier
                .width(320.dp)
                .height(60.dp)
                .clickable { expanded = true },
            readOnly = true,
            label = { Text("Seleccione Bodega") },
            trailingIcon = {
                Icon(
                    imageVector = if (expanded) Icons.Filled.ArrowUpward else Icons.Filled.ArrowDownward,
                    contentDescription = null,
                    modifier = Modifier.clickable { expanded = !expanded }
                )
            }
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onOptionSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SecondScreen(navController: NavController, param: String, param2: String , usuarioAsignado: String)

{
    val ubicacionFocusRequester = remember { FocusRequester() }

    val cantidadFocusRequester = remember { FocusRequester() }
    val itemFocusRequester = remember { FocusRequester() }
    var text by remember { mutableStateOf("") }

    var extractedText by remember { mutableStateOf("") }
    var extractedText2 by remember { mutableStateOf("") }
    var extractedText3 by remember { mutableStateOf("") }
    var extractedText4 by remember { mutableStateOf("") }
    var response by rememberSaveable { mutableStateOf<List<ItemResponse>>(emptyList()) }
    var errorState by rememberSaveable { mutableStateOf<String?>(null) }
    var textFieldValue2 by remember { mutableStateOf("") }
    var showErrorDialog by remember { mutableStateOf(false) }
    var ubicacion by remember { mutableStateOf("") }
    var cantidad by remember { mutableStateOf("") }
    val apiService = RetrofitClient.apiService
    val keyboardController = LocalSoftwareKeyboardController.current

    var gTipoItem by remember { mutableStateOf("") }
    var gLocal    by remember { mutableStateOf("") }
    val scrollState = rememberScrollState()
    var ultimaubicacion by remember { mutableStateOf("") }
    val context = LocalContext.current
    var isLoading by remember { mutableStateOf(false) } // Estado para el loading


    fun validarCampos(): Boolean {
        return cantidad.isNotEmpty()
    }
    Surface(modifier = Modifier.fillMaxSize())
    {
        gTipoItem = param ?: gTipoItem
        gLocal = param2 ?: gLocal
        textFieldValue2 = "" // Descripcion

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            val gnombreDispositivo = remember { obtenerNombreDelDispositivo(context) }

            val subtitulo = "$gLocal $gnombreDispositivo"

            Titulo2(param = gTipoItem, param2 =subtitulo)
            Separar()

            Spacer(modifier = Modifier.height(12.dp))

            LaunchedEffect(Unit) {
                try {
                    val respuesta = apiService.obtenerUltimaUbicacion(
                        "INVENTARIO",
                        gTipoItem,
                        gnombreDispositivo,
                        formatoFechaSS(System.currentTimeMillis()),
                        gLocal
                    )

                    if (respuesta.isNotEmpty()) {
                        withContext(Dispatchers.Main) { // Asegura que se actualiza en el hilo principal
                            ultimaubicacion = respuesta.first().ubicacion // Solo toma la primera ubicación
                            ubicacionFocusRequester.requestFocus()
                        }
                    }
                } catch (e: Exception) {
                    Log.e("ErrorAPI", "Error al obtener la ubicación", e)
                }
            }


            TextField(
                value = ultimaubicacion,
                onValueChange = { /* No se permite la edición */ },
                label = { Text("Ultima Ubicacion") },
                readOnly = true, // Este campo es solo de lectura
                modifier = Modifier
                    .width(300.dp) // Definir ancho
                    .height(50.dp),
                textStyle = TextStyle(
                    fontSize = 18.sp, // Tamaño del texto
                    color = Color.Red, // Color del texto
                    fontFamily = FontFamily.Serif, // Familia de fuentes
                    fontWeight = FontWeight.Bold // Peso de la fuente
                ),
                enabled = false

            )
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedTextField(
                value = ubicacion,
                onValueChange = {
                    ubicacion = it.uppercase().trim()
                    Log.d("*MAKITA*", "Ubicación ingresada: ${it.length}")
                    // Mover el foco al siguiente campo si se cumple la condición
                    if (it.length >= 5) {
                        keyboardController?.hide()
                        itemFocusRequester.requestFocus()
                    }
                },
                label = { Text("Escanee Ubicación") },
                placeholder = { Text("Ingrese la ubicación") },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Text
                ),
                modifier = Modifier
                    .width(300.dp)
                    .height(60.dp)
                    .focusRequester(ubicacionFocusRequester)
                    .onFocusChanged { focusState ->
                        if (focusState.isFocused) {
                            keyboardController?.hide()
                        }
                    },
                singleLine = true,
                leadingIcon = { Icon(Icons.Default.LocationOn, contentDescription = "Icono de ubicación") },
                trailingIcon = {
                    if (ubicacion.isNotEmpty()) {
                        IconButton(onClick = { ubicacion = "" }) {
                            Icon(Icons.Default.Clear, contentDescription = "Borrar texto")
                        }
                    }
                },
                isError = ubicacion.length > 8 // Mostrar error si el texto supera 8 caracteres
            )

            // Mostrar un mensaje de error opcional
            if (ubicacion.length > 10) {
                val mensajeError2 = "La ubicación no debe exceder los 10 caracteres"
                mostrarDialogo3(context, "Error", mensajeError2)
                ubicacion = ""
                extractedText = ""
                cantidad = ""
                response = emptyList()
                ubicacionFocusRequester.requestFocus()

            }

            Spacer(modifier = Modifier.height(16.dp))
            if (isLoading) {
                LoadingIndicator()
            }
            OutlinedTextField(
                value = text,
                onValueChange = { newText ->
                    text = newText

                    // Procesar los datos según la longitud del texto
                    if (newText.length >= 20) {
                        extractedText = newText.substring(0, 20) // Primeros 20 caracteres (item)
                        extractedText2 = newText.substring(20, newText.length.coerceAtMost(29)) // Serie desde
                        extractedText3 = newText.substring(29, newText.length.coerceAtMost(38)) // Serie hasta
                        extractedText4 = newText.substring(39, newText.length.coerceAtMost(52)) // EAN
                    } else {
                        extractedText = newText
                        extractedText2 = ""
                        extractedText3 = ""
                        extractedText4 = ""
                    }

                    if (newText.length >= 20)
                    {
                        keyboardController?.hide() // Ocultar teclado
                        cantidadFocusRequester.requestFocus() // Pasar el foco al siguiente campo
                    }
                },
                label = { Text("Item") },
                placeholder = { Text("Escanee código item") },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Text
                ),
                modifier = Modifier
                    .width(300.dp)
                    .height(120.dp)
                    .focusRequester(itemFocusRequester) // Asociar FocusRequester
                    .onFocusChanged { focusState ->
                        if (focusState.isFocused) {
                            keyboardController?.hide()
                        }
                    }
                    .padding(bottom = 16.dp),
                maxLines = 5,
                singleLine = false,
                leadingIcon = { Icon(Icons.Default.Edit, contentDescription = "Icono de edición") },
                trailingIcon = {
                    if (text.isNotEmpty()) {
                        IconButton(onClick = { text = "" }) {
                            Icon(Icons.Default.Clear, contentDescription = "Limpiar texto")
                        }
                    }
                }
            )

            Spacer(modifier = Modifier.height(10.dp))

            TextField(
                value = extractedText,
                onValueChange = { /* No se permite la edición */ },
                label = { Text("00 - 20") },
                readOnly = true, // Este campo es solo de lectura
                modifier = Modifier
                    .width(300.dp) // Definir ancho
                    .height(60.dp),
                textStyle = TextStyle(
                    fontSize = 20.sp, // Tamaño del texto
                    color = Color.Red, // Color del texto
                    fontFamily = FontFamily.Serif, // Familia de fuentes
                    fontWeight = FontWeight.Bold // Peso de la fuente
                ),
                enabled = false

            )


            LaunchedEffect(extractedText) {
                if (extractedText.isNullOrEmpty()) {
                    Toast.makeText(context, "Seleccione Nueva Ubicación", Toast.LENGTH_SHORT).show()
                    return@LaunchedEffect
                }

                if (!isNetworkAvailable(context)) {
                    Toast.makeText(context, "No hay conexión a Internet", Toast.LENGTH_SHORT).show()
                    return@LaunchedEffect
                }

                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        val response35 = apiService.validarTipoItem(extractedText.trim(), gTipoItem)

                        withContext(Dispatchers.Main) {
                            if (response35 == "NO") {
                                Log.d("*MAKITA*AQUI*", "RESPUESTA NO - ENTRA validarTipoItem: $response35")

                                textFieldValue2 = ""
                                val mensajeError = "Item: ${extractedText.trim()} NO CORRESPONDE A $gTipoItem"
                                Log.d("*MAKITA*AQUI*", "NO ENTRA API validarTipoItem: $mensajeError")

                                // Mostrar diálogo de error
                                mostrarDialogo3(context, "Error", mensajeError)

                                // Limpiar valores
                                text = ""
                                ubicacion = ""
                                extractedText = ""
                                extractedText2 = ""
                                extractedText3 = ""
                                extractedText4 = ""
                                cantidad = ""
                                response = emptyList()

                                // Enfocar el campo nuevamente
                                itemFocusRequester.requestFocus()
                                return@withContext  // 🔥 Detiene ejecución si response35 es "NO"
                            }
                        }

                        // Reset descripción antes de obtener datos de la API
                        textFieldValue2 = ""

                        val apiResponse = apiService.obtenerUbicacionItem(extractedText)
                        Log.d("*MAKITA*", "Respuesta obtenerUbicacionItem: $extractedText")

                        withContext(Dispatchers.Main) {
                            if (apiResponse.isNullOrEmpty()) {
                                errorState = "No se encontraron datos para el item proporcionado"
                                return@withContext
                            }

                            errorState = null
                            val tieneValoresNulos = apiResponse.any { it.item == null }

                            if (tieneValoresNulos) {
                                Log.d("*MAKITA*", "La respuesta contiene valores nulos")
                                showErrorDialog = true

                                // Limpiar valores
                                text = ""
                                extractedText2 = ""
                                extractedText3 = ""
                                extractedText4 = ""
                                textFieldValue2 = ""
                                response = emptyList()

                                // Enfocar nuevamente el campo
                                itemFocusRequester.requestFocus()
                            } else {
                                response = apiResponse
                                if (response.isNotEmpty()) {
                                    textFieldValue2 = response.first().descripcion
                                }
                            }
                        }
                    } catch (e: Exception) {
                        withContext(Dispatchers.Main) {
                            Log.e("*MAKITA*", "Error obteniendo datos: ${e.message}")
                            Toast.makeText(context, "Error al obtener los datos, revise WiFi: ${e.message}", Toast.LENGTH_LONG).show()
                            showErrorDialog = true

                            // Limpiar valores en caso de error
                            text = ""
                            extractedText2 = ""
                            extractedText3 = ""
                            extractedText4 = ""
                            textFieldValue2 = ""
                            response = emptyList()

                            // Esperar antes de reenfocar el campo
                            delay(3000)
                            itemFocusRequester.requestFocus()
                        }
                    }
                }
            }

            if (response.isNotEmpty()) {
                textFieldValue2 = response.first().descripcion
            }

            Spacer(modifier = Modifier.height(16.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {
                // Texto a la izquierda y a la derecha en una fila
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween // Distribuir los textos entre los extremos
                ) {



                // Campo de solo lectura
                TextField(
                    value = textFieldValue2,
                    onValueChange = {},
                    readOnly = true,
                    modifier = Modifier
                        .width(150.dp)
                        .height(80.dp),
                    textStyle = TextStyle(
                        fontSize = 14.sp,
                        color = Color.Red,
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.Bold
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Campo editable con validación
                    OutlinedTextField(
                        value = cantidad,
                        onValueChange = { newValue ->
                            if (newValue.all { it.isDigit() } && newValue.length <= 10)
                            { // Limitar a 10 caracteres
                                cantidad = newValue
                            }
                        },

                        placeholder = { Text("Ingrese Cantidad") },
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                keyboardController?.hide() // Oculta el teclado cuando se presiona Done
                            }
                        ),
                        modifier = Modifier
                            .width(150.dp)
                            .height(80.dp)
                            .border(2.dp, Color.Black, shape = RoundedCornerShape(4.dp))
                            .focusRequester(cantidadFocusRequester),
                        textStyle = TextStyle(
                            fontSize = 24.sp,
                            color = Color.Blue,
                            fontFamily = FontFamily.Serif,
                            fontWeight = FontWeight.Bold
                        ),
                        singleLine = true,
                        isError = cantidad.length > 10 // Cambiado para reflejar el límite correcto
                    )


                    if (cantidad.length > 10)
                {
                    Text(
                        text = "La cantidad no debe exceder los 10 caracteres",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                    )
                 }
                }

            }

            Spacer(modifier = Modifier.height(15.dp))

            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Bottom
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly // Espaciado uniforme entre botones
                ) {
                    val buttonModifier = Modifier
                        .width(110.dp)  // Asegura que todos los botones tengan el mismo ancho
                        .height(45.dp)  // Asegura que todos los botones tengan la misma altura
                        .padding(horizontal = 4.dp)

                    val buttonColors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF00909E),
                        contentColor = Color.White
                    )

                    Button(
                        onClick = { navController.popBackStack() },
                        colors = buttonColors,
                        modifier = buttonModifier,
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(text = "VOLVER", fontSize = 13.sp)
                    }

                    Button(
                        onClick = {
                            text = ""
                            ubicacion = ""
                            extractedText = ""
                            extractedText2 = ""
                            extractedText3 = ""
                            extractedText4 = ""
                            textFieldValue2 = ""
                            cantidad = ""
                            response = emptyList()
                            itemFocusRequester.requestFocus()
                        },
                        colors = buttonColors,
                        modifier = buttonModifier,
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(text = "LIMPIAR", fontSize = 13.sp)
                    }

                    Button(
                        onClick = {
                            isLoading = true
                            if (extractedText.isNotEmpty()) {
                                CoroutineScope(Dispatchers.Main).launch {
                                    try {
                                        val FechaFija = formatoFechaSS(System.currentTimeMillis())
                                        val Usuario = gnombreDispositivo

                                        val response33 = apiService.validarUbicacionProducto(
                                            FechaFija,
                                            extractedText.trim(),
                                            extractedText2.trim(),
                                            Usuario
                                        )

                                        Log.d("*MAKITA*", "API validarUbicacionProducto: $response33")

                                        if (!response33.isNullOrEmpty()) {
                                            errorState = "No se encontraron datos para el item proporcionado"

                                            if (response33 == "NO") {
                                                val requestRegistroInventario = RegistraInventarioRequest(
                                                    Id = "1",
                                                    Empresa = "MAKITA",
                                                    FechaInventario = FechaFija,
                                                    TipoInventario = "INVENTARIO",
                                                    Bodega = gLocal,
                                                    Clasif1 = gTipoItem,
                                                    Ubicacion = ubicacion,
                                                    Item = extractedText.trim(),
                                                    Cantidad = cantidad,
                                                    Estado = "Ingresado",
                                                    Usuario = usuarioAsignado,
                                                    NombreDispositivo = gnombreDispositivo
                                                )
                                                Log.d("*MAKITA*", "Datos enviados en requestRegistroInventario: $requestRegistroInventario")

                                                val bitacoraRegistroUbi = apiService.insertarinventario(requestRegistroInventario)

                                                Log.d("*MAKITA*", "RESPUESTA DE INSERTAR INVENTARIO: $bitacoraRegistroUbi")

                                                guardarRespaldo(context, requestRegistroInventario)
                                                delay(1500)
                                                Toast.makeText(context, "Registro Grabado", Toast.LENGTH_LONG).show()
                                            } else {
                                                val linea = "Item: ${extractedText.trim()} en Ubicacion ${extractedText2.trim()} YA INVENTARIADO"
                                                mostrarDialogo2(context, "Error", linea)
                                            }
                                        }

                                        val respuesta = apiService.obtenerUltimaUbicacion(
                                            "INVENTARIO",
                                            gTipoItem,
                                            gnombreDispositivo,
                                            formatoFechaSS(System.currentTimeMillis()),
                                            gLocal
                                        )

                                        if (respuesta.isNotEmpty()) {
                                            ultimaubicacion = respuesta.first().ubicacion
                                        }

                                    } catch (e: Exception) {
                                        Log.e("*MAKITA*", "ERROR: ${e.message}")
                                        errorState = "Error: ${e.message}"
                                        delay(1500)
                                        Toast.makeText(context, "Error al grabar el item, intentelo nuevamente", Toast.LENGTH_LONG).show()
                                    } finally {
                                        isLoading = false
                                        text = ""
                                        ubicacion = ""
                                        extractedText = ""
                                        extractedText2 = ""
                                        extractedText3 = ""
                                        extractedText4 = ""
                                        textFieldValue2 = ""
                                        cantidad = ""
                                        response = emptyList()
                                        itemFocusRequester.requestFocus()
                                    }
                                }
                            }
                        },
                        colors = buttonColors,
                        modifier = buttonModifier,
                        shape = RoundedCornerShape(8.dp),
                        enabled = !isLoading && validarCampos()
                    ) {
                        Text(text = "GRABAR", fontSize = 13.sp)
                    }
                }
            }

        }
    }
}

fun obtenerNombreDelDispositivo(context: Context): String {
    return Settings.Global.getString(context.contentResolver, Settings.Global.DEVICE_NAME) ?: "Desconocido"
}


/*fun obtenerNombreDelDispositivo(context: Context): String {
    return "Honeywell-30"
}*/

fun mostrarDialogo(context: Context, titulo: String, mensaje: String)
{
    val builder = AlertDialog.Builder(context)
    builder.setTitle(titulo)
    builder.setMessage(mensaje)
    builder.setPositiveButton("OK", null)
    builder.show()
}

fun mostrarDialogo3(context: Context, titulo: String, mensaje: String) {
    AlertDialog.Builder(context)
        .setTitle(titulo)
        .setMessage(mensaje)
        .setPositiveButton("Aceptar") { dialog, _ ->
            dialog.dismiss()
        }
        .create()
        .show()
}

fun mostrarDialogo2(context: Context, titulo: String, mensaje: String) {
    val builder = AlertDialog.Builder(context)
    builder.setTitle(titulo)
        .setMessage(mensaje)
        .setPositiveButton("Aceptar") { dialog, _ ->
            dialog.dismiss()  // Cierra el diálogo al hacer clic en "Aceptar"
        }
        .create()
        .show()
}

fun isNetworkAvailable(context: Context): Boolean {
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val networkInfo = connectivityManager.activeNetworkInfo
    return networkInfo != null && networkInfo.isConnected
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TerceraScreen(navController: NavController, param: String, param2: String , usuarioAsignado: String)

{
    val ubicacionFocusRequester = remember { FocusRequester() }
    val cantidadFocusRequester = remember { FocusRequester() }
    val itemFocusRequester = remember { FocusRequester() }
    var text by remember { mutableStateOf("") }
    var extractedText by remember { mutableStateOf("") }
    var extractedText2 by remember { mutableStateOf("") }
    var extractedText3 by remember { mutableStateOf("") }
    var extractedText4 by remember { mutableStateOf("") }
    var response by rememberSaveable { mutableStateOf<List<ItemResponse>>(emptyList()) }
    var response2 by rememberSaveable { mutableStateOf<List<ItemResponse>>(emptyList()) }
    var errorState by rememberSaveable { mutableStateOf<String?>(null) }
    var errorMessage by remember { mutableStateOf("") }
    var textFieldValue2 by remember { mutableStateOf("") }
    var showErrorDialog by remember { mutableStateOf(false) }
    var ubicacion by remember { mutableStateOf("") }
    var cantidad by remember { mutableStateOf("") }
    val apiService = RetrofitClient.apiService
    val keyboardController = LocalSoftwareKeyboardController.current
    var gTipoItem by remember { mutableStateOf("") }
    var gLocal    by remember { mutableStateOf("") }
    val scrollState = rememberScrollState()
    var ultimaubicacion by remember { mutableStateOf("") }
    val context = LocalContext.current
    val gnombreDispositivo = remember { obtenerNombreDelDispositivo(context) }
    var isLoading by remember { mutableStateOf(false) } // Estado para el loading

    fun validarCampos(): Boolean {
        return cantidad.isNotEmpty()
    }


    Surface(
        modifier = Modifier.fillMaxSize(),
    ) {

        LaunchedEffect(Unit) {
            itemFocusRequester.requestFocus()
        }

        gTipoItem = param ?: gTipoItem
        gLocal = param2 ?: gLocal
        val subtitulo = "$gLocal $gnombreDispositivo"

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Titulo()
            Titulo2(param = gTipoItem, param2 =subtitulo)
            Separar()

            LaunchedEffect(Unit) {
                try {
                    val respuesta = apiService.obtenerUltimaUbicacion("INVENTARIO"
                        ,gTipoItem
                        ,gnombreDispositivo
                        ,formatoFechaSS(System.currentTimeMillis())
                        ,gLocal
                    )

                    Log.d("MAKITA" , "Obtenemos la ultima ubicacion : $respuesta")

                    if (respuesta.isNotEmpty())
                    {
                        respuesta.forEach { item ->

                            ultimaubicacion = item.ubicacion

                        }
                    }
                } catch (e: Exception) {
                    Log.e("ErrorAPI", "Error al obtener la ubicación", e)
                }
            }


            TextField(
                value = ultimaubicacion,
                onValueChange = { /* No se permite la edición */ },
                label = { Text("Ultima Ubicacion") },
                readOnly = true, // Este campo es solo de lectura
                modifier = Modifier
                    .width(300.dp) // Definir ancho
                    .height(50.dp),
                textStyle = TextStyle(
                    fontSize = 18.sp, // Tamaño del texto
                    color = Color.Red, // Color del texto
                    fontFamily = FontFamily.Serif, // Familia de fuentes
                    fontWeight = FontWeight.Bold // Peso de la fuente
                ),
                enabled = false

            )
            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = text,
                onValueChange = { newText ->
                    text = newText

                    // Procesar los datos según la longitud del texto
                    Log.d("*MAKITA*", "Longitud del texto: ${newText.length}")

                   if (newText.length == 51)
                   {
                        extractedText = ""
                        extractedText  = newText.substring(0, 20) // Primeros 20 caracteres (item)
                        extractedText2 = newText.substring(20, (20 + 18).coerceAtMost(newText.length))
                        Log.d("*MAKITA*", "INGRESA A LARGO 51: $extractedText")
                   }

                    if (newText.length == 41  || newText.length == 50  || newText.length == 51 || newText.length == 52 || newText.length == 53 ||  newText.length == 54 || newText.length == 55 || newText.length == 56)
                    {
                        extractedText  = newText.substring(0, 20) // Primeros 20 caracteres (item)
                        extractedText2 = newText.substring(20, (20 + 18).coerceAtMost(newText.length))

                    }

                    if (newText.length == 37)
                    {
                        extractedText  = newText.substring(0, 20) // Primeros 20 caracteres (item)
                        extractedText2 = newText.substring(20, (20 + 5).coerceAtMost(newText.length))

                    }

                    if (newText.length == 41)
                    {
                        extractedText  = newText.substring(0, 20) // Primeros 20 caracteres (item)
                        extractedText2 = newText.substring(20, (20 + 8).coerceAtMost(newText.length))

                    }


                    if (newText.length == 38)
                    {
                        extractedText  = newText.substring(0, 20) // Primeros 20 caracteres (item)
                        extractedText2 = newText.substring(20, (20 + 6).coerceAtMost(newText.length))

                    }

                    if (newText.length >= 20)
                    {
                        keyboardController?.hide() // Ocultar teclado
                        cantidadFocusRequester.requestFocus() // Pasar el foco al siguiente campo
                    }
                },
                label = { Text("Item") },
                placeholder = { Text("Escanee código item") },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Text
                ),
                modifier = Modifier
                    .width(300.dp)
                    .height(100.dp)
                    .focusRequester(itemFocusRequester) // Asociar FocusRequester
                    .onFocusChanged { focusState ->
                        if (focusState.isFocused) {
                            keyboardController?.hide()
                        }
                    }
                    .padding(bottom = 16.dp),
                maxLines = 5,
                singleLine = false,
                leadingIcon = { Icon(Icons.Default.Edit, contentDescription = "Icono de edición") },
                trailingIcon = {
                    if (text.isNotEmpty()) {
                        IconButton(onClick = {
                            text = ""
                            ubicacion = ""
                            extractedText = ""
                            extractedText2 = ""
                            extractedText3 = ""
                            extractedText4 = ""
                            textFieldValue2 = ""
                            cantidad = ""
                            response = emptyList()
                            itemFocusRequester.requestFocus()
                        }) {
                            Icon(Icons.Default.Clear, contentDescription = "Limpiar texto")
                        }
                    }
                }
            )

            Spacer(modifier = Modifier.height(10.dp))
            if (isLoading) {
                LoadingIndicator()
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),  // Margen alrededor de la fila
                horizontalArrangement = Arrangement.SpaceBetween // Espacia los botones entre sí
            ) {

                TextField(
                    value = extractedText,
                    onValueChange = { /* No se permite la edición */ },
                    label = { Text("Item") },
                    readOnly = true, // Este campo es solo de lectura
                    modifier = Modifier
                        .width(150.dp) // Definir ancho
                        .height(80.dp)
                        .border(2.dp, Color.Black, shape = RoundedCornerShape(4.dp)),
                    textStyle = TextStyle(
                        fontSize = 18.sp, // Tamaño del texto
                        color = Color.Red, // Color del texto
                        fontFamily = FontFamily.Serif, // Familia de fuentes
                        fontWeight = FontWeight.Bold // Peso de la fuente
                    ),
                    enabled = false

                )

                Spacer(modifier = Modifier.height(14.dp))

                TextField(
                    value = extractedText2,
                    onValueChange = { /* No se permite la edición */ },
                    label = { Text("Ubicacion") },
                    readOnly = true, // Este campo es solo de lectura
                    modifier = Modifier
                        .width(150.dp) // Definir ancho
                        .height(80.dp)
                        .border(2.dp, Color.Black, shape = RoundedCornerShape(4.dp)),
                    textStyle = TextStyle(
                        fontSize = 18.sp, // Tamaño del texto
                        color = Color.Red, // Color del texto
                        fontFamily = FontFamily.Serif, // Familia de fuentes
                        fontWeight = FontWeight.Bold // Peso de la fuente
                    ),
                )

                LaunchedEffect(extractedText) {
                    // Validamos si extractedText no está vacío antes de proceder
                    if (extractedText.isNotEmpty()) {
                        if (isNetworkAvailable(context)) {
                            CoroutineScope(Dispatchers.IO).launch {
                                try {
                                    val response35 = try {
                                        apiService.validarTipoItem(extractedText.trim(), gTipoItem)
                                    } catch (e: IOException) {
                                        withContext(Dispatchers.Main) {
                                            Toast.makeText(
                                                context,
                                                "Error de conexión al validar el tipo de ítem",
                                                Toast.LENGTH_LONG
                                            ).show()
                                            errorMessage = "Error de conexión al validar el tipo de ítem"
                                            showErrorDialog = true
                                        }
                                        return@launch
                                    }

                                    Log.d("MAKITA" , "validarTipoItem 35 $response35")
                                    withContext(Dispatchers.Main) {
                                        if (response35 == "NO") {
                                            Log.d("*MAKITA*AQUI*", "RESPUESTA NO - ENTRA validarTipoItem: $response35")

                                            textFieldValue2 = ""
                                            val mensajeError = "Item: ${extractedText.trim()} NO CORRESPONDE A $gTipoItem"
                                            Log.d("*MAKITA*AQUI*", "NO ENTRA API validarTipoItem: $mensajeError")

                                            // Mostrar un diálogo de error
                                            mostrarDialogo3(context, "Error", mensajeError)

                                            // Limpiar valores
                                            text = ""
                                            ubicacion = ""
                                            extractedText = ""
                                            extractedText2 = ""
                                            extractedText3 = ""
                                            extractedText4 = ""
                                            cantidad = ""
                                            response = emptyList()

                                            // Enfocar el campo nuevamente
                                            ubicacionFocusRequester.requestFocus()

                                            return@withContext
                                        }
                                    }

                                    // Solo si response35 NO es "NO", ejecuta la segunda API
                                    Log.d("*MAKITA*", "DATO ENVIADO :: $extractedText")
                                    val apiResponse = apiService.obtenerUbicacionItem(extractedText)
                                    Log.d("*MAKITA*", "obtenerUbicacionItem: $apiResponse")

                                    withContext(Dispatchers.Main) {
                                        response = apiResponse

                                        if (apiResponse.isNullOrEmpty()) {
                                            Log.d("*MAKITA*", "apiResponse ES NULL O VACIO: $apiResponse")
                                            errorState = "No se encontraron datos para el item proporcionado"
                                        } else {
                                            errorState = null

                                            val tieneValoresNulos = apiResponse.any { it.item == null }
                                            if (tieneValoresNulos) {
                                                errorMessage = "Advertencia! No existe última Ubicación"
                                                showErrorDialog = true
                                                text = ""
                                                extractedText2 = ""
                                                textFieldValue2 = ""
                                                response = emptyList()
                                                itemFocusRequester.requestFocus()
                                            } else {
                                                response2 = apiResponse
                                                if (response2.isNotEmpty()) {
                                                    textFieldValue2 = response2.first().descripcion
                                                }
                                            }
                                        }
                                    }
                                } catch (e: Exception) {
                                    withContext(Dispatchers.Main) {
                                        Toast.makeText(context, "Error al obtener los datos 01: ${e.message}", Toast.LENGTH_LONG).show()
                                        errorMessage = "Error al obtener los datos 02: ${e.message}"
                                        showErrorDialog = true
                                        text = ""
                                        extractedText2 = ""
                                        textFieldValue2 = ""
                                        response = emptyList()
                                        itemFocusRequester.requestFocus()
                                    }
                                }
                            }
                        } else {
                            Toast.makeText(context, "No hay conexión a Internet", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                // Texto a la izquierda y a la derecha en una fila
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween // Distribuir los textos entre los extremos
                ) {


                    // Campo de solo lectura
                    TextField(
                        value = textFieldValue2,
                        label = { Text("Descripcion") },
                        onValueChange = {},
                        readOnly = true,
                        modifier = Modifier
                            .width(150.dp)
                            .height(80.dp)
                            .border(2.dp, Color.Black, shape = RoundedCornerShape(4.dp)),
                        textStyle = TextStyle(
                            fontSize = 15.sp,
                            color = Color.Blue,
                            fontFamily = FontFamily.Serif,
                            fontWeight = FontWeight.Bold,

                        )
                    )

                    Spacer(modifier = Modifier.height(15.dp))

                    // Campo editable con validación
                    OutlinedTextField(
                        value = cantidad,
                        onValueChange = { newValue ->
                            if (newValue.all { it.isDigit() } && newValue.length <= 10)
                            { // Limitar a 10 caracteres
                                cantidad = newValue
                            }
                        },

                        placeholder = { Text("Ingrese Cantidad") },
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                keyboardController?.hide() // Oculta el teclado cuando se presiona Done
                            }
                        ),
                        modifier = Modifier
                            .width(150.dp)
                            .height(80.dp)
                            .border(2.dp, Color.Black, shape = RoundedCornerShape(4.dp))
                            .focusRequester(cantidadFocusRequester),
                        textStyle = TextStyle(
                            fontSize = 24.sp,
                            color = Color.Blue,
                            fontFamily = FontFamily.Serif,
                            fontWeight = FontWeight.Bold
                        ),
                        singleLine = true,
                        isError = cantidad.length > 10 // Cambiado para reflejar el límite correcto
                    )

                    if (cantidad.length > 10)
                    {
                        Text(
                            text = "La cantidad no debe exceder los 10 caracteres",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                        )
                    }
                }

            }

            Spacer(modifier = Modifier.height(14.dp))

            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Bottom
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly // Espaciado uniforme entre botones
                ) {
                    val buttonModifier = Modifier
                        .width(110.dp)  // Asegura que todos los botones tengan el mismo ancho
                        .height(45.dp)  // Asegura que todos los botones tengan la misma altura
                        .padding(horizontal = 4.dp)

                    val buttonColors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF00909E),
                        contentColor = Color.White
                    )

                    Button(
                        onClick = { navController.popBackStack() },
                        colors = buttonColors,
                        modifier = buttonModifier,
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(text = "VOLVER", fontSize = 13.sp)
                    }

                    Button(
                        onClick = {
                            text = ""
                            ubicacion = ""
                            extractedText = ""
                            extractedText2 = ""
                            extractedText3 = ""
                            extractedText4 = ""
                            textFieldValue2 = ""
                            cantidad = ""
                            response = emptyList()
                            itemFocusRequester.requestFocus()
                        },
                        colors = buttonColors,
                        modifier = buttonModifier,
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(text = "LIMPIAR", fontSize = 13.sp)
                    }

                    Button(
                        onClick = {
                            isLoading = true
                            if (extractedText.isNotEmpty()) {
                                CoroutineScope(Dispatchers.Main).launch {
                                    try {
                                        val FechaFija = formatoFechaSS(System.currentTimeMillis())
                                        val Usuario = gnombreDispositivo

                                        val response33 = apiService.validarUbicacionProducto(
                                            FechaFija,
                                            extractedText.trim(),
                                            extractedText2.trim(),
                                            Usuario
                                        )

                                        Log.d("*MAKITA*", "API validarUbicacionProducto: $response33")

                                        if (!response33.isNullOrEmpty()) {
                                            errorState = "No se encontraron datos para el item proporcionado"

                                            if (response33 == "NO") {
                                                val requestRegistroInventario = RegistraInventarioRequest(
                                                    Id = "1",
                                                    Empresa = "MAKITA",
                                                    FechaInventario = FechaFija,
                                                    TipoInventario = "INVENTARIO",
                                                    Bodega = gLocal,
                                                    Clasif1 = gTipoItem,
                                                    Ubicacion = extractedText2.trim(),
                                                    Item = extractedText.trim(),
                                                    Cantidad = cantidad,
                                                    Estado = "Ingresado",
                                                    Usuario = usuarioAsignado,
                                                    NombreDispositivo = gnombreDispositivo
                                                )

                                                val bitacoraRegistroUbi = apiService.insertarinventario(requestRegistroInventario)

                                                Log.d("*MAKITA*", "RESPUESTA DE INSERTAR INVENTARIO: $bitacoraRegistroUbi")

                                                guardarRespaldo(context, requestRegistroInventario)
                                                delay(1500)
                                                Toast.makeText(context, "Registro Grabado", Toast.LENGTH_LONG).show()
                                            } else {
                                                val linea = "Item: ${extractedText.trim()} en Ubicacion ${extractedText2.trim()} YA INVENTARIADO"
                                                mostrarDialogo2(context, "Error", linea)
                                            }
                                        }

                                        val respuesta = apiService.obtenerUltimaUbicacion(
                                            "INVENTARIO",
                                            gTipoItem,
                                            gnombreDispositivo,
                                            formatoFechaSS(System.currentTimeMillis()),
                                            gLocal
                                        )

                                        if (respuesta.isNotEmpty()) {
                                            ultimaubicacion = respuesta.first().ubicacion
                                        }

                                    } catch (e: Exception) {
                                        Log.e("*MAKITA*", "ERROR: ${e.message}")
                                        errorState = "Error: ${e.message}"
                                        delay(1500)
                                        Toast.makeText(context, "Error al grabar el item, intentelo nuevamente", Toast.LENGTH_LONG).show()
                                    } finally {
                                        isLoading = false
                                        text = ""
                                        ubicacion = ""
                                        extractedText = ""
                                        extractedText2 = ""
                                        extractedText3 = ""
                                        extractedText4 = ""
                                        textFieldValue2 = ""
                                        cantidad = ""
                                        response = emptyList()
                                        itemFocusRequester.requestFocus()
                                    }
                                }
                            }
                        },
                        colors = buttonColors,
                        modifier = buttonModifier,
                        shape = RoundedCornerShape(8.dp),
                        enabled = !isLoading && validarCampos()
                    ) {
                        Text(text = "GRABAR", fontSize = 13.sp)
                    }
                }
            }

        }
    }
}
fun validarCampos(text: String, ubicacion: String, cantidad: String): Boolean {
    return text.isNotEmpty() && ubicacion.isNotEmpty() && cantidad.isNotEmpty()
}

@Composable
fun Titulo() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 18.dp)
    ) {
        Text(
            text = "INVENTARIO",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .align(Alignment.Center),
            color = Color(0xFF00909E)
        )
    }
}

@Composable
fun Separar(){
    Divider(
        color = Color(0xFFFF7F50),
        thickness = 1.dp,
        modifier = Modifier
            .padding(vertical = 8.dp)
            .padding(8.dp),
    )
}

@Composable
fun Titulo2(param: String?, param2: String?)
{
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 15.dp)
    ) {
        Text(
            text ="Item ${param ?: "No hay parámetro"}  Local: ${param2 ?: "Sin fecha"}",
            fontSize   = 15.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                    .padding(5.dp)
                    .align(Alignment.Center)
        )

    }
}

fun guardarRespaldo(context: Context, registro: RegistraInventarioRequest) {
    // Obtener la fecha actual en formato DD-MM-YYYY
    val fechaActual = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())

    // Crear el nombre del archivo con la fecha
    val nombreArchivo = "inventario-$fechaActual.csv"

    // Ruta del archivo en el directorio de la aplicación
    val archivo = File(context.filesDir, nombreArchivo)

    Log.d("*MAKITA*", "directorio $archivo")
    Log.d("*MAKITA*", "directorio de datos ${context.filesDir}")

    // Verificar si el archivo existe, si no, crearlo con los encabezados
    if (!archivo.exists()) {
        val encabezado = "Id;Empresa;FechaInventario;TipoInventario;Bodega;Clasif1;Ubicacion;Item;Cantidad;Estado;Usuario;NombreDispositivo\n"
        archivo.writeText(encabezado) // Escribir encabezado en el archivo
    }

    // Construir el contenido del archivo
    val contenido = "${registro.Id};${registro.Empresa};${registro.FechaInventario};${registro.TipoInventario};${registro.Bodega};${registro.Clasif1};${registro.Ubicacion};${registro.Item};${registro.Cantidad};${registro.Estado};${registro.Usuario};${registro.NombreDispositivo}"

    Log.d("*MAKITA*", "guardarRespaldo: $contenido")

    try {
        // Escribir los datos en el archivo CSV, añadiendo una nueva línea
        archivo.appendText(contenido + "\n")
        Toast.makeText(context, "Datos guardados en $nombreArchivo", Toast.LENGTH_SHORT).show()
    } catch (e: Exception) {
        Toast.makeText(context, "Error al guardar los datos: ${e.message}", Toast.LENGTH_SHORT).show()
        e.printStackTrace()
    }
}

@Composable
fun LoadingIndicator() {
    val infiniteTransition = rememberInfiniteTransition()
    val angle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000, easing = LinearEasing)
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.Center)
    ) {
        Canvas(modifier = Modifier.size(60.dp)) {
            drawArc(
                color = Color(0xFF00909E), // Color personalizado
                startAngle = angle,
                sweepAngle = 270f,
                useCenter = false,
                style = Stroke(width = 6.dp.toPx(), cap = StrokeCap.Round)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Cargando...",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Red,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewMainScreen() {
    MainScreen(navController = rememberNavController())
}













