package com.cucho.tecsupfit.data

import com.cucho.tecsupfit.model.Clase

val listaClases = listOf(
    Clase(
        id = 1,
        nombre = "Yoga funcional",
        hora = "7:00 am",
        sala = "Sala 2",
        duracion = "45 min",
        categoria = "Hoy",
        descripcion = "Sesion de movilidad y respiracion para empezar el dia.",
        cuposDisponibles = 5,
        cuposTotales = 15,
        horarios = listOf("7:00 am", "9:00 am", "6:00 pm")
    ),
    Clase(
        id = 2,
        nombre = "Cross Training",
        hora = "6:00 pm",
        sala = "Sala 1",
        duracion = "45 min",
        categoria = "Hoy",
        descripcion = "Entrenamiento funcional de alta intensidad. Cupos limitados.",
        cuposDisponibles = 8,
        cuposTotales = 12,
        horarios = listOf("6:00 pm", "7:30 pm")
    ),
    Clase(
        id = 3,
        nombre = "Spinning",
        hora = "7:30 pm",
        sala = "Sala 3",
        duracion = "50 min",
        categoria = "Hoy",
        descripcion = "Ciclismo indoor con musica y trabajo por intervalos.",
        cuposDisponibles = 3,
        cuposTotales = 20,
        horarios = listOf("7:30 pm", "8:30 pm")
    ),
    Clase(
        id = 4,
        nombre = "Pilates",
        hora = "10:00 am",
        sala = "Sala 2",
        duracion = "60 min",
        categoria = "Esta semana",
        descripcion = "Fortalecimiento del core y control postural.",
        cuposDisponibles = 10,
        cuposTotales = 15,
        horarios = listOf("10:00 am", "4:00 pm")
    )
)

fun buscarClasePorId(id: Int): Clase? {
    return listaClases.find { clase -> clase.id == id }
}