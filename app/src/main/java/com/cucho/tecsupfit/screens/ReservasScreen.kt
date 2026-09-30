package com.cucho.tecsupfit.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.EventAvailable
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.cucho.tecsupfit.model.Reserva
import com.cucho.tecsupfit.navigation.BarraInferior

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReservasScreen(navController: NavController, reservas: MutableList<Reserva>) {

    // Estado para saber qué reserva se desea cancelar (null cuando no hay diálogo visible)
    var reservaACancelar by remember { mutableStateOf<Reserva?>(null) }

    /*
     * EXPLICACIÓN: ¿Por qué el diálogo se muestra con un 'if' y no con una función de abrir?
     *
     * En Jetpack Compose (y en la arquitectura declarativa de UI), la interfaz se construye en
     * función del estado actual de la aplicación. No existen métodos imperativos como "dialog.open()"
     * o "dialog.show()" para controlar la visibilidad de componentes de pantalla.
     *
     * En su lugar:
     * 1. El estado 'reservaACancelar' determina declarativamente la presencia del diálogo.
     * 2. Cuando el usuario presiona "Cancelar reserva", 'reservaACancelar' pasa a contener la reserva seleccionada.
     * 3. La condición 'if (reservaACancelar != null)' se evalúa como verdadera durante la recomposición,
     *    lo que incluye el composable 'AlertDialog' en el árbol de la interfaz de usuario.
     * 4. Al confirmar, descartar o presionar "No, volver", simplemente restablecemos 'reservaACancelar = null'.
     *    Esto desencadena una recomposición donde el 'if' se evalúa como falso y Compose remueve
     *    el AlertDialog del árbol de UI automáticamente, cerrándolo.
     */
    if (reservaACancelar != null) {
        val reserva = reservaACancelar!!
        AlertDialog(
            onDismissRequest = { reservaACancelar = null },
            icon = {
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = "Advertencia de cancelación",
                    tint = MaterialTheme.colorScheme.error,
                )
            },
            title = {
                Text(text = "Cancelar reserva")
            },
            text = {
                Text(
                    text = "¿Estás seguro de que deseas cancelar tu reserva para la clase \"${reserva.nombreClase}\" en el horario ${reserva.horario}?",
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        reservas.remove(reserva)
                        reservaACancelar = null
                    },
                ) {
                    Text(
                        text = "Si, cancelar",
                        color = MaterialTheme.colorScheme.error,
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { reservaACancelar = null },
                ) {
                    Text("No, volver")
                }
            },
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Mis reservas",
                        fontWeight = FontWeight.Bold,
                    )
                },
            )
        },
        bottomBar = { BarraInferior(navController) },
    ) { espacioSeguro ->

        if (reservas.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(espacioSeguro)
                    .padding(32.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f)),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        imageVector = Icons.Default.CalendarToday,
                        contentDescription = null,
                        modifier = Modifier.size(48.dp),
                        tint = MaterialTheme.colorScheme.primary,
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "Todavia no tienes reservas",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Explora las clases disponibles desde la pantalla de inicio y agenda tu próximo entrenamiento.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(horizontal = 16.dp),
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.padding(espacioSeguro),
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp),
            ) {
                items(reservas) { reserva ->
                    TarjetaReserva(
                        reserva = reserva,
                    ) { reservaACancelar = reserva }
                }
            }
        }
    }
}

@Composable
fun TarjetaReserva(
    reserva: Reserva,
    onCancelar: () -> Unit,
) {
    val esConfirmada = reserva.estado == "Confirmada"

    // La píldora de "Confirmada" y la de "Completada" se ven claramente distintas
    val colorFondoEstado = if (esConfirmada) {
        MaterialTheme.colorScheme.primaryContainer
    } else {
        MaterialTheme.colorScheme.secondaryContainer
    }

    val colorTextoEstado = if (esConfirmada) {
        MaterialTheme.colorScheme.onPrimaryContainer
    } else {
        MaterialTheme.colorScheme.onSecondaryContainer
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                // Icono dentro de un círculo a la izquierda
                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .clip(CircleShape)
                        .background(
                            if (esConfirmada) MaterialTheme.colorScheme.primaryContainer
                            else MaterialTheme.colorScheme.surfaceVariant,
                        ),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        imageVector = Icons.Default.EventAvailable,
                        contentDescription = null,
                        modifier = Modifier.size(26.dp),
                        tint = if (esConfirmada) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = reserva.nombreClase,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.AccessTime,
                            contentDescription = null,
                            modifier = Modifier.size(14.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = reserva.horario,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }

                    Spacer(modifier = Modifier.height(2.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Place,
                            contentDescription = null,
                            modifier = Modifier.size(14.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = reserva.sala,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }

                // Píldora de estado distintiva
                Surface(
                    shape = RoundedCornerShape(50),
                    color = colorFondoEstado,
                ) {
                    Text(
                        text = reserva.estado,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = colorTextoEstado,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    )
                }
            }

            // Solo se muestra el botón de cancelación si el estado es "Confirmada"
            if (esConfirmada) {
                Spacer(modifier = Modifier.height(14.dp))
                OutlinedButton(
                    onClick = onCancelar,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.error,
                    ),
                ) {
                    Text(
                        text = "Cancelar reserva",
                        fontWeight = FontWeight.SemiBold,
                    )
                }
            }
        }
    }
}
