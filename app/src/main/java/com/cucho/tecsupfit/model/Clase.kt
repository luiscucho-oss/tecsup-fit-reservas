package com.cucho.tecsupfit.model

data class Clase(
    val id: Int,
    val nombre: String,
    val hora: String,
    val sala: String,
    val duracion: String,
    val categoria: String,
    val descripcion: String,
    val cuposDisponibles: Int,
    val cuposTotales: Int,
    val horarios: List<String>
)