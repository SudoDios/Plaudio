package components

import androidx.compose.animation.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit

@Composable
fun AnimatedText(
    modifier: Modifier,
    text : String,
    color: Color = Color.Unspecified,
    fontSize: TextUnit = TextUnit.Unspecified,
    textAlign: TextAlign? = null,
    overflow: TextOverflow = TextOverflow.Clip,
    maxLines: Int = Int.MAX_VALUE,
) {

    AnimatedContent(
        targetState = text,
        transitionSpec = {
            (slideInVertically { height -> height } + fadeIn() togetherWith
                    slideOutVertically { height -> -height } + fadeOut()).using(SizeTransform(false))
        }
    ) {
        Text(
            text = text,
            modifier = modifier,
            color = color,
            fontSize = fontSize,
            textAlign = textAlign,
            overflow = overflow,
            maxLines = maxLines
        )
    }

}