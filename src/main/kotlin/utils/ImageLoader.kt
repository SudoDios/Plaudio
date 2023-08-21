package utils

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.jetbrains.skia.Image
import java.io.File

object ImageLoader {

    private val cachedImages = mutableMapOf<String, ImageBitmap>()
    val emptyImage = ImageBitmap(1,1)

    fun loadImage (path : String,callback : (ImageBitmap) -> Unit) {
        if (path.isNotEmpty()) {
            if (cachedImages.containsKey(path)) {
                callback.invoke(cachedImages[path]!!)
            } else {
                CoroutineScope(Dispatchers.IO).launch {
                    val loadedBitmap = loadImageBitmap(File(path).readBytes())
                    CoroutineScope(Dispatchers.Main).launch {
                        cachedImages[path] = loadedBitmap
                        callback.invoke(loadedBitmap)
                    }
                }
            }
        }
    }

    private fun loadImageBitmap(inputStream: ByteArray): ImageBitmap =
        Image.makeFromEncoded(inputStream).toComposeImageBitmap()

}