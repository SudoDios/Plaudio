package components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.jetbrains.skia.Rect
import org.jetbrains.skia.skottie.Animation
import org.jetbrains.skia.sksg.InvalidationController
import theme.ColorBox

@Composable
fun LikeAnimationIcon(
    liked : Boolean,
    padding: PaddingValues = PaddingValues(),
    size : Dp = 48.dp,
    onClicked : () -> Unit
) {

    val source = """
{"nm":"Artboard","ddd":0,"h":1080,"w":1080,"meta":{"g":"LottieFiles AE 0.1.20"},"layers":[{"ty":4,"nm":" ↳Color 7","sr":1,"st":8,"op":90,"ip":8,"hd":false,"ddd":0,"bm":0,"hasMask":false,"ao":0,"ks":{"a":{"a":0,"k":[0,0,0],"ix":1},"s":{"a":0,"k":[25,25,100],"ix":6},"sk":{"a":0,"k":0},"p":{"a":1,"k":[{"o":{"x":0.167,"y":0.167},"i":{"x":0,"y":1},"s":[540,540,0],"t":8,"ti":[315.75,-3.05,0],"to":[-243.75,-42.95,0]},{"s":[165.5,758.3,0],"t":68}],"ix":2},"r":{"a":0,"k":0,"ix":10},"sa":{"a":0,"k":0},"o":{"a":1,"k":[{"o":{"x":0.333,"y":0},"i":{"x":0.667,"y":1},"s":[100],"t":53},{"s":[0],"t":68}],"ix":11}},"ef":[{"ty":0,"mn":"Pseudo/MHAC PrCtrl NWX 4","nm":"AC IN [NWX] Controls","ix":1,"en":1,"ef":[{"ty":0,"mn":"Pseudo/MHAC PrCtrl NWX 4-0001","nm":"Number of bounces","ix":1,"v":{"a":0,"k":2,"ix":1}},{"ty":0,"mn":"Pseudo/MHAC PrCtrl NWX 4-0002","nm":"Scale","ix":2,"v":{"a":0,"k":0,"ix":2}}]}],"shapes":[{"ty":"gr","bm":0,"hd":false,"mn":"ADBE Vector Group","nm":" ↳Color","ix":1,"cix":2,"np":2,"it":[{"ty":"sh","bm":0,"hd":false,"mn":"ADBE Vector Shape - Group","nm":"Path 1","ix":1,"d":1,"ks":{"a":0,"k":{"c":true,"i":[[0,0],[0,0],[0,35.85],[-29.26,0],[-10.36,-12.14],[-16.53,0],[0,-29.2],[48.92,-44.38]],"o":[[0,0],[-48.93,-44.28],[0,-29.2],[16.53,0],[10.36,-12.14],[29.26,0],[0,35.85],[0,0]],"v":[[0,87],[-13.77,74.48],[-95,-34.85],[-42.75,-87],[0,-67.18],[42.75,-87],[95,-34.85],[13.78,74.58]]},"ix":2}},{"ty":"fl","bm":0,"hd":false,"mn":"ADBE Vector Graphic - Fill","nm":"Fill 1","c":{"a":0,"k":[1,0,0],"ix":4},"r":1,"o":{"a":0,"k":100,"ix":5}},{"ty":"tr","a":{"a":0,"k":[0,0],"ix":1},"s":{"a":0,"k":[300,300],"ix":3},"sk":{"a":0,"k":0,"ix":4},"p":{"a":0,"k":[0,0],"ix":2},"r":{"a":0,"k":0,"ix":6},"sa":{"a":0,"k":0,"ix":5},"o":{"a":0,"k":100,"ix":7}}]}],"ind":1},{"ty":4,"nm":" ↳Color 6","sr":1,"st":10,"op":90,"ip":10,"hd":false,"ddd":0,"bm":0,"hasMask":false,"ao":0,"ks":{"a":{"a":0,"k":[0,0,0],"ix":1},"s":{"a":0,"k":[15,15,100],"ix":6},"sk":{"a":0,"k":0},"p":{"a":1,"k":[{"o":{"x":0.167,"y":0.167},"i":{"x":0,"y":1},"s":[540,540,0],"t":10,"ti":[-164.25,-171.05,0],"to":[292.25,-4.95,0]},{"s":[385.5,818.3,0],"t":70}],"ix":2},"r":{"a":0,"k":0,"ix":10},"sa":{"a":0,"k":0},"o":{"a":1,"k":[{"o":{"x":0.333,"y":0},"i":{"x":0.667,"y":1},"s":[100],"t":55},{"s":[0],"t":70}],"ix":11}},"ef":[{"ty":0,"mn":"Pseudo/MHAC PrCtrl NWX 4","nm":"AC IN [NWX] Controls","ix":1,"en":1,"ef":[{"ty":0,"mn":"Pseudo/MHAC PrCtrl NWX 4-0001","nm":"Number of bounces","ix":1,"v":{"a":0,"k":2,"ix":1}},{"ty":0,"mn":"Pseudo/MHAC PrCtrl NWX 4-0002","nm":"Scale","ix":2,"v":{"a":0,"k":0,"ix":2}}]}],"shapes":[{"ty":"gr","bm":0,"hd":false,"mn":"ADBE Vector Group","nm":" ↳Color","ix":1,"cix":2,"np":2,"it":[{"ty":"sh","bm":0,"hd":false,"mn":"ADBE Vector Shape - Group","nm":"Path 1","ix":1,"d":1,"ks":{"a":0,"k":{"c":true,"i":[[0,0],[0,0],[0,35.85],[-29.26,0],[-10.36,-12.14],[-16.53,0],[0,-29.2],[48.92,-44.38]],"o":[[0,0],[-48.93,-44.28],[0,-29.2],[16.53,0],[10.36,-12.14],[29.26,0],[0,35.85],[0,0]],"v":[[0,87],[-13.77,74.48],[-95,-34.85],[-42.75,-87],[0,-67.18],[42.75,-87],[95,-34.85],[13.78,74.58]]},"ix":2}},{"ty":"fl","bm":0,"hd":false,"mn":"ADBE Vector Graphic - Fill","nm":"Fill 1","c":{"a":0,"k":[1,0,0],"ix":4},"r":1,"o":{"a":0,"k":100,"ix":5}},{"ty":"tr","a":{"a":0,"k":[0,0],"ix":1},"s":{"a":0,"k":[300,300],"ix":3},"sk":{"a":0,"k":0,"ix":4},"p":{"a":0,"k":[0,0],"ix":2},"r":{"a":0,"k":0,"ix":6},"sa":{"a":0,"k":0,"ix":5},"o":{"a":0,"k":100,"ix":7}}]}],"ind":2},{"ty":4,"nm":" ↳Color 5","sr":1,"st":6,"op":90,"ip":6,"hd":false,"ddd":0,"bm":0,"hasMask":false,"ao":0,"ks":{"a":{"a":0,"k":[0,0,0],"ix":1},"s":{"a":0,"k":[20,20,100],"ix":6},"sk":{"a":0,"k":0},"p":{"a":1,"k":[{"o":{"x":0.167,"y":0.167},"i":{"x":0,"y":1},"s":[540,540,0],"t":6,"ti":[-164.25,-171.05,0],"to":[292.25,-4.95,0]},{"s":[725.5,918.3,0],"t":66}],"ix":2},"r":{"a":0,"k":0,"ix":10},"sa":{"a":0,"k":0},"o":{"a":1,"k":[{"o":{"x":0.333,"y":0},"i":{"x":0.667,"y":1},"s":[100],"t":51},{"s":[0],"t":66}],"ix":11}},"ef":[{"ty":0,"mn":"Pseudo/MHAC PrCtrl NWX 4","nm":"AC IN [NWX] Controls","ix":1,"en":1,"ef":[{"ty":0,"mn":"Pseudo/MHAC PrCtrl NWX 4-0001","nm":"Number of bounces","ix":1,"v":{"a":0,"k":2,"ix":1}},{"ty":0,"mn":"Pseudo/MHAC PrCtrl NWX 4-0002","nm":"Scale","ix":2,"v":{"a":0,"k":0,"ix":2}}]}],"shapes":[{"ty":"gr","bm":0,"hd":false,"mn":"ADBE Vector Group","nm":" ↳Color","ix":1,"cix":2,"np":2,"it":[{"ty":"sh","bm":0,"hd":false,"mn":"ADBE Vector Shape - Group","nm":"Path 1","ix":1,"d":1,"ks":{"a":0,"k":{"c":true,"i":[[0,0],[0,0],[0,35.85],[-29.26,0],[-10.36,-12.14],[-16.53,0],[0,-29.2],[48.92,-44.38]],"o":[[0,0],[-48.93,-44.28],[0,-29.2],[16.53,0],[10.36,-12.14],[29.26,0],[0,35.85],[0,0]],"v":[[0,87],[-13.77,74.48],[-95,-34.85],[-42.75,-87],[0,-67.18],[42.75,-87],[95,-34.85],[13.78,74.58]]},"ix":2}},{"ty":"fl","bm":0,"hd":false,"mn":"ADBE Vector Graphic - Fill","nm":"Fill 1","c":{"a":0,"k":[1,0,0],"ix":4},"r":1,"o":{"a":0,"k":100,"ix":5}},{"ty":"tr","a":{"a":0,"k":[0,0],"ix":1},"s":{"a":0,"k":[300,300],"ix":3},"sk":{"a":0,"k":0,"ix":4},"p":{"a":0,"k":[0,0],"ix":2},"r":{"a":0,"k":0,"ix":6},"sa":{"a":0,"k":0,"ix":5},"o":{"a":0,"k":100,"ix":7}}]}],"ind":3},{"ty":4,"nm":" ↳Color 4","sr":1,"st":2,"op":90,"ip":5,"hd":false,"ddd":0,"bm":0,"hasMask":false,"ao":0,"ks":{"a":{"a":0,"k":[0,0,0],"ix":1},"s":{"a":0,"k":[40,40,100],"ix":6},"sk":{"a":0,"k":0},"p":{"a":1,"k":[{"o":{"x":0.167,"y":0.167},"i":{"x":0,"y":1},"s":[540,540,0],"t":2,"ti":[-210.25,-3.05,0],"to":[166.25,-170.95,0]},{"s":[925.5,698.3,0],"t":62}],"ix":2},"r":{"a":0,"k":0,"ix":10},"sa":{"a":0,"k":0},"o":{"a":1,"k":[{"o":{"x":0.333,"y":0},"i":{"x":0.667,"y":1},"s":[100],"t":47},{"s":[0],"t":62}],"ix":11}},"ef":[{"ty":0,"mn":"Pseudo/MHAC PrCtrl NWX 4","nm":"AC IN [NWX] Controls","ix":1,"en":1,"ef":[{"ty":0,"mn":"Pseudo/MHAC PrCtrl NWX 4-0001","nm":"Number of bounces","ix":1,"v":{"a":0,"k":2,"ix":1}},{"ty":0,"mn":"Pseudo/MHAC PrCtrl NWX 4-0002","nm":"Scale","ix":2,"v":{"a":0,"k":0,"ix":2}}]}],"shapes":[{"ty":"gr","bm":0,"hd":false,"mn":"ADBE Vector Group","nm":" ↳Color","ix":1,"cix":2,"np":2,"it":[{"ty":"sh","bm":0,"hd":false,"mn":"ADBE Vector Shape - Group","nm":"Path 1","ix":1,"d":1,"ks":{"a":0,"k":{"c":true,"i":[[0,0],[0,0],[0,35.85],[-29.26,0],[-10.36,-12.14],[-16.53,0],[0,-29.2],[48.92,-44.38]],"o":[[0,0],[-48.93,-44.28],[0,-29.2],[16.53,0],[10.36,-12.14],[29.26,0],[0,35.85],[0,0]],"v":[[0,87],[-13.77,74.48],[-95,-34.85],[-42.75,-87],[0,-67.18],[42.75,-87],[95,-34.85],[13.78,74.58]]},"ix":2}},{"ty":"fl","bm":0,"hd":false,"mn":"ADBE Vector Graphic - Fill","nm":"Fill 1","c":{"a":0,"k":[1,0,0],"ix":4},"r":1,"o":{"a":0,"k":100,"ix":5}},{"ty":"tr","a":{"a":0,"k":[0,0],"ix":1},"s":{"a":0,"k":[300,300],"ix":3},"sk":{"a":0,"k":0,"ix":4},"p":{"a":0,"k":[0,0],"ix":2},"r":{"a":0,"k":0,"ix":6},"sa":{"a":0,"k":0,"ix":5},"o":{"a":0,"k":100,"ix":7}}]}],"ind":4},{"ty":4,"nm":" ↳Color 3","sr":1,"st":4,"op":90,"ip":5,"hd":false,"ddd":0,"bm":0,"hasMask":false,"ao":0,"ks":{"a":{"a":0,"k":[0,0,0],"ix":1},"s":{"a":0,"k":[20,20,100],"ix":6},"sk":{"a":0,"k":0},"p":{"a":1,"k":[{"o":{"x":0.167,"y":0.167},"i":{"x":0,"y":1},"s":[540,540,0],"t":4,"ti":[-120.25,178.95,0],"to":[-227.75,-240.95,0]},{"s":[845.5,218.3,0],"t":64}],"ix":2},"r":{"a":0,"k":0,"ix":10},"sa":{"a":0,"k":0},"o":{"a":1,"k":[{"o":{"x":0.333,"y":0},"i":{"x":0.667,"y":1},"s":[100],"t":49},{"s":[0],"t":64}],"ix":11}},"ef":[{"ty":0,"mn":"Pseudo/MHAC PrCtrl NWX 4","nm":"AC IN [NWX] Controls","ix":1,"en":1,"ef":[{"ty":0,"mn":"Pseudo/MHAC PrCtrl NWX 4-0001","nm":"Number of bounces","ix":1,"v":{"a":0,"k":2,"ix":1}},{"ty":0,"mn":"Pseudo/MHAC PrCtrl NWX 4-0002","nm":"Scale","ix":2,"v":{"a":0,"k":0,"ix":2}}]}],"shapes":[{"ty":"gr","bm":0,"hd":false,"mn":"ADBE Vector Group","nm":" ↳Color","ix":1,"cix":2,"np":2,"it":[{"ty":"sh","bm":0,"hd":false,"mn":"ADBE Vector Shape - Group","nm":"Path 1","ix":1,"d":1,"ks":{"a":0,"k":{"c":true,"i":[[0,0],[0,0],[0,35.85],[-29.26,0],[-10.36,-12.14],[-16.53,0],[0,-29.2],[48.92,-44.38]],"o":[[0,0],[-48.93,-44.28],[0,-29.2],[16.53,0],[10.36,-12.14],[29.26,0],[0,35.85],[0,0]],"v":[[0,87],[-13.77,74.48],[-95,-34.85],[-42.75,-87],[0,-67.18],[42.75,-87],[95,-34.85],[13.78,74.58]]},"ix":2}},{"ty":"fl","bm":0,"hd":false,"mn":"ADBE Vector Graphic - Fill","nm":"Fill 1","c":{"a":0,"k":[1,0,0],"ix":4},"r":1,"o":{"a":0,"k":100,"ix":5}},{"ty":"tr","a":{"a":0,"k":[0,0],"ix":1},"s":{"a":0,"k":[300,300],"ix":3},"sk":{"a":0,"k":0,"ix":4},"p":{"a":0,"k":[0,0],"ix":2},"r":{"a":0,"k":0,"ix":6},"sa":{"a":0,"k":0,"ix":5},"o":{"a":0,"k":100,"ix":7}}]}],"ind":5},{"ty":4,"nm":" ↳Color 2","sr":1,"st":0,"op":90,"ip":5,"hd":false,"ddd":0,"bm":0,"hasMask":false,"ao":0,"ks":{"a":{"a":0,"k":[0,0,0],"ix":1},"s":{"a":0,"k":[30,30,100],"ix":6},"sk":{"a":0,"k":0},"p":{"a":1,"k":[{"o":{"x":0.167,"y":0.167},"i":{"x":0,"y":1},"s":[540,540,0],"t":0,"ti":[-120.25,178.95,0],"to":[-227.75,-240.95,0]},{"s":[145.5,138.3,0],"t":60}],"ix":2},"r":{"a":0,"k":0,"ix":10},"sa":{"a":0,"k":0},"o":{"a":1,"k":[{"o":{"x":0.333,"y":0},"i":{"x":0.667,"y":1},"s":[100],"t":45},{"s":[0],"t":60}],"ix":11}},"ef":[{"ty":0,"mn":"Pseudo/MHAC PrCtrl NWX 4","nm":"AC IN [NWX] Controls","ix":1,"en":1,"ef":[{"ty":0,"mn":"Pseudo/MHAC PrCtrl NWX 4-0001","nm":"Number of bounces","ix":1,"v":{"a":0,"k":2,"ix":1}},{"ty":0,"mn":"Pseudo/MHAC PrCtrl NWX 4-0002","nm":"Scale","ix":2,"v":{"a":0,"k":0,"ix":2}}]}],"shapes":[{"ty":"gr","bm":0,"hd":false,"mn":"ADBE Vector Group","nm":" ↳Color","ix":1,"cix":2,"np":2,"it":[{"ty":"sh","bm":0,"hd":false,"mn":"ADBE Vector Shape - Group","nm":"Path 1","ix":1,"d":1,"ks":{"a":0,"k":{"c":true,"i":[[0,0],[0,0],[0,35.85],[-29.26,0],[-10.36,-12.14],[-16.53,0],[0,-29.2],[48.92,-44.38]],"o":[[0,0],[-48.93,-44.28],[0,-29.2],[16.53,0],[10.36,-12.14],[29.26,0],[0,35.85],[0,0]],"v":[[0,87],[-13.77,74.48],[-95,-34.85],[-42.75,-87],[0,-67.18],[42.75,-87],[95,-34.85],[13.78,74.58]]},"ix":2}},{"ty":"fl","bm":0,"hd":false,"mn":"ADBE Vector Graphic - Fill","nm":"Fill 1","c":{"a":0,"k":[1,0,0],"ix":4},"r":1,"o":{"a":0,"k":100,"ix":5}},{"ty":"tr","a":{"a":0,"k":[0,0],"ix":1},"s":{"a":0,"k":[300,300],"ix":3},"sk":{"a":0,"k":0,"ix":4},"p":{"a":0,"k":[0,0],"ix":2},"r":{"a":0,"k":0,"ix":6},"sa":{"a":0,"k":0,"ix":5},"o":{"a":0,"k":100,"ix":7}}]}],"ind":6},{"ty":4,"nm":" ↳Color","sr":1,"st":0,"op":90,"ip":0,"hd":false,"ddd":0,"bm":0,"hasMask":false,"ao":0,"ks":{"a":{"a":0,"k":[0,0,0],"ix":1},"s":{"a":1,"k":[{"o":{"x":0.167,"y":0},"i":{"x":0,"y":1},"s":[0,0,100],"t":0},{"o":{"x":1,"y":0},"i":{"x":0.568,"y":0.855},"s":[110,110,100],"t":30},{"o":{"x":0.642,"y":-4.307},"i":{"x":0.843,"y":1},"s":[100,100,100],"t":40},{"o":{"x":1,"y":0},"i":{"x":0.833,"y":0.833},"s":[100,100,100],"t":60},{"s":[0,0,100],"t":75}],"ix":6},"sk":{"a":0,"k":0},"p":{"a":0,"k":[540,540,0],"ix":2},"r":{"a":0,"k":0,"ix":10},"sa":{"a":0,"k":0},"o":{"a":0,"k":100,"ix":11}},"ef":[{"ty":0,"mn":"Pseudo/MHAC PrCtrl NWX 4","nm":"AC IN [NWX] Controls","ix":1,"en":1,"ef":[{"ty":0,"mn":"Pseudo/MHAC PrCtrl NWX 4-0001","nm":"Number of bounces","ix":1,"v":{"a":0,"k":2,"ix":1}},{"ty":0,"mn":"Pseudo/MHAC PrCtrl NWX 4-0002","nm":"Scale","ix":2,"v":{"a":0,"k":0,"ix":2}}]}],"shapes":[{"ty":"gr","bm":0,"hd":false,"mn":"ADBE Vector Group","nm":" ↳Color","ix":1,"cix":2,"np":2,"it":[{"ty":"sh","bm":0,"hd":false,"mn":"ADBE Vector Shape - Group","nm":"Path 1","ix":1,"d":1,"ks":{"a":0,"k":{"c":true,"i":[[0,0],[0,0],[0,35.85],[-29.26,0],[-10.36,-12.14],[-16.53,0],[0,-29.2],[48.92,-44.38]],"o":[[0,0],[-48.93,-44.28],[0,-29.2],[16.53,0],[10.36,-12.14],[29.26,0],[0,35.85],[0,0]],"v":[[0,87],[-13.77,74.48],[-95,-34.85],[-42.75,-87],[0,-67.18],[42.75,-87],[95,-34.85],[13.78,74.58]]},"ix":2}},{"ty":"fl","bm":0,"hd":false,"mn":"ADBE Vector Graphic - Fill","nm":"Fill 1","c":{"a":0,"k":[1,0,0],"ix":4},"r":1,"o":{"a":0,"k":100,"ix":5}},{"ty":"tr","a":{"a":0,"k":[0,0],"ix":1},"s":{"a":0,"k":[300,300],"ix":3},"sk":{"a":0,"k":0,"ix":4},"p":{"a":0,"k":[0,0],"ix":2},"r":{"a":0,"k":0,"ix":6},"sa":{"a":0,"k":0,"ix":5},"o":{"a":0,"k":100,"ix":7}}]}],"ind":7}],"v":"5.5.7","fr":60,"op":90,"ip":0,"assets":[]}
"""

    val animate = animateFloatAsState(
        targetValue = if (liked) 1.1f else 0f,
        animationSpec = tween(550)
    )
    val bgColor = lerp(ColorBox.primary.copy(0.1f),Color.Red.copy(0.1f),animate.value.fix())

    Box(modifier = Modifier.padding(padding).size(size).clip(RoundedCornerShape(50)).background(bgColor).clickable {
        onClicked.invoke()
    }, contentAlignment = Alignment.Center) {
        Icon(
            modifier = Modifier.alpha(1.1f - animate.value).size(24.dp),
            painter = painterResource("icons/heart.svg"),
            contentDescription = null,
            tint = ColorBox.text.copy(0.8f)
        )
        //overlay
        val animation = Animation.makeFromString(source)
        val invalidationController = remember { InvalidationController() }
        animation.seekFrameTime(animate.value, invalidationController)

        Canvas(Modifier.size(44.dp)) {
            drawIntoCanvas {
                animation.render(
                    canvas = it.nativeCanvas,
                    dst = Rect.makeWH(this.size.width, this.size.height)
                )
            }
        }
    }

}

private fun Float.fix () : Float = if (this > 1f) 1f else this