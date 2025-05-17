package com.example.waygo.ui.view

import android.app.DatePickerDialog
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.waygo.BuildConfig
import com.example.waygo.domain.model.Hotel
import com.example.waygo.ui.viewmodel.BookViewModel
import com.example.waygo.ui.viewmodel.ReservationsAllViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

// ------------------------ Navigation Destinations ---------------------------
sealed class Screen(val route: String, val icon: ImageVector, val label: String) {
    object Book     : Screen("book", Icons.Default.Search, "Book")
    object MyRes    : Screen("my_reservations", Icons.Default.ListAlt, "My Reservations")
    object AllRes   : Screen("all_reservations", Icons.Default.AdminPanelSettings, "All Reservations")
    object Hotel    : Screen("hotel/{hotelId}/{groupId}/{start}/{end}", Icons.Default.Hotel, "Hotel") {
        fun create(hid: String, gid: String, s: String, e: String) = "hotel/$hid/$gid/$s/$e"
    }

}

val base = BuildConfig.HOTELS_API_URL.trimEnd('/')

@Composable
fun HomeHotel(rootNav: NavController) {

    /* tabs de la bottom-bar */
    val tabs = listOf(Screen.MyRes, Screen.AllRes, Screen.Book)



    /*  NavController exclusivo de los tabs  */
    val tabNav = rememberNavController()

    /* ---------- Scaffold con bottom-bar ---------- */
    Scaffold(
        bottomBar = {
            NavigationBar {
                val dest = tabNav.currentBackStackEntryAsState().value?.destination
                tabs.forEach { screen ->
                    NavigationBarItem(
                        selected = dest?.route == screen.route,
                        onClick = {
                            tabNav.navigate(screen.route) {
                                launchSingleTop = true
                                restoreState = true
                                popUpTo(tabNav.graph.startDestinationId) {
                                    saveState = true          // conserva scroll, etc.
                                }
                            }
                        },
                        icon  = { Icon(screen.icon, screen.label) },
                        label = { Text(screen.label) }
                    )
                }
            }
        }
    ) { padding ->

        /* ---------- Contenido que cambia por tab ---------- */
        NavHost(
            navController = tabNav,
            startDestination = Screen.AllRes.route,
            modifier = Modifier.padding(padding)
        ) {

            composable(Screen.AllRes.route) {
                val resVm: ReservationsAllViewModel = hiltViewModel()
                val groups by resVm.uiState.collectAsState()

                // carga/recarga cada vez que entramos en esta pestaña
                LaunchedEffect(Unit) { resVm.load() }

                AllReservationsScreen(groups)
            }

            composable(Screen.Book.route) {
                BookScreen(rootNav)        // usa el NavController raíz para ir a HotelDetail
            }

            composable(Screen.MyRes.route) {
                ReservationsScreen()
            }
        }
    }
}


// ----------------------------- Book Screen ----------------------------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookScreen(
    nav: NavController,
    vm: BookViewModel = hiltViewModel()
) {
    val ui by vm.uiState.collectAsState()

    Column(Modifier.padding(16.dp)) {

        /* ───── Selector de ciudad ───── */
        ExposedDropdownMenuBox(
            expanded = ui.cityMenu,
            onExpandedChange = { vm.toggleCityMenu() }
        ) {
            TextField(
                value = ui.city,
                onValueChange = {},
                readOnly = true,
                label = { Text("City") },
                leadingIcon = { Icon(Icons.Default.Place, null) },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )

            /* ⬇⬇  ¡todos los TODO() eliminados! ⬇⬇ */
            ExposedDropdownMenu(
                expanded = ui.cityMenu,
                onDismissRequest = { vm.toggleCityMenu() }
            ) {
                listOf("Barcelona", "Paris", "Londres").forEach { c ->
                    DropdownMenuItem(
                        text = { Text(c) },
                        onClick = { vm.selectCity(c) }
                    )
                }
            }
        }

        Spacer(Modifier.height(8.dp))
        DateField("Start", ui.startDate) { vm.pickStart(it) }
        Spacer(Modifier.height(8.dp))
        DateField("End", ui.endDate) { vm.pickEnd(it) }

        Spacer(Modifier.height(16.dp))
        Button(
            onClick = { vm.search() },
            modifier = Modifier.fillMaxWidth()
        ) { Text("Search") }

        Spacer(Modifier.height(16.dp))

        if (ui.loading) {
            CircularProgressIndicator()
        } else {
            HotelList(ui.hotels) { h ->
                nav.navigate(
                    Screen.Hotel.create(
                        h.id,
                        vm.groupId,
                        ui.startDate.toString(),
                        ui.endDate.toString()
                    )
                )
            }

            if (ui.message != null) {
                Text(
                    text = ui.message!!,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
        }
    }
}


@Composable
fun DateField(
    label: String,
    date: LocalDate?,
    onPick: (LocalDate) -> Unit
) {
    val context = LocalContext.current
    val formatter = DateTimeFormatter.ISO_DATE

    OutlinedTextField(
        value = date?.format(formatter) ?: "",
        onValueChange = {},
        readOnly = true,
        enabled = false,                     // ← evita que consuma el click
        label = { Text(label) },
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                val now = date ?: LocalDate.now()
                DatePickerDialog(
                    context,
                    { _, y, m, d ->
                        onPick(LocalDate.of(y, m + 1, d))   // meses 0-based
                    },
                    now.year, now.monthValue - 1, now.dayOfMonth
                ).show()
            }
    )
}


@Composable
fun HotelList(hotels: List<Hotel>, onClick: (Hotel) -> Unit) {

    LazyColumn {
        items(hotels) { h ->
            Card(modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp).clickable { onClick(h) }) {
                Log.d("home", h.id)
                val id = h.id
                Row(Modifier.height(120.dp)) {
                    Image(
                        painter = rememberAsyncImagePainter(base + ( h.imageUrl ?: "")),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.width(120.dp).fillMaxHeight()
                    )
                    Column(Modifier.padding(8.dp)) {
                        Text(h.name + " ($id)", fontWeight = FontWeight.Bold)
                        Text(h.address)
                        Spacer(Modifier.weight(1f))
                        Text("From ${h.rooms?.minOfOrNull { it.price } ?: "-"}€", fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }
    }


}


