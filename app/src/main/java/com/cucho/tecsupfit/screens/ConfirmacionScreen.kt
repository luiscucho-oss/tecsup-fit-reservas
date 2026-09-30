package com.cucho.tecsupfit.screens

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.cucho.tecsupfit.data.buscarClasePorId
import com.cucho.tecsupfit.navigation.Screen

@Composable
fun ConfirmacionScreen(
    navController: NavController,
    claseId: Int,
    horarioIndex: Int,
) {
    val clase = buscarClasePorId(claseId)

    // Si el indice no existe, se usa un texto de respaldo
    val horarioElegido = clase?.horarios?.getOrNull(horarioIndex) ?: "Horario no disponible"

    Scaffold { espacioSeguro ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(espacioSeguro)
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {

            // Círculo de éxito grande y destacado con degradado
            Box(
                modifier = Modifier
                    .size(108.dp)
                    .clip(CircleShape)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.primary,
                                MaterialTheme.colorScheme.primaryContainer,
                            ),
                        ),
                    ),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = Icons.Filled.CheckCircle,
                    contentDescription = "Éxito",
                    modifier = Modifier.size(64.dp),
                    tint = MaterialTheme.colorScheme.onPrimary,
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "¡Cupo reservado!",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
            )

            Text(
                text = "Tu clase ha sido confirmada con éxito",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )

            Spacer(modifier = Modifier.height(28.dp))

            // Tarjeta con el resumen detallado de la reserva
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                ),
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    Text(
                        text = "Detalles de la reserva",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                    )

                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

                    // Fila Clase
                    FilaDetalleConfirmacion(
                        icono = Icons.Default.FitnessCenter,
                        etiqueta = "Clase",
                        valor = clase?.nombre ?: "Clase",
                    )

                    // Fila Horario
                    FilaDetalleConfirmacion(
                        icono = Icons.Default.Schedule,
                        etiqueta = "Horario",
                        valor = horarioElegido,
                    )

                    // Fila Sala
                    FilaDetalleConfirmacion(
                        icono = Icons.Default.Place,
                        etiqueta = "Lugar / Sala",
                        valor = clase?.sala ?: "Por asignar",
                    )

                    // Fila Estado
                    FilaDetalleConfirmacion(
                        icono = Icons.Default.Verified,
                        etiqueta = "Estado",
                        valor = "Confirmada",
                    )
                }
            }

            Spacer(modifier = Modifier.height(36.dp))

            // Botón principal: Ver mis reservas
            Button(
                onClick = {
                    navController.navigate(Screen.Reservas.route) {
                        popUpTo(Screen.Inicio.route)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(16.dp),
            ) {
                Text(
                    text = "Ver mis reservas",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Botón secundario: Volver al inicio
            OutlinedButton(
                onClick = {
                    navController.navigate(Screen.Inicio.route) {
                        popUpTo(Screen.Inicio.route) { inclusive = true }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(16.dp),
            ) {
                Text(
                    text = "Volver al inicio",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                )
            }
        }
    }
}

@Composable
fun FilaDetalleConfirmacion(
    icono: ImageVector,
    etiqueta: String,
    valor: String,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = icono,
                contentDescription = null,
                modifier = Modifier.size(20.dp),
                tint = MaterialTheme.colorScheme.onPrimaryContainer,
            )
        }

        Spacer(modifier = Modifier.width(14.dp))

        Column {
            Text(
                text = etiqueta,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Text(
                text = valor,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
            )
        }
    }
}
