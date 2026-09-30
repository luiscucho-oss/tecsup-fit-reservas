# TECSUP Fit — Reserva de clases

Curso: Programación en Móviles — Tecsup  
Docente: Juan León S.  
Alumno: Luis Cucho

Opción B — Reserva de clases de gimnasio con navegación secundaria por bottomBar.

## Descripción

Aplicación Android para reservar clases de gimnasio. Integra layouts y controles,
listas eficientes, navegación secuencial con paso de parámetros y navegación
secundaria mediante barra inferior.

El estado se maneja únicamente con `remember`, `rememberSaveable`,
`mutableStateOf` y `mutableStateListOf`. **No se utiliza ViewModel ni MVVM.**

## Tecnologías

- Kotlin
- Jetpack Compose
- Material 3
- Navigation Compose 2.7.7
- Minimum SDK: 24

## Requisitos funcionales cumplidos

| # | Requisito del documento | Implementación |
|---|---|---|
| 1 | Inicio: LazyRow con chips de filtro (mínimo 2) y LazyColumn con lista de clases (mínimo 3), cada tarjeta con nombre y horario | `InicioScreen`: `LazyRow` con `FilterChip` para "Hoy" y "Esta semana"; `LazyColumn` con 3 clases en "Hoy"; `TarjetaClase` muestra nombre, hora y sala |
| 2 | Detalle de clase: recibe los datos de la clase elegida por parámetro de navegación; botón "Reservar cupo" | `DetalleScreen` recibe `claseId` como `NavType.IntType` y busca la clase con `buscarClasePorId`; botón "Reservar cupo" deshabilitado hasta elegir horario |
| 3 | Confirmación: resumen de la reserva (clase, horario); botón para ver reservas | `ConfirmacionScreen` muestra clase, horario y sala; botón "Ver mis reservas" con `popUpTo` |
| 4 | bottomBar visible en Inicio, Reservas y Perfil, con 4 pestañas; el ícono activo se resalta según la pantalla actual | `BarraInferior` incluida en el `Scaffold` de Inicio, Reservas, Rutinas y Perfil; el resaltado se calcula con `currentBackStackEntryAsState` |
| 5 | Reservas: LazyColumn con las clases reservadas, cada una con su estado diferenciado visualmente | `ReservasScreen` con `LazyColumn`; píldora de color: "Confirmada" en `primaryContainer`, "Completada" en `surfaceVariant` |
| 6 | Perfil: datos del usuario y estadísticas simples | Avatar con iniciales, nombre, plan y dos tarjetas de estadísticas: Clases y Rachas |

### Requisitos adicionales de la rúbrica

| Criterio | Implementación |
|---|---|
| Selección de opción única antes de confirmar | Chips de horario dentro de un `selectableGroup`, con el estado guardado en un solo `Int` |
| Scaffold correcto con padding aplicado en todas las pantallas | Cada pantalla tiene su propio `Scaffold` y aplica el `PaddingValues` que entrega |
| Navegación secuencial con paso de parámetros | Inicio → Detalle (`claseId`) → Confirmación (`claseId` + `horarioIndex`) |

## Estructura del proyecto

```
com.cucho.tecsupfit
├── model
│   ├── Clase.kt              Modelo de una clase de gimnasio
│   └── Reserva.kt            Modelo de una reserva
├── data
│   ├── DatosClases.kt        Lista de clases y busqueda por id
│   └── DatosReservas.kt      Reserva inicial de ejemplo
├── navigation
│   ├── Screen.kt             6 rutas definidas con sealed class
│   ├── AppNavigation.kt      NavHost y estado compartido de reservas
│   └── BarraInferior.kt      NavigationBar de 4 pestanas
├── screens
│   ├── InicioScreen.kt       LazyRow de filtros + LazyColumn de clases
│   ├── DetalleScreen.kt      Detalle y seleccion unica de horario
│   ├── ConfirmacionScreen.kt Resumen de la reserva realizada
│   ├── ReservasScreen.kt     Reservas con estado Confirmada o Completada
│   ├── RutinasScreen.kt      Listado de rutinas
│   └── PerfilScreen.kt       Datos del usuario y estadisticas
└── MainActivity.kt
```

## Navegación

### Flujo secuencial

Inicio → Detalle de clase → Confirmación → Mis reservas

- Inicio a Detalle: pasa `claseId` como argumento `Int`
- Detalle a Confirmación: pasa `claseId` y `horarioIndex`, ambos `Int`
- Confirmación usa `popUpTo` para que el botón atrás no regrese al detalle

### Navegación secundaria

Barra inferior con 4 destinos: Inicio, Reservas, Rutinas y Perfil.

La pestaña activa se determina leyendo la ruta actual con
`currentBackStackEntryAsState`, no con una variable propia, de modo que el
resaltado siempre coincide con la pantalla visible.

Cada cambio de pestaña usa `launchSingleTop`, `popUpTo` con `saveState` y
`restoreState` para evitar que el back stack crezca al alternar pestañas.

## Decisiones técnicas

**Por qué se pasa el índice del horario y no el texto**  
Un String como "6:00 pm" contiene espacios y dos puntos, que rompen la ruta de
navegación. Se pasa la posición como `Int` y el texto se recupera desde la lista
de horarios de la clase.

**Por qué la selección de horario funciona como RadioButton**  
El estado es un solo entero, `horarioSeleccionado`. Cada chip calcula su
`selected` comparándose con ese número, por lo que solo uno puede estar activo.
Se aplica `Modifier.selectableGroup()` para que la accesibilidad lo anuncie como
grupo de opción única.

**Por qué el Scaffold está en cada pantalla y no envolviendo al NavHost**  
Las pantallas del flujo secuencial (Detalle y Confirmación) no llevan barra
inferior, mientras que las cuatro pestañas sí. Con un Scaffold por pantalla, cada
una decide sus propias barras.

**Dónde vive el estado de las reservas**  
La lista se declara con `mutableStateListOf` dentro de `AppNavigation`, que es el
punto común más cercano entre la pantalla que agrega la reserva y la que la
muestra. Es `mutableStateListOf` y no `listOf` porque Compose debe recomponer al
agregar un elemento.

## Capturas

**Inicio**

<img width="409" height="838" alt="Captura de pantalla 2026-09-30 000501" src="https://github.com/user-attachments/assets/2b77a5a4-1d29-4d90-aba9-bd089f0b7517" />


**Detalle de clase**

<img width="423" height="833" alt="Captura de pantalla 2026-09-30 000528" src="https://github.com/user-attachments/assets/b5911af0-acf0-487d-ade0-f4e6a2c9622c" />


**Confirmación**

<img width="416" height="843" alt="Captura de pantalla 2026-09-30 000551" src="https://github.com/user-attachments/assets/f528383d-b324-4f9b-8241-9d242408ac17" />


**Mis reservas**

<img width="430" height="835" alt="Captura de pantalla 2026-09-30 000607" src="https://github.com/user-attachments/assets/1a2b17ab-ba48-43f2-acc7-b20c86bd68a6" />


**Rutinas**

<img width="425" height="843" alt="Captura de pantalla 2026-09-30 000635" src="https://github.com/user-attachments/assets/db2fd167-5b15-442a-96ea-45268f6625a3" />


**Perfil**

<img width="417" height="845" alt="Captura de pantalla 2026-09-30 000654" src="https://github.com/user-attachments/assets/10c2c0ea-ef9b-4fe8-822d-4c4c56aa0d31" />

## Mejora con IA

Esta rama (`mejora-ia`) parte de `main` e incorpora una mejora funcional y un
rediseño visual, ambos realizados con **Gemini**, integrado en Android Studio.

### Mejora funcional

Se agregó la posibilidad de **cancelar una reserva** con un `AlertDialog` de
confirmación:

- Las reservas con estado "Confirmada" muestran un botón "Cancelar reserva"
- Las reservas con estado "Completada" no lo muestran, porque no tiene sentido
  cancelar una clase que ya ocurrió
- El diálogo muestra el nombre de la clase y el horario antes de confirmar
- Solo al confirmar se elimina la reserva de la lista compartida

El estado del diálogo guarda **la reserva seleccionada** (`Reserva?`) y no un
booleano, de modo que el mismo valor sirve para saber si el diálogo está abierto,
qué texto mostrar y qué elemento eliminar. El diálogo se cierra poniendo ese
estado en `null` en los tres caminos posibles: confirmar, cancelar y tocar fuera.

En Compose un diálogo no se abre con una función imperativa: se incluye o se
excluye del árbol de UI según el estado, y la recomposición hace el resto.

### Mejora visual

Rediseño de las seis pantallas con un mismo criterio: cabeceras con degradado
construidas con `Brush.verticalGradient`, tarjetas con esquinas redondeadas y
elevación, iconos dentro de círculos de color y etiquetas tipo píldora. Todos los
colores provienen de `MaterialTheme.colorScheme`, sin valores hexadecimales fijos.

### Documentación de los prompts

Los tres prompts utilizados, junto con lo que hubo que corregir de cada
respuesta, están documentados en [PROMPTS.md](PROMPTS.md).

## Capturas de la mejora

**Inicio**

<img width="375" height="785" alt="image" src="https://github.com/user-attachments/assets/8b510e3d-3eb9-4f5c-b37c-644c7820d2fc" />


**Detalle de clase**

<img width="408" height="813" alt="image" src="https://github.com/user-attachments/assets/9806e3b2-542d-4297-ac67-6b5d12ac3eaf" />


**Confirmación**

<img width="436" height="813" alt="image" src="https://github.com/user-attachments/assets/dd920b69-4e5d-4961-b23c-360c15246adb" />


**Mis reservas**

<img width="427" height="809" alt="image" src="https://github.com/user-attachments/assets/9197599b-cf56-45e5-92a6-4a551429ced2" />


**AlertDialog de cancelación**

<img width="425" height="812" alt="image" src="https://github.com/user-attachments/assets/e57c3c4a-342d-4bda-8d38-bf2b276d9b4b" />


**Rutinas**

<img width="412" height="812" alt="image" src="https://github.com/user-attachments/assets/17d6f848-7aad-42ac-9593-6f99a531ac39" />


**Perfil**

<img width="417" height="802" alt="image" src="https://github.com/user-attachments/assets/cd05bf97-9d45-48c6-8c77-602eceb9f7a6" />


## Cómo ejecutar

1. Clonar el repositorio
2. Abrir en Android Studio
3. Sincronizar Gradle
4. Ejecutar en emulador o dispositivo con API 24 o superior

## Ramas

- `main` — desarrollo sin asistentes de IA
- `mejora-ia` — mejora visual y funcional realizada con IA, documentada en PROMPTS.md
