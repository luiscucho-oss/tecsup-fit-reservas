package com.cucho.tecsupfit.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.cucho.tecsupfit.navigation.BarraInferior

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RutinasScreen(navController: NavController) {

    val rutinas = listOf(
        "Fuerza tren superior",
        "Fuerza tren inferior",
        "Cardio moderado",
        "Movilidad y estiramiento"
    )

    Scaffold(
        topBar = { TopAppBar(title = { Text("Rutinas") }) },
        bottomBar = { BarraInferior(navController) }
    ) { espacioSeguro ->
        LazyColumn(
            modifier = Modifier.padding(espacioSeguro),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(rutinas) { rutina ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = rutina,
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = "3 series · 12 repeticiones",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}