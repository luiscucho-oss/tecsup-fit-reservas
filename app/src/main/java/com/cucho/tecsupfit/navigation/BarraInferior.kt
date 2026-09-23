package com.cucho.tecsupfit.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EventAvailable
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun BarraInferior(navController: NavController) {

    val entradaActual by navController.currentBackStackEntryAsState()
    val rutaActual = entradaActual?.destination?.route

    NavigationBar {
        NavigationBarItem(
            selected = rutaActual == Screen.Inicio.route,
            onClick = { navegarAPestana(navController, Screen.Inicio.route) },
            icon = { Icon(Icons.Filled.Home, contentDescription = "Inicio") },
            label = { Text("Inicio") }
        )
        NavigationBarItem(
            selected = rutaActual == Screen.Reservas.route,
            onClick = { navegarAPestana(navController, Screen.Reservas.route) },
            icon = { Icon(Icons.Filled.EventAvailable, contentDescription = "Reservas") },
            label = { Text("Reservas") }
        )
        NavigationBarItem(
            selected = rutaActual == Screen.Rutinas.route,
            onClick = { navegarAPestana(navController, Screen.Rutinas.route) },
            icon = { Icon(Icons.Filled.FitnessCenter, contentDescription = "Rutinas") },
            label = { Text("Rutinas") }
        )
        NavigationBarItem(
            selected = rutaActual == Screen.Perfil.route,
            onClick = { navegarAPestana(navController, Screen.Perfil.route) },
            icon = { Icon(Icons.Filled.Person, contentDescription = "Perfil") },
            label = { Text("Perfil") }
        )
    }
}

private fun navegarAPestana(navController: NavController, ruta: String) {
    navController.navigate(ruta) {
        popUpTo(navController.graph.startDestinationId) { saveState = true }
        launchSingleTop = true
        restoreState = true
    }
}