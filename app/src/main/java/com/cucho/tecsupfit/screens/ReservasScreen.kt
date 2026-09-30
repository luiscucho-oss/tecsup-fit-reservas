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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
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
            TopAppBar(title = { Text("Mis reservas") })
        },
        bottomBar = { BarraInferior(navController) },
    ) { espacioSeguro ->

        if (reservas.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(espacioSeguro)
                    .padding(24.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = "Todavia no tienes reservas",
                    style = MaterialTheme.typography.titleMedium,
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Reserva una clase desde la pantalla de inicio",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.padding(espacioSeguro),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
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
                style = MaterialTheme.typography.titleMedium,
            )

            Text(
                text = reserva.horario + " · " + reserva.sala,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )

            Spacer(modifier = Modifier.height(8.dp))

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .background(colorFondoEstado)
                    .padding(horizontal = 12.dp, vertical = 4.dp),
            ) {
                Text(
                    text = reserva.estado,
                    style = MaterialTheme.typography.labelSmall,
                    color = colorTextoEstado,
                )
            }

            // Solo se muestra el botón de cancelación si el estado es "Confirmada"
            if (esConfirmada) {
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedButton(
                    onClick = onCancelar,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.error,
                    ),
                ) {
                    Text("Cancelar reserva")
                }
            }
        }
    }
}
