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


## Cómo ejecutar

1. Clonar el repositorio
2. Abrir en Android Studio
3. Sincronizar Gradle
4. Ejecutar en emulador o dispositivo con API 24 o superior

## Ramas

- `main` — desarrollo sin asistentes de IA
- `mejora-ia` — mejora visual y funcional realizada con IA, documentada en PROMPTS.md
