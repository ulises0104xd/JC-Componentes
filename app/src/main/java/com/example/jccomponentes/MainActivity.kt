package com.example.jccomponentes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.jccomponentes.ui.theme.JCComponentesTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            JCComponentesTheme {
                // Video 28: finish() solo se puede llamar desde la Activity, por eso se pasa como función
                ContentMain(finalizar = { finish() })
            }
        }
    }
}

@Composable
fun ContentMain(finalizar: () -> Unit) {
    // Variables del snackbar (video 27)
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    // Mensajes: se leen de strings.xml (cambian solos entre español e inglés)
    val mensajeEventoExitoso = stringResource(R.string.mensaje_evento_exitoso)
    val mensajeHastaPronto = stringResource(R.string.mensaje_hasta_pronto)

    // Variables del menú de tres puntos (video 28)
    var expandirMenu by remember { mutableStateOf(false) }

    // Variable de la posición del botón flotante (video 29)
    var faPosition by remember { mutableStateOf(FabPosition.End) }

    // Video 30: el switch de ContentView muestra u oculta el botón flotante
    var showFab by remember { mutableStateOf(true) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        bottomBar = {
            BottomAppBar(containerColor = colorResource(R.color.barra_botones)) {
                CompositionLocalProvider(
                    LocalContentColor provides MaterialTheme.colorScheme.onSurface
                ) {
                    // Menú de hamburguesa (izquierda): muestra el snackbar
                    IconButton(onClick = {
                        scope.launch { snackbarHostState.showSnackbar(mensajeEventoExitoso) }
                    }) {
                        Icon(
                            imageVector = Icons.Filled.Menu,
                            contentDescription = stringResource(R.string.menu)
                        )
                    }
                }
                // Empuja el siguiente botón hasta el otro extremo de la barra
                Spacer(modifier = Modifier.weight(1f, true))
                // Tres puntitos (derecha). El DropdownMenu va DENTRO del Box para
                // que se abra junto al botón y no del lado izquierdo.
                Box {
                    IconButton(onClick = { expandirMenu = true }) {
                        Icon(
                            imageVector = Icons.Filled.MoreVert,
                            contentDescription = stringResource(R.string.opciones)
                        )
                    }
                    DropdownMenu(
                        expanded = expandirMenu,
                        onDismissRequest = { expandirMenu = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text(stringResource(R.string.salir)) },
                            onClick = {
                                expandirMenu = false
                                scope.launch {
                                    snackbarHostState.showSnackbar(
                                        mensajeHastaPronto,
                                        duration = SnackbarDuration.Short
                                    )
                                    finalizar()
                                }
                            }
                        )
                    }
                }
            }
        },
        floatingActionButton = {
            // Video 29: al pulsarlo alterna entre la derecha (End) y el centro (Center)
            // Video 30: AnimatedVisibility lo hace aparecer/desaparecer escalándose
            AnimatedVisibility(
                visible = showFab,
                enter = scaleIn(),
                exit = scaleOut()
            ) {
                FloatingActionButton(onClick = {
                    faPosition = if (faPosition == FabPosition.End) {
                        FabPosition.Center
                    } else {
                        FabPosition.End
                    }
                }) {
                    Icon(
                        imageVector = Icons.Filled.SwapHoriz,
                        contentDescription = stringResource(R.string.cambiar_posicion)
                    )
                }
            }
        },
        floatingActionButtonPosition = faPosition
    ) { innerPadding ->
        ContentView(
            modifier = Modifier.padding(innerPadding),
            onContentEvent = { isSwitchChecked -> showFab = isSwitchChecked }
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ContentPreview() {
    JCComponentesTheme {
        ContentMain {}
    }
}
