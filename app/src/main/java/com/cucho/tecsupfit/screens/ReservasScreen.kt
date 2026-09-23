package com.cucho.tecsupfit.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.cucho.tecsupfit.model.Reserva
import com.cucho.tecsupfit.navigation.BarraInferior

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReservasScreen(navController: NavController, reservas: List<Reserva>) {

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Mis reservas") })
        },
        bottomBar = { BarraInferior(navController) }
    ) { espacioSeguro ->

        if (reservas.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(espacioSeguro)
                    .padding(24.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Todavia no tienes reservas",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Reserva una clase desde la pantalla de inicio",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.padding(espacioSeguro),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(reservas) { reserva ->
                    TarjetaReserva(reserva)
                }
            }
        }
    }
}

@Composable
fun TarjetaReserva(reserva: Reserva) {

    val esConfirmada = reserva.estado == "Confirmada"

    val colorFondoEstado = if (esConfirmada) {
        MaterialTheme.colorScheme.primaryContainer
    } else {
        MaterialTheme.colorScheme.surfaceVariant
    }

    val colorTextoEstado = if (esConfirmada) {
        MaterialTheme.colorScheme.onPrimaryContainer
    } else {
        MaterialTheme.colorScheme.onSurfaceVariant
    }

    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {

            Text(
                text = reserva.nombreClase,
                style = MaterialTheme.typography.titleMedium
            )

            Text(
                text = reserva.horario + " · " + reserva.sala,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(8.dp))

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .background(colorFondoEstado)
                    .padding(horizontal = 12.dp, vertical = 4.dp)
            ) {
                Text(
                    text = reserva.estado,
                    style = MaterialTheme.typography.labelSmall,
                    color = colorTextoEstado
                )
            }
        }
    }
}