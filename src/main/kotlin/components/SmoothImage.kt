package components

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Image
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import theme.ColorBox
import utils.ImageLoader

@Composable
fun SmoothImage(
    modifier: Modifier,
    image : String?,
    fadeOnChange : Boolean = false,
    placeHolder : String? = "icons/audio-square.svg",
    contentScale: ContentScale = ContentScale.Crop
) {

    if (fadeOnChange) {
        Crossfade(modifier = modifier,targetState = image) {
            CustomImage(modifier, it, placeHolder, contentScale)
        }
    } else {
        CustomImage(modifier, image, placeHolder, contentScale)
    }

}

@Composable
private fun CustomImage (modifier: Modifier,image: String?,placeHolder: String?,contentScale: ContentScale) {
    if (image.toString() == "null") {
        if (placeHolder != null) {
            Image(
                modifier = modifier,
                painter = painterResource(placeHolder),
                contentDescription = null,
                contentScale = contentScale,
                colorFilter = ColorFilter.tint(ColorBox.text.copy(0.8f))
            )
        }
    } else {
        var loadedImage by remember { mutableStateOf(ImageLoader.emptyImage) }

        ImageLoader.loadImage(image!!) {
            loadedImage = it
        }

        Image(
            modifier = modifier,
            bitmap = loadedImage,
            contentDescription = null,
            contentScale = contentScale
        )
    }
}