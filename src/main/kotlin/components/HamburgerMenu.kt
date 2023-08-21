package components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.lerp
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import theme.ColorBox

@Composable
fun HamburgerMenu(
    modifier: Modifier,
    size: Dp = 24.dp,
    isOpen: Boolean,
    onClicked: () -> Unit
) {

    val divide3 = size / 3f
    val divide2 = divide3 / 2f

    val fraction = animateFloatAsState(if (isOpen) 0f else 1f)

    //firstLine
    val firstLineStartOpen = Offset((size * 0.1f).value, (size / 2f).value)
    val firstLineStartClose = Offset(0f, divide2.value)
    val firstLineEndOpen = Offset((size / 2f).value, (size * 0.1f).value)
    val firstLineEndClose = Offset(size.value, divide2.value)
    val startFirstLine = lerp(firstLineStartOpen,firstLineStartClose,fraction.value)
    val endFirstLine = lerp(firstLineEndOpen,firstLineEndClose,fraction.value)

    //secondLine
    val secondLineStartOpen = Offset((size * 0.1f).value, (size / 2f).value)
    val secondLineStartClose = Offset(0f, (size / 2f).value)
    val secondLineEndOpen = Offset(size.value, (size / 2f).value)
    val secondLineEndClose = Offset(size.value, (size / 2f).value)
    val startSecondLine = lerp(secondLineStartOpen,secondLineStartClose,fraction.value)
    val endSecondLine = lerp(secondLineEndOpen,secondLineEndClose,fraction.value)

    //thirdLine
    val thirdLineStartOpen = Offset((size * 0.1f).value, (size / 2f).value)
    val thirdLineStartClose = Offset(0f, divide2.value + (divide3.value * 2))
    val thirdLineEndOpen = Offset((size / 2f).value, (size * 0.9f).value)
    val thirdLineEndClose = Offset(size.value, divide2.value + (divide3.value * 2))
    val startThirdLine = lerp(thirdLineStartOpen,thirdLineStartClose,fraction.value)
    val endThirdLine = lerp(thirdLineEndOpen,thirdLineEndClose,fraction.value)

    Box(
        modifier = modifier.clip(RoundedCornerShape(50)).clickable {
            onClicked.invoke()
        },
        contentAlignment = Alignment.Center
    ) {
        Canvas(
            modifier = Modifier.size(size)
        ) {
            drawLine(
                color = ColorBox.text.copy(0.8f),
                start = startFirstLine,
                end = endFirstLine,
                cap = StrokeCap.Round,
                strokeWidth = 2.dp.toPx()
            )
            drawLine(
                color = ColorBox.text.copy(0.8f),
                start = startSecondLine,
                end = endSecondLine,
                cap = StrokeCap.Round,
                strokeWidth = 2.dp.toPx()
            )
            drawLine(
                color = ColorBox.text.copy(0.8f),
                start = startThirdLine,
                end = endThirdLine,
                cap = StrokeCap.Round,
                strokeWidth = 2.dp.toPx()
            )
        }
    }

}