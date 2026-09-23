package com.cucho.tecsupfit.navigation

sealed class Screen(val route: String) {

    // Pestañas del bottomBar
    object Inicio : Screen("inicio")
    object Reservas : Screen("reservas")
    object Rutinas : Screen("rutinas")
    object Perfil : Screen("perfil")

    // Flujo secuencial con argumentos
    object Detalle : Screen("detalle/{claseId}") {
        fun createRoute(claseId: Int): String = "detalle/$claseId"
    }

    object Confirmacion : Screen("confirmacion/{claseId}/{horarioIndex}") {
        fun createRoute(claseId: Int, horarioIndex: Int): String =
            "confirmacion/$claseId/$horarioIndex"
    }
}