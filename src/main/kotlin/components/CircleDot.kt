package components

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun CircleDot(modifier: Modifier,color: Color) {

    Canvas(modifier = modifier) {
        drawCircle(
            color = color
        )
    }

}