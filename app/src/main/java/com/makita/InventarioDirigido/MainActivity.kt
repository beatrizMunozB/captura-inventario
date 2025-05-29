package com.makita.InventarioDirigido

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.DatePicker
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.makita.InventarioDirigido.RetrofitClient.apiService
import com.makita.InventarioDirigido.ui.theme.InventarioDirigidoTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.IOException
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.Calendar
import java.util.Date
import java.util.Locale

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            InventarioDirigidoTheme {
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

    NavHost(navController = navController, startDestination = "main_screen")
    {
        composable("main_screen") {
            MainScreen(navController)
        }

        composable(
            route = "second_screen/{param}/{param2}/{param3}/{param4}/{param5}",
            arguments = listOf(navArgument("param") { type = NavType.StringType },
                navArgument("param2") { type = NavType.StringType },
                navArgument("param3") { type = NavType.StringType },
                navArgument("param4") { type = NavType.StringType },
                navArgument("param5") { type = NavType.StringType }

            )
        ) { backStackEntry ->

            val param = backStackEntry.arguments?.getString("param") ?: "DefaultParam"
            val param2 = backStackEntry.arguments?.getString("param2") ?: "DefaultParam2"
            val param3 = backStackEntry.arguments?.getString("param3") ?: "DefaultParam3"
            val param4 = backStackEntry.arguments?.getString("param4") ?: "DefaultParam4"
            val param5 = backStackEntry.arguments?.getString("param4") ?: "DefaultParam5"

            SecondScreen(
                navController = navController,
                param = param,
                param2 = param2,
                param3 = param3,
                param4 = param4,
                param5 = param5
            )
        }

        composable(
            route = "third_screen/{param}/{param2}/{param3}",
            arguments = listOf(navArgument("param") { type = NavType.StringType },
                navArgument("param2") { type = NavType.StringType },
                navArgument("param3") { type = NavType.StringType }
            )
        ) { backStackEntry ->

            val param = backStackEntry.arguments?.getString("param") ?: "DefaultParam"
            val param2 = backStackEntry.arguments?.getString("param2") ?: "DefaultParam2"
            val param3 = backStackEntry.arguments?.getString("param3") ?: "DefaultParam3"

            TerceraScreen(
                navController = navController,
                param = param,
                param2 = param2,
                param3 = param3
            )

        }


        composable(
            route = "cuarta_screen/{param}/{param2}/{param3}/{param4}/{param5}",
            arguments = listOf(
                navArgument("param") { type = NavType.StringType },
                navArgument("param2") { type = NavType.StringType },
                navArgument("param3") { type = NavType.StringType },
                navArgument("param4") { type = NavType.StringType },
                navArgument("param5") { type = NavType.StringType }

            )
        ) { backStackEntry ->

            val param = backStackEntry.arguments?.getString("param") ?: "DefaultParam"
            val param2 = backStackEntry.arguments?.getString("param2") ?: "DefaultParam2"
            val param3 = backStackEntry.arguments?.getString("param3") ?: "DefaultParam3"
            val param4 = backStackEntry.arguments?.getString("param4") ?: "DefaultParam4"
            val param5 = backStackEntry.arguments?.getString("param5") ?: "DefaultParam5"


            CuartaScreen(
                navController = navController,
                param = param,
                param2 = param2,
                param3 = param3,
                param4 = param4,
                param5 = param5
            )
        }

        composable(
            route = "quinta_screen/{param}/{param2}/{param3}/{param4}/{param5}",
            arguments = listOf(
                navArgument("param") { type = NavType.StringType },
                navArgument("param2") { type = NavType.StringType },
                navArgument("param3") { type = NavType.StringType },
                navArgument("param4") { type = NavType.StringType },
                navArgument("param5") { type = NavType.StringType }

            )
        ) { backStackEntry ->

            val param = backStackEntry.arguments?.getString("param") ?: "DefaultParam"
            val param2 = backStackEntry.arguments?.getString("param2") ?: "DefaultParam2"
            val param3 = backStackEntry.arguments?.getString("param3") ?: "DefaultParam3"
            val param4 = backStackEntry.arguments?.getString("param4") ?: "DefaultParam4"
            val param5 = backStackEntry.arguments?.getString("param5") ?: "DefaultParam5"

            QuintaScreen(
                navController = navController,
                param = param,
                param2 = param2,
                param3 = param3,
                param4 = param4,
                param5 = param5
            )
        }

        composable(
            route = "sexta_screen/{param}/{param2}/{param3}/{param4}/{param5}",
            arguments = listOf(
                navArgument("param") { type = NavType.StringType },
                navArgument("param2") { type = NavType.StringType },
                navArgument("param3") { type = NavType.StringType },
                navArgument("param4") { type = NavType.StringType },
                navArgument("param5") { type = NavType.StringType }

            )
        ) { backStackEntry ->

            val param = backStackEntry.arguments?.getString("param") ?: "DefaultParam"
            val param2 = backStackEntry.arguments?.getString("param2") ?: "DefaultParam2"
            val param3 = backStackEntry.arguments?.getString("param3") ?: "DefaultParam3"
            val param4 = backStackEntry.arguments?.getString("param4") ?: "DefaultParam4"
            val param5 = backStackEntry.arguments?.getString("param5") ?: "DefaultParam5"


            SextaScreen(
                navController = navController,
                param = param,
                param2 = param2,
                param3 = param3,
                param4 = param4,
                param5 = param5
            )

        }


        composable(
            route = "septima_screen/{param}/{param2}/{param3}/{param4}/{param5}/{param6}",
            arguments = listOf(
                navArgument("param") { type = NavType.StringType },
                navArgument("param2") { type = NavType.StringType },
                navArgument("param3") { type = NavType.StringType },
                navArgument("param4") { type = NavType.StringType },
                navArgument("param5") { type = NavType.StringType },
                navArgument("param6") { type = NavType.IntType },

                )
        ) { backStackEntry ->

            val param = backStackEntry.arguments?.getString("param") ?: "DefaultParam"
            val param2 = backStackEntry.arguments?.getString("param2") ?: "DefaultParam2"
            val param3 = backStackEntry.arguments?.getString("param3") ?: "DefaultParam3"
            val param4 = backStackEntry.arguments?.getString("param4") ?: "DefaultParam4"
            val param5 = backStackEntry.arguments?.getString("param5") ?: "DefaultParam5"
            val param6 = backStackEntry.arguments?.getInt("param6") ?: 0

            SeptimaScreen(
                navController = navController,
                param = param,
                param2 = param2,
                param3 = param3,
                param4 = param4,
                param5 = param5,
                param6 = param6
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun MainScreen(navController: NavController) {

    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)
    val context = LocalContext.current
    var selectedOption by remember { mutableStateOf("") } // Estado global
    var selectedTipo by remember { mutableStateOf("") } // Estado global
    var selectedLocal by remember { mutableStateOf("") } // Estado global
    var selectedBodega by remember { mutableStateOf("") }
    var selectedCategoria by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }
    val activity = context as? Activity
    var usuarioasigando by remember { mutableStateOf("") }
    var selectedDate by remember { mutableStateOf("$day/${month + 1}/$year") }

    // Configuración del DatePickerDialog
    val datePickerDialog = DatePickerDialog(
        context,
        { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
            val monthFormatted = String.format("%02d", month + 1)  // Asegura dos dígitos en el mes
            val dayFormatted = String.format("%02d", dayOfMonth)  // Asegura dos dígitos en el día
            selectedDate = "$dayFormatted/$monthFormatted/$year"
        },
        year, month, day
    )

    val gnombreDispositivo = remember { obtenerNombreDelDispositivo(context) }
    val anioActual = LocalDate.now().year
    val mesActual = String.format("%02d", LocalDate.now().monthValue)
    var showErrorDialog by remember { mutableStateOf(false) }

    var showDialog by remember { mutableStateOf(false) }
    var dialogMessage by remember { mutableStateOf("") }
    var showErrorDialogUSU by remember { mutableStateOf(false) }
    var errorMessageUSU by remember { mutableStateOf("") }
    CambiarColorBarraEstado(color = Color(0xFF00909E), darkIcons = true)

    Surface(
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.padding(8.dp) // Espaciado alrededor
    )

    {
        val scrollState = rememberScrollState()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .fillMaxHeight(1f),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top

        ) {

            Image(
                painter = painterResource(id = R.drawable.makitarojosmall),
                contentDescription = "Makita Inventario",
                modifier = Modifier
                    .size(120.dp) // Aumentar tamaño del logo
                    .align(Alignment.Start) // Alineación hacia la izquierda
                    .padding(top = 0.dp)
            )
            var fechaSeleccionada by rememberSaveable { mutableStateOf("") }
            DatePickerWithTextField(
                selectedDate = fechaSeleccionada,
                { date -> selectedDate = date })


            LaunchedEffect(Unit) {

                try {

                    if (!isNetworkAvailable(context)) {
                        Toast.makeText(context, "No hay conexión a Internet", Toast.LENGTH_SHORT)
                            .show()
                        return@LaunchedEffect
                    }

                    val respuesta01 = withContext(Dispatchers.IO) {
                        Log.d(
                            "*MAKITA*111*",
                            "Usuario obXXXtenido: $gnombreDispositivo $mesActual $anioActual"
                        )
                        apiService.obtenerUsuario(
                            gnombreDispositivo, mesActual,
                            anioActual.toString()

                        )
                    }

                    val usuario = respuesta01.data?.Usuario
                    Log.d("*MAKITA*111*", "Usuario obXXXtenido: $usuario")

                    if (usuario.isNullOrBlank()) {
                        // El valor es null, "" o solo espacios
                        withContext(Dispatchers.Main) {
                            showErrorDialogUSU = true

                            Toast.makeText(
                                context,
                                "⚠️ Usuario no asignado al dispositivo",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    } else {
                        usuarioasigando = usuario
                    }

                    //usuarioasigando = "BEATRIZ MUNOZ"
                    Log.d("*MAKITA*111*", "Usuario obXXXtenido: $usuarioasigando $mesActual")

                } catch (e: IOException) {

                    showErrorDialogUSU = true
                    Toast.makeText(
                        context,
                        "⚠️ Usuario no asignado, revisar periodo",
                        Toast.LENGTH_LONG
                    ).show()
                    Log.d("*MAKITA*111*", "Usuario obXXXtenido: $usuarioasigando")
//                   // mostrarDialogo(context, "Error", "Error de red: No hay conexión a Internet")

                } catch (e: Exception) {
                    errorMessageUSU = "Usuario no definido para el periodo actual"
                    showErrorDialogUSU = true
                }
            }

            TextField(
                value = usuarioasigando.uppercase(),
                onValueChange = { /* No se permite la edición */ },
                label = { Text("Usuario Asignado a Capturador") },
                readOnly = true, // Este campo es solo de lectura
                modifier = Modifier
                    .width(320.dp) // Definir ancho
                    .height(60.dp),
                textStyle = TextStyle(
                    fontSize = 20.sp, // Tamaño del texto
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

            if (showErrorDialogUSU) {
                mostrarDialogo(
                    titulo = "Informacion",
                    mensaje = errorMessageUSU,
                    onDismiss = { showErrorDialog = false }
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            ComboBoxWithTextField(selectedOption = selectedOption,
                onOptionSelected = { selectedOption = it }

            )

            Spacer(modifier = Modifier.height(10.dp))

            ComboBoxTipoProducto(
                selectedOption = selectedTipo,
                onOptionSelected = { selectedTipo = it }
            )
            ComboBoxLocal(
                selectedOption = selectedLocal,
                onOptionSelected = {
                    selectedLocal = it
                    selectedBodega = ""
                }
            )

            ComboBoxGrupoBodega(
                selectedOption = selectedBodega,
                onOptionSelected = { selectedBodega = it }, local = selectedLocal.take(2)
            )

            Log.d("*MAKITA*111*", "Pasa por selectedBodega: $selectedBodega")
            Log.d("*MAKITA*111*", "Pasa por selectedBodega: $selectedBodega")
            Log.d("*MAKITA*111*", "Pasa por selectedBodega: $selectedLocal")


            if (selectedOption == "INVENTARIO" && selectedTipo == "ACCESORIOS" && selectedBodega == "2") {

                ComboBoxCategoria(
                    selectedOption = selectedCategoria,
                    onOptionSelected = { selectedCategoria = it }
                )
            }
            Spacer(modifier = Modifier.height(10.dp))

            Button(
                onClick =
                {
                    if (selectedOption.isEmpty()) {
                        showError = true // Muestra el error si no hay selección
                        Toast.makeText(
                            context,
                            "Campo Obligatorio, debe ingresar Tipo de Inventario",
                            Toast.LENGTH_LONG
                        ).show()
                    } else {
                        showError = false
                        // Acción adicional si la selección es válida
                        println("Opción seleccionada: $selectedOption")
                    }

                    if (usuarioasigando.isEmpty()) {
                        showError = true // Muestra el error si no hay selección
                        Toast.makeText(
                            context,
                            "Usuario No Asociado a capturador, solicite al Administracion su Asignacion",
                            Toast.LENGTH_LONG
                        ).show()
                    } else {
                        showError = false

                    }

                    val fechaFormateada = formatearFecha(selectedDate)
                    val fechaCodificada =
                        URLEncoder.encode(fechaFormateada, StandardCharsets.UTF_8.toString())

                    if (selectedOption == "INVENTARIO") {
                        if (selectedTipo == "ACCESORIOS" || selectedTipo == "REPUESTOS") { // Reemplaza "specific_option" con la opción deseada

                            if (selectedCategoria == "BATERIAS" && selectedTipo == "ACCESORIOS" && selectedBodega == "2") {

                                Log.d(
                                    "*MAKITA*111*",
                                    "Pasa por selectedCategoria: $selectedCategoria"
                                )

                                navController.navigate("quinta_screen/$selectedTipo/$selectedLocal/$usuarioasigando/$fechaCodificada/$selectedBodega")

                            } else {
                                if (selectedCategoria != "BATERIAS" && (selectedTipo == "ACCESORIOS" || selectedTipo == "REPUESTOS") && selectedBodega == "1") {
                                    navController.navigate("third_screen/$selectedTipo/$selectedLocal/$usuarioasigando")
                                }
                            }

                        } else {
                            navController.navigate("second_screen/$selectedTipo/$selectedLocal/$usuarioasigando/$fechaCodificada/$selectedBodega")
                        }
                    } else {

                        if (selectedOption == "RECONTEO") {

                            val fechaFormateada = formatearFecha(selectedDate)
                            val fechaCodificada = URLEncoder.encode(
                                fechaFormateada,
                                StandardCharsets.UTF_8.toString()
                            )
                            navController.navigate("cuarta_screen/$selectedTipo/$selectedLocal/$usuarioasigando/$fechaCodificada/$selectedBodega")
                        }
                    }
                },
                enabled = selectedOption.isNotEmpty() && selectedTipo.isNotEmpty() && selectedLocal.isNotEmpty() && usuarioasigando.isNotEmpty() && selectedBodega.isNotEmpty() && selectedBodega.isNotEmpty() && selectedDate.isNotEmpty(), // Habilita el botón solo si todos los campos están llenos,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF00909E),
                    contentColor = Color.White
                ),
                modifier = Modifier
                    .padding(horizontal = 8.dp, vertical = 4.dp)
                    .width(300.dp)
                    .height(45.dp),
                shape = RoundedCornerShape(8.dp),
            )
            {

                Text(
                    text = "${selectedOption.uppercase()} ${selectedTipo.uppercase()} ${selectedCategoria.uppercase()}",
                    color = Color.White,
                    fontSize = 13.sp,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            Button(
                onClick = { activity?.finish() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF00909E),
                    contentColor = Color.White
                ),
                modifier = Modifier
                    .padding(horizontal = 8.dp, vertical = 4.dp)
                    .width(300.dp)
                    .height(45.dp),
                shape = RoundedCornerShape(8.dp),
            ) {
                Text(
                    text = "Salir",
                    color = Color.White,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(top = 8.dp)
                )

            }

            // Mensaje de error si no se selecciona una opción
            if (showError && selectedOption.isEmpty() && selectedTipo.isEmpty() && selectedLocal.isEmpty() && selectedBodega.isEmpty()) {
                Text(
                    text = "Este campo es obligatorio",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                )
                showDialog = true
                // mostrarDialogo(context, "Error", "Seleccione los campos obligatorios Tipo,TipoItem,Local,Bodega")
            }
        }
    }
}


fun formatearFecha(selectedDate: String): String {
    return try {
        val inputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        val outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd") // Formato deseado
        val date = LocalDate.parse(selectedDate, inputFormatter)
        date.format(outputFormatter)
    } catch (e: Exception) {
        "0000-00-00"
    }

}


fun formatoFechaSS(timestamp: Long): String {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    return dateFormat.format(Date(timestamp))
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerWithTextField(selectedDate: String, onDateSelected: (String) -> Unit) {
    var showDatePickerDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)
    var selectedDate2 by remember {
        mutableStateOf(
            if (selectedDate.isNotEmpty()) selectedDate else String.format(
                "%02d/%02d/%d",
                day,
                month + 1,
                year
            )
        )
    }
    val defaultDate = String.format("%02d/%02d/%d", day, month + 1, year)

    LaunchedEffect(Unit) {
        if (selectedDate.isEmpty()) {
            onDateSelected(defaultDate)
        }
    }

    val datePickerDialog = remember {
        DatePickerDialog(
            context,
            { _, selectedYear, selectedMonth, selectedDayOfMonth ->
                val monthFormatted =
                    String.format("%02d", selectedMonth + 1) // Asegura dos dígitos en el mes
                val dayFormatted =
                    String.format("%02d", selectedDayOfMonth)  // Asegura dos dígitos en el día
                selectedDate2 = "$dayFormatted/$monthFormatted/$selectedYear"
                onDateSelected(selectedDate2)
            },
            year, month, day
        )
    }

    OutlinedTextField(
        value = selectedDate2,
        onValueChange = {},
        modifier = Modifier
            .width(320.dp)
            .height(60.dp)
            .clickable {
                datePickerDialog.show()
            },
        readOnly = true,
        label = { Text("INGRESE FECHA INVENTARIO") },
        trailingIcon = {
            Icon(
                imageVector = Icons.Default.CalendarToday,
                contentDescription = "Calendario"
            )
        }
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ComboBoxWithTextField(
    selectedOption: String,
    onOptionSelected: (String) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) } // Controla si el menú está abierto
    var showError by remember { mutableStateOf(false) }
    val options = listOf("INVENTARIO", "RECONTEO")

    Box {
        OutlinedTextField(
            value = selectedOption,
            onValueChange = {}, // Campo de solo lectura
            modifier = Modifier
                .height(60.dp)
                .width(320.dp)
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
                    text = {
                        Text(option, color = Color.Blue, fontSize = 20.sp)

                    },
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
    onOptionSelected: (String) -> Unit,

    ) {
    var expanded by remember { mutableStateOf(false) }
    val options = listOf("REPUESTOS", "ACCESORIOS", "HERRAMIENTAS") // Opciones del segundo ComboBox


    Box {
        OutlinedTextField(
            value = selectedOption,
            onValueChange = {},
            modifier = Modifier
                .height(60.dp)
                .width(320.dp)
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
                    text = {
                        Text(option, color = Color.Blue, fontSize = 20.sp)
                    },
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
    onOptionSelected: (String) -> Unit,

    ) {
    var expanded by remember { mutableStateOf(false) }
    val options = listOf(
        "01-ENEA",
        "03-TEMUCO",
        "04-ANTOFAGASTA",
        "05-COPIAPO"
    ) // Opciones del segundo ComboBox

    Box {
        OutlinedTextField(
            value = selectedOption,
            onValueChange = {},
            modifier = Modifier
                .width(320.dp)
                .height(60.dp)
                .clickable { expanded = true },
            readOnly = true,
            label = { Text("Seleccione Local") },
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

                    text = {
                        Text(option, color = Color.Blue, fontSize = 20.sp)

                    },

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
fun ComboBoxGrupoBodega(
    selectedOption: String,
    onOptionSelected: (String) -> Unit,
    local: String,

    ) {
    var expanded by remember { mutableStateOf(false) }
    var opciones by remember { mutableStateOf<List<GrupoBodegaResponse>>(emptyList()) }
    val context = LocalContext.current
    var showErrorDialog by remember { mutableStateOf(false) }

    LaunchedEffect(local) {
        //val localValue = if (local.isNullOrEmpty()) "01" else local
        val localValue = if (local.isNullOrEmpty()) "01" else local
        try {

            val respuesta22 = apiService.obtenerGrupoBodega("MAKITA", localValue.trim())

            if (respuesta22.isNotEmpty()) {
                opciones = respuesta22
                Log.d("*MAKITA*", "Grupos obtenidos: $respuesta22")
            }

        } catch (e: Exception) {
            // Log.e("MAKITA", "Error al obtener grupo ", e)
            //val linea = "Debe Seleccionar local y Grupo " + e.message
            showErrorDialog = true

            val linea = "Debe Seleccionar local y Grupo "
        }
    }

    if (showErrorDialog) {
        mostrarDialogo(
            titulo = "Error",
            mensaje = "Debe Seleccionar local y Grupo ",
            onDismiss = { showErrorDialog = false }
        )
    }

    Box {
        OutlinedTextField(
            value = selectedOption,
            onValueChange = {},
            modifier = Modifier
                .height(60.dp)
                .width(320.dp)
                .clickable { expanded = true },
            readOnly = true,
            label = { Text("Seleccione Grupo de Bodega ") },
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
            opciones.forEach { option ->
                DropdownMenuItem(
                    text = {
                        Text(
                            option.NombreGrupoBodega,
                            color = if (selectedOption == option.NombreGrupoBodega) Color.Blue else Color.Blue,
                            fontSize = 20.sp
                        )

                    }, // Reemplaza 'nombre' con la propiedad correcta
                    onClick = {

                        onOptionSelected(option.GrupoBodega)
                        expanded = false
                    }
                )
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ComboBoxCategoria(
    selectedOption: String,
    onOptionSelected: (String) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    var opciones by remember { mutableStateOf<List<CategoriaResponse>>(emptyList()) }
    val context = LocalContext.current
    var showErrorDialog2 by remember { mutableStateOf(false) }

    LaunchedEffect(selectedOption) {
        try {
            val respuesta24 = apiService.obtenerCategoria("MAKITA")

            if (respuesta24.isNotEmpty()) {
                opciones = respuesta24

            } else {
                showErrorDialog2 = true
                Log.d("*MAKITA*111*", "vacias nulas: $respuesta24")
            }

        } catch (e: Exception) {

            val linea = "Debe Seleccionar Categoria " + e.message
            Toast.makeText(context, linea, Toast.LENGTH_SHORT).show()
            showErrorDialog2 = true
        }
    }

    if (showErrorDialog2) {
        mostrarDialogo2(
            titulo = "Informacion",
            mensaje = "Debe Seleccionar Categoria",
            onDismiss = { showErrorDialog2 = false }
        )
    }

    Box {
        OutlinedTextField(
            value = selectedOption,
            onValueChange = {},
            modifier = Modifier
                .height(60.dp)
                .width(320.dp)
                .clickable { expanded = true },
            readOnly = true,
            label = { Text("Seleccione Categoria  ") },
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
            opciones.forEach { option ->
                DropdownMenuItem(
                    text = {
                        Text(
                            option.Descripcion,
                            color = if (selectedOption == option.Descripcion) Color.Blue else Color.Blue,
                            fontSize = 20.sp
                        )
                    },
                    onClick = {
                        onOptionSelected(option.Descripcion)
                        expanded = false
                    }
                )
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SecondScreen(
    navController: NavController,
    param: String,
    param2: String,
    param3: String,
    param4: String,
    param5: String,
) {
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
    var showDialog by remember { mutableStateOf(false) }
    var textFieldValue2 by remember { mutableStateOf("") }
    var showErrorDialog by remember { mutableStateOf(false) }
    var ubicacion by remember { mutableStateOf("") }
    var cantidad by remember { mutableStateOf("") }
    val apiService = RetrofitClient.apiService
    val keyboardController = LocalSoftwareKeyboardController.current

    var gTipoItem by remember { mutableStateOf("") }
    var gLocal by remember { mutableStateOf("") }
    var gusuarioasigando by remember { mutableStateOf("") }
    var gFechaInventario by remember { mutableStateOf("") }
    var gFechaInventario2 by remember { mutableStateOf("") }
    var gGrupoBodega by remember { mutableStateOf("") }


    val scrollState = rememberScrollState()

    var ultimaubicacion by remember { mutableStateOf("") }
    var mensajeError2 by remember { mutableStateOf("") }
    var mensajeError by remember { mutableStateOf("") }
    var botonVer by remember { mutableStateOf(true) }

    var isLoading by remember { mutableStateOf(false) } // Estado para el loading

    fun validarCampos(): Boolean {
        return cantidad.isNotEmpty()
    }

    Surface(
        modifier = Modifier.fillMaxSize(),

        ) {

        gTipoItem = param ?: gTipoItem
        gLocal = param2 ?: gLocal
        gusuarioasigando = param3 ?: gusuarioasigando
        gFechaInventario2 = param4 ?: gFechaInventario
        gGrupoBodega = param5 ?: gGrupoBodega

        gFechaInventario = URLDecoder.decode(gFechaInventario2, StandardCharsets.UTF_8.toString())


        textFieldValue2 = "" // Descripcion

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            val context = LocalContext.current
            val gnombreDispositivo = remember { obtenerNombreDelDispositivo(context) }
            val subtitulo = "$gLocal $gnombreDispositivo"

            Titulo2(param = gTipoItem, param2 = subtitulo)
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
                            ultimaubicacion =
                                respuesta.first().ubicacion // Solo toma la primera ubicación
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

            LaunchedEffect(Unit) {
                ubicacionFocusRequester.requestFocus()
            }

            OutlinedTextField(
                value = ubicacion,
                onValueChange = {
                    ubicacion = it
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
                leadingIcon = {
                    Icon(
                        Icons.Default.LocationOn,
                        contentDescription = "Icono de ubicación"
                    )
                },
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
                mensajeError2 = "La ubicación no debe exceder los 10 caracteres"
                // mostrarDialogo3(context, "Error", mensajeError2)
                mostrarDialogo3(
                    titulo = "Error",
                    mensaje = mensajeError2,
                    onDismiss = { showDialog = false }
                )

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
                        extractedText2 =
                            newText.substring(20, newText.length.coerceAtMost(29)) // Serie desde
                        extractedText3 =
                            newText.substring(29, newText.length.coerceAtMost(38)) // Serie hasta
                        extractedText4 =
                            newText.substring(39, newText.length.coerceAtMost(52)) // EAN
                    } else {
                        extractedText = newText
                        extractedText2 = ""
                        extractedText3 = ""
                        extractedText4 = ""
                    }

                    if (newText.length >= 20) {
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
                                Log.d(
                                    "*MAKITA*AQUI*",
                                    "RESPUESTA NO - ENTRA validarTipoItem: $response35"
                                )

                                textFieldValue2 = ""
                                mensajeError =
                                    "Item: ${extractedText.trim()} NO CORRESPONDE A $gTipoItem"
                                Log.d(
                                    "*MAKITA*AQUI*",
                                    "NO ENTRA API validarTipoItem: $mensajeError"
                                )

                                showDialog = true

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
                            Toast.makeText(
                                context,
                                "Error al obtener los datos, revise WiFi: ${e.message}",
                                Toast.LENGTH_LONG
                            ).show()
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

            if (showDialog) {
                mostrarDialogo3(
                    titulo = "Error",
                    mensaje = mensajeError,
                    onDismiss = { showDialog = false }
                )
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
                            if (newValue.all { it.isDigit() } && newValue.length <= 10) { // Limitar a 10 caracteres
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


                    if (cantidad.length > 10) {
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
                        .padding(14.dp),
                    //horizontalArrangement = Arrangement.SpaceEvenly // Espaciado uniforme entre boton
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    val buttonModifier = Modifier
                        .width(110.dp)  // Asegura que todos los botones tengan el mismo ancho
                        .height(45.dp)  // Asegura que todos los botones tengan la misma altura
                        .padding(horizontal = 3.dp)

                    val buttonColors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF00909E),
                        contentColor = Color.White
                    )

                    Button(
                        onClick = {
                            navController.navigate("septima_screen/$param/$param2/$param3/$param4/$param5/0")

                        },

                        enabled = botonVer,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF00909E),
                            contentColor = Color.White
                        ),
                        modifier = Modifier
                            .padding(horizontal = 7.dp, vertical = 4.dp)
                            .width(100.dp)
                            .height(43.dp),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 6.dp, vertical = 0.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ReceiptLong,
                            contentDescription = "Ver lo ingresado",
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = "Ver",
                            color = Color.White,
                            fontSize = 14.sp,
                            modifier = Modifier.padding(top = 4.dp)
                        )


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
                        modifier = Modifier
                            .padding(horizontal = 7.dp, vertical = 4.dp)
                            .width(100.dp)
                            .height(43.dp),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 6.dp, vertical = 0.dp)

                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Borrar",
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                        Text(text = "Borrar", fontSize = 14.sp)
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
                                            extractedText.trim(),  //item
                                            extractedText2.trim(), //ubicacion
                                            Usuario
                                        )

                                        Log.d(
                                            "*MAKITA*",
                                            "API validarUbicacionProducto: $response33"
                                        )

                                        if (!response33.isNullOrEmpty()) {
                                            errorState =
                                                "No se encontraron datos para el item proporcionado"

                                            if (response33 == "NO") {
                                                val requestRegistroInventario =
                                                    RegistraInventarioRequest(
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
                                                        Usuario = gusuarioasigando,
                                                        NombreDispositivo = gnombreDispositivo
                                                    )
                                                Log.d(
                                                    "*MAKITA*",
                                                    "Datos enviados en requestRegistroInventario: $requestRegistroInventario"
                                                )

                                                val bitacoraRegistroUbi =
                                                    apiService.insertarinventario(
                                                        requestRegistroInventario
                                                    )

                                                Log.d(
                                                    "*MAKITA*",
                                                    "RESPUESTA DE INSERTAR INVENTARIO: $bitacoraRegistroUbi"
                                                )

                                                guardarRespaldo(
                                                    context,
                                                    requestRegistroInventario,
                                                    FechaFija
                                                )
                                                delay(1500)
                                                Toast.makeText(
                                                    context,
                                                    "Registro Grabado",
                                                    Toast.LENGTH_LONG
                                                ).show()
                                            } else {
                                                var linea =
                                                    "Item: ${extractedText.trim()} en Ubicacion ${extractedText2.trim()} YA INVENTARIADO"
                                                Toast.makeText(context, linea, Toast.LENGTH_SHORT)
                                                    .show()
                                                //mostrarDialogo2(context, "Error", linea)
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
                                        Toast.makeText(
                                            context,
                                            "Error al grabar el item, intentelo nuevamente",
                                            Toast.LENGTH_LONG
                                        ).show()
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
                                        ubicacionFocusRequester.requestFocus()
                                    }
                                }
                            }
                        },
                        colors = buttonColors,
                        modifier = Modifier
                            .padding(horizontal = 7.dp, vertical = 4.dp)
                            .width(100.dp)
                            .height(43.dp),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 6.dp, vertical = 0.dp),
                        enabled = !isLoading && validarCampos()
                    ) {
                        Icon(
                            imageVector = Icons.Default.Done,
                            contentDescription = "Guardar datos",
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )

                        Text(text = "Guardar", fontSize = 14.sp)
                    }
                }
            }

        }
    }
}


/*
fun obtenerNombreDelDispositivo(): String {
    val fabricante = Build.MANUFACTURER // Ejemplo: "Honeywell"
    val modelo = Build.MODEL           // Ejemplo: "CT50"

    return "Dispositivo $fabricante $modelo"
}
*/
fun obtenerNombreDelDispositivo(context: Context): String {

    return Settings.Global.getString(context.contentResolver, Settings.Global.DEVICE_NAME)
        ?: "Desconocido"
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


@Composable
fun MostrarDialogoMasivoCompose(
    titulo: String,
    mensaje: String,
    onDismiss: () -> Unit,
) {
    LaunchedEffect(Unit) {
        delay(1000L)
        onDismiss()
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = titulo) },
        text = { Text(text = mensaje) },
        confirmButton = {} // Sin botón
    )
}


@Composable
fun mostrarDialogo(
    titulo: String,
    mensaje: String,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = titulo) },
        text = { Text(text = mensaje, fontSize = 16.sp) },

        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("OK")
            }
        }
    )
}

@Composable
fun mostrarDialogo4(
    titulo: String,
    mensaje: String,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = titulo) },
        text = { Text(text = mensaje) },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("OK")
            }
        },
        containerColor = Color(0xFF33B5E5) // Aproximado a holo_blue_light
    )
}


@Composable
fun mostrarDialogo3(
    titulo: String,
    mensaje: String,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = titulo) },
        text = { Text(text = mensaje) },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Aceptar")
            }
        }
    )
}

@Composable
fun mostrarDialogo2(
    titulo: String,
    mensaje: String,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = titulo) },
        text = { Text(text = mensaje) },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Aceptar")
            }
        }
    )
}


fun isNetworkAvailable(context: Context): Boolean {
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val networkInfo = connectivityManager.activeNetworkInfo
    return networkInfo != null && networkInfo.isConnected
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TerceraScreen(navController: NavController, param: String, param2: String, param3: String) {
    val ubicacionFocusRequester = remember { FocusRequester() }
    val cantidadFocusRequester = remember { FocusRequester() }
    val itemFocusRequester = remember { FocusRequester() }
    var text by remember { mutableStateOf("") }
    var extractedText by remember { mutableStateOf("") }
    var extractedText2 by remember { mutableStateOf("") }
    var extractedText3 by remember { mutableStateOf("") }
    var extractedText4 by remember { mutableStateOf("") }
    var response4 by rememberSaveable { mutableStateOf<List<UltimaResponse>>(emptyList()) }
    val context = LocalContext.current
    var response by rememberSaveable { mutableStateOf<List<ItemResponse>>(emptyList()) }
    var response2 by rememberSaveable { mutableStateOf<List<ItemResponse>>(emptyList()) }
    var errorState by rememberSaveable { mutableStateOf<String?>(null) }
    var errorMessage by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }
    var textFieldValue2 by remember { mutableStateOf("") }
    var showErrorDialog by remember { mutableStateOf(false) }
    var ubicacion by remember { mutableStateOf("") }
    var cantidad by remember { mutableStateOf("") }
    val apiService = RetrofitClient.apiService
    val keyboardController = LocalSoftwareKeyboardController.current
    var gTipoItem by remember { mutableStateOf("") }
    var gLocal by remember { mutableStateOf("") }
    var gUsuarioAsignado by remember { mutableStateOf("") }
    val scrollState = rememberScrollState()
    var ultimaubicacion by remember { mutableStateOf("") }
    var mensajeDialogo by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) } // Estado para el loadingwqeqweqwe

    fun validarCampos(): Boolean {
        return cantidad.isNotEmpty()
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        // color = MaterialTheme.colorScheme.background
    ) {

        LaunchedEffect(Unit) {
            itemFocusRequester.requestFocus()
        }

        gTipoItem = param ?: gTipoItem
        gLocal = param2 ?: gLocal
        gUsuarioAsignado = param3 ?: gUsuarioAsignado

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            val context = LocalContext.current
            val gnombreDispositivo = remember { obtenerNombreDelDispositivo(context) }

            Log.d("*MAKITA*", "NOMBRE: $gnombreDispositivo")

            val subtitulo = "$gLocal $gnombreDispositivo"

            Titulo()
            Titulo2(param = gTipoItem, param2 = subtitulo)
            Separar()

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
            if (isLoading) {
                LoadingIndicator()
            }
            OutlinedTextField(
                value = text,
                onValueChange = { newText ->
                    text = newText

                    // Procesar los datos según la longitud del texto
                    Log.d("*MAKITA*", "Longitud del texto: ${newText.length}")

                    if (newText.length == 51) {
                        extractedText = ""
                        extractedText = newText.substring(0, 20) // Primeros 20 caracteres (item)
                        extractedText2 =
                            newText.substring(20, (20 + 18).coerceAtMost(newText.length))
                        Log.d("*MAKITA*", "INGRESA A LARGO 51: $extractedText")
                    }

                    if (newText.length == 41 || newText.length == 50 || newText.length == 51 || newText.length == 52 || newText.length == 53 || newText.length == 54 || newText.length == 55 || newText.length == 56) {
                        extractedText = newText.substring(0, 20) // Primeros 20 caracteres (item)
                        extractedText2 =
                            newText.substring(20, (20 + 18).coerceAtMost(newText.length))

                    }


                    if (newText.length == 37) {
                        extractedText = newText.substring(0, 20) // Primeros 20 caracteres (item)
                        extractedText2 =
                            newText.substring(20, (20 + 5).coerceAtMost(newText.length))

                    }


                    if (newText.length == 41) {
                        extractedText = newText.substring(0, 20) // Primeros 20 caracteres (item)
                        extractedText2 =
                            newText.substring(20, (20 + 8).coerceAtMost(newText.length))

                    }


                    if (newText.length == 38) {
                        extractedText = newText.substring(0, 20) // Primeros 20 caracteres (item)
                        extractedText2 =
                            newText.substring(20, (20 + 6).coerceAtMost(newText.length))

                    }


                    if (newText.length >= 20) {
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
                        IconButton(onClick = { text = "" }) {
                            Icon(Icons.Default.Clear, contentDescription = "Limpiar texto")
                        }
                    }
                }
            )

            Spacer(modifier = Modifier.height(10.dp))

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
                    if (extractedText.isNullOrEmpty()) {
                        Toast.makeText(
                            context,
                            "Seleccione Item correcto para el Tipo de Item",
                            Toast.LENGTH_SHORT
                        ).show()
                        return@LaunchedEffect
                    }

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
                                        errorMessage =
                                            "Error de conexión al validar el tipo de ítem"
                                        showErrorDialog = true
                                    }
                                    return@launch
                                }

                                withContext(Dispatchers.Main) {
                                    if (response35 == "NO") {
                                        Log.d(
                                            "*MAKITA*AQUI*",
                                            "RESPUESTA NO - ENTRA validarTipoItem: $response35"
                                        )

                                        textFieldValue2 = ""
                                        val mensajeError =
                                            "Item: ${extractedText.trim()} NO CORRESPONDE A $gTipoItem"
                                        Log.d(
                                            "*MAKITA*AQUI*",
                                            "NO ENTRA API validarTipoItem: $mensajeError"
                                        )
                                        Toast.makeText(
                                            context,
                                            "Error de red: No hay conexión a Internet",
                                            Toast.LENGTH_SHORT
                                        ).show()


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
                                val apiResponse = apiService.obtenerUbicacionItem(extractedText)
                                Log.d("*MAKITA*", "PASA A obtenerUbicacionItem: $apiResponse")

                                withContext(Dispatchers.Main) {
                                    response = apiResponse

                                    if (apiResponse.isNullOrEmpty()) {
                                        Log.d("*MAKITA*", "ES XX EMPTY: $apiResponse")
                                        errorState =
                                            "No se encontraron datos para el item proporcionado"
                                    } else {
                                        errorState = null

                                        val tieneValoresNulos =
                                            apiResponse.any { it.item == null }
                                        if (tieneValoresNulos) {
                                            errorMessage =
                                                "Advertencia! No existe última Ubicación"
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
                                    Toast.makeText(
                                        context,
                                        "Error al obtener los datos: ${e.message}",
                                        Toast.LENGTH_LONG
                                    ).show()
                                    errorMessage = "Error al obtener los datos: ${e.message}"
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
                        Toast.makeText(
                            context,
                            "No hay conexión a Internet",
                            Toast.LENGTH_SHORT
                        ).show()
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
                            if (newValue.all { it.isDigit() } && newValue.length <= 10) { // Limitar a 10 caracteres
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

                    if (cantidad.length > 10) {
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

                                        Log.d(
                                            "*MAKITA*",
                                            "API validarUbicacionProducto: $response33"
                                        )

                                        if (!response33.isNullOrEmpty()) {
                                            errorState =
                                                "No se encontraron datos para el item proporcionado"

                                            if (response33 == "NO") {
                                                val requestRegistroInventario =
                                                    RegistraInventarioRequest(
                                                        Id = "1",
                                                        Empresa = "MAKITA",
                                                        FechaInventario = FechaFija,
                                                        TipoInventario = "INVENTARIO",
                                                        Bodega = gLocal,
                                                        Clasif1 = gTipoItem,
                                                        Ubicacion = extractedText2.trim(), // Item =  "GA4530",//textFieldValue2'',
                                                        Item = extractedText.trim(),
                                                        Cantidad = cantidad,
                                                        Estado = "Ingresado",
                                                        Usuario = gUsuarioAsignado,
                                                        NombreDispositivo = gnombreDispositivo
                                                    )

                                                val bitacoraRegistroUbi =
                                                    apiService.insertarinventario(
                                                        requestRegistroInventario
                                                    )

                                                Log.d(
                                                    "*MAKITA*",
                                                    "RESPUESTA DE INSERTAR INVENTARIO: $bitacoraRegistroUbi"
                                                )

                                                guardarRespaldo(
                                                    context,
                                                    requestRegistroInventario,
                                                    FechaFija
                                                )
                                                delay(1500)
                                                Toast.makeText(
                                                    context,
                                                    "Registro Grabado",
                                                    Toast.LENGTH_LONG
                                                ).show()
                                            } else {
                                                val linea =
                                                    "Item: ${extractedText.trim()} en Ubicacion ${extractedText2.trim()} YA INVENTARIADO"
                                                Toast.makeText(context, linea, Toast.LENGTH_LONG)
                                                    .show()

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
                                        Toast.makeText(
                                            context,
                                            "Error al grabar el item, intentelo nuevamente",
                                            Toast.LENGTH_LONG
                                        ).show()
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


@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun CuartaScreen(
    navController: NavController,
    param: String,
    param2: String,
    param3: String,
    param4: String,
    param5: String,
) {
    var gTipoItem by remember { mutableStateOf("") }
    var gLocal by remember { mutableStateOf("") }
    var gUsuario by remember { mutableStateOf("") }
    var gFechaInventario by remember { mutableStateOf("") }
    var gFechaInventario2 by remember { mutableStateOf("") }
    var gGrupoBodega by remember { mutableStateOf("") }
    var respuesta55 by remember { mutableStateOf<List<ItemsReconteoResponse>>(emptyList()) }
    var errorState by remember { mutableStateOf<String?>(null) }
    var cantidadMap by remember { mutableStateOf(mutableMapOf<String, String>()) }
    var showDialog by remember { mutableStateOf(false) }
    var botonVolver by remember { mutableStateOf(true) }
    var botonLimpiar by remember { mutableStateOf(true) }
    var botonVer by remember { mutableStateOf(true) }
    var botonGrabar by remember { mutableStateOf(true) }
    var swCargando by remember { mutableStateOf(true) }
    var showItemDialog by remember { mutableStateOf(false) }
    var selectedItemTexto by remember { mutableStateOf("") }
    val listaItems = remember { mutableStateListOf<ItemConCantidad>() }
    var grabacionExitosa by remember { mutableStateOf(false) }
    var itemGrabado by remember { mutableStateOf("") }

    Surface(
        modifier = Modifier.fillMaxSize(),
    ) {

        gTipoItem = param ?: gTipoItem
        gLocal = param2 ?: gLocal
        gUsuario = param3 ?: gUsuario
        gFechaInventario2 = param4 ?: gFechaInventario
        gGrupoBodega = param5 ?: gGrupoBodega

        gFechaInventario = URLDecoder.decode(gFechaInventario2, StandardCharsets.UTF_8.toString())

        val gyear = ObtenerAno(gFechaInventario)
        val gmonth = ObtenerMes(gFechaInventario)

        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            val context = LocalContext.current
            val gnombreDispositivo = remember { obtenerNombreDelDispositivo(context) }
            val subtitulo = "$gLocal Dispositivo$gnombreDispositivo "

            TituloReconteo()
            Separar()
            Titulo3(
                param = gTipoItem,
                param2 = gLocal,
                param3 = gUsuario,
                param4 = gFechaInventario,
                param5 = gnombreDispositivo
            )
            Separar()

            if (swCargando) {
                LoadingIndicator()
            }

            LaunchedEffect(gyear, gmonth, gLocal, gTipoItem, gUsuario, gGrupoBodega) {

                try {

                    val resultado = apiService.obtenerReconteo(
                        "MAKITA",
                        gyear.toString(),
                        gmonth.toString(),
                        "RECONTEO",
                        gLocal,
                        gTipoItem,
                        gUsuario,
                        gGrupoBodega
                    )

                    respuesta55 = resultado

                    swCargando = false

                    listaItems.clear()


                    listaItems.addAll(resultado.map { item ->
                        ItemConCantidad(
                            tipoitem = item.Clasif1 ?: "Sin TipoItem",
                            numeroreconteo = item.NumeroReconteo ?: "Sin Reconteo",
                            ubicacion = item.Ubicacion ?: "Sin Ubicacion",
                            item = item.Item ?: "Sin Item",
                            cantidad = ""
                        )
                    })


                    botonVolver = true
                    botonLimpiar = true
                    botonGrabar = true
                    botonVer = true


                } catch (e: Exception) {
                    errorState = "Error al obtener Reconteos"

                    botonVolver = true
                    botonLimpiar = false
                    botonGrabar = false
                    swCargando = false

                    showDialog = true
                    val mensaje3 =
                        "No tiene asignados reconteos verifique con Supervisor su actividad. Usuario:  ${gUsuario} "
                }
            }


            if (showDialog) {
                mostrarDialogo(
                    titulo = "Informacion",
                    mensaje = "No tiene asignados reconteos verifique con Supervisor su actividad. Usuario:  ${gUsuario}  ",
                    onDismiss = { showDialog = false }
                )
            }


            val headers = listOf("Nro", "Item", "Ubicacion", "Cantidad")
            val fields = listOf<(ItemConCantidad) -> String>(
                { it.tipoitem },
                { it.numeroreconteo },
                { it.item },
                { it.ubicacion }
            )

            Box(modifier = Modifier.horizontalScroll(rememberScrollState())) {
                Column {
                    // Crear las cabeceras en una fila fija
                    Row(modifier = Modifier.fillMaxWidth()) {
                        headers.forEach { header ->
                            Text(
                                text = header,
                                modifier = Modifier
                                    .padding(horizontal = 8.dp, vertical = 3.dp),
                                fontWeight = FontWeight.Bold,
                                color = Color.Blue,
                                fontSize = 18.sp,
                                maxLines = 1,
                                textAlign = TextAlign.Start
                            )
                        }
                    }
                    val cantidades = remember { mutableStateMapOf<Int, String>() }

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxHeight(0.8f)
                            .width(600.dp)
                            .padding(top = 3.dp)
                    ) {

                        stickyHeader {
                            Text(
                                text = "Total ítems por contar: ${listaItems.size}",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF00909E),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Color.LightGray)
                                    .padding(8.dp)
                            )
                        }



                        itemsIndexed(listaItems) { index, item ->
                            val rowColor = if (index % 2 == 0) Color(0xFFF1F1F1) else Color.White
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(rowColor)

                                    .padding(vertical = 3.dp)
                            )
                            {

                                /*INDICE*/
                                Text(
                                    text = (index + 1).toString(),
                                    color = Color.DarkGray,
                                    modifier = Modifier
                                        .width(40.dp)
                                        .padding(horizontal = 3.dp, vertical = 5.dp),
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.Bold
                                )


                                // Mostrar los campos de la respuesta (TipoItem, Item, Ubicacion)
                                fields.forEachIndexed { index, field ->


                                    if (index == 1) {

                                        val valorCampo = field(item)

                                        val textColor = if (index == 3) Color.Blue
                                        else Color.Black


                                        Text(
                                            text = field(item),
                                            color = textColor,
                                            modifier = Modifier
                                                .width(40.dp)
                                                .padding(horizontal = 3.dp)
                                                .padding(vertical = 5.dp),
                                            fontSize = 17.sp,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )

                                    }

                                    if (index > 1) {

                                        val textColor = if (index == 3) Color.Blue
                                        else Color.Black

                                        Text(
                                            text = field(item),
                                            color = textColor,
                                            modifier = Modifier
                                                .width(120.dp)
                                                .padding(horizontal = 2.dp)
                                                .padding(vertical = 5.dp)
                                                .clickable {
                                                    if (index == 2) {
                                                        selectedItemTexto =
                                                            item.item  // o cualquier campo que quieras mostrar
                                                        showItemDialog = true
                                                        //AQUI

                                                        Toast
                                                            .makeText(
                                                                context,
                                                                selectedItemTexto,
                                                                Toast.LENGTH_SHORT
                                                            )
                                                            .show()
                                                    }
                                                },
                                            fontSize = 17.sp,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )

                                    }
                                }

                                val cantidad = cantidades[index] ?: ""

                                TextField(
                                    value = cantidad,
                                    onValueChange = { newValue ->
                                        val anteriorIndex = index - 1
                                        if (newValue.all { it.isDigit() } && newValue.length <= 4) {
                                            // Si es el primer ítem o el ítem anterior ya fue enviado (ya no está en la lista)
                                            val permitirIngreso = if (index == 0) {
                                                true
                                            } else {
                                                val anteriorItem =
                                                    listaItems.getOrNull(anteriorIndex)
                                                anteriorItem == null || !anteriorItem.cantidad.isNullOrBlank()
                                            }

                                            if (permitirIngreso) {
                                                cantidades[index] = newValue
                                                listaItems[index] =
                                                    listaItems[index].copy(cantidad = newValue)
                                                Log.d(
                                                    "*MAKITA*",
                                                    "Cantidad actualizada para ${listaItems[index].item}: $newValue"
                                                )
                                            } else {
                                                Toast.makeText(
                                                    context,
                                                    "Debe enviar primero la cantidad anterior",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                                cantidades[index] = ""
                                                listaItems[index] =
                                                    listaItems[index].copy(cantidad = "")
                                            }
                                        }

                                        if (newValue.all { it.isDigit() } && newValue.length <= 4) {
                                            cantidades[index] = newValue
                                            listaItems[index] =
                                                listaItems[index].copy(cantidad = newValue)

                                            Log.d(
                                                "*MAKITA*111*",
                                                "Cantidad actualizada para ${listaItems[index].item}: ${listaItems[index].cantidad}"
                                            )

                                        }
                                    },
                                    placeholder = { Text("Cantidad") },
                                    keyboardOptions = KeyboardOptions.Default.copy(
                                        keyboardType = KeyboardType.Number,
                                        imeAction = ImeAction.Done
                                    ),
                                    modifier = Modifier
                                        .width(84.dp)
                                        .height(50.dp)
                                        /*  .border(2.dp, Color.Black, shape = RoundedCornerShape(4.dp))*/
                                        .padding(horizontal = 2.dp),
                                    textStyle = TextStyle(
                                        fontSize = 18.sp,
                                        color = Color.Red,
                                        fontFamily = FontFamily.Serif,
                                        fontWeight = FontWeight.Bold
                                    ),
                                    singleLine = true
                                )

                                Button(
                                    onClick = {
                                        if (listaItems[index].cantidad.isEmpty()) {
                                            Toast.makeText(
                                                context,
                                                "Error: Ingrese la cantidad del item",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            return@Button
                                        }

                                        CoroutineScope(Dispatchers.Main).launch {
                                            try {
                                                val ubicacionValida: String =
                                                    listaItems[index].ubicacion?.takeIf { it.isNotEmpty() }
                                                        ?: ""

                                                val requestRegistroReconteo =
                                                    RegistraReconteoRequest(
                                                        Id = "1",
                                                        Empresa = "MAKITA",
                                                        Agno = gyear.toString(),
                                                        Mes = gmonth.toString(),
                                                        FechaInventario = gFechaInventario,
                                                        TipoInventario = "RECONTEO",
                                                        NumeroReconteo = listaItems[index].numeroreconteo,
                                                        NumeroLocal = gLocal,
                                                        GrupoBodega = gGrupoBodega,
                                                        Clasif1 = listaItems[index].tipoitem,
                                                        Ubicacion = ubicacionValida,
                                                        Item = listaItems[index].item,
                                                        Cantidad = listaItems[index].cantidad,
                                                        Estado = "Ingresado",
                                                        Usuario = gUsuario,
                                                        NombreDispositivo = gnombreDispositivo
                                                    )

                                                val response = withContext(Dispatchers.IO) {
                                                    apiService.insertarReconteo(
                                                        requestRegistroReconteo
                                                    )
                                                }

                                                if (response.isSuccessful) {

                                                    grabacionExitosa = true
                                                    guardarRespaldoReconteo(
                                                        context,
                                                        requestRegistroReconteo,
                                                        gFechaInventario
                                                    )
                                                    itemGrabado = listaItems[index].item
                                                    listaItems.removeAt(index)
                                                    cantidades.remove(index)

                                                    delay(800)

                                                } else {
                                                    val mensaje =
                                                        "Error al enviar ${listaItems[index].item}: ${
                                                            response.errorBody()?.string()
                                                        }"
                                                    Toast.makeText(
                                                        context,
                                                        mensaje,
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                    listaItems.removeAt(index)
                                                }
                                            } catch (e: Exception) {
                                                val mensaje = e.message
                                                    ?: "Error desconocido al enviar ${listaItems[index].item}"
                                                Toast.makeText(context, mensaje, Toast.LENGTH_SHORT)
                                                    .show()
                                            }
                                        }
                                    },
                                    enabled = botonGrabar,
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color(0xFF00909E),
                                        contentColor = Color.White
                                    ),
                                    modifier = Modifier
                                        .padding(start = 8.dp)
                                        .width(140.dp)
                                        .height(43.dp),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.Send,
                                        contentDescription = "Enviar"
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "Enviar",
                                        color = Color.White,
                                        fontSize = 14.sp,
                                        modifier = Modifier.padding(top = 8.dp)
                                    )
                                }


                            }
                        }

                    }


                    if (grabacionExitosa) {
                        LaunchedEffect(Unit) {
                            delay(2000)
                            grabacionExitosa = false
                        }
                        AlertDialog(
                            onDismissRequest = { grabacionExitosa = false },
                            confirmButton = {
                                TextButton(onClick = { grabacionExitosa = false }) {
                                    Text("Aceptar")
                                }
                            },
                            title = { Text("Grabado Conteo Normal") },
                            text = { Text("Item $itemGrabado grabado de forma correcta.") }
                        )
                    }

                    Column(
                        modifier = Modifier.fillMaxSize(),  // Ajusta la columna a todo el espacio disponible
                        verticalArrangement = Arrangement.Bottom // Mueve los elementos al final
                    )
                    {


                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp),  // Margen alrededor de la fila
                            horizontalArrangement = Arrangement.SpaceBetween // Espacia los botones entre sí
                        ) {


                            Button(
                                onClick = {
                                    // respuesta55 = emptyList() /// limpia todo
                                    cantidadMap = mutableMapOf()
                                    respuesta55.forEachIndexed { index, _ ->
                                        cantidades[index] = ""
                                    }
                                },

                                enabled = botonLimpiar,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF00909E),
                                    contentColor = Color.White
                                ),
                                modifier = Modifier
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                                    .width(140.dp)
                                    .height(43.dp),
                                shape = RoundedCornerShape(8.dp),
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Delete,
                                    contentDescription = "Limpiar",
                                    tint = Color.White
                                )
                                Text(
                                    text = "Limpiar",
                                    color = Color.White,
                                    fontSize = 16.sp,
                                    modifier = Modifier.padding(top = 4.dp)
                                )


                            }


                            Button(
                                onClick = {
                                    navController.navigate("sexta_screen/$param/$param2/$param3/$param4/$param5")

                                },

                                enabled = botonVer,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF00909E),
                                    contentColor = Color.White
                                ),
                                modifier = Modifier
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                                    .width(140.dp)
                                    .height(43.dp),
                                shape = RoundedCornerShape(8.dp),
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ReceiptLong,
                                    contentDescription = "Ver lo ingresado",
                                    tint = Color.White
                                )
                                Text(
                                    text = "Ver",
                                    color = Color.White,
                                    fontSize = 16.sp,
                                    modifier = Modifier.padding(top = 4.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun QuintaScreen(
    navController: NavController,
    param: String,
    param2: String,
    param3: String,
    param4: String,
    param5: String,
) {
    var gTipoItem by remember { mutableStateOf("") }
    var gLocal by remember { mutableStateOf("") }
    var gUsuario by remember { mutableStateOf("") }
    var gFechaInventario by remember { mutableStateOf("") }
    var gFechaInventario2 by remember { mutableStateOf("") }
    var gGrupoBodega by remember { mutableStateOf("") }
    var respuesta55 by remember { mutableStateOf<List<ItemsReconteoResponse>>(emptyList()) }
    var errorState by remember { mutableStateOf<String?>(null) }
    val listaItems = remember { mutableStateListOf<ItemConCantidad>() }
    var botonVolver by remember { mutableStateOf(true) }
    var botonLimpiar by remember { mutableStateOf(true) }
    var botonGrabar by remember { mutableStateOf(true) }
    var swCargando by remember { mutableStateOf(true) }
    var selectedItemTexto by remember { mutableStateOf("") }
    var showUbicacionDialog by remember { mutableStateOf(false) }
    var ubicacionIngresada by remember { mutableStateOf("") }
    var indexSeleccionado by remember { mutableStateOf(-1) }
    var showErrorDialog by remember { mutableStateOf(false) }

    val focusRequester = remember { FocusRequester() }
    var mostrarAdvertencia by remember { mutableStateOf(false) }
    var showErrorDialog5 by remember { mutableStateOf(false) }
    var grabacionExitosa by remember { mutableStateOf(false) }
    var itemGrabado by remember { mutableStateOf("") }
    var botonVer by remember { mutableStateOf(true) }
    var totalitem by remember { mutableStateOf(0) }

    Surface(
        modifier = Modifier.fillMaxSize(),
    ) {

        gTipoItem = param ?: gTipoItem
        gLocal = param2 ?: gLocal
        gUsuario = param3 ?: gUsuario
        gFechaInventario2 = param4 ?: gFechaInventario
        gGrupoBodega = param5 ?: gGrupoBodega


        gFechaInventario = URLDecoder.decode(gFechaInventario2, StandardCharsets.UTF_8.toString())


        val gyear = ObtenerAno(gFechaInventario)
        val gmonth = ObtenerMes(gFechaInventario)

        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            val context = LocalContext.current
            val gnombreDispositivo = remember { obtenerNombreDelDispositivo(context) }
            val subtitulo = "$gLocal Dispositivo$gnombreDispositivo BATERIAS "

            TituloReconteoBAT()
            Separar()
            Titulo3(
                param = gTipoItem,
                param2 = gLocal,
                param3 = gUsuario,
                param4 = gFechaInventario,
                param5 = gnombreDispositivo
            )
            Separar()

            LaunchedEffect(gFechaInventario, gLocal, gTipoItem, gUsuario, gGrupoBodega) {

                try {

                    val resultado = apiService.obtenerReconteo99(
                        "MAKITA",
                        gyear.toString(),
                        gmonth.toString(),
                        gFechaInventario,
                        "RECONTEO",
                        gLocal,
                        gTipoItem,
                        gUsuario,
                        gGrupoBodega
                    )

                    Log.d("*MAKITA*111*", "PASA por obtenerReconteo99")

                    respuesta55 = resultado
                    swCargando = false
                    listaItems.clear()

                    listaItems.addAll(resultado.map { item ->
                        ItemConCantidad(
                            tipoitem = item.Clasif1 ?: "Sin TipoItem",
                            numeroreconteo = item.NumeroReconteo ?: "Sin Reconteo",
                            ubicacion = item.Ubicacion ?: "Sin Ubicacion",
                            item = item.Item ?: "Sin Item",
                            cantidad = ""
                        )
                    })

                    botonVolver = true
                    botonLimpiar = true
                    botonGrabar = true


                } catch (e: Exception) {
                    errorState = "Error al obtener Reconteos"

                    botonVolver = true
                    botonLimpiar = false
                    botonGrabar = false
                    swCargando = false

                    showErrorDialog5 = true
                }
            }


            if (showErrorDialog5) {
                mostrarDialogo(
                    titulo = "Informacion",
                    mensaje = "No tiene conteos asignados verifique con Supervisor o revise la fecha de inventario. Usuario:  ${gUsuario} fecha:${gFechaInventario}",
                    onDismiss = { showErrorDialog5 = false }
                )
            }


            val headers = listOf("Nro", "Item", "Ubicacion", "Cantidad")
            val fields = listOf<(ItemConCantidad) -> String>(
                { it.tipoitem },
                { it.numeroreconteo },
                { it.item },
                { it.ubicacion }
            )

            //aqui el box

            Box(
                modifier = Modifier
                    .horizontalScroll(rememberScrollState())
                    .height(900.dp)
            ) {
                Column {
                    // Crear las cabeceras en una fila fija


                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(15.dp)
                    ) {
                        headers.forEach { header ->
                            Text(
                                text = header,
                                modifier = Modifier
                                    .padding(horizontal = 8.dp, vertical = 3.dp),
                                fontWeight = FontWeight.Bold,
                                color = Color.Blue,
                                fontSize = 18.sp,
                                maxLines = 1,
                                textAlign = TextAlign.Start
                            )
                        }
                    }


                    val cantidades = remember { mutableStateMapOf<Int, String>() }
                    val enviados = remember { mutableStateMapOf<Int, Boolean>() }

                    totalitem = listaItems.size

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxHeight(0.8f)
                            .width(600.dp)
                            .padding(top = 3.dp)
                    ) {

                        stickyHeader {
                            Text(
                                text = "Total ítems por contar: ${listaItems.size}",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF00909E),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Color.LightGray)
                                    .padding(8.dp)
                            )
                        }


                        itemsIndexed(listaItems) { index, item ->
                            val rowColor = if (index % 2 == 0) Color(0xFFF1F1F1) else Color.White
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(rowColor)
                                    .clickable {

                                        Log.d(
                                            "*MAKITA*111*",
                                            "pasa clickable  1 quinta para ${item.item} ${index} "
                                        )

                                        if (item.ubicacion == "SUBICA") {
                                            indexSeleccionado = index
                                            showUbicacionDialog = true
                                            // Toast.makeText(context, "Permitido para los sin ubicacion", Toast.LENGTH_SHORT).show()
                                            // showItemDialog = true
                                        } else {

                                            selectedItemTexto = """ ítem: ${item.item} 
                                                         //       Codigo: ${item.tipoitem}
                                                         //       Ubicación: ${item.ubicacion} """.trimIndent()
                                            // showItemDialog = true
                                            // Toast.makeText(context, selectedItemTexto, Toast.LENGTH_SHORT).show()

                                        }
                                    }

                                    .padding(vertical = 3.dp)
                            )
                            {

                                /*INDICE*/
                                Text(
                                    text = (index + 1).toString(),
                                    color = Color.DarkGray,
                                    modifier = Modifier
                                        .width(40.dp)
                                        .padding(horizontal = 3.dp, vertical = 5.dp),
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.Bold
                                )

                                // Mostrar los campos de la respuesta (TipoItem, Item, Ubicacion)
                                fields.forEachIndexed { fieldIndex, field ->

                                    if (fieldIndex > 1) {

                                        val textColor = if (fieldIndex == 3) Color.Blue
                                        else Color.Black

                                        Text(
                                            text = field(item),
                                            color = textColor,
                                            modifier = Modifier
                                                .width(120.dp)
                                                .padding(horizontal = 2.dp)
                                                .padding(vertical = 5.dp)
                                                .clickable {
                                                    if (fieldIndex == 2) {
                                                        selectedItemTexto =
                                                            item.item  // o cualquier campo que quieras mostrar
                                                        Log.d(
                                                            "*MAKITA*111*",
                                                            "pasa clickable  2 quinta para ${item.item} ${index} "
                                                        )
                                                        if (item.ubicacion == "SUBICA") {
                                                            indexSeleccionado = index
                                                            showUbicacionDialog = true
                                                            Log.d(
                                                                "*MAKITA*111*",
                                                                "pasa clickable 1 quinta para ${indexSeleccionado} "
                                                            )

                                                        }
                                                    }
                                                },
                                            fontSize = 17.sp,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )

                                    }
                                }

                                val cantidad = cantidades[index] ?: ""

                                TextField(
                                    value = cantidad,
                                    onValueChange = { newValue ->

                                        val anteriorIndex = index - 1

                                        if (newValue.all { it.isDigit() } && newValue.length <= 4) {
                                            // Si es el primer ítem o el ítem anterior ya fue enviado (ya no está en la lista)
                                            val permitirIngreso = if (index == 0) {
                                                true
                                            } else {
                                                val anteriorItem =
                                                    listaItems.getOrNull(anteriorIndex)
                                                anteriorItem == null || !anteriorItem.cantidad.isNullOrBlank()
                                            }

                                            if (permitirIngreso) {
                                                cantidades[index] = newValue
                                                listaItems[index] =
                                                    listaItems[index].copy(cantidad = newValue)
                                                Log.d(
                                                    "*MAKITA*",
                                                    "Cantidad actualizada para ${listaItems[index].item}: $newValue"
                                                )
                                            } else {
                                                Toast.makeText(
                                                    context,
                                                    "Debe enviar por el orden definido ",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                                cantidades[index] = ""
                                                listaItems[index] =
                                                    listaItems[index].copy(cantidad = "")
                                            }
                                        }

                                        if (newValue.all { it.isDigit() } && newValue.length <= 4) {
                                            cantidades[index] = newValue
                                            listaItems[index] =
                                                listaItems[index].copy(cantidad = newValue)

                                            Log.d(
                                                "*MAKITA*111*",
                                                "Cantidad actualizada para ${listaItems[index].item}: ${listaItems[index].cantidad}"
                                            )

                                        }
                                    },
                                    placeholder = { Text("Cantidad") },
                                    keyboardOptions = KeyboardOptions.Default.copy(
                                        keyboardType = KeyboardType.Number,
                                        imeAction = ImeAction.Done
                                    ),
                                    modifier = Modifier
                                        .width(84.dp)
                                        .height(50.dp)
                                        /*  .border(2.dp, Color.Black, shape = RoundedCornerShape(4.dp))*/
                                        .padding(horizontal = 2.dp),
                                    textStyle = TextStyle(
                                        fontSize = 20.sp,
                                        color = Color.Red,
                                        fontFamily = FontFamily.Serif,
                                        fontWeight = FontWeight.Bold
                                    ),
                                    singleLine = true
                                )

                                Spacer(modifier = Modifier.width(8.dp))

                                Button(
                                    onClick =
                                    {
                                        val cantidadActual = item.cantidad
                                        if (cantidadActual.isNullOrEmpty()) {
                                            Toast.makeText(
                                                context,
                                                "Debe ingresar una cantidad",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            return@Button
                                        }

                                        CoroutineScope(Dispatchers.Main).launch {

                                            val cantidadInt = item.cantidad.toIntOrNull()

                                            if (cantidadInt != null && cantidadInt >= 0) {

                                                try {

                                                    //val ubicacionValida: String? = if (item.ubicacion.isNullOrEmpty()) null else item.ubicacion
                                                    val ubicacionValida: String =
                                                        item.ubicacion?.takeIf { it.isNotEmpty() }
                                                            ?: ""


                                                    val requestRegistroInventario =
                                                        RegistraInventarioRequest(
                                                            Id = "1",
                                                            Empresa = "MAKITA",
                                                            FechaInventario = gFechaInventario,
                                                            TipoInventario = "INVENTARIO",
                                                            Bodega = gLocal,
                                                            Clasif1 = item.tipoitem,
                                                            Ubicacion = ubicacionValida,
                                                            Item = item.item,
                                                            Cantidad = item.cantidad,
                                                            Estado = "Ingresado",
                                                            Usuario = gUsuario,
                                                            NombreDispositivo = gnombreDispositivo
                                                        )

                                                    Log.d(
                                                        "*MAKITA*111*",
                                                        "Datos enviados en requestRegistroInventario: $requestRegistroInventario"
                                                    )

                                                    val bitacoraRegistroUbi =
                                                        apiService.insertarinventario(
                                                            requestRegistroInventario
                                                        )

                                                    if (bitacoraRegistroUbi.isSuccessful) {

                                                        enviados[index] = true

                                                        val requestRegistroReconteo =
                                                            RegistraReconteoRequest(
                                                                Id = "1",
                                                                Empresa = "MAKITA",
                                                                Agno = gyear.toString(),
                                                                Mes = gmonth.toString(),
                                                                FechaInventario = gFechaInventario,
                                                                TipoInventario = "RECONTEO",
                                                                NumeroReconteo = item.numeroreconteo,
                                                                NumeroLocal = gLocal,
                                                                GrupoBodega = gGrupoBodega,
                                                                Clasif1 = item.tipoitem,
                                                                Ubicacion = ubicacionValida,
                                                                Item = item.item,
                                                                Cantidad = item.cantidad,
                                                                Estado = "Ingresado",
                                                                Usuario = gUsuario,
                                                                NombreDispositivo = gnombreDispositivo
                                                            )

                                                        try {
                                                            val respuestaPreconteo =
                                                                withContext(Dispatchers.IO) {
                                                                    apiService.updateReconteo99(
                                                                        requestRegistroReconteo
                                                                    )
                                                                }
                                                            Log.d(
                                                                "*MAKITA*111*",
                                                                "Cantidad XXXXXactualizada ${respuestaPreconteo.exito}"
                                                            )

                                                            Log.d(
                                                                "*MAKITA*111*",
                                                                "Respuesta del servidor: ${respuestaPreconteo.mensaje}"
                                                            )



                                                            if (respuestaPreconteo.exito) {
                                                                botonVer = true
                                                                Log.d(
                                                                    "*MAKITA*111*",
                                                                    "Cantidad actualizada correctamente"
                                                                )
                                                            } else {
                                                                Log.d(
                                                                    "*MAKITA*111*",
                                                                    "No se actualizó la cantidad"
                                                                )
                                                            }

                                                        } catch (e: Exception) {
                                                            Log.e(
                                                                "*MAKITA*111*",
                                                                "Error al obtener la ubicación",
                                                                e
                                                            )

                                                        }


                                                        guardarRespaldo(
                                                            context,
                                                            requestRegistroInventario,
                                                            gFechaInventario
                                                        )
                                                        grabacionExitosa = true
                                                        val mensajee2 =
                                                            "Item ${item.item} grabado exitosamente(1)."


                                                        itemGrabado = item.item
                                                        listaItems.removeAt(index)

                                                        cantidades.remove(index)

                                                    } else {
                                                        Log.e(
                                                            "MAKITA*ACA*",
                                                            "No Correcto is sucess"
                                                        )
                                                        grabacionExitosa = false
                                                        val errorCode =
                                                            bitacoraRegistroUbi.code()  // Código de error HTTP
                                                        val errorBody =
                                                            bitacoraRegistroUbi.errorBody()
                                                                ?.string()  // Cuerpo del error (si existe)


                                                        var mensajen =
                                                            "Error al grabar el item ${item.item}: ${
                                                                bitacoraRegistroUbi.errorBody()
                                                                    ?.string()
                                                            }"
                                                        // mostrarDialogo(context, "Error", mensajen)
                                                        Toast.makeText(
                                                            context,
                                                            mensajen,
                                                            Toast.LENGTH_SHORT
                                                        ).show()

                                                        Log.e(
                                                            "*MAKITA*ACA*4*",
                                                            "Error al grabar el item ${item.item}: ${
                                                                bitacoraRegistroUbi.errorBody()
                                                                    ?.string()
                                                            }"
                                                        )
                                                    }

                                                } catch (e: Exception) {

                                                    grabacionExitosa = false
                                                    val errorMessage: String = e.message?.toString()
                                                        ?: "Descripción no disponible"

                                                    val errorState: String =
                                                        if (errorMessage.contains("500") || errorMessage.contains(
                                                                "200"
                                                            )
                                                        ) {
                                                            "No se encontraron datos para el item proporcionado"
                                                        } else {
                                                            "Error al grabar: $errorMessage"
                                                        }

                                                    //mostrarDialogo(context, "Error", errorState)
                                                    Toast.makeText(
                                                        context,
                                                        errorState,
                                                        Toast.LENGTH_SHORT
                                                    ).show()

                                                }
                                            }


                                        }

                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color(0xFF00909E),
                                        contentColor = Color.White
                                    ),
                                    modifier = Modifier
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                        .width(140.dp)
                                        .height(43.dp),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.Send,
                                        contentDescription = "Enviar"
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "Enviar",
                                        color = Color.White,
                                        fontSize = 16.sp,
                                        modifier = Modifier.padding(top = 4.dp)

                                    )
                                }

                            }


                        }
                    }
                    /*paso*/
                    if (grabacionExitosa) {
                        LaunchedEffect(Unit) {
                            delay(2000)
                            grabacionExitosa = false
                        }
                        AlertDialog(
                            onDismissRequest = { grabacionExitosa = false },
                            confirmButton = {
                                TextButton(onClick = { grabacionExitosa = false }) {
                                    Text("Aceptar")
                                }
                            },
                            title = { Text("Grabado Conteo Bateria") },
                            text = { Text("Item $itemGrabado grabado de forma correcta.") }
                        )
                    }


                    if (showUbicacionDialog) {

                        LaunchedEffect(Unit) {
                            focusRequester.requestFocus()
                        }

                        AlertDialog(
                            onDismissRequest = {
                                showUbicacionDialog = false
                                ubicacionIngresada = ""
                            },
                            title = { Text("Ingrese Ubicacion") },
                            text = {
                                Column {
                                    Text("Item sin ubicacion. INGRESE UNA UBICACION (menor a 10 caracteres):")
                                    Spacer(modifier = Modifier.height(8.dp))
                                    TextField(
                                        value = ubicacionIngresada,
                                        onValueChange = { nuevoTexto ->
                                            if (nuevoTexto.length <= 10) {
                                                ubicacionIngresada = nuevoTexto
                                                mostrarAdvertencia = false
                                            } else {
                                                mostrarAdvertencia = true
                                            }
                                        },
                                        placeholder = { Text("Ej: T01D05") },
                                        singleLine = true,
                                        modifier = Modifier.focusRequester(focusRequester)
                                    )
                                }
                            },
                            confirmButton = {
                                TextButton(onClick = {
                                    Log.d(
                                        "*MAKITA*111*",
                                        "entra lista graba indice ok ${indexSeleccionado} "
                                    )
                                    if (ubicacionIngresada.isNotBlank() && indexSeleccionado >= 0) {
                                        Log.d("*MAKITA*111*", "pasa ")
                                        Log.d("*MAKITA*111*", "fila ${indexSeleccionado}")

                                        listaItems[indexSeleccionado] =
                                            listaItems[indexSeleccionado].copy(
                                                ubicacion = ubicacionIngresada
                                            )
                                        Log.d(
                                            "*MAKITA*111*",
                                            "filaXX ${listaItems[indexSeleccionado].ubicacion}"
                                        )

                                        showUbicacionDialog = false
                                        ubicacionIngresada = ""
                                    }
                                }) {
                                    Text("Guardar")
                                }
                            },
                            dismissButton = {
                                TextButton(onClick = {
                                    showUbicacionDialog = false
                                    ubicacionIngresada = ""
                                }) {
                                    Text("Cancelar")
                                }
                            }
                        )
                        //AQUI

                        if (mostrarAdvertencia) {
                            mostrarDialogo(
                                titulo = "Error",
                                mensaje = "Largo de Ubicacion supera los 10 caracteres, etiqueta incorrecta",
                                onDismiss = { showErrorDialog = mostrarAdvertencia }
                            )
                        }

                    }

                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Bottom
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            //horizontalArrangement = Arrangement.SpaceEvenly // Espaciado uniforme entre boton
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {


                            Button(
                                onClick = {
                                    navController.navigate("septima_screen/$param/$param2/$param3/$param4/$param5/$totalitem")

                                },

                                enabled = botonVer,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF00909E),
                                    contentColor = Color.White
                                ),
                                modifier = Modifier
                                    .padding(horizontal = 7.dp, vertical = 4.dp)
                                    .width(100.dp)
                                    .height(43.dp),
                                shape = RoundedCornerShape(8.dp),
                                contentPadding = PaddingValues(horizontal = 6.dp, vertical = 0.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ReceiptLong,
                                    contentDescription = "Ver lo ingresado",
                                    tint = Color.White,
                                    modifier = Modifier.size(18.dp)
                                )
                                Text(
                                    text = "Ver",
                                    color = Color.White,
                                    fontSize = 14.sp,
                                    modifier = Modifier.padding(top = 4.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun SextaScreen(
    navController: NavController,
    param: String,
    param2: String,
    param3: String,
    param4: String,
    param5: String,
) {
    var gTipoItem by remember { mutableStateOf("") }
    var gLocal by remember { mutableStateOf("") }
    var gUsuario by remember { mutableStateOf("") }
    var gFechaInventario by remember { mutableStateOf("") }
    var gFechaInventario2 by remember { mutableStateOf("") }
    var gGrupoBodega by remember { mutableStateOf("") }
    var respuesta55 by remember { mutableStateOf<List<VerListadoResponse2>>(emptyList()) }
    var errorState by remember { mutableStateOf<String?>(null) }
    val listaItems = remember { mutableStateListOf<ItemConCantidad>() }
    var botonVolver by remember { mutableStateOf(true) }
    var botonLimpiar by remember { mutableStateOf(true) }
    var botonGrabar by remember { mutableStateOf(true) }
    var swCargando by remember { mutableStateOf(true) }
    var selectedItemTexto by remember { mutableStateOf("") }
    var showUbicacionDialog by remember { mutableStateOf(false) }
    var indexSeleccionado by remember { mutableStateOf(-1) }
    var showErrorDialog5 by remember { mutableStateOf(false) }


    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFFB2EBF2)
    ) {

        gTipoItem = param ?: gTipoItem
        gLocal = param2 ?: gLocal
        gUsuario = param3 ?: gUsuario
        gFechaInventario2 = param4 ?: gFechaInventario
        gGrupoBodega = param5 ?: gGrupoBodega


        gFechaInventario = URLDecoder.decode(gFechaInventario2, StandardCharsets.UTF_8.toString())


        val gyear = ObtenerAno(gFechaInventario)
        val gmonth = ObtenerMes(gFechaInventario)

        Log.d("*MAKITA*111*", "PASA OBTENER*sexta")
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            val context = LocalContext.current
            val gnombreDispositivo = remember { obtenerNombreDelDispositivo(context) }
            val subtitulo = "$gLocal Dispositivo$gnombreDispositivo  "

            TituloVerReconteo()
            Separar()
            Titulo3(
                param = gTipoItem,
                param2 = gLocal,
                param3 = gUsuario,
                param4 = gFechaInventario,
                param5 = gnombreDispositivo
            )
            Separar()

            LaunchedEffect(Unit) {

                try {

                    Log.d(
                        "*MAKITA*111*",
                        "tipo=${gTipoItem}, usuario=${gUsuario}, fecha=${gFechaInventario}, local=${gLocal}"
                    )

                    val resultado = apiService.VerLoCapturadoReconteo(
                        "RECONTEO", gTipoItem, gUsuario, gFechaInventario, gLocal
                    )

                    respuesta55 = resultado
                    swCargando = false
                    listaItems.clear()

                    listaItems.addAll(resultado.map { item ->
                        ItemConCantidad(
                            tipoitem = item.Clasif1 ?: "Sin TipoItem",
                            numeroreconteo = item.NumeroReconteo ?: "Sin Numero",
                            item = item.Item ?: "Sin Item",
                            ubicacion = item.Ubicacion ?: "Sin Ubicacion",
                            cantidad = item.Cantidad ?: "Sin Cantidad",
                        )
                    })
                    botonVolver = true
                    botonLimpiar = true
                    botonGrabar = true


                } catch (e: Exception) {
                    errorState = "Error al obtener Reconteos"

                    botonVolver = true
                    botonLimpiar = false
                    botonGrabar = false
                    swCargando = false

                    showErrorDialog5 = true

                }
            }


            if (showErrorDialog5) {
                mostrarDialogo(
                    titulo = "Informacion",
                    mensaje = "No hay reconteos capturados por el Usuario:  ${gUsuario}, revise la fecha de inventario:${gFechaInventario}",
                    onDismiss = { showErrorDialog5 = false }
                )
            }


            val headers = listOf("Reconteo", "Item", "Ubicacion", "Cantidad")
            val fields = listOf<(ItemConCantidad) -> String>(
                { it.tipoitem },
                { it.numeroreconteo },
                { it.item },
                { it.ubicacion },
                { it.cantidad }
            )

            //aqui el box

            Box(
                modifier = Modifier
                    .horizontalScroll(rememberScrollState())
                    .height(900.dp)
            ) {
                Column {

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(15.dp)
                    ) {
                        headers.forEach { header ->
                            Text(
                                text = header,
                                modifier = Modifier
                                    .padding(horizontal = 8.dp, vertical = 3.dp),
                                fontWeight = FontWeight.Bold,
                                color = Color.Blue,
                                fontSize = 18.sp,
                                maxLines = 1,
                                textAlign = TextAlign.Start
                            )
                        }
                    }

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxHeight(0.8f)
                            .width(600.dp)
                            .padding(top = 3.dp)
                    ) {

                        stickyHeader {
                            Text(
                                text = "Total ítems contadors: ${listaItems.size}",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF00909E),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Color.LightGray)
                                    .padding(8.dp)
                            )
                        }


                        itemsIndexed(listaItems) { index, item ->
                            val rowColor = if (index % 2 == 0) Color(0xFFF1F1F1) else Color.White
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(rowColor)
                                    .clickable {

                                        Log.d(
                                            "*MAKITA*111*",
                                            "pasa clickable  1 quinta para ${item.item} ${index} "
                                        )

                                        if (item.ubicacion == "SUBICA") {
                                            indexSeleccionado = index
                                            showUbicacionDialog = true
                                            // Toast.makeText(context, "Permitido para los sin ubicacion", Toast.LENGTH_SHORT).show()
                                            // showItemDialog = true
                                        } else {

                                            selectedItemTexto = """ ítem: ${item.item} 
                                                         //       Codigo: ${item.tipoitem}
                                                         //       Ubicación: ${item.ubicacion} """.trimIndent()
                                            // showItemDialog = true
                                            // Toast.makeText(context, selectedItemTexto, Toast.LENGTH_SHORT).show()

                                        }
                                    }

                                    .padding(vertical = 3.dp)
                            )
                            {

                                /*INDICE*/
                                Text(
                                    text = (index + 1).toString(),
                                    color = Color.DarkGray,
                                    modifier = Modifier
                                        .width(40.dp)
                                        .padding(horizontal = 3.dp, vertical = 5.dp),
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.Bold
                                )

                                // Mostrar los campos de la respuesta (TipoItem, Item, Ubicacion)
                                fields.forEachIndexed { fieldIndex, field ->
                                    if (fieldIndex > 1) {

                                        val textColor = if (fieldIndex == 3) Color.Blue
                                        else Color.Black

                                        Text(
                                            text = field(item),
                                            color = textColor,
                                            modifier = Modifier
                                                .width(120.dp)
                                                .padding(horizontal = 2.dp)
                                                .padding(vertical = 5.dp)
                                                .clickable {
                                                    if (fieldIndex == 2) {
                                                        selectedItemTexto =
                                                            item.item  // o cualquier campo que quieras mostrar
                                                        Log.d(
                                                            "*MAKITA*111*",
                                                            "pasa clickable  2 quinta para ${item.item} ${index} "
                                                        )
                                                        if (item.ubicacion == "SUBICA") {
                                                            indexSeleccionado = index
                                                            showUbicacionDialog = true
                                                            Log.d(
                                                                "*MAKITA*111*",
                                                                "pasa clickable 1 quinta para ${indexSeleccionado} "
                                                            )

                                                        }
                                                    }
                                                },
                                            fontSize = 17.sp,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )

                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun SeptimaScreen(
    navController: NavController,
    param: String,
    param2: String,
    param3: String,
    param4: String,
    param5: String,
    param6: Int,
) {
    var gTipoItem by remember { mutableStateOf("") }
    var gLocal by remember { mutableStateOf("") }
    var gUsuario by remember { mutableStateOf("") }
    var gFechaInventario by remember { mutableStateOf("") }
    var gFechaInventario2 by remember { mutableStateOf("") }
    var gGrupoBodega by remember { mutableStateOf("") }
    var gTotal by remember { mutableStateOf(0) }
    var respuesta55 by remember { mutableStateOf<List<VerListadoResponse3>>(emptyList()) }
    var errorState by remember { mutableStateOf<String?>(null) }
    val listaItems = remember { mutableStateListOf<ItemConCantidadHE>() }
    var botonVolver by remember { mutableStateOf(true) }
    var botonLimpiar by remember { mutableStateOf(true) }
    var botonGrabar by remember { mutableStateOf(true) }
    var swCargando by remember { mutableStateOf(true) }
    var selectedItemTexto by remember { mutableStateOf("") }
    var showUbicacionDialog by remember { mutableStateOf(false) }
    var indexSeleccionado by remember { mutableStateOf(-1) }
    var showErrorDialog5 by remember { mutableStateOf(false) }


    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFFFFF9C4)
    ) {

        gTipoItem = param ?: gTipoItem
        gLocal = param2 ?: gLocal
        gUsuario = param3 ?: gUsuario
        gFechaInventario2 = param4 ?: gFechaInventario
        gGrupoBodega = param5 ?: gGrupoBodega
        gTotal = param6


        gFechaInventario = URLDecoder.decode(gFechaInventario2, StandardCharsets.UTF_8.toString())

        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            val context = LocalContext.current
            val gnombreDispositivo = remember { obtenerNombreDelDispositivo(context) }
            val subtitulo = "$gLocal Dispositivo$gnombreDispositivo  "

            TituloVerconteo()
            Separar()
            Titulo3(
                param = gTipoItem,
                param2 = gLocal,
                param3 = gUsuario,
                param4 = gFechaInventario,
                param5 = gnombreDispositivo
            )
            Separar()

            LaunchedEffect(Unit) {

                try {


                    Log.d(
                        "*MAKITA*111*",
                        "tipo=${gTipoItem}, usuario=${gUsuario}, fecha=${gFechaInventario}, local=${gLocal}"
                    )

                    val resultado = apiService.VerLoCapturado(
                        "INVENTARIO", gTipoItem, gUsuario, gFechaInventario, gLocal
                    )

                    respuesta55 = resultado
                    swCargando = false
                    listaItems.clear()

                    listaItems.addAll(resultado.map { item ->
                        ItemConCantidadHE(
                            tipoitem = item.Clasif1 ?: "Sin TipoItem",
                            numeroreconteo = item.NumeroReconteo ?: "Sin Numero",
                            item = item.Item ?: "Sin Item",
                            ubicacion = item.Ubicacion ?: "Sin Ubicacion",
                            cantidad = item.Cantidad ?: "Sin Cantidad",
                            fechainventario = item.FechaInventario ?: "Sin Fecha",
                        )
                    })


                    botonVolver = true
                    botonLimpiar = true
                    botonGrabar = true


                } catch (e: Exception) {
                    errorState = "Error al obtener Inventario"

                    botonVolver = true
                    botonLimpiar = false
                    botonGrabar = false
                    swCargando = false

                    showErrorDialog5 = true


                    val mensaje3 =
                        "Verifique con Supervisor su actividad o fecha de inventario incorrecta. Usuario:  ${gUsuario} Inventario ${gFechaInventario} "

                }
            }


            if (showErrorDialog5) {
                mostrarDialogo(
                    titulo = "Informacion",
                    mensaje = "No hay conteos realizados por  ${gUsuario}, revise la fecha de inventario:${gFechaInventario}",
                    onDismiss = { showErrorDialog5 = false }
                )
            }


            val headers = listOf("Item", "Ubicacion", "Cantidad", "Fecha")


            val fields = listOf<(ItemConCantidadHE) -> String>(
                { it.tipoitem },
                { it.numeroreconteo },
                { it.item },
                { it.ubicacion },
                { it.cantidad },
                { it.fechainventario }

            )

            //aqui el box

            Box(
                modifier = Modifier
                    .horizontalScroll(rememberScrollState())
                    .height(900.dp)
            ) {
                Column {

                    val contados = listaItems.size

                    if (gTotal.toFloat() > 0) {

                        val porce =
                            (contados.toFloat() * 100f) / (gTotal.toFloat() + contados.toFloat())

                        Log.d("*MAKITA*111*", "pasa clickable  1 quinta para ${porce}  ")

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 16.dp),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            CircularGrafico(
                                porcentaje = porce,
                                titulo = "Avance Conteo"
                            )
                        }
                    }


                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(15.dp)
                    ) {
                        headers.forEach { header ->
                            Text(
                                text = header,
                                modifier = Modifier
                                    .padding(horizontal = 8.dp, vertical = 3.dp),
                                fontWeight = FontWeight.Bold,
                                color = Color.Blue,
                                fontSize = 18.sp,
                                maxLines = 1,
                                textAlign = TextAlign.Start
                            )
                        }
                    }


                    //val cantidades = remember { mutableStateMapOf<Int, String>() }
                    //val enviados   = remember { mutableStateMapOf<Int, Boolean>() }


                    LazyColumn(
                        modifier = Modifier
                            .fillMaxHeight(0.8f)
                            .width(600.dp)
                            .padding(top = 3.dp)
                    ) {

                        stickyHeader {
                            Text(
                                text = "Total ítems contadors: ${listaItems.size}",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF00909E),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Color.LightGray)
                                    .padding(8.dp)
                            )
                        }

                        itemsIndexed(listaItems) { index, item ->
                            val rowColor = if (index % 2 == 0) Color(0xFFF1F1F1) else Color.White
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(rowColor)
                                    .clickable {

                                        Log.d(
                                            "*MAKITA*111*",
                                            "pasa clickable  1 quinta para ${item.item} ${index} "
                                        )

                                        if (item.ubicacion == "SUBICA") {
                                            indexSeleccionado = index
                                            showUbicacionDialog = true
                                            // Toast.makeText(context, "Permitido para los sin ubicacion", Toast.LENGTH_SHORT).show()
                                            // showItemDialog = true
                                        } else {

                                            selectedItemTexto = """ ítem: ${item.item} 
                                                         //       Codigo: ${item.tipoitem}
                                                         //       Ubicación: ${item.ubicacion} """.trimIndent()


                                        }
                                    }

                                    .padding(vertical = 3.dp)
                            )
                            {

                                /*INDICE*/
                                Text(
                                    text = (index + 1).toString(),
                                    color = Color.DarkGray,
                                    modifier = Modifier
                                        .width(40.dp)
                                        .padding(horizontal = 3.dp, vertical = 5.dp),
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.Bold
                                )

                                // Mostrar los campos de la respuesta (TipoItem, Item, Ubicacion)
                                fields.forEachIndexed { fieldIndex, field ->
                                    if (fieldIndex > 1) {

                                        val textColor = if (fieldIndex == 3) Color.Blue
                                        else Color.Black

                                        Text(
                                            text = field(item),
                                            color = textColor,
                                            modifier = Modifier
                                                .width(120.dp)
                                                .padding(horizontal = 2.dp)
                                                .padding(vertical = 5.dp),
                                            fontSize = 17.sp,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )

                                    }
                                }


                            }


                        }


                    }


                }


            }


        }


    }


}


@Composable
fun CircularGrafico(
    porcentaje: Float, // de 0.0f a 1.0f
    modifier: Modifier = Modifier,
    color: Color = Color(0xFF2196F3),
    titulo: String = "Progreso",
) {

    val porcentajeNormalizado = porcentaje.coerceIn(0f, 100f) / 100f

    Text(
        text = titulo,
        fontSize = 16.sp,
        fontWeight = FontWeight.Medium,
        color = Color.DarkGray,
        modifier = Modifier.padding(bottom = 8.dp)
    )

    Box(
        modifier = modifier
            .size(100.dp)
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawArc(
                color = color.copy(alpha = 0.3f),
                startAngle = -90f,
                sweepAngle = 360f,
                useCenter = false,
                style = Stroke(20f, cap = StrokeCap.Round)
            )
            drawArc(
                color = color,
                startAngle = -90f,
                sweepAngle = 360f * porcentajeNormalizado,
                useCenter = false,
                style = Stroke(20f, cap = StrokeCap.Round)
            )
        }
        Text(
            text = "${String.format("%.2f%%", porcentaje)}",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
    }
}


@Composable
fun ObtenerAno(gFechaInventario: String): Int {
    return try {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        LocalDate.parse(gFechaInventario, formatter).year
    } catch (e: Exception) {
        0 // Devuelve 0 si hay error
    }
}

@Composable
fun ObtenerMes(gFechaInventario: String): String {
    return try {  // <- Aquí comienza el bloque try
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val mes = LocalDate.parse(gFechaInventario, formatter).month.value
        String.format("%02d", mes) // <- Aquí se devuelve el mes en formato "MM"
    } catch (e: DateTimeParseException) {  // <- Si hay un error, entra aquí
        "00"  // <- Se devuelve "00" en caso de error
    }
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
fun TituloReconteo() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
    ) {
        Text(
            text = "RECONTEO",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .align(Alignment.Center),
            color = Color(0xFF00909E)
        )
    }
}


@Composable
fun TituloReconteoBAT() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
    ) {
        Text(
            text = "CONTEO BATERIAS",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .align(Alignment.Center),
            color = Color(0xFF00909E)
        )
    }
}

@Composable
fun TituloVerReconteo() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
    ) {
        Text(
            text = "INFORME DE RECONTEOS POR USUARIO",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .align(Alignment.Center),
            color = Color(0xFF00909E)
        )
    }
}

@Composable
fun TituloVerconteo() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
    ) {
        Text(
            text = "ITEMS CAPTURADOS POR USUARIO",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .align(Alignment.Center),
            color = Color(0xFF00909E)
        )
    }
}


@Composable
fun Separar() {
    Divider(
        color = Color(0xFFFF7F50),
        thickness = 1.dp,
        modifier = Modifier
            .padding(vertical = 8.dp)
            .padding(8.dp),
    )
}


@Composable
fun Titulo2(param: String?, param2: String?) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 15.dp)
    ) {
        Text(
            text = "Item ${param ?: "No hay parámetro"}  Local: ${param2 ?: "Sin fecha"}",
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(5.dp)
                .align(Alignment.Center)
        )

    }
}


@Composable
fun Titulo3(param: String?, param2: String?, param3: String?, param4: String?, param5: String?) {

    Box(
        modifier = Modifier
            .padding(top = 4.dp)
    ) {
        Text(
            text = "Tipo ${param ?: "No hay parámetro"}  Local: ${param2 ?: "No hay parámetro"} ",
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(2.dp)
                .align(Alignment.Center)
        )

    }

    Box(
        modifier = Modifier
            .padding(top = 4.dp)
    ) {
        Text(
            text = "Usuario ${param3 ?: "No hay parámetro"}  Fecha: ${param4 ?: "No hay parámetro"} ",
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Red,
            modifier = Modifier
                .padding(2.dp)
                .align(Alignment.Center)
        )

    }

    Box(
        modifier = Modifier
            .padding(top = 4.dp)
    ) {
        Text(
            text = "Dispositivo ${param5 ?: "No hay parámetro"}   ",
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(5.dp)
                .align(Alignment.Center)
        )

    }
}

fun guardarRespaldo(
    context: Context,
    registro: RegistraInventarioRequest,
    fechaInventario: String,
) {
    // Ruta del archivo

    val archivo = File(context.filesDir, "inventario_${fechaInventario}.csv")

    Log.d(
        "*MAKITA*",
        "directorio $archivo"
    )

    Log.d(
        "*MAKITA*",
        "directorio de datos $context.filesDir"
    )

    // Verificar si el archivo existe, si no, escribir el encabezado
    if (!archivo.exists()) {
        val encabezado =
            "Id;Empresa;FechaInventario;TipoInventario;Bodega;Clasif1;Ubicacion;Item;Cantidad;Estado;Usuario;NombreDispositivo\n"
        archivo.writeText(encabezado) // Escribir encabezado en el archivo
    }

    // Construir contenido del archivo
    val contenido = """
        ${registro.Id};
        ${registro.Empresa};
        ${registro.FechaInventario};
        ${registro.TipoInventario};
        ${registro.Bodega};
        ${registro.Clasif1};
        ${registro.Ubicacion};
        ${registro.Item};
        ${registro.Cantidad};
        ${registro.Estado};
        ${registro.Usuario};
        ${registro.NombreDispositivo}
    """.trimIndent()

    Log.d(
        "*MAKITA*",
        "guardarRespaldo $contenido"
    )


    try {
        // Escribir los datos en el archivo, añadiendo una nueva línea
        archivo.appendText(contenido + "\n")
        Toast.makeText(context, "Datos guardados exitosamente en formato CSV", Toast.LENGTH_SHORT)
            .show()
    } catch (e: Exception) {
        // Manejar errores
        Toast.makeText(context, "Error al guardar los datos: ${e.message}", Toast.LENGTH_SHORT)
            .show()
        e.printStackTrace()
    }
}

fun guardarRespaldoReconteo(
    context: Context,
    registro: RegistraReconteoRequest,
    fechaInventario: String,
) {
    // Ruta del archivo

    val fechaSinCaracter = fechaInventario.replace("/", "")

    val archivo = File(
        context.filesDir,
        "reconteo_${fechaSinCaracter}.csv"
    )  // Concatenamos la fecha al nombre

    Log.e("*MAKITA*ACA*4", "Muestra dir item ${context.filesDir}")
    Log.e("*MAKITA*ACA*4", "Nombre archivo el item ${archivo}")

    // Verificar si el archivo existe, si no, escribir el encabezado
    if (!archivo.exists()) {
        val encabezado =
            "Id;Empresa;Agno;Mes;FechaInventario;NumeroReconteo;NumeroLocal;GrupoBodega;Clasif1;Ubicacion;Item;Cantidad;Estado;Usuario;NombreDispositivo\n"
        archivo.writeText(encabezado) // Escribir encabezado en el archivo
    }

    // Construir contenido del archivo
    val contenido =
        """${registro.Id};${registro.Empresa};${registro.Agno};${registro.Mes};${registro.FechaInventario};${registro.NumeroReconteo};${registro.NumeroLocal};${registro.GrupoBodega};${registro.Clasif1};${registro.Ubicacion};${registro.Item};${registro.Cantidad};${registro.Estado};${registro.Usuario};${registro.NombreDispositivo}""".trimIndent()


    try {
        // Escribir los datos en el archivo, añadiendo una nueva línea
        archivo.appendText(contenido + "\n")
        Toast.makeText(context, "Datos guardados exitosamente en formato CSV", Toast.LENGTH_SHORT)
            .show()
    } catch (e: Exception) {
        // Manejar errores
        Toast.makeText(context, "Error al guardar los datos: ${e.message}", Toast.LENGTH_SHORT)
            .show()
        // e.printStackTrace()
    }
}










