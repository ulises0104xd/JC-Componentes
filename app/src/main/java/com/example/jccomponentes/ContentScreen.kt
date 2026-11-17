package com.example.jccomponentes

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.example.jccomponentes.ui.theme.JCComponentesTheme


@Composable
fun ContentView(
    modifier: Modifier = Modifier,
    onContentEvent: (Boolean) -> Unit = {}
) {
    // Video 30: el switch recuerda su estado; empieza encendido
    var switchChecked by remember { mutableStateOf(true) }

    Column(
        modifier = modifier.verticalScroll(rememberScrollState())
    ) {
        // Texto del switch: cambia entre "Apagar FAB" y "Prender FAB" según su estado
        Text(
            text = if (switchChecked) {
                stringResource(R.string.apagar_fab)
            } else {
                stringResource(R.string.prender_fab)
            },
            modifier = Modifier.padding(start = 16.dp, top = 8.dp)
        )
        // Botón de encendido y apagado: muestra u oculta el botón flotante del Scaffold
        Switch(
            checked = switchChecked,
            onCheckedChange = { valorActual ->
                switchChecked = valorActual
                onContentEvent(switchChecked)
            },
            modifier = Modifier.padding(start = 16.dp)
        )

        // ---------------- Reto 30: Card con ConstraintLayout ----------------
        // Constraints (de adentro hacia afuera):
        //  - imagen:      arriba-izquierda de la tarjeta, 128 dp.
        //  - título:      a la derecha de la imagen, alineado a su borde superior.
        //  - descripción: debajo del título, mismo inicio, máximo 3 líneas con "…".
        //  - Carreras:    debajo de la descripción, mismo inicio que ella.
        //  - Brincar:     a la derecha de Carreras, centrado verticalmente con él.
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            colors = CardDefaults.cardColors(containerColor = colorResource(R.color.azul_tecnm))
        ) {
            ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
                val (imagen, titulo, descripcion, btnCarreras, btnSkip) = createRefs()

                Image(
                    painter = painterResource(R.drawable.foto_robot),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .constrainAs(imagen) {
                            start.linkTo(parent.start, margin = 8.dp)
                            top.linkTo(parent.top, margin = 16.dp)
                        }
                        .size(128.dp)
                        .background(colorResource(R.color.azul_tecnm))
                )

                Text(
                    text = stringResource(R.string.nombre_institucion),
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color.White,
                    modifier = Modifier.constrainAs(titulo) {
                        start.linkTo(imagen.end, margin = 8.dp)
                        end.linkTo(parent.end, margin = 16.dp)
                        top.linkTo(imagen.top)
                        width = Dimension.fillToConstraints
                    }
                )

                Text(
                    text = stringResource(R.string.descripcion_tecnm),
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.constrainAs(descripcion) {
                        start.linkTo(titulo.start)
                        end.linkTo(parent.end, margin = 16.dp)
                        top.linkTo(titulo.bottom, margin = 12.dp)
                        width = Dimension.fillToConstraints
                    }
                )

                Button(
                    onClick = { },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(R.color.azul_boton),
                        contentColor = Color.White
                    ),
                    modifier = Modifier.constrainAs(btnCarreras) {
                        start.linkTo(descripcion.start)
                        top.linkTo(descripcion.bottom, margin = 12.dp)
                        bottom.linkTo(parent.bottom, margin = 16.dp)
                    }
                ) {
                    Icon(
                        imageVector = Icons.Filled.AccountBalance,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        text = stringResource(R.string.carreras),
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }

                TextButton(
                    onClick = { },
                    modifier = Modifier.constrainAs(btnSkip) {
                        start.linkTo(btnCarreras.end)
                        end.linkTo(parent.end)
                        top.linkTo(btnCarreras.top)
                        bottom.linkTo(btnCarreras.bottom)
                    }
                ) {
                    Text(text = stringResource(R.string.skip), color = Color.White)
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ContentViewPreview() {
    JCComponentesTheme {
        ContentView()
    }
}
