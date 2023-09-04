package utils

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.asComposeRenderEffect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import org.jetbrains.skia.FilterTileMode
import org.jetbrains.skia.ImageFilter
import org.jetbrains.skia.RuntimeEffect
import org.jetbrains.skia.RuntimeShaderBuilder

@Composable
fun Modifier.areaBlur(
    leftTop : Offset = Offset(0f,0f),
    size : Offset,
    radius : Float = 0f
) : Modifier {

    val areaBlurSksl = """
                uniform shader content;
                uniform shader blur;
            
                uniform vec4 rectangle;
                uniform float radius;
            
                float roundedRectangleSDF(vec2 position, vec2 box, float radius) {
                    vec2 q = abs(position) - box + vec2(radius);
                    return min(max(q.x, q.y), 0.0) + length(max(q, 0.0)) - radius;
                }
                
                vec4 main(vec2 coord) {
                    vec2 shiftRect = (rectangle.zw - rectangle.xy) / 2.0;
                    vec2 shiftCoord = coord - rectangle.xy;
                    float distanceToClosestEdge = roundedRectangleSDF(
                        shiftCoord - shiftRect, shiftRect, radius);
                        
            
                    vec4 c = content.eval(coord);
                    if (distanceToClosestEdge > 0.0) {
                        if (distanceToClosestEdge < 2.0) {
                            float darkenFactor = (2.0 - distanceToClosestEdge) / 2.0;
                            darkenFactor = pow(darkenFactor, 1.6);
                            return c * (0.9 + (1.0 - darkenFactor) / 10.0);
                        }
                        return c;
                    }
            
                    vec4 b = blur.eval(coord);
                    return b;
                }
            """

    val compositeRuntimeEffect = RuntimeEffect.makeForShader(areaBlurSksl)
    val compositeShaderBuilder = RuntimeShaderBuilder(compositeRuntimeEffect)

    val density = LocalDensity.current.density
    compositeShaderBuilder.uniform(
        "rectangle",
        leftTop.x * density, leftTop.y * density, size.x * density, (size.y) * density
    )
    compositeShaderBuilder.uniform("radius", radius * density)

    return graphicsLayer {
        this.renderEffect =
            ImageFilter.makeRuntimeShader(
            runtimeShaderBuilder = compositeShaderBuilder,
            shaderNames = arrayOf("content", "blur"),
            inputs = arrayOf(
                null, ImageFilter.makeBlur(
                    sigmaX = 10.0f,
                    sigmaY = 10.0f,
                    mode = FilterTileMode.CLAMP
                )
            )
        ).asComposeRenderEffect()
    }

}


@Composable
fun Modifier.clickable() = this.then(
    clickable(interactionSource = remember { MutableInteractionSource() }, indication = null) {}
)