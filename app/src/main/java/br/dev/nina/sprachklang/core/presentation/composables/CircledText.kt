package br.dev.nina.sprachklang.core.presentation.composables

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.unit.dp

@Composable
fun CircledText(
    text: String,
    modifier: Modifier = Modifier,
    circleColor: Color = MaterialTheme.colorScheme.primary,
    circleStyle: DrawStyle = Fill
) {
    Text(
        text = text,
        color = MaterialTheme.colorScheme.onPrimary,
        modifier = modifier
            .padding(2.dp)
            .drawBehind {
                drawCircle(
                    color = circleColor,
                    radius = this.size.minDimension,
                    style = circleStyle
                )
            },
    )
}
