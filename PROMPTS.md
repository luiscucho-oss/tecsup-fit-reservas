# PROMPTS.md — Fase 2: Mejora con IA

Rama: `mejora-ia`  
Asistente utilizado: **Gemini**, integrado en Android Studio  
Alumno: Luis Cucho

Los archivos se adjuntaron como contexto con `@`, por lo que no fue necesario
pegar el código dentro de los prompts.

---

## Prompt 1 — Mejora funcional: AlertDialog para cancelar una reserva

### Qué le pedí

```
@ReservasScreen.kt @AppNavigation.kt @Reserva.kt

Agrega una mejora funcional a mi app de reservas de gimnasio en Jetpack Compose con Material 3: quiero poder cancelar una reserva con un AlertDialog de confirmación.

Comportamiento esperado:
- Cada tarjeta de reserva con estado "Confirmada" muestra un botón "Cancelar reserva"
- Las reservas con estado "Completada" NO muestran ese botón
- Al tocar el botón aparece un AlertDialog con título, un icono de advertencia, y un mensaje que incluya el nombre de la clase y su horario
- El diálogo tiene dos botones: "Si, cancelar" y "No, volver"
- Si confirma, la reserva se elimina de la lista y el diálogo se cierra
- Si cancela o toca fuera del diálogo, no se elimina nada y el diálogo se cierra

Restricciones:
- No uses ViewModel ni MVVM. El estado se maneja con remember y mutableStateOf
- La lista de reservas ya existe como mutableStateListOf en AppNavigation
- No modifiques Screen.kt ni la navegación
- Usa solo colores de MaterialTheme.colorScheme, sin hexadecimales fijos
- No agregues librerías externas
- Agrega comentarios explicando por qué el diálogo se muestra con un if y no con una función de abrir

Devuélveme el archivo completo con sus imports.
```

### Qué tuve que corregir

Nada. El resultado cumplió todos los requisitos a la primera.

Atribuyo esto a que el prompt incluía las restricciones desde el inicio. En
particular, la restricción de manejar el estado con `remember` evitó que
propusiera un ViewModel, y detallar el comportamiento de los tres caminos de
cierre (confirmar, cancelar y tocar fuera) evitó el error típico de que el
diálogo quede abierto después de confirmar.

### Qué verifiqué antes de aceptarlo

- La firma cambió a `reservas: MutableList<Reserva>`, necesaria para poder usar `remove`
- El estado guarda **la reserva seleccionada** (`mutableStateOf<Reserva?>(null)`) y no solo un booleano, de modo que el diálogo sabe qué nombre mostrar y qué elemento eliminar
- `reservaACancelar = null` aparece en los tres caminos de cierre
- Ningún color hexadecimal fijo
- La navegación quedó intacta

---

## Prompt 2 — Rediseño visual del flujo principal

### Qué le pedí

```
@InicioScreen.kt @DetalleScreen.kt @ConfirmacionScreen.kt

Rediseña por completo la interfaz de estas tres pantallas de mi app de reservas de gimnasio. No quiero un ajuste de espaciados: quiero que parezca un producto terminado, al nivel de una app publicada en Play Store. El diseño actual es demasiado plano.

Identidad visual:
- Nombre de la app: TECSUP Fit
- Estilo: moderno y limpio, con cabeceras destacadas y tarjetas con profundidad
- Todos los colores deben salir de MaterialTheme.colorScheme, sin hexadecimales fijos
- Los degradados se hacen con Brush.verticalGradient combinando colores del colorScheme

Rediseña cada pantalla así:

InicioScreen
- Cabecera con fondo en degradado ocupando la parte superior, con esquinas inferiores redondeadas
- Dentro de la cabecera: saludo grande "Hola, Luis" y subtitulo "Reserva tu proxima clase"
- Los FilterChip de "Hoy" y "Esta semana" dentro de la cabecera o justo debajo, bien destacados
- Cada clase como una Card mas alta, con: un icono dentro de un circulo de color a la izquierda, el nombre en negrita, la hora y sala debajo, y una etiqueta pequena con los cupos disponibles

DetalleScreen
- Cabecera visual mas grande con degradado y el icono de la clase dentro de un circulo, centrado
- Debajo, el nombre de la clase y una fila de datos con iconos pequenos: duracion, sala y cupos
- La descripcion dentro de una Card
- La seleccion de horario con un titulo claro y los chips mas grandes y separados
- El boton "Reservar cupo" ancho completo, fijo en la parte inferior

ConfirmacionScreen
- Circulo de exito mas grande y destacado
- El resumen de la reserva dentro de una Card con filas de informacion: cada fila con un icono pequeno, una etiqueta en texto chico y gris, y el valor en negrita debajo
- Los dos botones al final, bien separados

Restricciones que NO puedes romper:
- No cambies los nombres de las funciones composables ni sus parametros
- Manten toda la navegacion tal como esta:
  - InicioScreen: navigate(Screen.Detalle.createRoute(clase.id))
  - DetalleScreen: reservas.add(...) antes de navigate(Screen.Confirmacion.createRoute(...)), y popBackStack() en la flecha
  - ConfirmacionScreen: los dos navigate con sus popUpTo
- Manten el boton "Reservar cupo" deshabilitado hasta elegir un horario (enabled = horarioSeleccionado >= 0)
- Manten Modifier.selectableGroup() en la fila de horarios
- No modifiques Screen.kt, AppNavigation.kt ni BarraInferior.kt
- Cada pantalla conserva su Scaffold y aplica el PaddingValues que entrega
- No agregues librerias externas, ViewModel ni dependencias nuevas

Devuelveme los tres archivos completos con sus imports.
```

### Qué tuve que corregir

**Eliminó la `TopAppBar` de `InicioScreen` y `DetalleScreen`** y la reemplazó por
la cabecera con degradado. No se lo había prohibido explícitamente.

Decidí **mantener ese cambio** porque la cabecera cumple la misma función que la
barra superior: identifica la pantalla y aloja la acción de volver. Es un patrón
habitual en apps con cabecera visual. Las tres pantallas de pestaña sí conservan
`TopAppBar`, y en las seis el `Scaffold` sigue aplicando su `PaddingValues`.

Lo dejo documentado porque fue una decisión consciente, no un descuido.

### Qué verifiqué antes de aceptarlo

- `enabled = horarioSeleccionado >= 0` sigue presente
- `Modifier.selectableGroup()` sigue presente
- `reservas.add(...)` se ejecuta **antes** del `navigate`, no después
- Los dos `popUpTo` de la confirmación quedaron intactos
- Ningún color hexadecimal fijo

### Qué aprendí de la respuesta

Cambió `mutableStateOf(-1)` por `mutableIntStateOf(-1)`, que es la versión
optimizada para enteros: guarda el valor primitivo en vez de envolverlo en un
objeto. Hace lo mismo con menos memoria.

---

## Prompt 3 — Rediseño visual de las pestañas

### Qué le pedí

```
@ReservasScreen.kt @RutinasScreen.kt @PerfilScreen.kt

Rediseña la interfaz de estas tres pantallas de mi app de reservas de gimnasio. Ya rediseñé el flujo principal (Inicio, Detalle y Confirmación) con cabeceras en degradado, tarjetas con esquinas de 20dp, iconos dentro de círculos de color y etiquetas tipo píldora. Quiero que estas tres queden en el mismo estilo.

Criterio visual:
- Todos los colores desde MaterialTheme.colorScheme, sin hexadecimales fijos
- Los degradados con Brush.verticalGradient
- Tarjetas con RoundedCornerShape de 20dp y elevación
- Iconos dentro de círculos con fondo de color

Rediseña cada pantalla así:

ReservasScreen
- Mantén la TopAppBar con el título "Mis reservas"
- Cada reserva como una Card más alta, con un icono dentro de un círculo a la izquierda, el nombre de la clase en negrita, el horario y la sala debajo con iconos pequeños, y la píldora de estado
- La píldora de "Confirmada" y la de "Completada" deben verse claramente distintas
- El estado vacío con un icono grande y texto centrado, más trabajado

RutinasScreen
- Mantén la TopAppBar con el título "Rutinas"
- Cada rutina como una Card con un icono dentro de un círculo, el nombre en negrita, las series y repeticiones debajo, y una etiqueta de nivel o duración

PerfilScreen
- Mantén la TopAppBar con el título "Mi perfil"
- Cabecera con degradado ocupando la parte superior, con esquinas inferiores redondeadas
- El avatar circular con las iniciales superpuesto sobre esa cabecera
- Nombre y plan debajo del avatar
- Las tarjetas de estadísticas con el número grande, la etiqueta debajo y un icono pequeño

Restricciones que NO puedes romper:
- No cambies los nombres de las funciones composables ni sus parámetros
- En ReservasScreen conserva EXACTAMENTE la funcionalidad del AlertDialog de cancelar reserva: el estado reservaACancelar, el botón solo en las reservas con estado "Confirmada", los tres puntos donde se pone en null (confirmButton, dismissButton y onDismissRequest), y el reservas.remove(reserva)
- Conserva la firma reservas: MutableList<Reserva>
- Las tres pantallas mantienen su Scaffold con bottomBar = { BarraInferior(navController) } y aplican el PaddingValues
- No modifiques Screen.kt, AppNavigation.kt ni BarraInferior.kt
- No agregues librerías externas, ViewModel ni dependencias nuevas

Devuélveme los tres archivos completos con sus imports.
```

### Qué tuve que corregir

**1. Declaró la `data class InfoRutina` dentro de `RutinasScreen.kt`**, en el
paquete `screens`, cuando el resto de los modelos (`Clase`, `Reserva`) viven en
el paquete `model`. La moví a `model/InfoRutina.kt` para mantener la coherencia
de la estructura.

**2. El avatar no quedó superpuesto sobre la cabecera.** Lo pedí explícitamente,
pero generó el degradado y el avatar como bloques consecutivos, sin
desplazamiento negativo, así que el avatar queda debajo y no montado sobre el
degradado.

### Qué verifiqué antes de aceptarlo

Lo primero que revisé fue que el `AlertDialog` del prompt 1 hubiera sobrevivido
sin cambios, porque era el riesgo más grande de este prompt. Se conservó
completo: el estado, los tres puntos de cierre, el `remove` y la condición de que
el botón solo aparezca en las reservas confirmadas.

---

## Conclusiones sobre el uso de IA

**Lo que más influyó en la calidad de las respuestas fueron las restricciones.**
Cuando el prompt listaba explícitamente qué no debía tocarse, no tocó nada. El
único requisito que se perdió, la `TopAppBar`, fue justamente el que no había
declarado como restricción.

**Pedir cambios en bloques pequeños funcionó mejor que pedir todo junto.** Separé
las seis pantallas en dos prompts, y cada respuesta fue manejable de revisar. Con
las seis de golpe habría sido más difícil detectar qué se rompió.

**Describir elementos visuales concretos da mejores resultados que pedir
criterios generales.** Frases como "icono dentro de un círculo a la izquierda" o
"cabecera con degradado y esquinas inferiores redondeadas" produjeron un cambio
real de diseño, mientras que pedir solo "mejor tipografía y espaciado" habría
devuelto el mismo diseño apenas retocado.