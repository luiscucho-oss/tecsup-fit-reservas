package com.cucho.tecsupfit.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.cucho.tecsupfit.data.reservasIniciales
import com.cucho.tecsupfit.model.Reserva
import com.cucho.tecsupfit.screens.ConfirmacionScreen
import com.cucho.tecsupfit.screens.DetalleScreen
import com.cucho.tecsupfit.screens.InicioScreen
import com.cucho.tecsupfit.screens.PerfilScreen
import com.cucho.tecsupfit.screens.ReservasScreen
import com.cucho.tecsupfit.screens.RutinasScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    // Lista de reservas compartida por toda la app
    val reservas = remember {
        mutableStateListOf<Reserva>().also { lista ->
            lista.addAll(reservasIniciales)
        }
    }

    NavHost(
        navController = navController,
        startDestination = Screen.Inicio.route
    ) {
        composable(Screen.Inicio.route) {
            InicioScreen(navController)
        }
        composable(Screen.Reservas.route) {
            ReservasScreen(navController, reservas)
        }
        composable(Screen.Rutinas.route) {
            RutinasScreen(navController)
        }
        composable(Screen.Perfil.route) {
            PerfilScreen(navController)
        }
        composable(
            route = Screen.Detalle.route,
            arguments = listOf(
                navArgument("claseId") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val claseId = backStackEntry.arguments?.getInt("claseId") ?: 0
            DetalleScreen(navController, claseId)
        }
        composable(
            route = Screen.Confirmacion.route,
            arguments = listOf(
                navArgument("claseId") { type = NavType.IntType },
                navArgument("horarioIndex") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val claseId = backStackEntry.arguments?.getInt("claseId") ?: 0
            val horarioIndex = backStackEntry.arguments?.getInt("horarioIndex") ?: 0
            ConfirmacionScreen(navController, claseId, horarioIndex, reservas)
        }
    }
}